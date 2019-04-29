package me.rndstad.drugsrpg.consume;

import me.rndstad.drugsrpg.DrugsPlugin;
import me.rndstad.drugsrpg.common.Module;
import me.rndstad.drugsrpg.common.tools.ChatUtils;
import me.rndstad.drugsrpg.consume.listeners.ConsumeListeners;
import me.rndstad.drugsrpg.database.enums.DatabaseQuery;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DrugsManager extends Module {

    private final DrugsPlugin drugsrpg;
    private List<Drug> drugs = new ArrayList<>();

    public DrugsManager(DrugsPlugin drugsrpg) {
        super("Drugs Manager");
        this.drugsrpg = drugsrpg;

        if (drugsrpg.getDatabaseManager().use_mysql()) {
            drugs = loadDrugs();
        } else {
            for (String drugName : drugsrpg.getDrugsFile().getConfig().getConfigurationSection("drugs").getKeys(false)) {
                ItemStack i = new ItemStack(Material.valueOf(drugsrpg.getDrugsFile().getConfig().getString("drugs." + drugName + ".recipe.product")));
                ItemMeta meta = i.getItemMeta();
                meta.setDisplayName(ChatUtils.format(drugsrpg.getDrugsFile().getConfig().getString("drugs." + drugName + ".name")));
                meta.setLore(Arrays.asList(ChatUtils.format(drugsrpg.getDrugsFile().getConfig().getString("drugs." + drugName + ".lore"))));
                i.setItemMeta(meta);

                ShapedRecipe sr = new ShapedRecipe(i);

                sr.shape(drugsrpg.getDrugsFile().getConfig().getString("drugs." + drugName + ".recipe.rows.1"),
                        drugsrpg.getDrugsFile().getConfig().getString("drugs." + drugName + ".recipe.rows.2"),
                        drugsrpg.getDrugsFile().getConfig().getString("drugs." + drugName + ".recipe.rows.3"));

                for (String c : drugsrpg.getDrugsFile().getConfig().getStringList("drugs." + drugName + ".recipe.ingredients")) {
                    String[] chr = c.split(":");
                    char key = chr[0].charAt(0);
                    Material mat = Material.valueOf(chr[1]);

                    if (chr.length == 3) {
                        byte sub_id = Byte.valueOf(chr[2]);
                        MaterialData data = new MaterialData(mat, sub_id);
                        sr.setIngredient(key, data);
                    } else if (chr.length == 2) {
                        sr.setIngredient(key, mat);
                    }
                }

                String message = ChatUtils.format(drugsrpg.getDrugsFile().getConfig().getString("drugs." + drugName + ".message"));

                Drug drug = new Drug(drugsrpg, drugName, message, i, sr, true);
                drugs.add(drug);
            }
        }

        drugsrpg.getServer().getPluginManager().registerEvents(new ConsumeListeners(drugsrpg), drugsrpg);
    }

    public List<Drug> loadDrugs() {
        List<Drug> drugs = new ArrayList<>();
        try (Connection connection = drugsrpg.getDatabaseManager().getPool().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DatabaseQuery.SELECT_DRUGS.toString());

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String drug = rs.getString("drug");
                String displayName = rs.getString("displayName");
                String message = rs.getString("message");
                String lore = rs.getString("lore");
                String product = rs.getString("product");

                List<String> rows = Arrays.asList(rs.getString("rows").split("/"));
                List<String> ingredients = Arrays.asList(rs.getString("ingredients").split("/"));

                ItemStack item = new ItemStack(Material.valueOf(product));
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatUtils.format(displayName));
                meta.setLore(Arrays.asList(ChatUtils.format(lore)));
                item.setItemMeta(meta);

                ShapedRecipe recipe = new ShapedRecipe(item);
                recipe.shape(rows.get(0), rows.get(1), rows.get(2));

                for (String ingredient : ingredients) {
                    String[] chr = ingredient.split(":");
                    char key = chr[0].charAt(0);
                    Material mat = Material.valueOf(chr[1]);

                    if (chr.length == 3) {
                        byte sub_id = Byte.valueOf(chr[2]);
                        MaterialData data = new MaterialData(mat, sub_id);
                        recipe.setIngredient(key, data);
                    } else if (chr.length == 2) {
                        recipe.setIngredient(key, mat);
                    }
                }

                drugs.add(new Drug(drugsrpg, drug, message, item, recipe, true));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drugs;
    }

    public void saveDrugs() {
        if (drugsrpg.getDatabaseManager().use_mysql()) {
            for (Drug drug : drugs) {
                if (drugsrpg.getDatabaseManager().isDrug(drug)) {
                    drugsrpg.getDatabaseManager().updateDrug(DatabaseQuery.UPDATE_DRUG, drug);
                    System.out.print("[DrugsRPG] Successfully updated drug to the MySQL database.");

                } else {
                    drugsrpg.getDatabaseManager().insertDrug(DatabaseQuery.INSERT_DRUG, drug);
                    System.out.print("[DrugsRPG] Successfully inserted drug to the MySQL database.");
                }
            }
        } else {
            drugsrpg.getDrugsFile().getConfig().set("drugs", null);

            for (Drug drug : drugs) {

                drugsrpg.getDrugsFile().getConfig().createSection("drugs");
                ConfigurationSection section = drugsrpg.getDrugsFile().getConfig().createSection("drugs." + drug.getName());

                section.set("name", drug.getName());
                section.set("message", drug.getMessage());
                section.set("lore", drug.getItemStack().getItemMeta().getLore().get(0));
                section.set("recipe.product", drug.getItemStack().getType().toString());
                section.set("recipe.rows.1", drug.getRecipe().getShape()[0]);
                section.set("recipe.rows.2", drug.getRecipe().getShape()[1]);
                section.set("recipe.rows.3", drug.getRecipe().getShape()[2]);

                List<String> ingredients = new ArrayList<>();
                for (Map.Entry<Character, ItemStack> entry : drug.getRecipe().getIngredientMap().entrySet()) {
                    ingredients.add(entry.getKey() + ":" + entry.getValue().getType() + ":" + entry.getValue().getType().getId());
                }
                section.set("ingredients", ingredients);

                for (PotionEffect effect : drug.getPotionEffects()) {
                    section.set("effects." + effect.getType().getName() + ".duration", effect.getDuration());
                    section.set("effects." + effect.getType().getName() + ".amplifier", effect.getAmplifier());
                }
            }
            drugsrpg.getDrugsFile().saveConfig();
        }
    }

    public List<ItemStack> toDrugIcons() {
        List<ItemStack> drugsConverted = new ArrayList<>();
        for (Drug drug : drugs) {
            ItemStack drugItem = new ItemStack(drug.getItemStack());
            ItemMeta meta = drugItem.getItemMeta();

            List<String> lores = new ArrayList<>();
            String[] shape = drug.getRecipe().getShape();
            lores.add(ChatUtils.format("&dRecipe: &7" + shape[0] + ", " + shape[1] + ", " + shape[2]));
            lores.add(ChatUtils.format("&dEffects:"));
            for (PotionEffect potion : drug.getPotionEffects()) {
                lores.add(ChatUtils.format("&7- " + potion.getType().getName()));
            }
            lores.add(" ");
            lores.add(ChatUtils.format("&aLeft-click&7 to get the drug."));
            lores.add(ChatUtils.format("&cRight-click&7 to remove drug."));
            meta.setLore(lores);

            drugItem.setItemMeta(meta);
            drugsConverted.add(drugItem);
        }

        return drugsConverted;
    }

    public List<Drug> getDrugs() {
        return drugs;
    }

    public Drug getDrug(String name) {
        for (Drug drug : drugs) {
            if (drug.getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase(name)) {
                return drug;
            }
        }
        return null;
    }
}
