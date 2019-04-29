package me.rndstad.drugsrpg;

import fr.minuskube.inv.InventoryManager;
import me.rndstad.drugsrpg.builder.BuilderManager;
import me.rndstad.drugsrpg.commands.DrugsCommand;
import me.rndstad.drugsrpg.common.tools.Metrics;
import me.rndstad.drugsrpg.common.tools.YamlConfig;
import me.rndstad.drugsrpg.consume.DrugsManager;
import me.rndstad.drugsrpg.database.DatabaseManager;
import me.rndstad.drugsrpg.glow.GlowManager;
import me.rndstad.drugsrpg.placeholder.PlaceholderManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DrugsPlugin extends JavaPlugin {

    private YamlConfig config;
    private YamlConfig drugs;

    private DatabaseManager databaseManager;
    private InventoryManager invManager;
    private DrugsManager drugsManager;
    private GlowManager glowManager;
    private PlaceholderManager placeholderManager;
    private BuilderManager builderManager;

    private Metrics metrics;

    @Override
    public void onEnable() {
        super.onEnable();

        config = new YamlConfig(this, "config.yml", true);
        if (!config.getConfig().getBoolean("preferences.use-mysql")) drugs = new YamlConfig(this, "drugs.yml", true);

        databaseManager = new DatabaseManager(this, config, drugs);
        invManager = new InventoryManager(this);
        drugsManager = new DrugsManager(this);
        glowManager = new GlowManager(this);
        placeholderManager = new PlaceholderManager(this);
        builderManager = new BuilderManager(this);


        getCommand("drugs").setExecutor(new DrugsCommand(this));

        metrics = new Metrics(this);

        invManager.init();

    }

    @Override
    public void onDisable() {
        drugsManager.saveDrugs();

        if (databaseManager.use_mysql()) {
            if (databaseManager.getPool().isClosed()) {
                databaseManager.getPool().close();
            }
        }
    }

    public DrugsManager getDrugsManager() {
        return drugsManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public InventoryManager getInventoryManager() {
        return invManager;
    }

    public GlowManager getGlowManager() {
        return glowManager;
    }

    public BuilderManager getBuilderManager() {
        return builderManager;
    }

    public PlaceholderManager getPlaceholderManager() {
        return placeholderManager;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public YamlConfig getConfigFile() {
        return config;
    }

    public YamlConfig getDrugsFile() {
        return drugs;
    }
}
