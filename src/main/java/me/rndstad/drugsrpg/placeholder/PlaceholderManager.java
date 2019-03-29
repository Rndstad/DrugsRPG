package me.rndstad.drugsrpg.placeholder;

import me.rndstad.drugsrpg.DrugsPlugin;
import me.rndstad.drugsrpg.common.Module;
import me.rndstad.drugsrpg.placeholder.variables.ConsumePlaceholder;
import org.bukkit.Bukkit;

public class PlaceholderManager extends Module {

    private final DrugsPlugin drugsrpg;

    public PlaceholderManager(DrugsPlugin drugsrpg) {
        super("Placeholder Manager");
        this.drugsrpg = drugsrpg;

        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            System.out.println("[DrugsRPG] Hooking in to PlaceholderAPI...");

            ConsumePlaceholder consume = new ConsumePlaceholder(drugsrpg);
            consume.register();

            System.out.println("[DrugsRPG] Placeholder is registered!");
        }
    }
}
