package me.rndstad.drugsrpg.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.rndstad.drugsrpg.managers.DrugManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class DrugPlaceholder extends PlaceholderExpansion {

    public String getIdentifier() {
        return "drug";
    }

    public String getAuthor() {
        return "Randstad";
    }

    public String getVersion() {
        return "1.0.0";
    }

    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return null;
        }

        /*
         %drug_in_hand%
         Returns the drug in your hand
          */
        if(identifier.equalsIgnoreCase("in_hand")) {
            if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                if (DrugManager.getInstance().getDrug(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName()) != null) {
                    return "You have " + DrugManager.getInstance().getDrug(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).getName() + " in your hand.";
                } else {
                    return "You don't have a drug in your hand.";
                }
            } else {
                return "You don't have a drug in your hand.";
            }
        }

        return null;
    }
}
