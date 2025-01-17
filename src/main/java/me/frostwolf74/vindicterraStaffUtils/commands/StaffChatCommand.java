package me.frostwolf74.vindicterraStaffUtils.commands;

import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class StaffChatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player p){
            if(!(p.hasPermission("VSU.staffchat"))) return true;

            StringBuilder messageRaw = new StringBuilder();

            if(strings.length == 0){
                toggleStaffChat(p);
                return true;
            }

            for (String string : strings) {
                messageRaw.append(string).append(" ");
            }

            sendStaffChatMessage(p, messageRaw.toString());

            return true;
        }
        return false;
    }

    private void toggleStaffChat(Player p) {
        if(Boolean.TRUE.equals(p.getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isStaffChatEnabled"), PersistentDataType.BOOLEAN))){
            p.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isStaffChatEnabled"), PersistentDataType.BOOLEAN, false);
            p.sendMessage(Component.text("You have disabled the staff chat.", NamedTextColor.BLUE));
        }
        else{
            p.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isStaffChatEnabled"), PersistentDataType.BOOLEAN, true);
            p.sendMessage(Component.text("You have enabled the staff chat.", NamedTextColor.BLUE));
        }
    }

    // TODO CHAT COLOURS ARE DEFINED BY CHAT PREFIXES

//    private static String getPlayerTeamColor(Player player) { // for vanilla teams
//        if (player.getScoreboard() != null) {
//            Team team = player.getScoreboard().getEntryTeam(player.getName());
//            if (team != null && team.getColor() != null) {
//                return team.getColor().toString();
//            }
//        }
//        return ChatColor.WHITE.toString(); // Default to white if no team or color
//    }

    public static void sendStaffChatMessage(Player p, String messageRaw) {
        for(Player p1 : p.getServer().getOnlinePlayers()) {

            List<Component> components = new ArrayList<>();

            components.add(0, Component.text("[", TextColor.color(192, 192, 192)));
            components.add(1, Component.text("Staff", TextColor.color(96, 96, 255)));
            components.add(2, Component.text("]", TextColor.color(192, 192, 192)));
            components.add(3, Component.text(" (" + p.getWorld().getName() + ") ", TextColor.color(192, 192, 192)));
            components.add(4, Component.text(p.getDisplayName()));
            components.add(5, Component.text(": ", TextColor.color(192, 192, 192)));
            components.add(6, Component.text(messageRaw, TextColor.color(192, 192, 192)));

            Component message = Component.text("");
            for (Component component : components) {
                message = message.append(component);
            }

            if (p1.hasPermission("VSU")) p1.sendMessage(message);
        }
    }
}
