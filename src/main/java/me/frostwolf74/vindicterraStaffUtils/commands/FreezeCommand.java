package me.frostwolf74.vindicterraStaffUtils.commands;

import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class FreezeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) { // TODO needs player-on-player testing
        if(strings.length == 0) return false;

        if(commandSender instanceof Player p){
            if(p.hasPermission("VSU.freeze")){
                Player target = p.getServer().getPlayer(strings[0]);

                if(target == null){
                    p.sendMessage(Component.text("Player not found or not online", TextColor.color(255, 0, 0)));
                    return true;
                }

                assert target != null;
                freezePlayer(target);

                return true;
            }
        }
        return false;
    }

    public static void freezePlayer(Player p){
        if(Boolean.TRUE.equals(p.getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isFrozen"), PersistentDataType.BOOLEAN))){
            p.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isFrozen"), PersistentDataType.BOOLEAN, false);
            // TODO implement functionality
        }
        else {
            p.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "isFrozen"), PersistentDataType.BOOLEAN, true);
            // TODO implement functionality
        }
    }
}
