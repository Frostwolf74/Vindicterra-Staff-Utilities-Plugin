package me.frostwolf74.vindicterraStaffUtils.commands;

import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.BanList;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
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
        if(commandSender instanceof Player p) {
            if(!p.hasPermission("VSU.punish.mute")) return false;

            Player target = p.getServer().getPlayer(strings[0]);

            if(target == null){
                p.sendMessage(Component.text("Player not found or not online", TextColor.color(255, 0, 0)));
                return true;
            }

            // splits argument into time type and time limit
            List<String> stringsSplit = new ArrayList<>(List.of(strings[1].split("")));

            String timeType = stringsSplit.get(stringsSplit.size() - 1);
            stringsSplit.remove(stringsSplit.size() - 1);

            float timeLimit = Float.parseFloat(String.join("", stringsSplit));

            int unixTime = (int) (System.currentTimeMillis() / 1000L);
            int muteLengthS = 0;

            switch (timeType) {
                case "h":
                    muteLengthS = Math.round(timeLimit * 60);
                    break;
                case "d":
                    muteLengthS = Math.round(timeLimit * 86400);
                    break;
                case "w":
                    muteLengthS = Math.round(timeLimit * 604800);
                    break;
                case "m":
                    muteLengthS = Math.round(timeLimit * 2592000);
                    break;
                case "y":
                    muteLengthS = Math.round(timeLimit * 2592000 * 12);
                default:
                    p.sendMessage(Component.text("Invalid time type: " + timeType, TextColor.color(255, 0, 0)));
            }

            target.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isMuted"), PersistentDataType.BOOLEAN, true);
            target.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "unmuteTimeStamp"), PersistentDataType.INTEGER, (unixTime + muteLengthS));

            int unMuteTimeStamp = target.getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "unmuteTimeStamp"), PersistentDataType.INTEGER);

            BukkitTask muteTask = p.getServer().getScheduler().runTaskTimer(VindicterraStaffUtils.getPlugin(), () -> {
                if(((int) (System.currentTimeMillis() / 1000L)) >= unMuteTimeStamp) {
                    target.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isMuted"), PersistentDataType.BOOLEAN, false);
                    VindicterraStaffUtils.getRunningPlayerMutedTasks().get(target.getUniqueId()).cancel();
                }
            }, 0L, 1200L); // updates every minute

            Map<UUID, BukkitTask> runningTasks = VindicterraStaffUtils.getRunningPlayerMutedTasks();
            runningTasks.put(target.getUniqueId(), muteTask);
            VindicterraStaffUtils.setRunningPlayerMutedTasks(runningTasks);

            p.sendMessage(Component.text(target.getName() + " has been muted for " + strings[1], TextColor.color(255, 128, 0)));
            target.sendMessage(Component.text("You have been muted for " + strings[1],  TextColor.color(255, 0, 0), TextDecoration.BOLD));

            return true;
        }
        return false;
//        if(commandSender instanceof Player p){
//            if(!p.hasPermission("VSU.punish.ban")) return false;
//
//            if(strings.length == 0){
//                return false;
//            }
//
//            OfflinePlayer target = p.getServer().getPlayer(strings[0]);
//
//            if(target == null){
//                p.sendMessage(Component.text("Player not found or not online", TextColor.color(255, 0, 0)));
//                return true;
//            }
//
//            Calendar expireDate = Calendar.getInstance();
//
//            int reasonIndex = 2;
//            String timeType = null;
//            List<String> stringsSplit = new ArrayList<>();
//
//            if(strings.length > 1){
//                stringsSplit = new ArrayList<>(List.of(strings[1].split("")));
//
//                timeType = stringsSplit.get(stringsSplit.size() - 1);
//            }
//            else{
//                return false;
//            }
//
//            if(isNumeric(stringsSplit.get(0))){
//                stringsSplit.remove(stringsSplit.size() - 1);
//
//                float timeLimit = Float.parseFloat(String.join("", stringsSplit));
//
//                int banLengthS = 0;
//
//                switch (timeType) {
//                    case "h":
//                        banLengthS = Math.round(timeLimit * 60);
//                        break;
//                    case "d":
//                        banLengthS = Math.round(timeLimit * 86400);
//                        break;
//                    case "w":
//                        banLengthS = Math.round(timeLimit * 604800);
//                        break;
//                    case "m":
//                        banLengthS = Math.round(timeLimit * 2592000);
//                        break;
//                    case "y":
//                        banLengthS = Math.round(timeLimit * 2592000 * 12);
//                        break;
//                    default:
//                        p.sendMessage(Component.text("Invalid time type: " + timeType, TextColor.color(255, 0, 0)));
//                        return true;
//                }
//
//                expireDate.setTimeInMillis(System.currentTimeMillis() + banLengthS * 1000L);
//
//                p.sendMessage(Component.text("Player " + target.getName() + " has been banned.", NamedTextColor.RED));
//            }
//            else{
//                expireDate = null;
//                reasonIndex = 1;
//
//                p.sendMessage(ChatColor.GREEN + "Player " + target.getName() + " has been permanently muted.");
//            }
//
//            StringBuilder reason = new StringBuilder();
//
//            for(int i = reasonIndex; i < strings.length; ++i){
//                reason.append(strings[i]).append(" ");
//            }
//
//            if(expireDate != null){
//                // TODO add temp mute
//            }
//            else{
//                // TODO add perm mute
//            }
//
//
//            if(target.isOnline()){
//                if(expireDate == null){
//                    ((Player) target).kick(Component.text("You have been permanently banned.\n\nReason: " + reason));
//                }
//                else{
//                    ((Player) target).kick(Component.text("You have been banned for " + strings[1] + "\n\nReason: " + reason));
//                }
//
//            }
//
//            return true;
//        }
//        else{
//            commandSender.getServer().getLogger().info("You must use /pardon if you wish to unban from console.");
//        }
//        return false;
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
