package me.rndstad.drugsrpg.consume.listeners;

import me.rndstad.drugsrpg.DrugsPlugin;
import me.rndstad.drugsrpg.common.tools.ChatUtils;
import me.rndstad.drugsrpg.common.tools.InventoryUtils;
import me.rndstad.drugsrpg.consume.Drug;
import me.rndstad.drugsrpg.consume.events.DrugConsumeEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ConsumeListeners implements Listener {

    private final DrugsPlugin drugsrpg;

    public ConsumeListeners(DrugsPlugin drugsrpg) {
        this.drugsrpg = drugsrpg;
    }

    @EventHandler
    public void whenConsume(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            for (Drug drug : drugsrpg.getDrugsManager().getDrugs()) {
                if (player.getInventory().getItemInMainHand().getType() == drug.getItemStack().getType()) {
                    if (player.getInventory().getItemInMainHand().getItemMeta().equals(drug.getItemStack().getItemMeta())) {
                        if (player.hasPermission("drugsrpg." + ChatColor.stripColor(drug.getName()))) {
                            DrugConsumeEvent consumeEvent = new DrugConsumeEvent(drug, player);
                            Bukkit.getPluginManager().callEvent(consumeEvent);

                            if (!consumeEvent.isCancelled()) {
                                e.setCancelled(true);
                                InventoryUtils.removeItem(player.getInventory(), drug.getItemStack().getType(), 1);
                                player.sendMessage(ChatUtils.format(drug.getMessage()));
                                player.addPotionEffects(drug.getPotionEffects());
                            }
                        }
                    }
                }
            }
        }
    }
}