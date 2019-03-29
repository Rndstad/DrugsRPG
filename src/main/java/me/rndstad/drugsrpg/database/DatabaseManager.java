package me.rndstad.drugsrpg.database;

import me.rndstad.drugsrpg.DrugsPlugin;
import me.rndstad.drugsrpg.common.Module;
import me.rndstad.drugsrpg.common.tools.YamlConfig;
import me.rndstad.drugsrpg.database.enums.DatabaseQuery;
import me.rndstad.pool.CredentialPackageFactory;
import me.rndstad.pool.Pool;
import me.rndstad.pool.PoolDriver;
import me.rndstad.pool.properties.PropertyFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

        // MySQL or flatfile
        if (config.getConfig().getBoolean("preferences.use-mysql")) {
            // mysql
            // make connection
            use_mysql = true;
            pool.withMin(5).withMax(5).withMysqlUrl(config.getConfig().getString("preferences.host"), config.getConfig().getInt("preferences.port"), config.getConfig().getString("preferences.database"));
            pool.withProperty(PropertyFactory.connectionTimeout(30000));
            pool.build();

            // create table
            try (Connection connection = pool.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(DatabaseQuery.DRUGSRPG_DRUGS.toString());
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            // flatfile
            use_mysql = false;
        }
    }

    public boolean use_mysql() {
        return use_mysql;
    }

    public Pool getPool() {
        return pool;
    }

    /**
     * import me.rndstad.piratetrophies.Core;
     * import me.rndstad.piratetrophies.database.DatabaseQuery;
     * import me.rndstad.piratetrophies.objects.Pirate;
     * import me.rndstad.piratetrophies.objects.Trophy;
     * import me.rndstad.piratetrophies.utils.ItemUtils;
     * import me.rndstad.piratetrophies.utils.StringUtils;
     * import me.rndstad.pool.Pool;
     * import org.bukkit.Bukkit;
     * import org.bukkit.inventory.ItemStack;
     *
     * import java.sql.Connection;
     * import java.sql.PreparedStatement;
     * import java.sql.ResultSet;
     * import java.sql.SQLException;
     * import java.util.*;
     *
     * public class SQLManager {
     *
     *     private Pool pool;
     *
     *     public SQLManager(Pool pool) {
     *         this.pool = pool;
     *     }
     *
     *     public void createTable(String query) {
     *         try (Connection connection = pool.getConnection()) {
     *             PreparedStatement statement = connection.prepareStatement(query);
     *             statement.execute();
     *         } catch (SQLException e) {
     *             e.printStackTrace();
     *         }
     *     }
     *
     *     public boolean isPirate(UUID uuid) {
     *         try (Connection connection = pool.getConnection()) {
     *             PreparedStatement statement = connection.prepareStatement(DatabaseQuery.SELECT_PLAYER.toString());
     *             statement.setString(1, uuid.toString());
     *
     *             ResultSet rs = statement.executeQuery();
     *             while (rs.next()) {
     *                 if (rs != null) {
     *                     return true;
     *                 } else {
     *                     return false;
     *                 }
     *             }
     *
     *         } catch (SQLException e) {
     *             e.printStackTrace();
     *         }
     *         return false;
     *     }
     *
     *     public boolean isTrophy(String trophyName) {
     *         boolean trophy = false;
     *         try (Connection connection = pool.getConnection()) {
     *             PreparedStatement statement = connection.prepareStatement(DatabaseQuery.SELECT_TROPHY.toString());
     *             statement.setString(1, trophyName);
     *
     *             ResultSet rs = statement.executeQuery();
     *             trophy = rs.next();
     *
     *         } catch (SQLException e) {
     *             e.printStackTrace();
     *         }
     *         return trophy;
     *     }
     *
     *     public void updatePirate(DatabaseQuery query, Pirate pirate) {
     *         Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), () -> {
     *             try (Connection connection = pool.getConnection()) {
     *                 PreparedStatement statement = connection.prepareStatement(query.toString());
     *                 if (query == DatabaseQuery.UPDATE_PLAYER) {
     *
     *                     if (pirate.getTrophies().size() > 0) {
     *                         StringBuilder builder = new StringBuilder();
     *                         for (Map.Entry<Trophy, Date> entry : pirate.getTrophies().entrySet()) {
     *                             builder.append(entry.getKey().getName() + "/" + StringUtils.dateToString(entry.getValue()) + ", ");
     *                         }
     *                         statement.setString(1, builder.toString());
     *                     } else {
     *                         statement.setString(1, "{}");
     *                     }
     *
     *                     statement.setString(2, pirate.getUniqueID().toString());
     *                 } else {
     *                     statement.setString(1, pirate.getUniqueID().toString());
     *                     if (pirate.getTrophies().size() > 0) {
     *                         StringBuilder builder = new StringBuilder();
     *                         for (Map.Entry<Trophy, Date> entry : pirate.getTrophies().entrySet()) {
     *                             builder.append(entry.getKey().getName() + "/" + StringUtils.dateToString(entry.getValue()) + ", ");
     *                         }
     *                     } else {
     *                         statement.setString(2, "{}");
     *                     }
     *                 }
     *                 statement.executeUpdate();
     *                 statement.close();
     *             } catch (SQLException e) {
     *                 e.printStackTrace();
     *             }
     *         });
     *     }
     *
     *     public void updateTrophy(DatabaseQuery query, Trophy trophy) {
     *         try (Connection connection = pool.getConnection()) {
     *             PreparedStatement statement = connection.prepareStatement(query.toString());
     *             if (query == DatabaseQuery.UPDATE_TROPHY) {
     *                 statement.setString(1, trophy.getIcon().getItemMeta().getDisplayName());
     *                 statement.setString(2, StringUtils.loreToString(trophy.getIcon().getItemMeta().getLore()));
     *                 statement.setString(3, trophy.getSkinURL());
     *                 statement.setString(4, trophy.getName());
     *             } else {
     *                 statement.setString(1, trophy.getName());
     *                 statement.setString(2, trophy.getIcon().getItemMeta().getDisplayName());
     *                 statement.setString(3, StringUtils.loreToString(trophy.getIcon().getItemMeta().getLore()));
     *                 statement.setString(4, trophy.getSkinURL());
     *             }
     *
     *             statement.executeUpdate();
     *             statement.close();
     *         } catch (SQLException e) {
     *             e.printStackTrace();
     *         }
     *     }
     *
     *     public List<Trophy> getTrophies() {
     *         List<Trophy> trophies = new ArrayList<>();
     *             try (Connection connection = pool.getConnection()) {
     *                 PreparedStatement statement = connection.prepareStatement(DatabaseQuery.SELECT_TROPHIES.toString());
     *
     *                 ResultSet rs = statement.executeQuery();
     *
     *                 while (rs.next()) {
     *                     String or = rs.getString("trophy");
     *                     String displayName = rs.getString("displayName");
     *                     List<String> lore = StringUtils.loreToStringList(rs.getString("lore"));
     *                     String skinTexture = rs.getString("skinTexture");
     *                     ItemStack item = ItemUtils.getCustomSkull(skinTexture, displayName, lore);
     *                     trophies.add(new Trophy(or, item, skinTexture));
     *                 }
     *
     *             } catch (SQLException e) {
     *                 e.printStackTrace();
     *             }
     *         return trophies;
     *     }
     *
     *     public Map<Trophy, Date> getPirateTrophies(UUID uuid) {
     *         Map<Trophy, Date> trophies = null;
     *         try (Connection connection = pool.getConnection()) {
     *             PreparedStatement statement = connection.prepareStatement(DatabaseQuery.SELECT_PLAYER.toString());
     *             statement.setString(1, uuid.toString());
     *
     *             ResultSet rs = statement.executeQuery();
     *             if (rs.next()) {
     *                 String or = rs.getString("trophies");
     *                 trophies = StringUtils.getTrophyList(or);
     *             }
     *
     *         } catch (SQLException e) {
     *             e.printStackTrace();
     *         }
     *
     *         return trophies;
     *     }
     * }
     */
}
