package rama;

import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;

import static rama.EndCore.*;

public class WorldManagement {

    public void unloadWorld(World world){
        String teleport = plugin.getConfig().getString("language.teleport");
        for(Player p : world.getPlayers()){
            Location teleport_location = plugin.getConfig().getLocation("config.teleport_location");
            p.teleport(teleport_location);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', teleport));
        }
        Boolean a = Bukkit.unloadWorld(world, false);
        if(a) {
            pluginLog("&eProgress: [&f##--------&e] &f25%");
        }else{
            BukkitScheduler scheduler = Bukkit.getScheduler();
            scheduler.cancelTasks(plugin);
        }
    }

    public void deleteWorld(String world){
        File world_folder = new File(Bukkit.getWorldContainer()+"/"+world);
        world_folder.delete();
        pluginLog("&eProgress: [&f#####-----&e] &f50%");
    }

    public void copyBackupFolder(String backup_name, String world_name) throws IOException {
        FileUtils.copyDirectory(new File("plugins/EndCore/" + backup_name), new File(world_name));
        pluginLog("&eProgress: [&f#######---&e] &f75%");
    }

    public void createEndWorld(String world_name){
        World world = Bukkit.createWorld(new WorldCreator(world_name).environment(World.Environment.THE_END));
        world.setKeepSpawnInMemory(false);
        pluginLog("&eProgress: [&f##########&e] &f100%");
        pluginLog("&aEnd restart complete!");
        String complete = plugin.getConfig().getString("language.restart_complete");
        pluginBroadcast(complete);
    }

    public void restartWorld() throws IOException {

        /*
        Progress: [----------]
        */

        pluginLog("&eProgress: [&f----------&e] &f0%");
        String restarting = plugin.getConfig().getString("language.restarting");
        pluginBroadcast(restarting);

        Long delay = plugin.getConfig().getLong("config.time_between_actions");
        String world_name = plugin.getConfig().getString("config.world_name");

        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(plugin, () -> {
            pluginLog("&eProgress: [&f#---------&e] &f12%");
            unloadWorld(Bukkit.getWorld(world_name));
        }, delay);


        scheduler.runTaskLater(plugin, () -> {
            pluginLog("&eProgress: [&f###-------&e] &f37%");
            deleteWorld(world_name);
        }, delay);


        scheduler.runTaskLater(plugin, () -> {
            pluginLog("&eProgress: [&f######----&e] &f62%");
            try {
                copyBackupFolder("end_backup", world_name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, delay);

        scheduler.runTaskLater(plugin, () -> {
            pluginLog("&eProgress: [&f########--&e] &f87%");
            createEndWorld(world_name);
        }, delay);


    }


}
