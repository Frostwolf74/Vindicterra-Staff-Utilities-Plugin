package me.frostwolf74.vindicterraStaffUtils.commands;

import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StaffChatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player p){
            if(!(p.hasPermission("VSU.staffchat"))) return true;

            StringBuilder messageRaw = new StringBuilder();

            if(strings.length < 1) toggleStaffChat(p);

            for(int i = 0; i < strings.length; i++){
                messageRaw.append(strings[i]).append(" ");
            }

            if(messageRaw.equals(" ")) return false;

            for(Player p1 : p.getServer().getOnlinePlayers()){
                String teamColor = getPlayerTeamColor(p);
                String formattedName = teamColor + p.getName();

                List<Component> components = new ArrayList<>();

                components.add(0, Component.text("[", TextColor.color(192, 192, 192)));
                components.add(1, Component.text("Staff", TextColor.color(96, 96, 255)));
                components.add(2, Component.text("]", TextColor.color(192, 192, 192)));
                components.add(3, Component.text(" (" + p.getWorld().getName() + ") ", TextColor.color(192, 192, 192)));
                components.add(4, Component.text(formattedName));
                components.add(5, Component.text(": ", TextColor.color(192, 192, 192)));
                components.add(6, Component.text(messageRaw.toString(), TextColor.color(192, 192, 192)));

                Component message = Component.text("");
                for(Component component : components){
                    message = message.append(component);
                }

                if (p1.hasPermission("VSU")) p1.sendMessage(message);
            }

            return true;
        }
        return false;
    }

    private void toggleStaffChat(Player p) {
        if(Boolean.TRUE.equals(p.getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isStaffChatEnabled"), PersistentDataType.BOOLEAN))){
            p.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isStaffChatEnabled"), PersistentDataType.BOOLEAN, false);
        }
        else{
            p.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isStaffChatEnabled"), PersistentDataType.BOOLEAN, true);
        }
    }

    private String getPlayerTeamColor(Player player) { // +++ created entirely by chatGPT
        if (player.getScoreboard() != null) {
            Team team = player.getScoreboard().getEntryTeam(player.getName());
            if (team != null && team.getColor() != null) {
                return team.getColor().toString();
            }
        }
        return ChatColor.WHITE.toString(); // Default to white if no team or color
    }
}
