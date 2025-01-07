package me.frostwolf74.vindicterraStaffUtils.listeners;

import me.frostwolf74.vindicterraStaffUtils.VindicterraStaffUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.UUID;

public class onInventoryCloseEventListener implements Listener {
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();

        if(Boolean.TRUE.equals(p.getPersistentDataContainer().get(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "viewingInventory"), PersistentDataType.BOOLEAN))){
            p.getPersistentDataContainer().set(new NamespacedKey(VindicterraStaffUtils.getPlugin(), "viewingInventory"), PersistentDataType.BOOLEAN, false);

            Map<UUID, Inventory> openInventories = VindicterraStaffUtils.getopenedInventories();

            openInventories.remove(e.getPlayer().getUniqueId());
        }
    }
}
