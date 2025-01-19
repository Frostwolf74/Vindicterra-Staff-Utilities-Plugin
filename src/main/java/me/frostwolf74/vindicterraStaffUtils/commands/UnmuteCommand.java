package me.frostwolf74.vindicterraStaffUtils.commands;
import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class UnmuteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player p){
            if(!p.hasPermission("VSU.punish.mute")) return true;

            List<UUID> mutedPlayers = VindicterraStaffUtils.getScheduleUnmutePlayers();

            if(strings[0].equals("all") && p.isOp()){ // unmutes all players, ops have access only
                for(OfflinePlayer target : p.getServer().getOfflinePlayers()) {
                    if(mutedPlayers.contains(target.getUniqueId())){
                        mutedPlayers.remove(target.getUniqueId());

                        unmute(target);
                    }
                }

                VindicterraStaffUtils.setScheduleUnmutePlayers(mutedPlayers);

                p.sendMessage(Component.text("All players have been unmuted.", NamedTextColor.GREEN));
                return true;
            }

            OfflinePlayer target = p.getServer().getOfflinePlayer(strings[0]);

            unmute(target);
            return true;
        }
        return false;
    }

    public static void unmute(OfflinePlayer offlineTarget){
        if(offlineTarget == null){
            return;
        }

        if(offlineTarget.isOnline()){
            Player target = Bukkit.getPlayer(offlineTarget.getUniqueId());

            target.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isMuted"), PersistentDataType.BOOLEAN, false);
            return;
        }

        VindicterraStaffUtils.getScheduleUnmutePlayers().add(offlineTarget.getUniqueId());
    }
}
