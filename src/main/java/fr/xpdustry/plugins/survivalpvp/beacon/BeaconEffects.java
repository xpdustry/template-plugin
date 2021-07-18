package fr.xpdustry.plugins.survivalpvp.beacon;

import arc.math.*;
import arc.math.geom.*;

import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;

import static mindustry.Vars.*;
import static fr.xpdustry.plugins.survivalpvp.SurvivalPvpPlugin.*;


public class BeaconEffects{
    private float[] circle;
    private final Rand rand = new Rand();

    private final Effect[] effects = {
        Fx.mine, Fx.mineBig, Fx.mineHuge};

    public void reset(){
        circle = Geometry.regPoly(
            Mathf.floor(currentMap.dropZoneRadius * Mathf.pi), currentMap.dropZoneRadius * tilesize);
    }

    public void draw(){
        currentMap.beacons.forEach(b -> {
            Geometry.iteratePolygon((x, y) -> {
                Call.effect(effects[rand.random(0, 2)], x + b.getTileX(), y + b.getTileY(), 0, b.getTeam().color);
            }, circle);
        });
    }
}
