package rama;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

import static rama.EndCore.plugin;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUse &e/endcore reset &cto reset the end world."));
        }else if(args.length == 1 && sender.hasPermission("endcore.*")){
            if(args[0].equalsIgnoreCase("reset")){
                WorldManagement wm = new WorldManagement();
                try {
                    wm.restartWorld();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eTrying to reset the end world right now."));
            }else if(args[0].equalsIgnoreCase("reload")){
                plugin.reloadConfig();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aConfig reloaded."));
            }else if(args[0].equalsIgnoreCase("setLocation")){
                Player p = (Player) sender;
                Location l = p.getLocation();
                plugin.getConfig().set("config.teleport_location", l);
                plugin.saveConfig();
                plugin.reloadConfig();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aTeleport location saved."));
            }
        }
        return false;
    }
}
