package rama;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class EndCore extends JavaPlugin implements Listener {

    public static DelayedTask task;
    private long hours;

    public static CronMain cronMain = new CronMain();

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
            Bukkit.getPluginManager().registerEvents(this, this);
        } else {
            pluginLog("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        new Placeholders().register();
        cronMain.buildUtils(plugin.getConfig().getString("config.dragon_timer"));
    }

    @Override
    public void onDisable() {
        task.saveTime();
    }


    public static void pluginLog(String print){
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', print));
    }

    public static void pluginBroadcast(String broadcast){
        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', broadcast));
        }
    }

    public static void executeCommands(List<String> commands){
        for(String c : commands){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), c);
        }
    }



}
