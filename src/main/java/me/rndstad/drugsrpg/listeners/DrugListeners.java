package me.rndstad.drugsrpg.listeners;

import me.rndstad.drugsrpg.Core;
import me.rndstad.drugsrpg.events.DrugConsumeEvent;
import me.rndstad.drugsrpg.managers.DrugManager;
import me.rndstad.drugsrpg.utils.StringUtils;
import me.rndstad.drugsrpg.utils.Utils;
import me.rndstad.drugsrpg.utils.YAML_API;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DrugListeners implements Listener {

    private YAML_API config = Core.getInstance().getConfigData();
    private Utils u = new Utils();

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            for (String drug : config.getConfig().getConfigurationSection("Drugs").getKeys(false)) {
                if (p.hasPermission("Drugs." + drug + ".permission")) {
                    if (p.getItemInHand().getType() == Material.valueOf(config.getConfig().getString("Drugs." + drug + ".recipe.product"))) {
                        if (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(StringUtils.format(config.getConfig().getString("Drugs." + drug + ".name")))) {
                            DrugConsumeEvent drugConsumeEvent = new DrugConsumeEvent(DrugManager.getInstance().getDrug(drug), p);
                            Bukkit.getPluginManager().callEvent(drugConsumeEvent);
                            if (!drugConsumeEvent.isCancelled()) {
                                e.setCancelled(true);
                                u.removeItem(p.getInventory(), Material.valueOf(config.getConfig().getString("Drugs." + drug + ".recipe.product")), 1);
                                p.sendMessage(StringUtils.format(config.getConfig().getString("Drugs." + drug + ".message")));
                                for (String effect : config.getConfig().getConfigurationSection("Drugs." + drug + ".effects").getKeys(false)) {
                                    p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect.toUpperCase()),
                                            config.getConfig().getInt("Drugs." + drug + ".effects." + effect + ".duration"),
                                            config.getConfig().getInt("Drugs." + drug + ".effects." + effect + ".amplifier")));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
