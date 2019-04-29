package me.rndstad.drugsrpg.builder.menus;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.rndstad.drugsrpg.DrugsPlugin;
import me.rndstad.drugsrpg.builder.enums.DrugInfo;
import me.rndstad.drugsrpg.common.tools.ChatUtils;
import me.rndstad.drugsrpg.consume.Drug;
import me.rndstad.drugsrpg.database.enums.ConfigurationState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class BuilderInventory implements InventoryProvider {

    private static final DrugsPlugin drugsrpg = JavaPlugin.getPlugin(DrugsPlugin.class);

    public static SmartInventory INVENTORY = SmartInventory.builder()
            .id("builderInventory")
            .provider(new BuilderInventory())
            .size(3, 9)
            .title("Builder Menu")
            .manager(drugsrpg.getInventoryManager())
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15)));

        boolean text = false;
        if (drugsrpg.getBuilderManager().getBuildedDrug(player.getUniqueId()) != null) {
            if (drugsrpg.getBuilderManager().getBuildedDrug(player.getUniqueId()).getName() != null) {
                if (drugsrpg.getBuilderManager().getBuildedDrug(player.getUniqueId()).getItemStack().getItemMeta().getDisplayName() != null) {
                    if (drugsrpg.getBuilderManager().getBuildedDrug(player.getUniqueId()).getItemStack().getItemMeta().getLore() != null) {
                        text = true;
                    }
                }
            }
        }

        ItemStack name = new ItemStack(Material.SIGN);
        ItemMeta nameMeta = name.getItemMeta();
        nameMeta.setDisplayName(ChatUtils.format("&fSet text - " + (text ? ConfigurationState.DONE.toString() : ConfigurationState.TO_DO.toString())));

        List<String> nameLore = new ArrayList<>();
        nameLore.add(ChatUtils.format("&7- drugsId"));
        nameLore.add(ChatUtils.format("&7- displayName"));
        nameLore.add(ChatUtils.format("&7- lore"));
        nameMeta.setLore(nameLore);

        name.setItemMeta(nameMeta);

        contents.set(1, 1, ClickableItem.of(name, e -> {
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains(ConfigurationState.DONE.toString())) {
                e.setCancelled(true);
            } else {
                e.getWhoClicked().closeInventory();
                drugsrpg.getBuilderManager().getChatMap().put(e.getWhoClicked().getUniqueId(), DrugInfo.DRUGS_ID);
                // Go further in chat listener, other class.
                e.getWhoClicked().sendMessage(ChatUtils.format("&aPlease type the wished drugsId here in the chat."));

            }
        }));

        boolean crafting = false;
        if (drugsrpg.getBuilderManager().getBuildedDrug(player.getUniqueId()) != null) {
            if (drugsrpg.getBuilderManager().getBuildedDrug(player.getUniqueId()).getItemStack() != null) {
                if (drugsrpg.getBuilderManager().getBuildedDrug(player.getUniqueId()).getItemStack().getType() != null) {
                    if (drugsrpg.getBuilderManager().getBuildedDrug(player.getUniqueId()).getRecipe() != null) {
                        if (drugsrpg.getBuilderManager().getBuildedDrug(player.getUniqueId()).getRecipe().getIngredientMap() != null) {
                            crafting = true;
                        }
                    }
                }
            }
        }

        ItemStack practical = new ItemStack(Material.WORKBENCH);
        ItemMeta practicalMeta = practical.getItemMeta();
        practicalMeta.setDisplayName(ChatUtils.format("&fSet crafting - " + (crafting ? ConfigurationState.DONE.toString() : ConfigurationState.TO_DO.toString())));

        List<String> practicalLore = new ArrayList<>();
        practicalLore.add(ChatUtils.format("&7- product"));
        practicalLore.add(ChatUtils.format("&7- rows"));
        practicalLore.add(ChatUtils.format("&7- ingredients"));
        practicalMeta.setLore(practicalLore);

        practical.setItemMeta(practicalMeta);

        contents.set(1, 2, ClickableItem.of(practical, e -> {
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains(ConfigurationState.DONE.toString())) {
                e.setCancelled(true);
            } else {
                e.getWhoClicked().closeInventory();
                drugsrpg.getBuilderManager().getChatMap().put(e.getWhoClicked().getUniqueId(), DrugInfo.PRODUCT);
                // Go further in chat listener, other class.
                e.getWhoClicked().sendMessage(ChatUtils.format("&aPlease type the wished product here in the chat, so as example: \"SUGAR\"."));
            }
        }));

        boolean potion = false;
        if (drugsrpg.getBuilderManager().getBuildedDrug(player.getUniqueId()) != null) {
            if (drugsrpg.getBuilderManager().getBuildedDrug(player.getUniqueId()).getPotionEffects() != null) {
                potion = true;
            }
        }

        ItemStack effects = new ItemStack(Material.SPLASH_POTION);
        ItemMeta effectsMeta = effects.getItemMeta();
        effectsMeta.setDisplayName(ChatUtils.format("&fSet effects - " + (potion ? ConfigurationState.DONE.toString() : ConfigurationState.TO_DO.toString())));

        List<String> effectsLore = new ArrayList<>();
        effectsLore.add(ChatUtils.format("&7- potion effects"));
        effectsMeta.setLore(effectsLore);

        effects.setItemMeta(effectsMeta);

        contents.set(1, 3, ClickableItem.of(effects, e -> {
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains(ConfigurationState.DONE.toString())) {
                e.setCancelled(true);
            } else {
                e.getWhoClicked().closeInventory();
                drugsrpg.getBuilderManager().getChatMap().put(e.getWhoClicked().getUniqueId(), DrugInfo.POTION_EFFECTS);
                // Go further in chat listener, other class.
                e.getWhoClicked().sendMessage(ChatUtils.format("&aAlright. So what effects do you want? Example: \"BLINDNESS:200 (duration):2 (amplifier) please add a slash (/) between every effect"));
            }
        }));

        if (text && crafting && potion) {
            ItemStack agree = new ItemStack(Material.WOOL, 1, (short) 5);
            ItemMeta agreeMeta = agree.getItemMeta();
            agreeMeta.setDisplayName(ChatUtils.format("&aConfirm"));
            agree.setItemMeta(agreeMeta);

            contents.set(1, 5, ClickableItem.of(agree, e -> {
                Drug drug = drugsrpg.getBuilderManager().getBuildedDrug(e.getWhoClicked().getUniqueId());
                drugsrpg.getDrugsManager().getDrugs().add(drug);
                e.getWhoClicked().closeInventory();
                e.setCancelled(true);

            }));

            ItemStack decline = new ItemStack(Material.WOOL, 1, (short) 14);
            ItemMeta declineMeta = agree.getItemMeta();
            declineMeta.setDisplayName(ChatUtils.format("&cDecline"));
            decline.setItemMeta(declineMeta);

            contents.set(1, 6, ClickableItem.of(decline, e -> {
                drugsrpg.getBuilderManager().getBuilders().remove(e.getWhoClicked().getUniqueId());
                e.getWhoClicked().closeInventory();
                e.setCancelled(true);
            }));
        }

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
