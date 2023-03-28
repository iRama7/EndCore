package rama;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;

import static rama.EndCore.plugin;

public class WorldManagement {

    public void unloadWorld(World world){
        for(Player p : world.getPlayers()){
            p.teleport(Bukkit.getWorld("world").getSpawnLocation());
        }
        Bukkit.unloadWorld(world, false);
        Bukkit.getLogger().info("Unloading world "+world.getName());
    }

    public void deleteWorld(String world){
        File world_folder = new File(Bukkit.getWorldContainer()+"/"+world);
        world_folder.delete();
        Bukkit.getLogger().info("Removing world "+world);
    }

    public void copyBackupFolder(String backup_name, String world_name) throws IOException {
        FileUtils.copyDirectory(new File("plugins/EndCore/" + backup_name), new File(world_name));
        Bukkit.getLogger().info("Copying backup "+backup_name + " to "+ world_name);
    }

    public void createEndWorld(String world_name){
        World world = Bukkit.createWorld(new WorldCreator(world_name).environment(World.Environment.THE_END));
        world.setKeepSpawnInMemory(false);
        Bukkit.getLogger().info("Loading world "+world_name );
    }

    public void restartWorld() throws IOException {

        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(plugin, () -> {
            unloadWorld(Bukkit.getWorld("world_the_end"));
        }, 40L);


        scheduler.runTaskLater(plugin, () -> {
            deleteWorld("world_the_end");
        }, 40L);

        scheduler.runTaskLater(plugin, () -> {
            try {
                copyBackupFolder("end_backup", "world_the_end");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 40L);

        scheduler.runTaskLater(plugin, () -> {
            createEndWorld("world_the_end");
        }, 40L);


    }

}
