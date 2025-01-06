package me.frostwolf74.vindicterraStaffUtils;

import me.frostwolf74.vindicterraStaffUtils.commands.*;
import me.frostwolf74.vindicterraStaffUtils.listeners.*;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public final class VindicterraStaffUtils extends JavaPlugin {
    private static VindicterraStaffUtils plugin;
    private static Map<UUID, BukkitTask> runningTasks = new HashMap<>(); // for hotbar text in staff mode
    private static Map<UUID, BukkitTask> runningPlayerMutedTasks = new HashMap<>(); // for muted players who leave and rejoin
    private static Map<UUID, PlayerInventory> inventoryStates = new HashMap<>(); // for viewed inventories


    @Override
    public void onEnable() {
        plugin = this;

        getCommand("staff").setExecutor(new StaffModeCommand());
        getCommand("staffchat").setExecutor(new StaffChatCommand());
        getCommand("freeze").setExecutor(new FreezeCommand());
        getCommand("inventorysee").setExecutor(new InventorySeeCommand());
        getCommand("vanish").setExecutor(new VanishCommand());
        getCommand("ban").setExecutor(new BanCommand());
        getCommand("kick").setExecutor(new KickCommand());
        getCommand("mute").setExecutor(new MuteCommand());
        getCommand("unmute").setExecutor(new UnmuteCommand());
        getCommand("unban").setExecutor(new UnbanCommand());

        getServer().getPluginManager().registerEvents(new ItemInteractionListener(), this);
        getServer().getPluginManager().registerEvents(new MenuClickEventListener(), this);
        getServer().getPluginManager().registerEvents(new ItemDropListener(), this);
        getServer().getPluginManager().registerEvents(new ItemPickUpListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new onChatMessageEventListener(), this);
        getServer().getPluginManager().registerEvents(new onJoinEventListener(), this);
    }

    @Override
    public void onDisable() {
        runningTasks.values().forEach(BukkitTask::cancel);
    }

    public static VindicterraStaffUtils getPlugin(){
        return plugin;
    }

    public static Map<UUID, BukkitTask> getRunningTasks() {
        return runningTasks;
    }

    public static void setRunningTasks(Map<UUID, BukkitTask> newRunningTasks) {
        runningTasks = newRunningTasks;
    }

    public static Map<UUID, BukkitTask> getRunningPlayerMutedTasks() {
        return runningPlayerMutedTasks;
    }

    public static void setRunningPlayerMutedTasks(Map<UUID, BukkitTask> runningPlayerMutedTasks) {
        VindicterraStaffUtils.runningPlayerMutedTasks = runningPlayerMutedTasks;
    }

    public static Map<UUID, PlayerInventory> getInventoryStates() {
        return inventoryStates;
    }

    public static void setInventoryStates(Map<UUID, PlayerInventory> inventoryStates) {
        VindicterraStaffUtils.inventoryStates = inventoryStates;
    }

}
