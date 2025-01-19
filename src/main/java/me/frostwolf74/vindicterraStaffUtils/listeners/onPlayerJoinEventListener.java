package me.frostwolf74.vindicterraStaffUtils.listeners;

import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;

import me.frostwolf74.vindicterraStaffUtils.commands.UnmuteCommand;
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
import java.util.List;


public class onPlayerJoinEventListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public static void onJoinEvent(PlayerJoinEvent e){
        // problems with keeping the hotbar bukkit task working, vanish is disabled on exit, however, staffmode remains
        if(Boolean.TRUE.equals(e.getPlayer().getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isVanished"), PersistentDataType.BOOLEAN))){
            e.getPlayer().getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isVanished"), PersistentDataType.BOOLEAN, false);
        }

        if(Boolean.TRUE.equals(e.getPlayer().getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isStaffChatEnabled"), PersistentDataType.BOOLEAN))){ // disable staff chat on leave
            e.getPlayer().getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isStaffChatEnabled"), PersistentDataType.BOOLEAN, false);
        }

        if(!Objects.equals(e.getPlayer().getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "awaitingInput"), PersistentDataType.STRING), "NONE")) { // TODO in progress
            e.getPlayer().getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "awaitingInput"), PersistentDataType.STRING, "NONE");
        }

        List<UUID> scheduleUnmutePlayer = VindicterraStaffUtils.getScheduleUnmutePlayers();

        if(scheduleUnmutePlayer.contains(e.getPlayer().getUniqueId())){
            VindicterraStaffUtils.getScheduleUnmutePlayers().remove(e.getPlayer().getUniqueId());

            UnmuteCommand.unmute(e.getPlayer());
        }
    }
}
