package me.rndstad.drugsrpg.builder.listeners;

import me.rndstad.drugsrpg.DrugsPlugin;
import me.rndstad.drugsrpg.builder.BuilderManager;
import me.rndstad.drugsrpg.builder.enums.DrugInfo;
import me.rndstad.drugsrpg.builder.menus.BuilderInventory;
import me.rndstad.drugsrpg.common.tools.ChatUtils;
import me.rndstad.drugsrpg.consume.Drug;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                    drug.setRecipe(recipe);

                    StringBuilder rows = new StringBuilder();
                    String[] original = drug.getRecipe().getShape();
                    rows.append(original[0]).append("/").append(original[1]).append("/").append(original[2]);
                    System.out.print("recipe: " + rows.toString());

                    bm.updateDrug(p.getUniqueId(), drug);
                    bm.updateInfo(p.getUniqueId(), DrugInfo.INGREDIENTS);
                    e.setCancelled(true);
                    p.sendMessage(ChatUtils.format("&aYou have set the rows to &e" + e.getMessage() + "&a."));
                    p.sendMessage(ChatUtils.format("&aExcellent. You have to enter the ingredients now. Example: @:IRON_BLOCK:0/#:INK_SACK:3"));

                }
            } else if (bm.getChatMap().get(p.getUniqueId()) == DrugInfo.INGREDIENTS) {
                Drug drug = bm.getBuildedDrug(p.getUniqueId());
                String[] ingredient = e.getMessage().split("/");
                ShapedRecipe recipe = drug.getRecipe();

                Bukkit.getServer().getScheduler().runTask(drugsrpg, () -> {
                    for (String i : ingredient) {
                        String[] split = i.split(":");
                        recipe.setIngredient(split[0].charAt(0), Material.getMaterial(split[1]));
                    }
                });

                drug.setRecipe(recipe);
                bm.updateDrug(p.getUniqueId(), drug);
                bm.getChatMap().remove(p.getUniqueId());
                e.setCancelled(true);
                p.sendMessage(ChatUtils.format("&aYou have succesfully configured the crafting part."));
                BuilderInventory.INVENTORY.open(p);
            } else if (bm.getChatMap().get(p.getUniqueId()) == DrugInfo.POTION_EFFECTS) {
                Drug drug = bm.getBuildedDrug(p.getUniqueId());
                String[] effects = e.getMessage().split("/");
                List<PotionEffect> potionEffectList = new ArrayList<>();

                for (String effect : effects) {
                    String[] split = effect.split(":");
                    System.out.printf("[0]: " + split[0]);
                    System.out.printf("Potion: " + PotionEffectType.getByName(split[0]).toString());
                    potionEffectList.add(new PotionEffect(PotionEffectType.getByName(split[0]), Integer.valueOf(split[1]), Integer.valueOf(split[2])));
                }

                drug.setPotionEffects(potionEffectList);
                bm.updateDrug(p.getUniqueId(), drug);
                bm.getChatMap().remove(p.getUniqueId());
                e.setCancelled(true);
                p.sendMessage(ChatUtils.format("&aYou have succesfully configured the effects part."));
                BuilderInventory.INVENTORY.open(p);
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
