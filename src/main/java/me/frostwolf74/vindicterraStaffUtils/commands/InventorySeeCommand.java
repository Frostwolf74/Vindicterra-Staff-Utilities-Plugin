package me.frostwolf74.vindicterraStaffUtils.commands;

import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import org.jetbrains.annotations.NotNull;

public class InventorySeeCommand implements CommandExecutor { // TODO needs player-on-player testing
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 0) return false;

        if(commandSender instanceof Player p){
            if(!(p.hasPermission("VSU.inventorysee"))) return true;

            if(p.getServer().getPlayer(strings[0]) == null){
                p.sendMessage(Component.text("Player not found or not online"));
            }

            openPlayerInventory(p, p.getServer().getPlayer(strings[0]));
        }
        return false;
    }

    public static void openPlayerInventory(Player p, Player target) {
        Inventory targetInventory = Bukkit.createInventory(p, 9 * 6, "Inventory: " + target.getName());

        targetInventory.setContents(target.getInventory().getContents());

        p.openInventory(targetInventory);
    }
}