package me.frostwolf74.vindicterraStaffUtils.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class KickCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player p){
            if(!p.hasPermission("VSU.punish.kick")) return false;

            Player target = p.getServer().getPlayer(strings[0]);

            if(target != null){
                p.sendMessage(Component.text("Player not found or not online", NamedTextColor.RED));
                return true;
            }

        }
        return false;
    }
}