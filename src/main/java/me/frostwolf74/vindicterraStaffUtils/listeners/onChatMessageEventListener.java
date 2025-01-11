package me.frostwolf74.vindicterraStaffUtils.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;
import me.frostwolf74.vindicterraStaffUtils.commands.StaffChatCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;


public class onChatMessageEventListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncChatEvent e) {
        if(Boolean.TRUE.equals(e.getPlayer().getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isMuted"), PersistentDataType.BOOLEAN))){
            e.setCancelled(true);
            int timeRemaining = (e.getPlayer().getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "unmuteTimeStamp"), PersistentDataType.INTEGER) - (int) (System.currentTimeMillis() / 1000L));

            e.getPlayer().sendMessage(Component.text("\nYou are muted for the next " + String.format("%.2f", (float) (timeRemaining/60)/60) + " hours, you cannot send messages.\n", NamedTextColor.RED, TextDecoration.BOLD));
        }

        String[] splitMessage = ((TextComponent) e.message()).content().split("", 2);

        if(Boolean.TRUE.equals(e.getPlayer().getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isStaffChatEnabled"), PersistentDataType.BOOLEAN)) || (splitMessage[0].equals("@") && e.getPlayer().hasPermission("VSU"))){
            if(e.message().equals(Component.text(" "))) return;

            String message = ((TextComponent) e.message()).content();

            if(splitMessage[0].equals("@")){
                message = message.replace("@", "");
            }

            StaffChatCommand.sendStaffChatMessage(e.getPlayer(), message);

            e.setCancelled(true);
        }
    }
}
