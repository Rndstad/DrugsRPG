package me.rndstad.drugsrpg.api;

import me.rndstad.drugsrpg.DrugsPlugin;
import me.rndstad.drugsrpg.common.Module;
import me.rndstad.drugsrpg.consume.Drug;

import java.util.List;

public class DrugsAPI extends Module {

    private DrugsPlugin drugsrpg;

    public DrugsAPI(DrugsPlugin drugsrpg) {
        super("Drugs API");
        this.drugsrpg = drugsrpg;
    }

    public List<Drug> getDrugs() {
        return drugsrpg.getDrugsManager().getDrugs();
    }

    public Drug getDrug(String name) {
        return drugsrpg.getDrugsManager().getDrug(name);
    }

    public boolean isDrug(String name) {
        return drugsrpg.getDrugsManager().getDrug(name) != null;
    }
}
