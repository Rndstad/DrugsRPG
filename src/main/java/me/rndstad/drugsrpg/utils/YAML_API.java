package me.rndstad.drugsrpg.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class YAML_API {

	private String fileName;
	private JavaPlugin plugin;
	private File ConfigFile;
	private FileConfiguration Configuration;

	public YAML_API(JavaPlugin plugin, String fileName, boolean save) {
		if (plugin == null) {

			throw new IllegalArgumentException("plugin cannot be null, set it to this or getInstance, plugin");
		}
		this.plugin = plugin;
		this.fileName = fileName;
		File dataFolder = plugin.getDataFolder();
		if (dataFolder == null) {

			throw new IllegalStateException();
		}
		this.ConfigFile = new File(dataFolder.toString() + File.separatorChar + this.fileName);
	    if(save) {
	    	saveDefaultConfig();
	    }
	}

	@SuppressWarnings("deprecation")
	public void reloadConfig() {

		try {

			this.Configuration = YamlConfiguration
					.loadConfiguration(new InputStreamReader(new FileInputStream(this.ConfigFile), "UTF-8"));
		} catch (UnsupportedEncodingException | FileNotFoundException e) {

			e.printStackTrace();
		}
		InputStream defConfigStream = this.plugin.getResource(this.fileName);
		if (defConfigStream != null) {

			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(ConfigFile);
			this.Configuration.setDefaults(defConfig);
		}
	}

	public FileConfiguration getConfig() {
		if (this.Configuration == null) {

			reloadConfig();
		}
		return this.Configuration;
	}

	public void saveConfig() {

		if ((this.Configuration == null) || (this.ConfigFile == null)) {
			return;
		}
		try {

			getConfig().save(this.ConfigFile);
		} catch (IOException ex) {

			this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.ConfigFile, ex);
		}
	}

	public void saveDefaultConfig() {
		if (!this.ConfigFile.exists()) {
			this.plugin.saveResource(this.fileName, false);
		}
	}
}