package me.frostwolf74.vindicterraStaffUtils.listeners;

import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;
import me.frostwolf74.vindicterraStaffUtils.commands.InventorySeeCommand;
import me.frostwolf74.vindicterraStaffUtils.commands.StaffModeCommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class MenuClickEventListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) { // TODO needs player-on-player testing
        if(e.getWhoClicked() instanceof Player p) {
            if(e.getView().title().equals(Component.text("Online Players"))){
                e.setCancelled(true);

                TextComponent headName = (TextComponent) (e.getCurrentItem().getItemMeta().displayName());

                openPlayerManagementInterface(e.getWhoClicked().getServer().getPlayer(headName.content())); // will never be null
            }

            if(e.getView().title().contains(Component.text("Manage"))){
                e.setCancelled(true);

                TextComponent headName = (TextComponent) (e.getCurrentItem().getItemMeta().displayName());

                switch(e.getCurrentItem().getType()){
                    case CHEST:
                        InventorySeeCommand.openPlayerInventory((Player) e.getWhoClicked(), e.getWhoClicked().getServer().getPlayer(headName.content()));
                        break;
                    case ENDER_CHEST:

                }
            }

            if(e.getViewers().size() > 1){ // TODO debug
                for(HumanEntity hP : e.getViewers()){
                    if(hP instanceof Player viewer){
                        InventorySeeCommand.openPlayerInventory(p, viewer);
                    }
                }
            }

            // if in staff mode
            if(Boolean.TRUE.equals(p.getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "inStaffMode"), PersistentDataType.BOOLEAN))){
                e.setCancelled(true);
                p.setItemOnCursor(new ItemStack(Material.AIR));
                p.updateInventory();
            }
        }
    }

    private void openPlayerManagementInterface(Player p){
        p.closeInventory();

        Inventory inv = Bukkit.createInventory(p, 9*3, Component.text("Manage " + p.getName()));

        ItemStack invSee = new ItemStack(Material.CHEST);
        invSee.setItemMeta(StaffModeCommand.applyDisplayName(invSee, Component.text("See Inventory", TextColor.color(175, 0,255))));

        ItemStack enderSee = new ItemStack(Material.ENDER_CHEST);
        enderSee.setItemMeta(StaffModeCommand.applyDisplayName(enderSee, Component.text("See Ender Chest", TextColor.color(175, 0, 255))));

        ItemStack punish = new ItemStack(Material.BARRIER);
        punish.setItemMeta(StaffModeCommand.applyDisplayName(punish, Component.text("Punish Player", TextColor.color(175, 0,255))));

        ItemStack teleport = new ItemStack(Material.ENDER_EYE);
        teleport.setItemMeta(StaffModeCommand.applyDisplayName(teleport, Component.text("Teleport to Player", TextColor.color(175, 0,255))));

        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta playersHeadMeta = (SkullMeta) playerHead.getItemMeta();

        playersHeadMeta.setOwningPlayer(p);
        playersHeadMeta.displayName(Component.text(p.getName(), TextColor.color(255, 215, 0)));
        playersHeadMeta.lore(List.of(Component.text("Currently Managing", TextColor.color(175, 0, 255))));
        playerHead.setItemMeta(playersHeadMeta);

        inv.setItem(4, playerHead);
        inv.setItem(19, invSee);
        inv.setItem(21, enderSee);
        inv.setItem(23, punish);
        inv.setItem(25, teleport);

        p.openInventory(inv);
    }
}
