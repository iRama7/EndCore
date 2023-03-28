package rama;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class EndCore extends JavaPlugin implements Listener {

    public static Plugin plugin;
    @Override
    public void onEnable() {
        plugin = this;
        this.saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getCommand("endcore").setExecutor(new Commands());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public static void pluginLog(String print){
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', print));
    }

    public void pluginBroadcast(String broadcast){
        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', broadcast));
        }
    }



}
