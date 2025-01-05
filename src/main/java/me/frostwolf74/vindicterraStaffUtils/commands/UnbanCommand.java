package me.frostwolf74.vindicterraStaffUtils.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import org.bukkit.BanList;
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
            OfflinePlayer target = p.getServer().getPlayer(strings[0]);

            if (target == null) {
                p.sendMessage(Component.text("Player not found", TextColor.color(255, 0 , 0)));
                return true;
            }

            if(target.isBanned()){
                target.getPlayer().getServer().getBanList(BanList.Type.NAME).pardon(target.getName());
            }
            else{
                p.sendMessage(Component.text("Player not found", TextColor.color(255, 0 , 0)));
            }

            return true;
        }
        return false;
    }
}
