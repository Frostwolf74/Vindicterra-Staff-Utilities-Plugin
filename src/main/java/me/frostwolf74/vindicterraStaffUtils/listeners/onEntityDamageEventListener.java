package me.frostwolf74.vindicterraStaffUtils.listeners;

import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataType;

public class onEntityDamageEventListener implements Listener {
    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if(e instanceof Player p){
            if (Boolean.TRUE.equals(p.getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "inStaffMode"), PersistentDataType.BOOLEAN)) && e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                e.setCancelled(true); // failsafe in case staff mode fails (after server restart or player dc)
            }
        }
    }
}
