package me.rndstad.drugsrpg.managers;

import me.rndstad.drugsrpg.Core;
import me.rndstad.drugsrpg.objects.Drug;
import me.rndstad.drugsrpg.utils.StringUtils;
import me.rndstad.drugsrpg.utils.YAML_API;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DrugManager {
	
	private List<Drug> drugs = new ArrayList<>();
    private YAML_API config = Core.getInstance().getConfigData();

    private static DrugManager instance = new DrugManager();

    public static DrugManager getInstance() {
        return instance;
    }

    public List<Drug> getDrugs() {
        return drugs;
    }

    public Drug getDrug(String name) {
        for (Drug drug : getDrugs()) {
            if (ChatColor.stripColor(drug.getName()).equalsIgnoreCase(ChatColor.stripColor(name))) {
                return drug;
            }
        }
        return null;
    }

    public void init() {
		for (String drug : config.getConfig().getConfigurationSection("Drugs").getKeys(false)) {
			ItemStack i = new ItemStack(Material.valueOf(config.getConfig().getString("Drugs." + drug + ".recipe.product")));
			ItemMeta meta = i.getItemMeta();
			meta.setDisplayName(StringUtils.format(config.getConfig().getString("Drugs." + drug + ".name")));
			meta.setLore(Arrays.asList(StringUtils.format(config.getConfig().getString("Drugs." + drug + ".lore"))));
			i.setItemMeta(meta);

			ShapedRecipe sr = new ShapedRecipe(i);

			sr.shape(config.getConfig().getString("Drugs." + drug + ".recipe.rows.1"),
					config.getConfig().getString("Drugs." + drug + ".recipe.rows.2"),
					config.getConfig().getString("Drugs." + drug + ".recipe.rows.3"));

			sr.setIngredient('@', Material.valueOf(config.getConfig().getString("Drugs." + drug + ".recipe.item.1")));
			sr.setIngredient('#', Material.valueOf(config.getConfig().getString("Drugs." + drug + ".recipe.item.2")));

			new Drug(drug, i, sr);
		}
	}
}
