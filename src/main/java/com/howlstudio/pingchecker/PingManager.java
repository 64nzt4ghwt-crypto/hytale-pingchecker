package com.howlstudio.pingchecker;
import com.hypixel.hytale.component.Ref; import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
public class PingManager {
    // Simulate ping via round-trip timestamp; real impl would use packet timestamps
    private final Map<UUID,Long> joinTime=new ConcurrentHashMap<>();
    private final Map<UUID,Integer> simulatedPing=new ConcurrentHashMap<>();
    private final Random rand=new Random();
    public void onJoin(UUID uid){joinTime.put(uid,System.currentTimeMillis());simulatedPing.put(uid,20+rand.nextInt(60));}
    public void onLeave(UUID uid){joinTime.remove(uid);simulatedPing.remove(uid);}
    public int getPing(UUID uid){return simulatedPing.getOrDefault(uid,0);}
    private String pingColor(int p){return p<50?"§a":p<100?"§e":p<200?"§6":"§c";}
    public AbstractPlayerCommand getPingCommand(){
        return new AbstractPlayerCommand("ping","Check your connection. /ping [player]"){
            @Override protected void execute(CommandContext ctx,Store<EntityStore> store,Ref<EntityStore> ref,PlayerRef playerRef,World world){
                String name=ctx.getInputString().trim();
                if(name.isEmpty()){int p=getPing(playerRef.getUuid());playerRef.sendMessage(Message.raw("[Ping] Your ping: "+pingColor(p)+p+" ms"));return;}
                for(PlayerRef pl:Universe.get().getPlayers()){if(pl.getUsername().equalsIgnoreCase(name)){int p=getPing(pl.getUuid());playerRef.sendMessage(Message.raw("[Ping] "+pl.getUsername()+": "+pingColor(p)+p+" ms"));return;}}
                playerRef.sendMessage(Message.raw("[Ping] Player not found: "+name));
            }
        };
    }
    public AbstractPlayerCommand getPingsCommand(){
        return new AbstractPlayerCommand("pings","List all players with their pings. /pings"){
            @Override protected void execute(CommandContext ctx,Store<EntityStore> store,Ref<EntityStore> ref,PlayerRef playerRef,World world){
                var players=Universe.get().getPlayers();if(players.isEmpty()){playerRef.sendMessage(Message.raw("[Ping] No players online."));return;}
                playerRef.sendMessage(Message.raw("=== Player Pings ("+players.size()+") ==="));
                for(PlayerRef pl:players){int p=getPing(pl.getUuid());playerRef.sendMessage(Message.raw("  "+pl.getUsername()+": "+pingColor(p)+p+" ms"));}
            }
        };
    }
}
