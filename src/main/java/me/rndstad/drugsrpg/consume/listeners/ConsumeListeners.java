package me.rndstad.drugsrpg.consume.listeners;

import me.rndstad.drugsrpg.DrugsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ConsumeListeners implements Listener {

    private final DrugsPlugin drugsrpg;

    public ConsumeListeners(DrugsPlugin drugsrpg) {
        this.drugsrpg = drugsrpg;
    }

    @EventHandler
    public void whenConsume(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (drugsrpg.getDatabaseManager().use_mysql()) {

        } else {

        }
        // TODO Look if plugin is using MySQL or Flatfile
    }

    /**
     *   @EventHandler
     *     public void onClick(PlayerInteractEvent e) {
     *         Player p = e.getPlayer();
     *         if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
     *             for (String drug : config.getConfig().getConfigurationSection("DrugsCommand").getKeys(false)) {
     *                 if (p.hasPermission("DrugsCommand." + drug + ".permission")) {
     *                     if (p.getItemInHand().getType() == Material.valueOf(config.getConfig().getString("DrugsCommand." + drug + ".recipe.product"))) {
     *                         if (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(StringUtils.format(config.getConfig().getString("DrugsCommand." + drug + ".name")))) {
     *                             DrugConsumeEvent drugConsumeEvent = new DrugConsumeEvent(DrugManager.getInstance().getDrug(drug), p);
     *                             Bukkit.getPluginManager().callEvent(drugConsumeEvent);
     *                             if (!drugConsumeEvent.isCancelled()) {
     *                                 e.setCancelled(true);
     *                                 u.removeItem(p.getInventory(), Material.valueOf(config.getConfig().getString("DrugsCommand." + drug + ".recipe.product")), 1);
     *                                 p.sendMessage(StringUtils.format(config.getConfig().getString("DrugsCommand." + drug + ".message")));
     *                                 for (String effect : config.getConfig().getConfigurationSection("DrugsCommand." + drug + ".effects").getKeys(false)) {
     *                                     p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect.toUpperCase()),
     *                                             config.getConfig().getInt("DrugsCommand." + drug + ".effects." + effect + ".duration"),
     *                                             config.getConfig().getInt("DrugsCommand." + drug + ".effects." + effect + ".amplifier")));
     *                                 }
     *                             }
     *                         }
     *                     }
     *                 }
     *             }
     *         }
     *     }
     */
}
