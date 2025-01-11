package me.frostwolf74.vindicterraStaffUtils.listeners;

import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataType;

public class PlayerDamageEntityEventListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void PlayerDamageEntityEvent(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player p){
            if(Boolean.TRUE.equals(p.getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "inStaffMode"), PersistentDataType.BOOLEAN))){
                e.setCancelled(true);
            }
        }
    }
}
