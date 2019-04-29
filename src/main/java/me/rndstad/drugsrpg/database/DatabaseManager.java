package me.rndstad.drugsrpg.database;

import me.rndstad.drugsrpg.DrugsPlugin;
import me.rndstad.drugsrpg.common.Module;
import me.rndstad.drugsrpg.common.tools.YamlConfig;
import me.rndstad.drugsrpg.consume.Drug;
import me.rndstad.drugsrpg.database.enums.DatabaseQuery;
import me.rndstad.pool.CredentialPackageFactory;
import me.rndstad.pool.Pool;
import me.rndstad.pool.PoolDriver;
import me.rndstad.pool.properties.PropertyFactory;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class DatabaseManager extends Module {

    private final DrugsPlugin drugsrpg;
    private final Pool pool;

    private final YamlConfig config;
    private final YamlConfig drugs;

    private final boolean use_mysql;

    public DatabaseManager(DrugsPlugin drugsrpg, YamlConfig config, YamlConfig drugs) {
        super("Database Manager");
        this.drugsrpg = drugsrpg;
        this.config = config;
        this.drugs = drugs;
        this.pool = new Pool(CredentialPackageFactory.get(config.getConfig().getString("preferences.username"), config.getConfig().getString("preferences.password")), PoolDriver.MYSQL);

        if (config.getConfig().getBoolean("preferences.use-mysql")) {
            use_mysql = true;
            pool.withMin(5).withMax(5).withMysqlUrl(config.getConfig().getString("preferences.host", "localhost"), config.getConfig().getInt("preferences.port", 3306), config.getConfig().getString("preferences.database"));
            pool.withProperty(PropertyFactory.connectionTimeout(30000));
            pool.build();

            try (Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(DatabaseQuery.DRUGSRPG_DRUGS.toString());
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            use_mysql = false;
        }
    }

    public boolean use_mysql() {
        return use_mysql;
    }

    public Pool getPool() {
        return pool;
    }

    public void insertDrug(DatabaseQuery query, Drug drug) {
        StringBuilder rawIngredients = new StringBuilder();
        int count = 0;
        for (Map.Entry<Character, ItemStack> entry : drug.getRecipe().getIngredientMap().entrySet()) {
            if (count == 0) {
                rawIngredients.append(entry.getKey().toString()).append(":").append(entry.getValue().getType().toString());
            } else {
                rawIngredients.append("/").append(entry.getKey().toString()).append(":").append(entry.getValue().getType().toString());
            }
            count++;
        }
        String ingredients = rawIngredients.toString();

        StringBuilder rawEffects = new StringBuilder();
        count = 0;
        for (PotionEffect potionEffect : drug.getPotionEffects()) {
            if (count == 0) {
                rawEffects.append(potionEffect.getType().getName()).append(":").append(potionEffect.getDuration()).append(":").append(potionEffect.getAmplifier());
            } else {
                rawEffects.append("/" + potionEffect.getType().getName()).append(":").append(potionEffect.getDuration()).append(":").append(potionEffect.getAmplifier());

            }
            count++;
        }
        String effects = rawEffects.toString();

        StringBuilder rawRows = new StringBuilder();
        String[] original = drug.getRecipe().getShape();
        rawRows.append(original[0]).append("/").append(original[1]).append("/").append(original[2]);
        String rows = rawRows.toString();

        try (Connection connection = pool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query.toString());
            if (query == DatabaseQuery.INSERT_DRUG) {
                statement.setString(1, drug.getName());
                statement.setString(2, drug.getItemStack().getItemMeta().getDisplayName());
                statement.setString(3, drug.getMessage());
                statement.setString(4, drug.getItemStack().getItemMeta().getLore().get(0));
                statement.setString(5, drug.getItemStack().getType().toString());
                statement.setString(6, rows);
                statement.setString(7, ingredients);
                statement.setString(8, effects);
            }

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDrug(DatabaseQuery query, Drug drug) {
        StringBuilder rawIngredients = new StringBuilder();
        int count = 0;
        for (Map.Entry<Character, ItemStack> entry : drug.getRecipe().getIngredientMap().entrySet()) {
            if (count == 0) {
                rawIngredients.append(entry.getKey().toString()).append(":").append(entry.getValue().getType().toString());
            } else {
                rawIngredients.append("/").append(entry.getKey().toString()).append(":").append(entry.getValue().getType().toString());
            }
            count++;
        }
        String ingredients = rawIngredients.toString();

        StringBuilder rawEffects = new StringBuilder();
        count = 0;
        for (PotionEffect potionEffect : drug.getPotionEffects()) {
            if (count == 0) {
                rawEffects.append(potionEffect.getType().getName()).append(":").append(potionEffect.getDuration()).append(":").append(potionEffect.getAmplifier());
            } else {
                rawEffects.append("/" + potionEffect.getType().getName()).append(":").append(potionEffect.getDuration()).append(":").append(potionEffect.getAmplifier());

            }
            count++;
        }
        String effects = rawEffects.toString();

        StringBuilder rawRows = new StringBuilder();
        String[] original = drug.getRecipe().getShape();
        rawRows.append(original[0]).append("/").append(original[1]).append("/").append(original[2]);
        String rows = rawRows.toString();

        Bukkit.getScheduler().runTaskAsynchronously(drugsrpg, () -> {
            try (Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(query.toString());
                if (query == DatabaseQuery.UPDATE_DRUG) {
                    statement.setString(1, drug.getName());
                    statement.setString(2, drug.getItemStack().getItemMeta().getDisplayName());
                    statement.setString(3, drug.getMessage());
                    statement.setString(4, drug.getItemStack().getItemMeta().getLore().get(0));
                    statement.setString(5, drug.getItemStack().getType().toString());
                    statement.setString(6, rows);
                    statement.setString(7, ingredients);
                    statement.setString(8, effects);
                }
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void deleteDrug(DatabaseQuery query, Drug drug) {
        try (Connection connection = pool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query.toString());
            if (query == DatabaseQuery.DELETE_DRUG) {
                statement.setString(1, drug.getName());
            }
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isDrug(Drug drug) {
        try (Connection connection = pool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DatabaseQuery.SELECT_DRUG.toString());
            statement.setString(1, drug.getName());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                if (rs != null) {
                    return true;
                } else {
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
