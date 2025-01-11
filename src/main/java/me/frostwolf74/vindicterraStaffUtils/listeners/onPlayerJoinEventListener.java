package me.frostwolf74.vindicterraStaffUtils.listeners;

import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;


public class onPlayerJoinEventListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public static void onJoinEvent(PlayerJoinEvent e){
        if(Boolean.TRUE.equals(e.getPlayer().getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isMuted"), PersistentDataType.BOOLEAN))){ // ensure players who leave tempmuted dont stay permanently muted
            int unMuteTimeStamp = e.getPlayer().getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "unmuteTimeStamp"), PersistentDataType.INTEGER);

            BukkitTask muteTask = e.getPlayer().getServer().getScheduler().runTaskTimer(VindicterraStaffUtils.getPlugin(), () -> {
                if(((int) (System.currentTimeMillis() / 1000L)) >= unMuteTimeStamp){
                    e.getPlayer().getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isMuted"), PersistentDataType.BOOLEAN, false);
                    VindicterraStaffUtils.getRunningPlayerMutedTasks().get(e.getPlayer().getUniqueId()).cancel();
                }
            }, 0L, 1200L); // updates every minute

            Map<UUID, BukkitTask> runningTasks = VindicterraStaffUtils.getRunningPlayerMutedTasks();
            runningTasks.put(e.getPlayer().getUniqueId(), muteTask);
            VindicterraStaffUtils.setRunningPlayerMutedTasks(runningTasks);
        }

        // problems with keeping the hotbar bukkit task working, vanish is disabled on exit, however, staffmode remains
        if(Boolean.TRUE.equals(e.getPlayer().getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isVanished"), PersistentDataType.BOOLEAN))){
            e.getPlayer().getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "inVanished"), PersistentDataType.BOOLEAN, false);
        }

        if(Boolean.TRUE.equals(e.getPlayer().getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isStaffChatEnabled"), PersistentDataType.BOOLEAN))){ // disable staff chat on leave
            e.getPlayer().getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isStaffChatEnabled"), PersistentDataType.BOOLEAN, false);
        }

        if(!Objects.equals(e.getPlayer().getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "awaitingInput"), PersistentDataType.STRING), "NONE")) { // TODO in progress
            e.getPlayer().getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "awaitingInput"), PersistentDataType.STRING, "NONE");
        }
    }
}
