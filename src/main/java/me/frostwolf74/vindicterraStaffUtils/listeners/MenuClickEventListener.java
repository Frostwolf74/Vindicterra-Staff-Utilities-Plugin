package me.frostwolf74.vindicterraStaffUtils.listeners;

import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;
import me.frostwolf74.vindicterraStaffUtils.commands.BanCommand;
import me.frostwolf74.vindicterraStaffUtils.commands.InventorySeeCommand;
import me.frostwolf74.vindicterraStaffUtils.commands.MuteCommand;
import me.frostwolf74.vindicterraStaffUtils.commands.StaffModeCommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.sign.*;
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
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class MenuClickEventListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getWhoClicked() instanceof Player p) {
            if(VindicterraStaffUtils.getopenedInventories().containsKey(p.getUniqueId())) {
                Map<UUID, Inventory> openedInventories = VindicterraStaffUtils.getopenedInventories();

                openedInventories.put(p.getUniqueId(), InventorySeeCommand.updatePlayerInventory(p));
            }

            if(e.getView().title().equals(Component.text("Online Players"))){
                e.setCancelled(true);

                TextComponent headName = (TextComponent) (e.getCurrentItem().getItemMeta().displayName());

                openPlayerManagementInterface(p, e.getWhoClicked().getServer().getPlayer(headName.content())); // will never be null
            }

            if(e.getView().getOriginalTitle().equalsIgnoreCase("Managing Player")){
                e.setCancelled(true);

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
                switch(Objects.requireNonNull(e.getCurrentItem()).getType()){
                    case CHEST:
                        // TODO
                        break;
                    case ENDER_CHEST:
                        p.openInventory(target.getEnderChest());
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
                switch(Objects.requireNonNull(e.getCurrentItem()).getType()){
                    case WHITE_WOOL:
                        p.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "awaitingInput"), PersistentDataType.STRING, "kick");

                        target.kick(Component.text(openVirtualReasonSign(p)));
                        break;
                    case ORANGE_WOOL:
                        p.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "awaitingInput"), PersistentDataType.STRING, "mute:duration");

                        String timeLimit = openVirtualTimeLimitSign(p);
                        String[] reason = new String[2];

                        reason[0] = timeLimit;
                        reason[1] = openVirtualReasonSign(p);

                        MuteCommand.mutePlayer(target, reason);
                        break;
                    case RED_WOOL:
                        p.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "awaitingInput"), PersistentDataType.STRING, "ban:duration");

                        String timeLimit1 = openVirtualTimeLimitSign(p);
                        String[] reason1 = new String[2];

                        reason1[0] = timeLimit1;
                        reason1[1] = openVirtualReasonSign(p);

                        BanCommand.banPlayer(p, p.getServer().getOfflinePlayer(target.name().toString()), reason1);

                        p.sendMessage("Found offline player, name: " + p.getServer().getOfflinePlayer(target.name().toString()).getName());
                        p.sendMessage(Component.text("Original string: " + target.name().toString()));
                        break;
                }
            }

            if(e.getViewers().size() > 1){ // TODO debug
                for(HumanEntity user : e.getViewers()){
                    if(user instanceof Player viewer){
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

    public String openVirtualTimeLimitSign(Player player) {
        org.bukkit.block.Sign sign = (org.bukkit.block.Sign) Material.OAK_SIGN.createBlockData();

        sign.getSide(Side.FRONT).line(0, Component.text("Time Limit (ex: 3d):"));
        sign.setEditable(true);

        player.openSign(sign, Side.FRONT);

        return sign.getBlockData().getAsString();
    }

    public String openVirtualReasonSign(Player player) {
        org.bukkit.block.Sign sign = (org.bukkit.block.Sign) Material.OAK_SIGN.createBlockData(); // sign builder? TODO debug

        sign.getSide(Side.FRONT).line(0, Component.text("Reason:"));
        sign.setEditable(true);

        player.openSign(sign, Side.FRONT);

        return sign.getBlockData().getAsString();
    }

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
