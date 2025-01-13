package me.frostwolf74.vindicterraStaffUtils;

import me.frostwolf74.vindicterraStaffUtils.commands.*;
import me.frostwolf74.vindicterraStaffUtils.listeners.*;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public final class VindicterraStaffUtils extends JavaPlugin {
    private static VindicterraStaffUtils plugin;
    private static Map<UUID, BukkitTask> runningTasks = new HashMap<>(); // for hotbar text in staff mode
    private static Map<UUID, BukkitTask> runningPlayerMutedTasks = new HashMap<>(); // for muted players who leave and rejoin
    private static Map<UUID, Inventory> openedInventories = new HashMap<>(); // for viewed inventories


    @Override
    public void onEnable() {
        plugin = this;

        this.getServer().getConsoleSender().sendMessage("\n\n" +
        NamedTextColor.DARK_RED                 +"██╗ " +                           "  ██╗" +                           "██╗" +                           "███╗ " +                           "  ██╗" +                           "███" +                           "███╗ " +                           "██╗" +                           " ███" +                           "███╗" +                           "████████╗" +                           "███" +                           "████╗" +                           "███" +                           "███╗ " +                           "███" +                           "███╗ " +                           " ██" +                           "███╗ " +                           "    ████" +                           "███╗" +                           "████████╗" +                           " ███" +                           "██╗ " +                           "███████╗" +                           "███████╗" +                           "    ██╗ " +                           "  ██╗" +                           "████████╗" +                           "██╗" +                           "██╗     " +                           "████" +                           "███╗\n" +
        NamedTextColor.DARK_RED                 +"██║ " +                           "  ██║" +                           "██║" +                           "████╗" +                           "  ██║" +                           "██╔" +                           "══██╗" +                           "██║" +                           "██╔═" +                           "═══╝" +                           "╚══██╔══╝" +                           "██╔" +                           "════╝" +                           "██╔" +                           "══██╗" +                           "██╔" +                           "══██╗" +                           "██╔" +                           "══██╗" +                           "    ██╔═" +                           "═══╝" +                           "╚══██╔══╝" +                           "██╔═" +                           "═██╗" +                           "██╔════╝" +                           "██╔════╝" +                           "    ██║ " +                           "  ██║" +                           "╚══██╔══╝" +                           "██║" +                           "██║     " +                           "██╔═" +                           "═══╝\n" +
        NamedTextColor.GRAY                     +"██║ " + NamedTextColor.DARK_RED + "  ██║" + NamedTextColor.DARK_RED + "██║" + NamedTextColor.DARK_RED + "██╔██" + NamedTextColor.GRAY     + "╗ ██║" + NamedTextColor.DARK_RED + "██║" + NamedTextColor.DARK_RED + "  ██║" + NamedTextColor.DARK_RED + "██║" + NamedTextColor.GRAY     + "██║ " + NamedTextColor.DARK_RED + "    " + NamedTextColor.DARK_RED + "   ██║   " + NamedTextColor.DARK_RED + "███" + NamedTextColor.GRAY     + "██╗  " + NamedTextColor.DARK_RED + "███" + NamedTextColor.DARK_RED + "███╔╝" + NamedTextColor.DARK_RED + "███" + NamedTextColor.GRAY     + "███╔╝" + NamedTextColor.DARK_RED + "███" + NamedTextColor.DARK_RED + "████║" + NamedTextColor.DARK_RED + "    ████" + NamedTextColor.GRAY     + "███╗" + NamedTextColor.DARK_RED + "   ██║   " + NamedTextColor.DARK_RED + "████" + NamedTextColor.DARK_RED + "███║" + NamedTextColor.GRAY     + "█████╗  " + NamedTextColor.DARK_RED + "█████╗  " + NamedTextColor.DARK_RED + "    ██║ " + NamedTextColor.DARK_RED + "  ██║" + NamedTextColor.GRAY     + "   ██║   " + NamedTextColor.DARK_RED + "██║" + NamedTextColor.DARK_RED + "██║     " + NamedTextColor.DARK_RED + "████" + NamedTextColor.GRAY     + "███╗\n" +
        NamedTextColor.GRAY                     +"╚██╗" + NamedTextColor.GRAY     + " ██╔╝" + NamedTextColor.DARK_RED + "██║" + NamedTextColor.GRAY     + "██║╚█" + NamedTextColor.GRAY     + "█╗██║" + NamedTextColor.GRAY     + "██║" + NamedTextColor.DARK_RED + "  ██║" + NamedTextColor.GRAY     + "██║" + NamedTextColor.GRAY     + "██║ " + NamedTextColor.GRAY     + "    " + NamedTextColor.DARK_RED + "   ██║   " + NamedTextColor.GRAY     + "██╔" + NamedTextColor.GRAY     + "══╝  " + NamedTextColor.GRAY     + "██╔" + NamedTextColor.DARK_RED + "══██╗" + NamedTextColor.GRAY     + "██╔" + NamedTextColor.GRAY     + "══██╗" + NamedTextColor.GRAY     + "██╔" + NamedTextColor.DARK_RED + "══██║" + NamedTextColor.GRAY     + "    ╚═══" + NamedTextColor.GRAY     + "═██║" + NamedTextColor.GRAY     + "   ██║   " + NamedTextColor.DARK_RED + "██╔═" + NamedTextColor.GRAY     + "═██║" + NamedTextColor.GRAY     + "██╔══╝  " + NamedTextColor.GRAY     + "██╔══╝  " + NamedTextColor.DARK_RED + "    ██║ " + NamedTextColor.GRAY     + "  ██║" + NamedTextColor.GRAY     + "   ██║   " + NamedTextColor.GRAY     + "██║" + NamedTextColor.DARK_RED + "██║     " + NamedTextColor.GRAY     + "╚═══" + NamedTextColor.GRAY     + "═██║\n" +
        NamedTextColor.GRAY                     +" ╚██" + NamedTextColor.GRAY     + "██╔╝ " + NamedTextColor.GRAY     + "██║" + NamedTextColor.GRAY     + "██║ ╚" + NamedTextColor.GRAY     + "████║" + NamedTextColor.GRAY     + "███" + NamedTextColor.GRAY     + "███╔╝" + NamedTextColor.GRAY     + "██║" + NamedTextColor.GRAY     + "╚███" + NamedTextColor.GRAY     + "███╗" + NamedTextColor.GRAY     + "   ██║   " + NamedTextColor.GRAY     + "███" + NamedTextColor.GRAY     + "████╗" + NamedTextColor.GRAY     + "██║" + NamedTextColor.GRAY     + "  ██║" + NamedTextColor.GRAY     + "██║" + NamedTextColor.GRAY     + "  ██║" + NamedTextColor.GRAY     + "██║" + NamedTextColor.GRAY     + "  ██║" + NamedTextColor.GRAY     + "    ████" + NamedTextColor.GRAY     + "███║" + NamedTextColor.GRAY     + "   ██║   " + NamedTextColor.GRAY     + "██║ " + NamedTextColor.GRAY     + " ██║" + NamedTextColor.GRAY     + "██║     " + NamedTextColor.GRAY     + "██║     " + NamedTextColor.GRAY     + "    ╚███" + NamedTextColor.GRAY     + "███╔╝" + NamedTextColor.GRAY     + "   ██║   " + NamedTextColor.GRAY     + "██║" + NamedTextColor.GRAY     + "███████╗" + NamedTextColor.GRAY     + "████" + NamedTextColor.GRAY     + "███║\n" +
        NamedTextColor.GRAY                     +"  ╚═" + NamedTextColor.GRAY     + "══╝  " + NamedTextColor.GRAY     + "╚═╝" + NamedTextColor.GRAY     + "╚═╝  " + NamedTextColor.GRAY     + "╚═══╝" + NamedTextColor.GRAY     + "╚══" + NamedTextColor.GRAY     + "═══╝ " + NamedTextColor.GRAY     + "╚═╝" + NamedTextColor.GRAY     + " ╚══" + NamedTextColor.GRAY     + "═══╝" + NamedTextColor.GRAY     + "   ╚═╝   " + NamedTextColor.GRAY     + "╚══" + NamedTextColor.GRAY     + "════╝" + NamedTextColor.GRAY     + "╚═╝" + NamedTextColor.GRAY     + "  ╚═╝" + NamedTextColor.GRAY     + "╚═╝" + NamedTextColor.GRAY     + "  ╚═╝" + NamedTextColor.GRAY     + "╚═╝" + NamedTextColor.GRAY     + "  ╚═╝" + NamedTextColor.GRAY     + "    ╚═══" + NamedTextColor.GRAY     + "═══╝" + NamedTextColor.GRAY     + "   ╚═╝   " + NamedTextColor.GRAY     + "╚═╝ " + NamedTextColor.GRAY     + " ╚═╝" + NamedTextColor.GRAY     + "╚═╝     " + NamedTextColor.GRAY     + "╚═╝     " + NamedTextColor.GRAY     + "     ╚══" + NamedTextColor.GRAY     + "═══╝ " + NamedTextColor.GRAY     + "   ╚═╝   " + NamedTextColor.GRAY     + "╚═╝" + NamedTextColor.GRAY     + "╚══════╝" + NamedTextColor.GRAY     + "╚═══" + NamedTextColor.GRAY     + "═══╝\n" +
                                            "\n" +
                                            "                                 _                 _____                        _                        _    __   _____   _  _   \n" +
                                            "                                | |__    _   _    |  ___|  _ __    ___    ___  | |_  __      __   ___   | |  / _| |___  | | || |  \n" +
                                            "                                | '_ \\  | | | |   | |_    | '__|  / _ \\  / __| | __| \\ \\ /\\ / /  / _ \\  | | | |_     / /  | || |_ \n" +
                                            "                                | |_) | | |_| |   |  _|   | |    | (_) | \\__ \\ | |_   \\ V  V /  | (_) | | | |  _|   / /   |__   _|\n" +
                                            "                                |_.__/   \\__, |   |_|     |_|     \\___/  |___/  \\__|   \\_/\\_/    \\___/  |_| |_|    /_/       |_|  \n" +
                                            "                                        |___/                                                                                          \n\n"); // enormous on-enable message because why not

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
        getServer().getPluginManager().registerEvents(new ItemDropListener(), this);
        getServer().getPluginManager().registerEvents(new ItemPickUpListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new onBlockInteractionEventListener(), this);
        getServer().getPluginManager().registerEvents(new onChatMessageEventListener(), this);
        getServer().getPluginManager().registerEvents(new onPlayerJoinEventListener(), this);
        getServer().getPluginManager().registerEvents(new onPlayerLeaveEventListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDamageEntityEventListener(), this);
        getServer().getPluginManager().registerEvents(new onPlayerMoveEventListener(), this);
        getServer().getPluginManager().registerEvents(new MenuClickEventListener(), this);
        getServer().getPluginManager().registerEvents(new onEntityDamageEventListener(), this);
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

    public static Map<UUID, Inventory> getopenedInventories() {
        return openedInventories;
    }

    public static void setOpenedInventories(Map<UUID, Inventory> openedInventories) {
        VindicterraStaffUtils.openedInventories = openedInventories;
    }
}
