package me.frostwolf74.vindicterraStaffUtils.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UnbanCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player p){
            if(!p.hasPermission("VSU.punish.ban")) return true;

            if(strings[0].equals("all") && p.isOp()){ // unbans all players, ops have access only
                for(OfflinePlayer player : p.getServer().getBannedPlayers()) {
                    if(player.isBanned()){
                        p.getServer().getBanList(BanList.Type.NAME).pardon(p.getName());
                    }
                }

                p.sendMessage(Component.text("All players have been unbanned.", NamedTextColor.GREEN));
                return true;
            }

            OfflinePlayer target = p.getServer().getOfflinePlayer(strings[0]);

            if (target == null) {
                p.sendMessage(Component.text("Player not found", NamedTextColor.RED));
                return true;
            }

            if(target.isBanned()){
                p.getServer().getBanList(BanList.Type.NAME).pardon(target.getName());
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

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player p){
            List<String> list = new ArrayList<>();

            for(OfflinePlayer player : p.getServer().getBannedPlayers()) {
                list.add(player.getName());
            }
            return list;
        }
        return null;
    }
}
