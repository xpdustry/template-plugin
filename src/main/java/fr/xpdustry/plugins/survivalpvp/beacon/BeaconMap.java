package fr.xpdustry.plugins.survivalpvp.beacon;

import arc.files.*;
import arc.struct.*;

import arc.util.serialization.*;
import arc.util.serialization.Json.*;
import mindustry.*;

import static fr.xpdustry.plugins.survivalpvp.SurvivalPvpPlugin.*;


public class BeaconMap{
    public String map;
    public Seq<Beacon> beacons;
    public int waveSpacing = 10;
    public float dropZoneRadius = 5;

    public BeaconMap(String map){
        this.map = map;
        this.beacons = new Seq<>();
    }

    public static BeaconMap fromFile(Fi directory, String map){
        Fi file = new Fi(directory.absolutePath() + "/" + map + ".json");

        if(file.exists()){
            return json.fromJson(BeaconMap.class, file.reader());
        }else{
            return new BeaconMap(map);
        }
    }

    public void saveFile(Fi directory){
        Fi file = new Fi(directory.absolutePath() + "/" + map + ".json");
        file.writeString(json.prettyPrint(this));
    }

    public void add(Beacon beacon){
        beacons.add(beacon);
    }
}
