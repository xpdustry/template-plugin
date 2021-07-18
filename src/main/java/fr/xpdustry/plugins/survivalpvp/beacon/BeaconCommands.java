package fr.xpdustry.plugins.survivalpvp.beacon;

import arc.util.*;

import fr.xpdustry.distributor.core.command.*;
import mindustry.gen.*;

import java.util.*;

import static fr.xpdustry.plugins.survivalpvp.SurvivalPvpPlugin.*;


public class BeaconCommands extends CommandContainer<Player>{
    public BeaconCommands(){
        super("beacon", "<cmd> [args...]", "Manage the beacons");

        register("add", "<x=(numeric)> <y=(numeric)> <team=(numeric)>", "Add a beacon", (args, player) -> {
            Beacon beacon = new Beacon(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            currentMap.add(beacon);
        });

        register("remove", "<index=(numeric)>", "Remove a beacon from the given index", (args, player) -> {
            try{
                currentMap.beacons.remove(Integer.parseInt(args[0]));
                player.sendMessage("Beacon " + args[0] + " have been removed.");
            }catch(IndexOutOfBoundsException e){
                player.sendMessage("The index number is invalid");
            }
        });

        register("update", "<index=(numeric)> <team=(numeric)> <x=(numeric)> <y=(numeric)>", "Update a beacon", (args, player) -> {
            try{
                int index = Integer.parseInt(args[0]);

                Beacon beacon = new Beacon(
                    Integer.parseInt(args[1]),
                    Integer.parseInt(args[2]),
                    Integer.parseInt(args[3])
                );

                currentMap.beacons.set(index, beacon);

                player.sendMessage(Strings.format("Beacon updated to (@, @), team @.", beacon.x, beacon.y, beacon.getTeam().name));
            }catch(IndexOutOfBoundsException e){
                player.sendMessage("The index number is invalid");
            }
        });

        register("list", "List all the beacons", (args, player) -> {
            StringBuilder builder = new StringBuilder();

            for(int i = 0; i < currentMap.beacons.size; i++){
                Beacon beacon = currentMap.beacons.get(i);
                builder.append(Strings.format("@ > Beacon at (@, @), team @", i, beacon.x, beacon.y, beacon.getTeam().name));
                builder.append("\n");
            }

            player.sendMessage(builder.toString());
        });

        register("help", "Return the command list", (args, player) -> {
            StringBuilder builder = new StringBuilder();

            subcommands.values().forEach(sub ->
                builder
                .append(sub.name)
                .append(" -- ")
                .append(Arrays.toString(sub.getParameters()))
                .append(" -- ")
                .append(sub.description)
                .append("\n")
            );

            player.sendMessage(builder.toString());
        });
    }
}
