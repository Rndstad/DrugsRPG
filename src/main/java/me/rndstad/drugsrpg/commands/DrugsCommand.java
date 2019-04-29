package me.rndstad.drugsrpg.commands;

import me.rndstad.drugsrpg.DrugsPlugin;
import me.rndstad.drugsrpg.builder.menus.BuilderInventory;
import me.rndstad.drugsrpg.common.tools.ChatUtils;
import me.rndstad.drugsrpg.consume.Drug;
import me.rndstad.drugsrpg.consume.menus.DrugInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DrugsCommand implements CommandExecutor {

    private DrugsPlugin drugsrpg;

    public DrugsCommand(DrugsPlugin drugsrpg) {
        this.drugsrpg = drugsrpg;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("menu")) {
                    if (player.hasPermission("drugsrpg." + args[0]) || player.hasPermission("drugsrpg.*")) {
                        DrugInventory.INVENTORY.open(player);
                    }
                }
                if (args[0].equalsIgnoreCase("create")) {
                    if (player.hasPermission("drugsrpg." + args[0]) || player.hasPermission("drugsrpg.*")) {
                        BuilderInventory.INVENTORY.open(player);
                    }
                }
            }
        }
        return false;
    }
}
