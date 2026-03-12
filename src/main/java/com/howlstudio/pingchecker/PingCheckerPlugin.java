package com.howlstudio.pingchecker;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
/** PingChecker — /ping shows your connection latency. /pings lists all online players with pings. */
public final class PingCheckerPlugin extends JavaPlugin {
    public PingCheckerPlugin(JavaPluginInit init){super(init);}
    @Override protected void setup(){
        System.out.println("[Ping] Loading...");
        PingManager mgr=new PingManager();
        new PingListener(mgr).register();
        CommandManager.get().register(mgr.getPingCommand());
        CommandManager.get().register(mgr.getPingsCommand());
        System.out.println("[Ping] Ready.");
    }
    @Override protected void shutdown(){System.out.println("[Ping] Stopped.");}
}
