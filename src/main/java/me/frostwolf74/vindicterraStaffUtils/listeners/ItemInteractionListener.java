package me.frostwolf74.vindicterraStaffUtils.listeners;

import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;
import me.frostwolf74.vindicterraStaffUtils.commands.FreezeCommand;
import me.frostwolf74.vindicterraStaffUtils.commands.VanishCommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;

import java.util.*;

public class ItemInteractionListener implements Listener {
    @EventHandler
    public void onItemInteraction(PlayerInteractEvent e) {
        if(!(e.getAction().isRightClick())) return;

        if(e.getItem() == null){
            return;
        }

        if(Boolean.TRUE.equals(e.getPlayer().getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "inStaffMode"), PersistentDataType.BOOLEAN))){
            if(Objects.equals(e.getPlayer().getInventory().getItemInMainHand().getType(), Material.POTION)){ // NV
                if(Boolean.TRUE.equals(e.getPlayer().getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "hasStaffNightVision"), PersistentDataType.BOOLEAN))){
                    e.getPlayer().clearActivePotionEffects();

                    e.getPlayer().getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "hasStaffNightVision"), PersistentDataType.BOOLEAN, false);

                    e.getPlayer().sendMessage("Staff night vision disabled.");
                }
                else{
                    PotionEffect nvPotion = new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false, false);

                    e.getPlayer().addPotionEffect(nvPotion);

                    e.getPlayer().getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "hasStaffNightVision"), PersistentDataType.BOOLEAN, true);

                    e.getPlayer().sendMessage("Staff night vision enabled.");
                }
            }
            else if(Objects.equals(e.getPlayer().getInventory().getItemInMainHand().getType(), Material.ICE)){ // freeze
                if(e.getInteractionPoint() == null) return;

                RayTraceResult trace = e.getPlayer().getServer().getWorld(e.getPlayer().getWorld().getName()).rayTraceEntities(e.getInteractionPoint(), e.getClickedPosition(), 10);

                if(trace != null){
                    e.getPlayer().sendMessage("Ray trace result: " + trace.getHitEntity());
                    if(trace instanceof Player target){
                        FreezeCommand.freezePlayer(e.getPlayer(), target);
                    }
                }
            }
            else if(Objects.equals(e.getPlayer().getInventory().getItemInMainHand().getType(), Material.ENDER_PEARL)){ // RTP
                Collection<?> onlinePlayers = e.getPlayer().getServer().getOnlinePlayers();
                Random rand = new Random();
                rand.setSeed(System.currentTimeMillis());

                Player target = (Player) onlinePlayers.toArray()[rand.nextInt(onlinePlayers.size())];

                // prevents rtps to other staff members
                if(target == e.getPlayer() && !target.hasPermission("VSU")) {
                    target = (Player) onlinePlayers.toArray()[rand.nextInt(onlinePlayers.size())];
                }

                e.getPlayer().teleport(target);
            }
            else if(Objects.equals(e.getPlayer().getInventory().getItemInMainHand().getType(), Material.ENDER_EYE)){ // vanish
                if(Boolean.TRUE.equals(e.getPlayer().getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isVanished"), PersistentDataType.BOOLEAN))){
                    VanishCommand.vanishPlayer(e.getPlayer(), true);
                }
                else{
                    VanishCommand.vanishPlayer(e.getPlayer(), false);
                }
            }
            else if(Objects.equals(e.getPlayer().getInventory().getItemInMainHand().getType(), Material.CHEST)){ // see inventory
                if(e.getInteractionPoint() == null) return;

                RayTraceResult trace = e.getPlayer().getServer().getWorld(e.getPlayer().getWorld().getName()).rayTraceEntities(e.getInteractionPoint(), e.getClickedPosition(), 10);

                if(!(trace == null)){
                    if(trace.getHitEntity() instanceof Player target){

                    }
                }
            }
            else if(Objects.equals(e.getPlayer().getInventory().getItemInMainHand().getType(), Material.PLAYER_HEAD)){ // online players
                Inventory onlinePlayersInventory = Bukkit.createInventory(null, 9 * 6, "Online Players");

                e.getPlayer().getServer().getOnlinePlayers().forEach(onlinePlayer -> {
                            ItemStack playersHead = new ItemStack(Material.PLAYER_HEAD, 1);
                            SkullMeta playersHeadMeta = (SkullMeta) playersHead.getItemMeta();

                            playersHeadMeta.setOwningPlayer(onlinePlayer);
                            playersHeadMeta.displayName(Component.text(onlinePlayer.getName(), TextColor.color(255, 215, 0)));
                            playersHeadMeta.lore(List.of(Component.text("Click to manage", TextColor.color(175, 0, 255))));
                            playersHead.setItemMeta(playersHeadMeta);

                            onlinePlayersInventory.addItem(playersHead);
                        }
                );

//                if(e.getPlayer().getServer().getOnlinePlayers().size() > 9*6){ // implement if Vindicterra ever gets past 54 online players
//                    List<Inventory> pages = new ArrayList<>();
//
//                    for(int i = 0; i < e.getPlayer().getServer().getOnlinePlayers().size()/(9*6); ++i){
//                        pages.add(Bukkit.createInventory(null, 9 * 6, "Online Players"));
//                    }
//
//                }

                e.getPlayer().openInventory(onlinePlayersInventory);
            }

            e.setCancelled(true); // prevent player from using the item
        }
    }
}
