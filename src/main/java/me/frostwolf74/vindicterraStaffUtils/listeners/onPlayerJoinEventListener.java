package me.frostwolf74.vindicterraStaffUtils.listeners;

import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;

import me.frostwolf74.vindicterraStaffUtils.commands.StaffModeCommand;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;


public class onPlayerJoinEventListener implements Listener {
    @EventHandler
    public static void onJoinEvent(PlayerJoinEvent e){
        if(Boolean.TRUE.equals(e.getPlayer().getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isMuted"), PersistentDataType.BOOLEAN))){
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

        if(Boolean.TRUE.equals(e.getPlayer().getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isStaffChatEnabled"), PersistentDataType.BOOLEAN))){
            e.getPlayer().getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isStaffChatEnabled"), PersistentDataType.BOOLEAN, false);
        }

        if(Boolean.TRUE.equals(e.getPlayer().getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "inStaffMode"), PersistentDataType.BOOLEAN))) {
            StaffModeCommand.applyStaffMode(e.getPlayer());
        }

        if(!Objects.equals(e.getPlayer().getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "awaitingInput"), PersistentDataType.STRING), "NONE")) {
            e.getPlayer().getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "awaitingInput"), PersistentDataType.STRING, "NONE");
        }
    }
}