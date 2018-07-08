package me.rndstad.drugsrpg.api;

import java.util.ArrayList;
import java.util.List;

import me.rndstad.drugsrpg.objects.Drug;
import me.rndstad.drugsrpg.managers.DrugManager;

public class DrugsAPI {
	
	public List<String> getDrugList() {
		List<String> drugs = new ArrayList<>();
		for (Drug drug : DrugManager.getInstance().getDrugs()) {
			drugs.add(drug.getName());
		}
		return drugs;
	}
	
	public Drug getDrug(String drugName) {
		for(Drug drug : DrugManager.getInstance().getDrugs()) {
			if (drugName.equalsIgnoreCase(drug.getName())) {
				return drug;
			}
		}
		return null;
	}
	
	public boolean isDrug(String drugName) {
		boolean bl = false;
		for(Drug drug : DrugManager.getInstance().getDrugs()) {
			if (drugName.equalsIgnoreCase(drug.getName())) {
				bl = true;
				break;
			} else {
				bl = false;
			}
		}
		return bl;
	}

}
