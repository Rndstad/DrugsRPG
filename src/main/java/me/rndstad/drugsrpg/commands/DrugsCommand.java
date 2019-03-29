package me.rndstad.drugsrpg.commands;

import me.rndstad.drugsrpg.builder.menus.BuilderInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DrugsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args[0].equalsIgnoreCase("create")) {
                BuilderInventory.INVENTORY.open(player);
                // <name> <displayName> <message> <lore>
                // please type the recipe of it for the crafting table of the rows with a slash (/) between every row.
                // @@@/@#@/@@@
                // what are the ingredients of @, #
                // @:IRON_BLOCK:0/#:INK_SACK:3
                // Alright. So what effects do you want? Example: "BLINDNESS:200 (duration):2 (amplifier) please add a slash (/) between every effect.
                // BLINDNESS:200:2/SLOW:200:2
                // Great, do you confirm this drug? YES or NO?
                // YES
                // Awesome! The drug "Cocaine" is added to the plugin!

            }
            if (args[0].equalsIgnoreCase("delete")) {

            }
            if (args[0].equalsIgnoreCase("menu")) {

            }
        }
        return false;
    }
}
