package me.frostwolf74.vindicterraStaffUtils.listeners;

import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.persistence.PersistentDataType;

public class onPlayerMoveEventListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerMoveEvent(PlayerMoveEvent e) {
        if(Boolean.TRUE.equals(e.getPlayer().getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isFrozen"), PersistentDataType.BOOLEAN))){
            e.setCancelled(true);
        }
    }
}
