package me.frostwolf74.vindicterraStaffUtils;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Getter
public class ConfigFile extends YamlConfiguration { // +++ from bearofbusiness

    @SuppressWarnings("CanBeFinal")
    private final JavaPlugin plugin;
    private File file;

    public ConfigFile(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), name + ".yml");

        if(!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(name + ".yml", false);
        }

        this.load();
    }

    public void load() {
        try {
            this.load(this.file);
        } catch(Exception exception) {
            plugin.getLogger().severe("Failed to load config file " + this.file.getName() + "." + exception.getMessage() + "\n" + Arrays.toString(exception.getStackTrace()));
        }
    }

    public void save() {
        try {
            this.save(this.file);
        } catch(IOException exception) {
            plugin.getLogger().severe("Failed to save config file " + this.file.getName() + "." + exception.getMessage() + "\n" + Arrays.toString(exception.getStackTrace()));
        }
    }

    public void reload() {
        this.file = new File(plugin.getDataFolder(), this.file.getName() + ".yml");
        this.load();
    }
}
