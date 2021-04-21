package fr.xpdustry.plugins.template;

import arc.util.CommandHandler;
import mindustry.mod.Plugin;

@SuppressWarnings("unused")  // <- Only used for this template so IntelliJ stop screaming at me...
public class TemplateClass extends Plugin{

    @Override
    public void init(){
        //called when game initializes
    }

    @Override
    public void registerServerCommands(CommandHandler handler){
        //register commands that run on the server
    }

    @Override
    public void registerClientCommands(CommandHandler handler){
        //register commands that player can invoke in-game
    }
}
