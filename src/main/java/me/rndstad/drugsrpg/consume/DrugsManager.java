package me.rndstad.drugsrpg.consume;

import me.rndstad.drugsrpg.DrugsPlugin;
import me.rndstad.drugsrpg.common.Module;
import me.rndstad.drugsrpg.common.tools.ChatUtils;
import me.rndstad.drugsrpg.consume.listeners.ConsumeListeners;
import me.rndstad.drugsrpg.database.enums.DatabaseQuery;
import org.bukkit.Material;
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

public class DrugsManager extends Module {

    private final DrugsPlugin drugsrpg;
    private List<Drug> drugs = new ArrayList<>();

    public DrugsManager(DrugsPlugin drugsrpg) {
        super("Drugs Manager");
        this.drugsrpg = drugsrpg;

        // load all drugs
        if (drugsrpg.getDatabaseManager().use_mysql()) {
            // TODO
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

                Drug drug = new Drug(drugsrpg, drugName, message, i, sr);
                drugs.add(drug);
            }
        }

        drugsrpg.getServer().getPluginManager().registerEvents(new ConsumeListeners(drugsrpg), drugsrpg);
    }

    public List<Drug> loadDrugs() {
        List<Drug> drugs = new ArrayList<>();
        try (Connection connection = drugsrpg.getDatabaseManager().getPool().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DatabaseQuery.SELECT_DRUG.toString());

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

                drugs.add(new Drug(drugsrpg, drug, displayName, item, recipe));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drugs;
    }

    public List<ItemStack> toDrugIcons() {
        List<ItemStack> drugsConverted = new ArrayList<>();
        for (Drug drug : drugs) {
            ItemStack drugItem = new ItemStack(drug.getItemStack());
            ItemMeta meta = drugItem.getItemMeta();
            meta.setDisplayName(ChatUtils.format("&f&l" + drug.getName()));

            List<String> lores = new ArrayList<>();
            lores.add(ChatUtils.format("&dRecipe:"));
            lores.addAll(Arrays.asList(drug.getRecipe().getShape()));
            lores.add(" ");
            lores.add(ChatUtils.format("&dEffects:"));
            for (PotionEffect potion : drug.getPotionEffects()) {
                lores.add(ChatUtils.format("&7- " + potion.getType().getName()));
            }
            meta.setLore(lores);

            drugItem.setItemMeta(meta);
            drugsConverted.add(drugItem);
        }

        return drugsConverted;
    }

    public Drug getDrug(String name) {
        for (Drug drug : drugs) {
            if (drug.getName().equalsIgnoreCase(name)) {
                return drug;
            }
        }
        return null;
    }
}
