package me.rndstad.drugsrpg.menus;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import me.rndstad.drugsrpg.Core;
import me.rndstad.drugsrpg.managers.DrugManager;
import me.rndstad.drugsrpg.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class DrugInventory implements InventoryProvider {

    public static SmartInventory INVENTORY = SmartInventory.builder()
            .id("drugsInventory")
            .provider(new DrugInventory())
            .size(3, 9)
            .title("Drugs Menu")
            .manager(Core.getInstance().getInvManager())
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();

        List<ItemStack> drugs = DrugManager.getInstance().toDrugIcons();
        ClickableItem[] items = new ClickableItem[drugs.size()];

        for (int i = 0; i < items.length; i++) {
            ItemStack drug = drugs.get(i);
            items[i] = ClickableItem.of(drug, e -> e.getWhoClicked().getInventory().addItem(DrugManager.getInstance().getDrug(drug.getItemMeta().getDisplayName()).getItemStack()));
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(7);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));

        ItemStack credit = new ItemStack(Material.BOOKSHELF);
        ItemMeta meta = credit.getItemMeta();
        meta.setDisplayName(StringUtils.format("&6&lDrugsRPG"));
        meta.setLore(Arrays.asList(StringUtils.format("&7Made by &9Randstad")));
        credit.setItemMeta(meta);

        contents.set(2, 4, ClickableItem.empty(credit));

        ItemStack previous = new ItemStack(Material.ARROW);
        ItemMeta pm = previous.getItemMeta();
        pm.setDisplayName(StringUtils.format("&7Previous Page"));
        previous.setItemMeta(pm);

        ItemStack next = new ItemStack(Material.ARROW);
        ItemMeta pn = previous.getItemMeta();
        pn.setDisplayName(StringUtils.format("&7Next Page"));
        next.setItemMeta(pn);

        contents.set(2, 3, ClickableItem.of(previous,
                e -> INVENTORY.open(player, pagination.previous().getPage())));

        contents.set(2, 5, ClickableItem.of(next,
                e -> INVENTORY.open(player, pagination.next().getPage())));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
