package me.frostwolf74.vindicterraStaffUtils.commands;
import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class UnmuteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player p){
            if(!p.hasPermission("VSU.punish.mute")) return true;

            if(strings[0].equals("all") && p.isOp()){ // unmutes all players, ops have access only
                Map<UUID, BukkitTask> mutedPlayers = VindicterraStaffUtils.getRunningPlayerMutedTasks();

                for(OfflinePlayer player : p.getServer().getOfflinePlayers()) {
                    if(mutedPlayers.containsKey(player.getUniqueId())){
                        mutedPlayers.get(player.getUniqueId()).cancel();
                        mutedPlayers.remove(player.getUniqueId());
                    }
                }

                VindicterraStaffUtils.setRunningPlayerMutedTasks(mutedPlayers);

                p.sendMessage(Component.text("All players have been unmuted.", NamedTextColor.GREEN));
                return true;
            }

            Player target = p.getServer().getPlayer(strings[0]);

            if (target == null) {
                p.sendMessage(Component.text("Player not found", NamedTextColor.RED));
                return true;
            }

            if(Boolean.TRUE.equals(target.getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isMuted"), PersistentDataType.BOOLEAN))){
                target.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isMuted"), PersistentDataType.BOOLEAN, false);

                VindicterraStaffUtils.getRunningPlayerMutedTasks().get(target.getUniqueId()).cancel();
                VindicterraStaffUtils.getRunningPlayerMutedTasks().remove(target.getUniqueId());
            }
            else{
                p.sendMessage(Component.text("Player not muted", NamedTextColor.RED));
            }
            return true;
        }
        return false;
    }
}
