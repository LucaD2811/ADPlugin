package me.LucaD2811.ADPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public final class ADPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    @Override
    public void onDisable() {
    }


    HashSet<UUID> cooldown = new HashSet<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("ad")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "The console cannot use this command");
                return false;
            }
            final Player player = (Player) sender;


            if(cooldown.contains(player.getUniqueId())){
                sender.sendMessage(ChatColor.RED + "Please wait " + getConfig().getInt("AdCooldownInSeconds") + " seconds before using this again!");
                return false;
            }
            if(args.length == 0){
                sender.sendMessage(ChatColor.RED + "Usage: /ad <message>");
                return false;
            }
            StringBuilder str = new StringBuilder();
            for(int i = 0; i<args.length; i++){
                str.append(args[i] + " ");
            }
            if(str.length() > getConfig().getInt("MaxAdLengthInCharacters")){
                sender.sendMessage(ChatColor.RED + "Your ad is too long! The maximum length is " + getConfig().getInt("MaxAdLengthInCharacters") + " characters.");
                return false;
            }
            String ad = str.toString();
            cooldown.add(player.getUniqueId());

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, () -> cooldown.remove(player.getUniqueId()), getConfig().getInt("AdCooldownInSeconds") * 20L);
            Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "AD" + ChatColor.GRAY + "] " + player.getName() + ": " + ChatColor.YELLOW + ad);
        }
        return false;
    }
}
