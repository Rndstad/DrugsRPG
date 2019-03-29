package me.rndstad.drugsrpg.builder.listeners;

import me.rndstad.drugsrpg.DrugsPlugin;
import me.rndstad.drugsrpg.builder.BuilderManager;
import me.rndstad.drugsrpg.builder.enums.DrugInfo;
import me.rndstad.drugsrpg.builder.menus.BuilderInventory;
import me.rndstad.drugsrpg.common.tools.ChatUtils;
import me.rndstad.drugsrpg.consume.Drug;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class BuilderListeners implements Listener {

    private DrugsPlugin drugsrpg;

    public BuilderListeners(DrugsPlugin drugsrpg) {
        this.drugsrpg = drugsrpg;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        BuilderManager bm = drugsrpg.getBuilderManager();
        if (bm.getChatMap().get(p.getUniqueId()) != null) {
            if (bm.getChatMap().get(p.getUniqueId()) == DrugInfo.DRUGS_ID) {
                bm.updateDrug(p.getUniqueId(), new Drug(drugsrpg, e.getMessage(), null, null, null));
                bm.updateInfo(p.getUniqueId(), DrugInfo.DISPLAY_NAME);
                e.setCancelled(true);
                p.sendMessage(ChatUtils.format("&aYou have set the drugsId &e" + e.getMessage() + "&a."));
                p.sendMessage(ChatUtils.format("&aPlease enter the display name."));
            } else if (bm.getChatMap().get(p.getUniqueId()) == DrugInfo.DISPLAY_NAME) {
                Drug drug = bm.getBuildedDrug(p.getUniqueId());

                ItemStack standard = new ItemStack(Material.CHEST);
                ItemMeta meta = standard.getItemMeta();
                meta.setDisplayName(ChatUtils.format(e.getMessage()));
                standard.setItemMeta(meta);

                drug.setItemStack(standard);

                bm.updateDrug(p.getUniqueId(), drug);
                bm.updateInfo(p.getUniqueId(), DrugInfo.MESSAGE);
                e.setCancelled(true);
                p.sendMessage(ChatUtils.format("&aAwesome! You have set the display name to &e" + e.getMessage() + "&a."));
                p.sendMessage(ChatUtils.format("&aPlease enter now the message."));
            } else if (bm.getChatMap().get(p.getUniqueId()) == DrugInfo.MESSAGE) {
                Drug drug = bm.getBuildedDrug(p.getUniqueId());
                drug.setMessage(ChatUtils.format(e.getMessage()));

                bm.updateDrug(p.getUniqueId(), drug);
                bm.updateInfo(p.getUniqueId(), DrugInfo.LORE);
                e.setCancelled(true);
                p.sendMessage(ChatUtils.format("&aYou have set the message to &e" + e.getMessage() + "&a."));
                p.sendMessage(ChatUtils.format("&aAt last but not least enter the lore."));

            } else if (bm.getChatMap().get(p.getUniqueId()) == DrugInfo.LORE) {
                Drug drug = bm.getBuildedDrug(p.getUniqueId());

                ItemMeta meta = drug.getItemStack().getItemMeta().clone();
                meta.setLore(Arrays.asList(ChatUtils.format(e.getMessage())));
                drug.getItemStack().setItemMeta(meta);

                bm.updateDrug(p.getUniqueId(), drug);
                bm.getChatMap().remove(p.getUniqueId());
                e.setCancelled(true);
                p.sendMessage(ChatUtils.format("&aYou have finished the text part."));
                p.sendMessage(ChatUtils.format("&aYou have set the lore to &e" + e.getMessage() + "&a."));
                BuilderInventory.INVENTORY.open(p);
            } else if (bm.getChatMap().get(p.getUniqueId()) == DrugInfo.PRODUCT) {
                Drug drug = bm.getBuildedDrug(p.getUniqueId());

                ItemStack drugItem = drug.getItemStack();
                drugItem.setType(Material.valueOf(e.getMessage()));

                drug.setItemStack(drugItem);

                bm.updateDrug(p.getUniqueId(), drug);
                bm.updateInfo(p.getUniqueId(), DrugInfo.ROWS);
                e.setCancelled(true);
                p.sendMessage(ChatUtils.format("&aYou have set the product to &e" + drugItem.getType().toString() + "&a."));
                p.sendMessage(ChatUtils.format("&aYou have to enter the rows now. As example: #@#/###/#@#"));
            } else if (bm.getChatMap().get(p.getUniqueId()) == DrugInfo.ROWS) {
                Drug drug = bm.getBuildedDrug(p.getUniqueId());
                String[] shape = e.getMessage().split("/");

                if (shape.length != 3) {
                    e.setCancelled(true);
                    p.sendMessage(ChatUtils.format("&cShape isn't right. The shape requires three rows."));
                    p.sendMessage(ChatUtils.format("&aPlease write the rows again."));
                    bm.updateInfo(p.getUniqueId(), DrugInfo.ROWS);
                } else {
                    ShapedRecipe recipe = new ShapedRecipe(drug.getItemStack());
                    recipe.shape(shape[0], shape[1], shape[2]);

                    bm.updateDrug(p.getUniqueId(), drug);
                    bm.updateInfo(p.getUniqueId(), DrugInfo.INGREDIENTS);
                    e.setCancelled(true);
                    p.sendMessage(ChatUtils.format("&aYou have set the rows to &e" + e.getMessage() + "&a."));
                    p.sendMessage(ChatUtils.format("&aExcellent. You have to enter the ingredients now."));

                }
            } else if (bm.getChatMap().get(p.getUniqueId()) == DrugInfo.INGREDIENTS) {
                
            }
        }
    }

    @EventHandler
    public void onForceQuit(PlayerQuitEvent e) {
        BuilderManager bm = drugsrpg.getBuilderManager();
        Player p = e.getPlayer();
        bm.getChatMap().remove(p.getUniqueId());
        bm.getBuilders().remove(p.getUniqueId());
    }
}
