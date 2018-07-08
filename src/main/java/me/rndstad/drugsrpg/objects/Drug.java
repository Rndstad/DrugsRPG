package me.rndstad.drugsrpg.objects;

import java.util.ArrayList;
import java.util.List;

import me.rndstad.drugsrpg.Core;
import me.rndstad.drugsrpg.managers.DrugManager;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Drug {

	private String drug;
	private ItemStack i;
	private ShapedRecipe sr;

	public Drug(String drug, ItemStack i, ShapedRecipe sr) {
		this.drug = drug;
		this.i = i;
		this.sr = sr;
		Bukkit.getServer().addRecipe(sr);
		DrugManager.getInstance().getDrugs().add(this);
	}

	public String getName() {
		return drug;
	}

	public ItemStack getItemStack() {
		return i;
	}

	public ShapedRecipe getRecipe() {
		return sr;
	}
	
	public List<PotionEffect> getPotionEffects() {
		List<PotionEffect> potions = new ArrayList<>();
		for (String p : Core.getInstance().getConfigData().getConfig().getConfigurationSection("Drugs." + drug + ".effects").getKeys(false)){
			potions.add(new PotionEffect(PotionEffectType.getByName(p), Core.getInstance().getConfigData().getConfig().getInt("Drugs." + drug + ".effects." + p + ".duration"), Core.getInstance().getConfigData().getConfig().getInt("Drugs." + drug + ".effects." + p + ".amplifier")));
		}
		return potions;
	}

}
