package me.frostwolf74.vindicterraStaffUtils.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.event.player.ChatEvent;
import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;
import me.frostwolf74.vindicterraStaffUtils.commands.BanCommand;
import me.frostwolf74.vindicterraStaffUtils.commands.MuteCommand;
import me.frostwolf74.vindicterraStaffUtils.commands.StaffChatCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.Objects;

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

        String reason = e.getPlayer().getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "awaitingInput-reason"), PersistentDataType.STRING);

        if(!Objects.equals(reason, "NONE") && reason != null){
            e.setCancelled(true);

            e.getPlayer().getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "awaitingInput-reason"), PersistentDataType.STRING, "NONE");

            Player target = VindicterraStaffUtils.getTargetPlayers().get(e.getPlayer());

            VindicterraStaffUtils.getTargetPlayers().remove(e.getPlayer());

            switch(Objects.requireNonNull(reason)){
                case "kick":
                    target.kick(e.message());
                    break;
                case "mute":
                    MuteCommand.mutePlayer(e.getPlayer(), target, e.message().toString().split(""));
                    break;
                case "ban":
                    BanCommand.banPlayer(e.getPlayer(), target, e.message().toString().split(""));
                    break;
            }
        }
    }
}
