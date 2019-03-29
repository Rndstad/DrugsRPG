package me.rndstad.drugsrpg.consume;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.rndstad.drugsrpg.DrugsPlugin;
import me.rndstad.drugsrpg.database.enums.DatabaseQuery;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Drug {

    private final DrugsPlugin drugsrpg;

    private String drug;
    private String message;
    private ItemStack i;
    private ShapedRecipe sr;

    public Drug(DrugsPlugin drugsrpg, String drug, String message, ItemStack i, ShapedRecipe sr) {
        this.drugsrpg = drugsrpg;
        this.drug = drug;
        this.message = message;
        this.i = i;
        this.sr = sr;
        Bukkit.getServer().addRecipe(sr);
    }

    public String getName() {
        return drug;
    }

    public void setName(String displayName) {
        this.drug = displayName;
    }

    public ItemStack getItemStack() {
        return i;
    }

    public void setItemStack(ItemStack item) {
        this.i = item;
    }

    public ShapedRecipe getRecipe() {
        return sr;
    }

    public List<PotionEffect> getPotionEffects() {
        List<PotionEffect> potions = new ArrayList<>();
        if (drugsrpg.getDatabaseManager().use_mysql()) {
            // TODO
            try (Connection connection = drugsrpg.getDatabaseManager().getPool().getConnection()) {
                PreparedStatement statement = connection.prepareStatement(DatabaseQuery.SELECT_DRUG.toString());

                ResultSet rs = statement.executeQuery();

                while (rs.next()) {
                    List<String> effects = Arrays.asList(rs.getString("effects").split("/"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            for (String p : drugsrpg.getDrugsFile().getConfig().getConfigurationSection("drugs." + drug + ".effects").getKeys(false)) {
                potions.add(new PotionEffect(PotionEffectType.getByName(p), drugsrpg.getDrugsFile().getConfig().getInt("drugs." + drug + ".effects." + p + ".duration"), drugsrpg.getDrugsFile().getConfig().getInt("drugs." + drug + ".effects." + p + ".amplifier")));
            }
        }
        return potions;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
