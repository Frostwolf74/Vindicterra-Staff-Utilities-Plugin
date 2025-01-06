package me.frostwolf74.vindicterraStaffUtils.commands;

import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;
import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class InventorySeeCommand implements CommandExecutor {
    // TODO needs player-on-player testing
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

    public static void updatePlayerInventoryState(Player p) {
        Inventory inventory = p.getInventory();
        ItemStack[] contents = inventory.getContents();
        ItemStack[] armorContents = p.getInventory().getArmorContents();

        ItemStack[] craftingItems = new ItemStack[4];

        for(int i = 0; i < 4; i++) {
            craftingItems[i] = inventory.getItem(i);
        }

//        PlayerInventory state = new PlayerInventory(contents, armorContents, craftingItems);

        Map<UUID, PlayerInventory>  inventoryStates = VindicterraStaffUtils.getInventoryStates();
//        inventoryStates.put(p.getUniqueId(), state);

        VindicterraStaffUtils.setInventoryStates(inventoryStates);
    }
}