package me.frostwolf74.vindicterraStaffUtils;

import lombok.Getter;
import lombok.Setter;
import me.frostwolf74.vindicterraStaffUtils.commands.*;
import me.frostwolf74.vindicterraStaffUtils.listeners.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import java.util.*;

@Getter
public final class VindicterraStaffUtils extends JavaPlugin {
    @Getter
    private static VindicterraStaffUtils plugin;
    @Getter
    private final static Map<UUID, BukkitTask> runningTasks = new HashMap<>(); // for hotbar text in staff mode
    @Getter
    @Setter
    private static Map<UUID, BukkitTask> runningPlayerMutedTasks = new HashMap<>(); // contains bukkit runnables that unmute the player when their mute expires
    @Getter
    private final static List<UUID> scheduleUnmutePlayers = new ArrayList<>(); // players scheduled to be unmuted the next time they are online
    private final ConfigFile configFile = new ConfigFile(this, "tasks");
    @Getter
    private final static Map<Player, Player> targetPlayers = new HashMap<>();
    //                    ^      ^
    //               staff mem   target

    @Override
    public void onEnable() {
        plugin = this;

        configFile.load();

        this.getServer().getConsoleSender().sendMessage("§4██╗   ██╗    ███████╗    ██╗   ██╗");
        this.getServer().getConsoleSender().sendMessage("§4██║   ██║    ██╔════╝    ██║   ██║");
        this.getServer().getConsoleSender().sendMessage("§4██║   ██║    ███████╗    ██║   ██║");
        this.getServer().getConsoleSender().sendMessage("§7╚██╗ ██╔╝    ╚════██║    ██║   ██║");
        this.getServer().getConsoleSender().sendMessage("§7 ╚████╔╝     ███████║    ╚██████╔╝");
        this.getServer().getConsoleSender().sendMessage("§7  ╚═══╝      ╚══════╝     ╚═════╝ ");
        this.getServer().getConsoleSender().sendMessage("§b          By Frostwolf74          ");


        getCommand("staff").setExecutor(new StaffModeCommand());
        getCommand("staffchat").setExecutor(new StaffChatCommand());
        getCommand("freeze").setExecutor(new FreezeCommand());
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
        configFile.save();
    }
}