package me.rndstad.drugsrpg.consume.menus;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import me.rndstad.drugsrpg.DrugsPlugin;
import me.rndstad.drugsrpg.common.tools.ChatUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class DrugInventory implements InventoryProvider {

    private static final DrugsPlugin drugsrpg = JavaPlugin.getPlugin(DrugsPlugin.class);

    public static SmartInventory INVENTORY = SmartInventory.builder()
            .id("drugsInventory")
            .provider(new DrugInventory())
            .size(3, 9)
            .title("Drugs Menu")
            .manager(drugsrpg.getInventoryManager())
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();

        List<ItemStack> drugs = drugsrpg.getDrugsManager().toDrugIcons();
        ClickableItem[] items = new ClickableItem[drugs.size()];

        for (int i = 0; i < items.length; i++) {
            ItemStack drug = drugs.get(i);
            items[i] = ClickableItem.of(drug, e -> e.getWhoClicked().getInventory().addItem(drugsrpg.getDrugsManager().getDrug(drug.getItemMeta().getDisplayName()).getItemStack()));
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(7);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));

        ItemStack credit = new ItemStack(Material.BOOKSHELF);
        ItemMeta meta = credit.getItemMeta();
        meta.setDisplayName(ChatUtils.format("&6&lDrugsRPG"));
        meta.setLore(Arrays.asList(ChatUtils.format("&7Made by &9Randstad")));
        credit.setItemMeta(meta);

        contents.set(2, 4, ClickableItem.empty(credit));

        ItemStack previous = new ItemStack(Material.ARROW);
        ItemMeta pm = previous.getItemMeta();
        pm.setDisplayName(ChatUtils.format("&7Previous Page"));
        previous.setItemMeta(pm);

        ItemStack next = new ItemStack(Material.ARROW);
        ItemMeta pn = previous.getItemMeta();
        pn.setDisplayName(ChatUtils.format("&7Next Page"));
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
