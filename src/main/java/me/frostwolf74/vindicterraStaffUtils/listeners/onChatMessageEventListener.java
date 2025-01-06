package me.frostwolf74.vindicterraStaffUtils.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;


public class onChatMessageEventListener implements Listener {
    @EventHandler
    public void onChat(AsyncChatEvent e) {
        if(Boolean.TRUE.equals(e.getPlayer().getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isMuted"), PersistentDataType.BOOLEAN))){
            e.setCancelled(true);
            int timeRemaining = (e.getPlayer().getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "unmuteTimeStamp"), PersistentDataType.INTEGER) - (int) (System.currentTimeMillis() / 1000L));

            e.getPlayer().sendMessage(Component.text("You are muted for the next " + ((float) (timeRemaining/60)/60) + " hours, you cannot send messages.", TextColor.color(255, 0, 0), TextDecoration.BOLD));
        }
    }
}
