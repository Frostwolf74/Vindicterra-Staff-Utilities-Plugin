package me.frostwolf74.vindicterraStaffUtils.commands;

import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InventorySeeCommand implements CommandExecutor {
    private Gui inventoryViewGui;

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
        PlayerInventory ti = target.getInventory();

//        "! ! ! ! @ $ $ $ $", <- armor slots + offhand + crafting matrix
//        "# # # # # # # # #", <- storage slots
//        "# # # # # # # # #",
//        "# # # # # # # # #",
//        "% % % % % % % % %", <- hotbar slots
//        "& & & < & > & & &"  <- page selector (inventory <-> ender chest)

        Inventory inv = Bukkit.createInventory(null, 9*6);

        List<ItemStack> itemList = new ArrayList<>();

        itemList.add(0, ti.getItem(103)); // armour slots
        itemList.add(1, ti.getItem(102));
        itemList.add(2, ti.getItem(101));
        itemList.add(3, ti.getItem(100));
        itemList.add(4, ti.getItem(40)); // off-hand
        itemList.add(5, ti.getItem(80)); // crafting grid
        itemList.add(6, ti.getItem(81));
        itemList.add(7, ti.getItem(82));
        itemList.add(8, ti.getItem(83));

        int index1 = 9;
        int index2 = 36;

        for(int i = 9; i < 35; i++){
            itemList.add(index1, nullSlotItemCheck(ti, i)); // storage slots
            ++index1;
        }
        itemList.add(35, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        for(int i = 0; i < 8; i++){
            itemList.add(index2, nullSlotItemCheck(ti, i));
            ++index2;
        }

        itemList.forEach(inv::addItem);

        p.openInventory(inv);

        return true;
    }

    public static ItemStack nullSlotItemCheck(PlayerInventory in, int slotIndex){
        if(in.getItem(slotIndex) == null) return new ItemStack(Material.AIR);
        else return in.getItem(slotIndex);
    }
}