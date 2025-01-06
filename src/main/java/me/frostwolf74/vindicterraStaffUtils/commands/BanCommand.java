package me.frostwolf74.vindicterraStaffUtils.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.BanList;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
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
        if(commandSender instanceof Player p) {
            if (!p.hasPermission("VSU.punish.ban")) return false;

            if (strings.length == 0) {
                return false;
            }

            OfflinePlayer target = p.getServer().getPlayer(strings[0]);

            if (target == null) {
                p.sendMessage(Component.text("Player not found or not online", NamedTextColor.RED));
                return true;
            }

            Calendar expireDate = Calendar.getInstance();

            int reasonIndex = 2;
            String timeType = null;
            List<String> stringsSplit = new ArrayList<>();

            if (strings.length > 1) {
                stringsSplit = new ArrayList<>(List.of(strings[1].split("")));

                timeType = stringsSplit.get(stringsSplit.size() - 1);
            } else {
                return false;
            }

            if (isNumeric(stringsSplit.get(0))) {
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
                        break;
                    default:
                        p.sendMessage(Component.text("Invalid time type: " + timeType, NamedTextColor.RED));
                        return true;
                }

                expireDate.setTimeInMillis(System.currentTimeMillis() + banLengthS * 1000L);

                p.sendMessage(Component.text("Player " + target.getName() + " has been banned.", NamedTextColor.RED));
            } else {
                expireDate = null;
                reasonIndex = 1;

                p.sendMessage(Component.text("Player " + target.getName() + " has been permanently banned.", NamedTextColor.RED));
            }

            StringBuilder reason = new StringBuilder();

            for (int i = reasonIndex; i < strings.length; ++i) {
                reason.append(strings[i]).append(" ");
            }

            if (expireDate != null) {
                p.getServer().getBanList(BanList.Type.NAME).addBan(target.getName(), reason.toString(), expireDate.getTime(), p.getName());
            } else {
                p.getServer().getBanList(BanList.Type.NAME).addBan(target.getName(), reason.toString(), null, p.getName());
            }


            if (target.isOnline()) {
                if (expireDate == null) {
                    ((Player) target).kick(Component.text("You have been permanently banned.\n\nReason: " + reason, NamedTextColor.RED));
                } else {
                    ((Player) target).kick(Component.text("You have been banned for " + strings[1] + "\n\nReason: " + reason, NamedTextColor.RED));
                }
            }

            return true;
        }
        return false;
    }

    public static boolean isNumeric(String str) { // +++ courtesy of stack overflow user CraigTP
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
