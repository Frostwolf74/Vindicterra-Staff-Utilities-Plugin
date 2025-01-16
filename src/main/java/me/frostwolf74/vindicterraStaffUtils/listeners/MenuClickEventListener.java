package me.frostwolf74.vindicterraStaffUtils.listeners;

import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;
import me.frostwolf74.vindicterraStaffUtils.commands.BanCommand;
import me.frostwolf74.vindicterraStaffUtils.commands.MuteCommand;
import me.frostwolf74.vindicterraStaffUtils.commands.StaffModeCommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class MenuClickEventListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getWhoClicked() instanceof Player p) {
            if(e.getView().title().equals(Component.text("Online Players"))){ // inside the online players menu
                e.setCancelled(true);

                TextComponent headName = (TextComponent) (e.getCurrentItem().getItemMeta().displayName()); // retrieve player info from clicked head

                openPlayerManagementInterface(p, e.getWhoClicked().getServer().getPlayer(headName.content())); // apply player info for next window
            }

            if(e.getView().getOriginalTitle().equalsIgnoreCase("Managing Player")){
                e.setCancelled(true);

                if(e.getCurrentItem() == null) return;

                Player target = null; // defined later

                for(ItemStack item : e.getView().getTopInventory().getContents()){
                    if(item == null){ // skip over empty slots
                        item = new ItemStack(Material.AIR);
                    }

                    if(item.getType() == Material.PLAYER_HEAD){ // looks for the named player head contained in the inventory and gets the player object from it
                        target = p.getServer().getPlayer(((TextComponent) Objects.requireNonNull(item.getItemMeta().displayName())).content());
                    }
                }

                assert target != null;
                switch(Objects.requireNonNull(e.getCurrentItem()).getType()){
                    case CHEST:
                        p.getServer().dispatchCommand(p, "inv " + target);
                        break;
                    case ENDER_CHEST:
                        p.getServer().dispatchCommand(p, "ender " + target);
                        break;
                    case BARRIER:
                        openPlayerPunishmentInterface(p, target);
                        break;
                    case ENDER_EYE:
                        p.teleport(target);
                        break;
                }
            }

            if(e.getView().getOriginalTitle().equalsIgnoreCase("Managing Player Punishments")){
                e.setCancelled(true);

                if(e.getCurrentItem() == null) return;

                Player target = null;

                for(ItemStack item : e.getView().getTopInventory().getContents()){
                    if(item == null){ // skip over empty slots
                        item = new ItemStack(Material.AIR);
                    }

                    if(item.getType() == Material.PLAYER_HEAD){ // looks for the named player head and gets the player
                        target = p.getServer().getPlayer(((TextComponent) Objects.requireNonNull(item.getItemMeta().displayName())).content());
                    }
                }

                assert target != null;
                switch(e.getCurrentItem().getType()){ // TODO
                    case WHITE_WOOL:
                        target.kick(Component.text(openVirtualAnvil(p)));
                        break;
                    case ORANGE_WOOL:
                        String[] s = new String[2];
                        s[1] = openVirtualAnvil(p);

                        MuteCommand.mutePlayer(target, s);
                        break;
                    case RED_WOOL:
                        String[] s1 = new String[2];
                        s1[1] = openVirtualAnvil(p);

                        BanCommand.banPlayer(p, p.getServer().getOfflinePlayer(target.name().toString()), s1);

                        p.sendMessage("Found offline player, name: " + p.getServer().getOfflinePlayer(target.name().toString()).getName());
                        p.sendMessage(Component.text("Original string: " + target.name().toString()));
                        break;
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

    public static String openVirtualAnvil(Player target){

        return target.getName();
    }

    // creates each inventory interface

    public void openPlayerPunishmentInterface(Player p, Player target) {
        Inventory inv = Bukkit.createInventory(p, 9*3, Component.text("Managing Player Punishments"));

        ItemStack kick = new ItemStack(Material.WHITE_WOOL);
        kick.setItemMeta(StaffModeCommand.applyDisplayName(kick, Component.text("Kick", TextColor.color(175, 0,255))));

        ItemStack mute = new ItemStack(Material.ORANGE_WOOL);
        mute.setItemMeta(StaffModeCommand.applyDisplayName(mute, Component.text("Mute", TextColor.color(175, 0,255))));

        ItemStack ban = new ItemStack(Material.RED_WOOL);
        ban.setItemMeta(StaffModeCommand.applyDisplayName(ban, Component.text("Ban", TextColor.color(175, 0,255))));

        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta playersHeadMeta = (SkullMeta) playerHead.getItemMeta();

        playersHeadMeta.setOwningPlayer(target);
        playersHeadMeta.displayName(Component.text(target.getName(), TextColor.color(255, 215, 0)));
        playersHeadMeta.lore(List.of(Component.text("Currently Managing", TextColor.color(175, 0, 255))));
        playerHead.setItemMeta(playersHeadMeta);

        inv.setItem(4, playerHead);
        inv.setItem(20, kick);
        inv.setItem(22, mute);
        inv.setItem(24, ban);

        p.closeInventory();
        p.openInventory(inv);
    }

    public void openPlayerManagementInterface(Player p, Player target){
        Inventory inv = Bukkit.createInventory(p, 9*3, Component.text("Managing Player"));

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

        playersHeadMeta.setOwningPlayer(target);
        playersHeadMeta.displayName(Component.text(target.getName(), TextColor.color(255, 215, 0)));
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
