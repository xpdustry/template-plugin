package fr.xpdustry.plugins.survivalpvp.beacon;

import arc.func.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;

import mindustry.ai.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.world.*;

import static mindustry.Vars.*;
import static fr.xpdustry.plugins.survivalpvp.SurvivalPvpPlugin.currentMap;


public class BeaconSpawner extends WaveSpawner{
    private boolean spawning = false;

    @Override
    public int countSpawns(){
        return currentMap.beacons.size;
    }

    @Override
    public Seq<Tile> getSpawns(){
        return currentMap.beacons.map(Beacon::asTile);
    }

    @Override
    public boolean playerNear(){
        return !currentMap.beacons.isEmpty() && !player.dead() && currentMap.beacons.contains(b -> Mathf.dst(b.getTileX(), b.getTileY(), player.x, player.y) < state.rules.dropZoneRadius && player.team() != b.getTeam());
    }

    @Override
    public void spawnEnemies(){
        spawning = true;

        eachGroundSpawn(beacon -> {
            doShockwave(beacon.getTileX(), beacon.getTileY());
        });

        for(SpawnGroup group : state.rules.spawns){
            if(group.type == null) continue;

            int spawned = group.getSpawned(state.wave - 1);

            if(!group.type.flying){
                float spread = tilesize * 2;

                eachGroundSpawn(beacon -> {
                    for(int i = 0; i < spawned; i++){
                        Tmp.v1.rnd(spread);
                        Unit unit = group.createUnit(beacon.getTeam(), state.wave - 1);
                        unit.set(beacon.getTileX() + Tmp.v1.x, beacon.getTileY() + Tmp.v1.y);
                        spawnEffect(unit);
                    }
                });
            }
        }

        Time.run(121f, () -> spawning = false);
    }

    public void doShockwave(float x, float y){
        Fx.spawnShockwave.at(x, y, state.rules.dropZoneRadius);
        Damage.damage(Beacon.getDamageTeam(), x, y, state.rules.dropZoneRadius, 99999999f, true);
    }

    private void eachGroundSpawn(Cons<Beacon> cons){
        if(!currentMap.beacons.isEmpty()){
            for(Beacon beacon : currentMap.beacons){
                cons.get(beacon);
            }
        }
    }

    private void spawnEffect(Unit unit){
        unit.rotation = unit.angleTo(world.width()/2f * tilesize, world.height()/2f * tilesize);
        unit.apply(StatusEffects.unmoving, 30f);
        unit.add();

        Call.spawnEffect(unit.x, unit.y, unit.rotation, unit.type);
    }

    @Override
    public boolean isSpawning(){
        return spawning && !net.client();
    }
}
