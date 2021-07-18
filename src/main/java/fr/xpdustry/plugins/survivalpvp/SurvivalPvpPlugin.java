package fr.xpdustry.plugins.survivalpvp;

import arc.*;
import arc.files.*;
import arc.struct.*;
import arc.util.*;
import arc.util.serialization.*;

import arc.util.serialization.JsonWriter.*;
import mindustry.*;
import mindustry.ai.*;
import mindustry.content.*;
import mindustry.core.GameState.*;
import mindustry.game.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.maps.*;
import mindustry.mod.Mods.*;
import mindustry.world.*;

import fr.xpdustry.distributor.core.command.*;
import fr.xpdustry.distributor.core.plugins.*;
import fr.xpdustry.distributor.core.util.*;
import fr.xpdustry.distributor.core.event.*;

import fr.xpdustry.plugins.survivalpvp.ext.*;
import fr.xpdustry.plugins.survivalpvp.beacon.*;

import static arc.util.Log.info;
import static mindustry.Vars.*;


public class SurvivalPvpPlugin extends RootPlugin{
    public static BeaconMap currentMap = null;
    public static final Json json;
    public static final Formatter f;
    public static final String gamemodeName = "SurvivalPvp";

    private static WaveSpawner waveSpawner;
    private static final BeaconEffects beaconEffects;
    private static final BeaconSpawner beaconSpawner;
    private static final BeaconCommands commands;

    private static final Interval interval = new Interval(1);
    private static final Rules rules;

    static{
        json = new Json();
        json.setOutputType(OutputType.json);
        json.setIgnoreUnknownFields(true);

        rules = new Rules();
        rules.tags.put(gamemodeName.toLowerCase(), "true");
        rules.waves = true;
        rules.waveTimer = true;
        rules.modeName = gamemodeName;
        // TODO the default json serializer smells like shit, move to gson
        rules.bannedBlocks.add(Blocks.airFactory);

        f = new StringFormatter(ObjectMap.of(
            "c1", "red"
        ));

        commands = new BeaconCommands();
        beaconEffects = new BeaconEffects();
        beaconSpawner = new BeaconSpawner();

        // Needed events
        PostMan.bind(WorldLoadEvent.class);
    }

    @Override
    public void init(){
        waveSpawner = spawner;

        Fi directory = getMapPath();
        if(!directory.exists()){
            directory.mkdirs();
        }

        Events.run(Trigger.update.getClass(), () -> {
            if(isActive()){
                if(interval.get(0, Time.toSeconds / 10)){
                    beaconEffects.draw();
                }
            }
        });
    }

    public void loadMap(Map map){
        // Load the map
        currentMap = BeaconMap.fromFile(getMapPath(), map.name());

        currentMap.beacons.add(new Beacon(10, 10, 1));
        currentMap.beacons.add(new Beacon(50, 50, 4));
    }

    public void loadRules(){
        // Setup the rules
        rules.waveSpacing = currentMap.waveSpacing * Time.toSeconds;
        rules.dropZoneRadius = currentMap.dropZoneRadius * tilesize;
    }

    public void setupWorld(){
        // Remove all default spawns
        for(Tile tile : world.tiles){
            if(tile.overlay() == Blocks.spawn){
                tile.setOverlay(Blocks.air);
            }
        }

        // Put the beacon spawns for the client
        currentMap.beacons.forEach(beacon -> {
            world.tiles.get(beacon.x, beacon.y).setOverlay(Blocks.spawn);
        });
    }

    public void loadSettings(){
        beaconEffects.reset();

        // Overwrite the spawn
        Vars.spawner = beaconSpawner;

        // Resets the wave spawner once the gamemode ends
        PostMan.on(new GlassWatcher<>(WorldLoadEvent.class, event -> {
            spawner = waveSpawner;
            currentMap.saveFile(getMapPath());
            currentMap = null;
        }));
    }

    /**
     * This method is called when the game register the server-side commands.
     * Make sure your plugin don't load the commands twice by adding a simple boolean check.
     */
    @Override
    public void registerServerCommands(CommandHandler handler){
        handler.register("survivalpvp", "[map]", "Triggers the survivalpvp gamemode...", args -> {
            if(!state.is(State.menu)){
                Log.err("Stop the server first.");
                return;
            }

            Map map;
            if(args.length == 0){
                map = maps.all().random(state.map);
            }else{
                map = maps.byName(args[0]);
                if(map == null){
                    info("Map not found.");
                    return;
                }
            }

            logic.reset();

            loadMap(map);
            loadRules();

            world.loadMap(map, map.rules(rules));
            state.rules = rules.copy();

            setupWorld();
            loadSettings();

            logic.play();
            netServer.openServer();
        });
    }

    @Override
    public void registerClientCommands(CommandHandler handler){
        Commands.registerToHandler(handler, commands);


        // For testing, I guess...
        handler.<Player>register("js", "<script...>", "Run arbitrary Javascript.", (args, player) -> {
            if(player.admin()){
                player.sendMessage(mods.getScripts().runConsole(args[0]));
            }else{
                player.sendMessage("No");
            }
        });
    }

    public static boolean isActive(){
        return state.rules.tags.getBool(gamemodeName.toLowerCase()) && !state.is(State.menu);
    }

    public LoadedMod asMod(){
        return mods.getMod(this.getClass());
    }

    public Fi getMapPath(){
        return new Fi(Core.files.external("./xpdustry/" + asMod().meta.name + "/maps/").absolutePath());
    }
}
