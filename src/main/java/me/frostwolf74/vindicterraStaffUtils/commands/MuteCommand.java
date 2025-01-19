package me.frostwolf74.vindicterraStaffUtils.commands;

import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import net.kyori.adventure.text.format.TextDecoration;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
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

            return mutePlayer(p, target, strings); // returns if the command was successful
        }
        return false;
    }

    public static boolean mutePlayer(Player p, Player target, String[] strings) {
        if (!p.hasPermission("VSU.punish.mute")) return false;

        Calendar expireDate = Calendar.getInstance();

        int reasonIndex = 2; // "2" is the index position in the string args for where the reason should be by default, ex: /mute [player] [limit] [reason]
                             //                                                                                                       0        1       2
        String timeType; // h, d, w, m, y
        List<String> stringsSplit;

        if (strings.length > 1) { // prevents empty command arguments
            stringsSplit = new ArrayList<>(List.of(strings[1].split(""))); // split argument limit into sections

            timeType = stringsSplit.get(stringsSplit.size() - 1); // gets the last index since thats always where the time type is
        }
        else {
            return false;
        }

        int muteLengthS = 0;

        if (isNumeric(stringsSplit.get(0))) { // checks if the argument specified a time limit or hasn't
            stringsSplit.remove(stringsSplit.size() - 1); // removes the time type character from the argument

            float timeLimit = Float.parseFloat(String.join("", stringsSplit)); // the remaining information can be put into the time limit as a float, we now have the time type and time limit

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

            expireDate.setTimeInMillis(System.currentTimeMillis() + muteLengthS * 1000L); // convert the seconds into a unix timestamp

            p.sendMessage(Component.text("\nPlayer " + target.getName() + " has been muted.\n", NamedTextColor.RED));
        }
        else { // if the limit argument isn't numeric it must be a string and therefore that must mean it is actually the reason argument and a time limit hasn't been specified therefore it must be a permanent mute
            expireDate = null;
            reasonIndex = 1;

            p.sendMessage(Component.text("\nPlayer " + target.getName() + " has been permanently muted.\n", NamedTextColor.RED));
        }

        StringBuilder reason = new StringBuilder(); // merge the reason arguments into a single string

        for (int i = reasonIndex; i < strings.length; ++i) {
            reason.append(strings[i]).append(" ");
        }

        int unixTime = (int) (System.currentTimeMillis() / 1000L); // calculating unmute time
        int unMuteTimeStamp = unixTime + muteLengthS;

        target.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isMuted"), PersistentDataType.BOOLEAN, true); // using persistent data containers so the data remains persistent and is on a per-player basis
        target.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "unmuteTimeStamp"), PersistentDataType.INTEGER, unMuteTimeStamp);
        VindicterraStaffUtils.getScheduleUnmutePlayers().add(target.getUniqueId());

        if (expireDate != null) { // creating a bukkit runnable to unmute the player when their mute time has expired
            new BukkitRunnable() { // this will repeat every minute to check if the player is online
                @Override
                public void run() {
                    if (((int) (System.currentTimeMillis() / 1000L)) >= unMuteTimeStamp) {
                        VindicterraStaffUtils.getScheduleUnmutePlayers().remove(target.getUniqueId());
                        UnmuteCommand.unmute(target);

                        this.cancel();
                    }
                }
            }.runTaskTimer(VindicterraStaffUtils.getPlugin(), 0, 1200L);

            target.sendMessage(Component.text("\nYou have been muted for " + strings[1] + "\nReason: " + reason + "\n", TextColor.color(255, 0, 0), TextDecoration.BOLD));
        }
        else { // we want to make sure the player is muted permanently but should also be in the running tasks hashmap so they can be unmuted manually via command

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
