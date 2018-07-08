package me.rndstad.drugsrpg;

import me.rndstad.drugsrpg.commands.Drugs;
import me.rndstad.drugsrpg.managers.DrugManager;
import me.rndstad.drugsrpg.listeners.DrugListeners;
import me.rndstad.drugsrpg.utils.Utils;
import me.rndstad.drugsrpg.utils.YAML_API;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {

	private static Core instance;
    private YAML_API config;

	String prefix = getConfig().getString("Prefix");

	public void onEnable() {
		instance = this;
		new Metrics(this);
		new Utils().registerGlow();
		config = new YAML_API(this, "config.yml", true);

		getCommand("drugs").setExecutor(new Drugs());

		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new DrugListeners(), this);

		DrugManager.getInstance().init();
	}
	
	public void onDisable() {
		DrugManager.getInstance().getDrugs().clear();
		instance = null;
	}

	public String getPrefix() {
		return prefix;
	}

	public static Core getInstance() {
		return instance;
	}

    public YAML_API getConfigData() {
        return config;
    }
}