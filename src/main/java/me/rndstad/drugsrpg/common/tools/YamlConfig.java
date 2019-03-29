package me.rndstad.drugsrpg.common.tools;

import java.io.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class YamlConfig {

    private String fileName;
    private JavaPlugin plugin;
    private File ConfigFile;
    private FileConfiguration Configuration;

    public YamlConfig(JavaPlugin plugin, String fileName, boolean save) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin's instance = null.");
        }
        this.plugin = plugin;
        this.fileName = fileName;
        File dataFolder = plugin.getDataFolder();
        if (dataFolder == null) {

            throw new IllegalStateException();
        }
        this.ConfigFile = new File(dataFolder.toString() + File.separatorChar + this.fileName);
        if (save) {
            saveDefaultConfig();
        }
    }

    public void reloadConfig() {
        if (ConfigFile == null) {
            this.ConfigFile = new File(plugin.getDataFolder(), this.fileName);
        }
        this.Configuration = YamlConfiguration.loadConfiguration(ConfigFile);
        Reader defConfigStream;
        try {
            defConfigStream = new InputStreamReader(plugin.getResource(this.fileName), "UTF-8");
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            this.Configuration.setDefaults(defConfig);
        } catch (UnsupportedEncodingException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        if (this.Configuration == null) {

            reloadConfig();
        }
        return this.Configuration;
    }

    public void saveConfig() {
        try {
            getConfig().save(this.ConfigFile);
        } catch (IOException e) {
            this.plugin.getServer().getConsoleSender().sendMessage("Config file can't be saved!");
        }
    }

    private void saveDefaultConfig() {
        if (!this.ConfigFile.exists()) {
            this.ConfigFile.getParentFile().mkdirs();
            if (this.plugin.getResource(this.ConfigFile.getName()) != null) {
                this.plugin.saveResource(this.ConfigFile.getName(), false);
                return;
            }
            try {
                this.ConfigFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}