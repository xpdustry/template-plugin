package fr.xpdustry.plugins.survivalpvp.ext;

import arc.func.*;
import fr.xpdustry.distributor.core.event.*;


/**
 * A watcher that dies after being triggered...
 * Might add it in Distributor...
 */
public class GlassWatcher<T> extends Watcher<T>{
    public GlassWatcher(Class<T> event, Cons<T> listener){
        super(event, listener);
    }

    @Override
    public void trigger(T type){
        try{
            listener.get(type);
        }finally{
            PostMan.remove(this);
        }
    }
}
