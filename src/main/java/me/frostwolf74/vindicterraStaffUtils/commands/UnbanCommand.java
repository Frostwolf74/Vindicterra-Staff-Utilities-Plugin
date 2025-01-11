package me.frostwolf74.vindicterraStaffUtils.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;

import org.jetbrains.annotations.NotNull;

public class UnbanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player p){
            if(!p.hasPermission("VSU.punish.ban")) return true;

            OfflinePlayer target = Bukkit.getPlayer(strings[0]);

            if (target == null) {
                p.sendMessage(Component.text("Player not found", NamedTextColor.RED));
                return true;
            }

            if(target.isBanned()){
                target.getPlayer().getServer().getBanList(BanList.Type.NAME).pardon(target.getName());
                p.sendMessage(Component.text("\n" + target.getName() + " has been unbanned\n", NamedTextColor.GREEN));
            }
            else{
                p.sendMessage(Component.text("Player not banned", NamedTextColor.RED));
            }

            return true;
        }
        else{
            commandSender.getServer().getLogger().info("You must use /pardon if you wish to unban from console.");
            return true;
        }
    }
}
