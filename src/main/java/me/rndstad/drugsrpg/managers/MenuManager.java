package me.rndstad.drugsrpg.managers;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.GuiLocation;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.rndstad.drugsrpg.Core;
import me.rndstad.drugsrpg.objects.Drug;
import me.rndstad.drugsrpg.utils.Glow;
import me.rndstad.drugsrpg.utils.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuManager {

    private static MenuManager instance = new MenuManager();

    public static MenuManager getInstance() {
        return instance;
    }

    private List<ItemStack> getMenuDrugList() {
        List<ItemStack> drugs = new ArrayList<>();

        for (Drug drug : DrugManager.getInstance().getDrugs()) {
            ItemStack drugItem = new ItemStack(drug.getItemStack());
            ItemMeta meta = drugItem.getItemMeta();
            meta.setDisplayName(StringUtils.format("&f&l" + drug.getName()));

            List<String> lores = new ArrayList<>();
            lores.add(StringUtils.format("&dRecipe:"));
            lores.addAll(Arrays.asList(drug.getRecipe().getShape()));
            lores.add(" ");
            lores.add(StringUtils.format("&dEffects:"));
            for (PotionEffect potion : drug.getPotionEffects()) {
                lores.add(StringUtils.format("&7- " + potion.getType().getName()));
            }
            meta.setLore(lores);

            drugItem.setItemMeta(meta);
            drugs.add(drugItem);
        }
        return drugs;
    }

    public Gui getMenu() {
        Gui menu = new Gui(Core.getInstance(), 2, "Drugs Menu");
        OutlinePane pane = new OutlinePane(new GuiLocation(0, 0), 9, 4);

        ItemStack credit = new ItemStack(Material.BOOKSHELF);
        ItemMeta meta = credit.getItemMeta();
        meta.setDisplayName(StringUtils.format("&6&lDrugsRPG"));
        meta.setLore(Arrays.asList(StringUtils.format("&7Made by &9Randstad")));
        credit.setItemMeta(meta);

        GuiItem menuItem = new GuiItem(credit, event -> event.getWhoClicked().closeInventory());
        pane.addItem(menuItem);

        for (ItemStack item : getMenuDrugList()) {
            GuiItem guiItem = new GuiItem(item, event -> {
                event.setCancelled(true);
                Drug drug = DrugManager.getInstance().getDrug(event.getCurrentItem().getItemMeta().getDisplayName());
                Player p = (Player) event.getWhoClicked();
                p.getInventory().addItem(drug.getItemStack());
            });
            pane.addItem(guiItem);
        }

        menu.addPane(pane);
        return menu;
    }
}
