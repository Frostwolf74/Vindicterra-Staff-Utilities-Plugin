package me.frostwolf74.vindicterraStaffUtils.listeners;

import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.UUID;

public class onPlayerLeaveEventListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLeaveEvent(PlayerQuitEvent e) {
        if(Boolean.TRUE.equals(e.getPlayer().getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isMuted"), PersistentDataType.BOOLEAN))){
            VindicterraStaffUtils.getRunningPlayerMutedTasks().get(e.getPlayer().getUniqueId()).cancel();
        }

        if(Boolean.TRUE.equals(e.getPlayer().getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isVanished"), PersistentDataType.BOOLEAN))){
            Map<UUID, BukkitTask> runningHotbarTasks = VindicterraStaffUtils.getRunningTasks();

            runningHotbarTasks.get(e.getPlayer().getUniqueId()).cancel();
            runningHotbarTasks.remove(e.getPlayer().getUniqueId());

            VindicterraStaffUtils.setRunningTasks(runningHotbarTasks);
        }
    }
}
