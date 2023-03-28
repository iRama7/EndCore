package rama;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class EndCore extends JavaPlugin implements Listener {

    public static Plugin plugin;
    @Override
    public void onEnable() {
        plugin = this;
        this.saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void chatEvent(PlayerChatEvent e) throws IOException {
        if(e.getMessage().equals("restart")){
            WorldManagement wm = new WorldManagement();
            wm.restartWorld();
        }
    }



}
