package me.frostwolf74.vindicterraStaffUtils.commands;

import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import net.kyori.adventure.text.format.TextDecoration;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MuteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player p) {
            if (!p.hasPermission("VSU.punish.mute")) return true;

            Player target = p.getServer().getPlayer(strings[0]);

            if (target == null) {
                p.sendMessage(Component.text("Player not found or not online", TextColor.color(255, 0, 0)));
                return true;
            }

            return mutePlayer(target, strings);
        }
        return false;
    }

    public static boolean mutePlayer(Player p, String[] strings) {
        if (!p.hasPermission("VSU.punish.mute")) return false;

        if (strings.length == 0) {
            return false;
        }

        Player target = p.getServer().getPlayer(strings[0]);

        if (target == null) {
            p.sendMessage(Component.text("Player not found or not online", NamedTextColor.RED));
            return true;
        }

        Calendar expireDate = Calendar.getInstance();

        int reasonIndex = 2;
        String timeType;
        List<String> stringsSplit;

        if (strings.length > 1) {
            stringsSplit = new ArrayList<>(List.of(strings[1].split("")));

            timeType = stringsSplit.get(stringsSplit.size() - 1);
        } else {
            return false;
        }

        int muteLengthS = 0;

        if (isNumeric(stringsSplit.get(0))) {
            stringsSplit.remove(stringsSplit.size() - 1);

            float timeLimit = Float.parseFloat(String.join("", stringsSplit));

            switch (timeType) {
                case "h":
                    muteLengthS = Math.round(timeLimit * 60 * 60);
                    break;
                case "d":
                    muteLengthS = Math.round(timeLimit * 60 * 60 * 24);
                    break;
                case "w":
                    muteLengthS = Math.round(timeLimit * 60 * 60 * 24 * 7);
                    break;
                case "m":
                    muteLengthS = Math.round(timeLimit * 60 * 60 * 24 * 30);
                    break;
                case "y":
                    muteLengthS = Math.round(timeLimit * 60 * 60 * 24 * 30 * 12);
                    break;
                default:
                    p.sendMessage(Component.text("Invalid time type: " + timeType, NamedTextColor.RED));
                    return true;
            }

            expireDate.setTimeInMillis(System.currentTimeMillis() + muteLengthS * 1000L);

            p.sendMessage(Component.text("\nPlayer " + target.getName() + " has been muted.\n", NamedTextColor.RED));
        } else {
            expireDate = null;
            reasonIndex = 1;

            p.sendMessage(Component.text("\nPlayer " + target.getName() + " has been permanently muted.\n", NamedTextColor.RED));
        }

        StringBuilder reason = new StringBuilder();

        for (int i = reasonIndex; i < strings.length; ++i) {
            reason.append(strings[i]).append(" ");
        }

        int unixTime = (int) (System.currentTimeMillis() / 1000L);
        int unMuteTimeStamp = unixTime + muteLengthS;

        target.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isMuted"), PersistentDataType.BOOLEAN, true);
        target.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "unmuteTimeStamp"), PersistentDataType.INTEGER, unMuteTimeStamp);

        if (expireDate != null) {
            BukkitTask muteTask = p.getServer().getScheduler().runTaskTimer(VindicterraStaffUtils.getPlugin(), () -> {
                if (((int) (System.currentTimeMillis() / 1000L)) >= unMuteTimeStamp) {
                    target.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isMuted"), PersistentDataType.BOOLEAN, false);
                    VindicterraStaffUtils.getRunningPlayerMutedTasks().get(target.getUniqueId()).cancel();
                }
            }, 0L, 1200L); // updates every minute

            Map<UUID, BukkitTask> runningTasks = VindicterraStaffUtils.getRunningPlayerMutedTasks();
            runningTasks.put(target.getUniqueId(), muteTask);
            VindicterraStaffUtils.setRunningPlayerMutedTasks(runningTasks);

            target.sendMessage(Component.text("\nYou have been muted for " + strings[1] + "\nReason: " + reason + "\n", TextColor.color(255, 0, 0), TextDecoration.BOLD));

        } else {
            BukkitTask muteTask = p.getServer().getScheduler().runTaskTimer(VindicterraStaffUtils.getPlugin(), () -> {
                if (((int) (System.currentTimeMillis() / 1000L)) >= unMuteTimeStamp) {
                    target.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isMuted"), PersistentDataType.BOOLEAN, false);
                    VindicterraStaffUtils.getRunningPlayerMutedTasks().get(target.getUniqueId()).cancel();
                }
            }, 0L, 1200L); // updates every minute

            Map<UUID, BukkitTask> runningTasks = VindicterraStaffUtils.getRunningPlayerMutedTasks();
            runningTasks.put(target.getUniqueId(), muteTask);
            VindicterraStaffUtils.setRunningPlayerMutedTasks(runningTasks);

            target.sendMessage(Component.text("\nYou have been permanently muted for: " + reason + "\n", TextColor.color(255, 0, 0), TextDecoration.BOLD));
        }

        return true;
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
