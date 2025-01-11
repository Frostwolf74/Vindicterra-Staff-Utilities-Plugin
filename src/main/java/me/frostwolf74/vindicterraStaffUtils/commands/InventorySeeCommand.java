package me.frostwolf74.vindicterraStaffUtils.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;


public class InventorySeeCommand implements CommandExecutor {
    // TODO needs player-on-player testing
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 0) return false;

        if(commandSender instanceof Player p){
            if(!(p.hasPermission("VSU.inventorysee"))) return true;

            if(p.getServer().getPlayer(strings[0]) == null){
                p.sendMessage(Component.text("Player not found or not online", NamedTextColor.RED));
                return true;
            }

            p.getServer().dispatchCommand(p, "inv " + p.getServer().getPlayer(strings[0]).getName());

            return true;
        }
        return false;
    }
}