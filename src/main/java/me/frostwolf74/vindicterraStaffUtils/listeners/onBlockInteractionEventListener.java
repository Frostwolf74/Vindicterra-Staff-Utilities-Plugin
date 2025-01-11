package me.frostwolf74.vindicterraStaffUtils.listeners;

import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;

import java.util.ArrayList;
import java.util.List;

public class onBlockInteractionEventListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockInteractionEvent(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if(Boolean.TRUE.equals(p.getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "inStaffMode"), PersistentDataType.BOOLEAN))
                || Boolean.TRUE.equals(p.getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isFrozen"), PersistentDataType.BOOLEAN))) {
            RayTraceResult trace = e.getPlayer().rayTraceBlocks(10);

            if(trace == null) return;
            List<Material> interactables = new ArrayList<>();

            for(Material material : Material.values()) {
                if((material.isInteractable())){
                    interactables.add(material);
                }
            }

            if (interactables.contains(trace.getHitBlock().getType())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onArmorStandInteractEvent(PlayerInteractAtEntityEvent e) {
        Player p = e.getPlayer();

        if(Boolean.TRUE.equals(p.getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "inStaffMode"), PersistentDataType.BOOLEAN)) ||
                Boolean.TRUE.equals(p.getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isFrozen"), PersistentDataType.BOOLEAN))) {
            if(e.getRightClicked().getType() == EntityType.ARMOR_STAND) {
                e.setCancelled(true);
            }
        }
    }
}
