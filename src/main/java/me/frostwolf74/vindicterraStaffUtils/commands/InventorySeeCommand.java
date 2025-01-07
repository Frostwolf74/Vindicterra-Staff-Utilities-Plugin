package me.frostwolf74.vindicterraStaffUtils.commands;

import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;
import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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

            return openPlayerInventory(p, p.getServer().getPlayer(strings[0]));
        }
        return false;
    }

    public static boolean openPlayerInventory(Player p, Player target) {
        Inventory inv = updatePlayerInventory(target);

        p.openInventory(inv);

        p.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "viewingInventory"), PersistentDataType.BOOLEAN, true);

        Map<UUID, Inventory> inventoryMap = VindicterraStaffUtils.getopenedInventories();
        inventoryMap.put(target.getUniqueId(), inv);

        VindicterraStaffUtils.setOpenedInventories(inventoryMap);

        return true;
    }

    public static Inventory updatePlayerInventory(Player target) {
        ItemStack[] ti = target.getInventory().getContents();

        Inventory inv = Bukkit.createInventory(null, 9*6);

        inv.setContents(ti);

//        should look like this
//        "! ! ! ! @ $ $ $ $", <- armor slots + offhand + crafting matrix
//        "# # # # # # # # #", <- storage slots
//        "# # # # # # # # #",
//        "# # # # # # # # #",
//        "% % % % % % % % %", <- hotbar slots
//        "& & & < & > & & &"  <- page selector (inventory <-> ender chest)


//        looks like this when taken directly from Inventory#contents()
//        "% % % % % % % % %", <- hotbar slots
//        "# # # # # # # # #", <- storage slots
//        "# # # # # # # # #",
//        "# # # # # # # # #",
//        "! ! ! ! @ $ $ $ $", <- armor slots + offhand + crafting matrix

        ItemStack[] hotBarSlots = new ItemStack[10]; // swapping placement to reflect real inventory
        ItemStack[] armorAndOffhandSlots = new ItemStack[10];

        inv.addItem(new ItemStack(Material.AIR)); // extending the length at the end
        inv.addItem(new ItemStack(Material.AIR));
        inv.addItem(new ItemStack(Material.AIR));
        inv.addItem(new ItemStack(Material.AIR));

        for(int i = 0; i < 9; ++i){ //
            hotBarSlots[i] = nullItemCheck(inv.getItem(i));
        }

        int j = -1;
        for(int i = 36; i < 44; ++i){
            if(i > 41){
                armorAndOffhandSlots[++j] = new ItemStack(Material.AIR);
            }
            else {
                armorAndOffhandSlots[++j] = nullItemCheck(inv.getItem(i));
            }
        }

        for(int i = 0; i < 9; ++i){
            inv.setItem(i, armorAndOffhandSlots[i]);
        }

        int k = -1;
        for(int i = 36; i < 45; i++){
            inv.setItem(i, hotBarSlots[++k]);
        }

        for(int i = 46; i < 54; ++i){
            inv.addItem(new ItemStack(Material.AIR));
        }

        return inv;
    }

    public static ItemStack nullItemCheck(ItemStack item){
        if(item == null) return new ItemStack(Material.AIR);
        else return item;
    }
}