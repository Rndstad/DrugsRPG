package me.rndstad.drugsrpg.builder;

import me.rndstad.drugsrpg.DrugsPlugin;
import me.rndstad.drugsrpg.builder.enums.DrugInfo;
import me.rndstad.drugsrpg.builder.listeners.BuilderListeners;
import me.rndstad.drugsrpg.common.Module;
import me.rndstad.drugsrpg.consume.Drug;

import java.util.*;

public class BuilderManager extends Module {

    private DrugsPlugin drugsrpg;

    private Map<UUID, Drug> builders = new HashMap<>();
    private Map<UUID, DrugInfo> chat = new HashMap<>();

    public BuilderManager(DrugsPlugin drugsrpg) {
        super("Builder Manager");
        this.drugsrpg = drugsrpg;
        drugsrpg.getServer().getPluginManager().registerEvents(new BuilderListeners(drugsrpg), drugsrpg);
    }

    public Drug getBuildedDrug(UUID uuid) {
        for (Map.Entry<UUID, Drug> entry : builders.entrySet()) {
            if (entry.getKey() == uuid) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void updateDrug(UUID uuid, Drug drug) {
        builders.put(uuid, drug);
    }

    public Map<UUID, Drug> getBuilders() {
        return builders;
    }

    public void updateInfo(UUID uuid, DrugInfo info) {
        chat.put(uuid, info);
    }

    public Map<UUID, DrugInfo> getChatMap() {
        return chat;
    }
}
