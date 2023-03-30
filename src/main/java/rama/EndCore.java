package rama;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class EndCore extends JavaPlugin implements Listener {

    public static DelayedTask task;
    private long hours;

    public static Plugin plugin;
    @Override
    public void onEnable() {
        plugin = this;
        this.saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getCommand("endcore").setExecutor(new Commands());
        long time = this.getConfig().getLong("time");
        if(time == 0){
            hours = this.getConfig().getLong("config.restart_time");
        }else{
            hours = (time / 3600000);
            if(hours < 1){
                hours = 1;
            }
        }
        task = new DelayedTask(this, hours);
        task.start();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            /*
             * We register the EventListener here, when PlaceholderAPI is installed.
             * Since all events are in the main class (this class), we simply use "this"
             */
            Bukkit.getPluginManager().registerEvents(this, this);
        } else {
            /*
             * We inform about the fact that PlaceholderAPI isn't installed and then
             * disable this plugin to prevent issues.
             */
            pluginLog("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        new Placeholders().register();
    }

    @Override
    public void onDisable() {
        task.saveTime();
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
