package fr.xpdustry.plugins.survivalpvp.beacon;

import mindustry.content.*;
import mindustry.game.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

import static mindustry.Vars.*;


public class Beacon{
    public final int x;
    public final int y;
    public final int t; // team id

    public Beacon(int x, int y, int t){
        this.x = x;
        this.y = y;
        this.t = t;
    }

    /** Returns the team associated with the id, stops at 254 */
    public Team getTeam(){
        return Team.get(t % 255);
    }

    /** Team 255 is the damage team... */
    public static Team getDamageTeam(){
        return Team.get(255);
    }

    public Tile asTile(){
        return new Tile(x * tilesize, y * tilesize){{
            overlay = (Floor)Blocks.spawn;
        }};
    }

    public int getTileX(){
        return x * tilesize;
    }

    public int getTileY(){
        return y * tilesize;
    }
}