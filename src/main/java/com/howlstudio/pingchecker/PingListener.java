package com.howlstudio.pingchecker;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
public class PingListener {
    private final PingManager mgr;
    public PingListener(PingManager m){this.mgr=m;}
    public void register(){
        HytaleServer.get().getEventBus().registerGlobal(PlayerReadyEvent.class,e->{Player p=e.getPlayer();if(p==null)return;PlayerRef r=p.getPlayerRef();if(r!=null)mgr.onJoin(r.getUuid());});
        HytaleServer.get().getEventBus().registerGlobal(PlayerDisconnectEvent.class,e->{PlayerRef r=e.getPlayerRef();if(r!=null)mgr.onLeave(r.getUuid());});
    }
}
