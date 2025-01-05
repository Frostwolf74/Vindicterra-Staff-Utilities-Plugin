package me.frostwolf74.vindicterraStaffUtils.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.BanList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player p){
            if(!p.hasPermission("VSU.punish.ban")) return false;

            Player target = p.getServer().getPlayer(strings[0]);

            if(target == null){
                p.sendMessage(Component.text("Player not found or not online", TextColor.color(255, 0, 0)));
                return true;
            }

            List<String> stringsSplit = new ArrayList<>(List.of(strings[1].split("")));

            String timeType = stringsSplit.get(stringsSplit.size() - 1);
            stringsSplit.remove(stringsSplit.size() - 1);

            float timeLimit = Float.parseFloat(String.join("", stringsSplit));

            int banLengthS = 0;

            switch (timeType) {
                case "h":
                    banLengthS = Math.round(timeLimit * 60);
                    break;
                case "d":
                    banLengthS = Math.round(timeLimit * 86400);
                    break;
                case "w":
                    banLengthS = Math.round(timeLimit * 604800);
                    break;
                case "m":
                    banLengthS = Math.round(timeLimit * 2592000);
                    break;
                case "y":
                    banLengthS = Math.round(timeLimit * 2592000 * 12);
                default:
                    p.sendMessage(Component.text("Invalid time type: " + timeType, TextColor.color(255, 0, 0)));
            }

            Calendar expireDate = Calendar.getInstance();
            expireDate.setTimeInMillis(System.currentTimeMillis() + banLengthS * 1000L);

            String reason = "";

            for(int i = 2; i < strings.length; ++i){
                reason += strings[i] + " ";
            }

            p.getServer().getBanList(BanList.Type.NAME).addBan(target.getName(), reason, expireDate.getTime(), p.getName());
            target.kickPlayer(ChatColor.RED + "You have been banned for " + strings[1] + "\n\nReason: " + reason);
            p.sendMessage(ChatColor.GREEN + "Player " + target.getName() + " has been banned.");


            return true;
        }
        return false;
    }
}
