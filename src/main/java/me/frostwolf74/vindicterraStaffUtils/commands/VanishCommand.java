package me.frostwolf74.vindicterraStaffUtils.commands;
import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;


public class VanishCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) { // TODO needs player-on-player testing
        if(commandSender instanceof Player p){
            if(!(p.hasPermission("VSU.vanish"))) return true;

            if(Boolean.TRUE.equals(p.getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isVanished"), PersistentDataType.BOOLEAN))){
                p.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isVanished"), PersistentDataType.BOOLEAN, false);
                if(strings.length < 1){ // unvanishes self
                    vanishPlayer(p, true);
                }
                else{ // unvanishes selected player
                    if(p.getServer().getPlayer(strings[0]) == null){
                        p.sendMessage(Component.text("Player not found or not online", NamedTextColor.RED));
                        return true;
                    }

                    Player target = p.getServer().getPlayer(strings[0]);

                    vanishPlayer(target, true);
                }
            }
            else{
                if(strings.length < 1){ // vanishes self
                    vanishPlayer(p, false);
                }
                else{ // vanishes selected player
                    if(p.getServer().getPlayer(strings[0]) == null){
                        p.sendMessage(Component.text("Player not found or not online", NamedTextColor.RED));
                        return true;
                    }

                    Player target = p.getServer().getPlayer(strings[0]);
                    vanishPlayer(target, false);
                }
            }
            return true;
        }
        return false;
    }

    public static void vanishPlayer(Player p, Boolean unvanish){
        for(Player onlinePlayer : p.getServer().getOnlinePlayers()){
            if(onlinePlayer.equals(p) || (onlinePlayer.hasPermission("VSU") && !unvanish)){ // prevents self and other staff from being invisible
                continue;
            }

            if(unvanish){
                onlinePlayer.showPlayer(VindicterraStaffUtils.getPlugin(), p);
            }
            else{
                onlinePlayer.hidePlayer(VindicterraStaffUtils.getPlugin(), p);
            }
        }

        p.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isVanished"), PersistentDataType.BOOLEAN, !unvanish);

        if(unvanish){
            p.sendMessage(Component.text("You are now visible."));

            p.getServer().dispatchCommand(p, "dynmap show");

            VindicterraStaffUtils.getRunningTasks().get(p.getUniqueId()).cancel();
            VindicterraStaffUtils.getRunningTasks().remove(p.getUniqueId());
        }
        else{
            p.sendMessage(Component.text("You are now invisible."));

            p.getServer().dispatchCommand(p, "dynmap hide"); // shortcut to hiding on dynmap

            BukkitTask actionBarTask = p.getServer().getScheduler().runTaskTimer(VindicterraStaffUtils.getPlugin(), () -> { // keeps action bar text above hotbar (typically disappears after 3 secs otherwise)
                p.sendActionBar(Component.text("Vanished", TextColor.color(96, 96, 96)));
            }, 0L, 40L); // updates every 2 secs

            VindicterraStaffUtils.getRunningTasks().put(p.getUniqueId(), actionBarTask);
        }
    }
}
