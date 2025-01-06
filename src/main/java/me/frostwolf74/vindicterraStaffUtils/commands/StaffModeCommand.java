package me.frostwolf74.vindicterraStaffUtils.commands;

import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class StaffModeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player p){
            if(strings.length == 0){
                return applyStaffMode(p);
            }
            else{
                if(p.getServer().getPlayer(strings[0]) == null){
                    p.sendMessage(Component.text("Player not found or not online", TextColor.color(255, 0 , 0)));
                    return false;
                }

                return applyStaffMode(p.getServer().getPlayer(strings[0]));
            }
        }
        return false;
    }

    public static boolean applyStaffMode(Player p){
        if(!(p.hasPermission("VSU.staff"))) return true;

        FileConfiguration config = VindicterraStaffUtils.getPlugin().getConfig();

        if(Boolean.TRUE.equals(p.getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "inStaffMode"), PersistentDataType.BOOLEAN))) {
            // key is disabled first as a failsafe
            p.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "inStaffMode"), PersistentDataType.BOOLEAN, false);
            VanishCommand.vanishPlayer(p, true);

            p.getInventory().clear();
            ItemStack[] items = new ItemStack[41];

            // literally cannot ever be null
            List<ItemStack> itemList = (List<ItemStack>) config.getList("savedInventory." + p.getUniqueId() + ".PlayerInventory.ItemStack");

            int i = 0;
            for(ItemStack item : itemList) {
                items[i++] = item;
            }

            p.clearActivePotionEffects();

            List<PotionEffect> pEffects = (List<PotionEffect>) config.getList("savedInventory." + p.getUniqueId() + ".PlayerEffects");
            for(PotionEffect pE : pEffects) {
                p.addPotionEffect(pE);
            }

            p.getInventory().setContents(items);
            p.setGameMode(GameMode.SURVIVAL);
        }
        else{ // if player is not in staff mode
            PlayerInventory savedInventory = p.getInventory();
            List<ItemStack> items = new ArrayList<>();

            VanishCommand.vanishPlayer(p, false);

            for(ItemStack e : savedInventory.getContents()){
                if(e == null){
                    items.add(new ItemStack(Material.AIR));
                }
                else{
                    items.add(e);
                }
            }

            Collection<PotionEffect> playerEffects = p.getActivePotionEffects();

            config.set("savedInventory." + p.getUniqueId() + ".PlayerInventory.ItemStack", items);
            config.set("savedInventory." + p.getUniqueId() + ".PlayerEffects", playerEffects.stream().toList());
            VindicterraStaffUtils.getPlugin().saveConfig();

            p.getInventory().clear();
            p.clearActivePotionEffects();

            // swap out player's inventory with the staff mode inventory

            p.getInventory().setContents(getStaffInventory(p).getContents());

            p.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "inStaffMode"), PersistentDataType.BOOLEAN, true);

            p.setGameMode(GameMode.CREATIVE);
        }
        return true;
    }

    public static ItemMeta applyDisplayName(ItemStack item, Component name) {
        ItemMeta meta = item.getItemMeta();

        meta.displayName(name);
        item.setItemMeta(meta);

        return meta;
    }

    public static PlayerInventory getStaffInventory(Player p){
        PlayerInventory staffInventory = p.getInventory();

        ItemStack freezeTool = new ItemStack(Material.ICE);
        freezeTool.setItemMeta(applyDisplayName(freezeTool, Component.text("Freeze", TextColor.color(255,215,0), TextDecoration.BOLD)));

        ItemStack invSeeTool = new ItemStack(Material.CHEST);
        invSeeTool.setItemMeta(applyDisplayName(invSeeTool, Component.text("See Inventory", TextColor.color(255,215,0), TextDecoration.BOLD)));

        ItemStack rtpTool = new ItemStack(Material.ENDER_PEARL);
        rtpTool.setItemMeta(applyDisplayName(rtpTool, Component.text("Random Teleport", TextColor.color(255,215,0), TextDecoration.BOLD)));

        ItemStack nvTool = new ItemStack(Material.POTION);
        nvTool.setItemMeta(applyDisplayName(nvTool, Component.text("Night Vision", TextColor.color(255,215,0), TextDecoration.BOLD)));

        ItemStack vanishTool = new ItemStack(Material.ENDER_EYE);
        vanishTool.setItemMeta(applyDisplayName(vanishTool, Component.text("Vanish", TextColor.color(255,215,0), TextDecoration.BOLD)));

        ItemStack onlinePlayersTool = new ItemStack(Material.PLAYER_HEAD);
        onlinePlayersTool.setItemMeta(applyDisplayName(onlinePlayersTool, Component.text("View Online Players", TextColor.color(255,215,0), TextDecoration.BOLD)));

        staffInventory.setItem(0, nvTool);
        staffInventory.setItem(1, freezeTool);
        staffInventory.setItem(3, rtpTool);
        staffInventory.setItem(4, vanishTool);
        staffInventory.setItem(7, invSeeTool);
        staffInventory.setItem(8, onlinePlayersTool);

        return staffInventory;
    }
}

