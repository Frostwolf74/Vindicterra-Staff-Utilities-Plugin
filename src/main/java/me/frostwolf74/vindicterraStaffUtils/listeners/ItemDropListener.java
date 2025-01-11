package me.frostwolf74.vindicterraStaffUtils.listeners;

import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;
import me.frostwolf74.vindicterraStaffUtils.commands.StaffModeCommand;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.persistence.PersistentDataType;

public class ItemDropListener implements Listener {
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if(Boolean.TRUE.equals(e.getPlayer().getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "inStaffMode"), PersistentDataType.BOOLEAN))){
            e.setCancelled(true);
            e.getPlayer().getInventory().clear();
            e.getPlayer().getInventory().setContents(StaffModeCommand.getStaffInventory(e.getPlayer()).getContents());
            e.getPlayer().getInventory().getItemInMainHand().setAmount(0); // prevent duplicate items
        }
    }
}
