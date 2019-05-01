package me.rndstad.drugsrpg.commands;

import me.rndstad.drugsrpg.DrugsPlugin;
import me.rndstad.drugsrpg.builder.menus.BuilderInventory;
import me.rndstad.drugsrpg.common.tools.ChatUtils;
import me.rndstad.drugsrpg.consume.Drug;
import me.rndstad.drugsrpg.consume.menus.DrugInventory;
import org.bukkit.Bukkit;
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
            if (args.length == 1) {
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
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("give")) {
                    String targetName = args[1];
                    Player target = Bukkit.getPlayer(targetName);
                    if (target != null) {
                        String drugName = args[2];
                        Drug drug = drugsrpg.getDrugsManager().getDrug(drugName);
                        if (drug != null) {
                            target.getInventory().addItem(drug.getItemStack());
                            player.sendMessage(ChatUtils.format("&aYou gave " + drug.getName() + " to " + target.getName() + "."));
                        } else {
                            player.sendMessage(ChatUtils.format("&aThis drug isn't found in the database."));
                        }
                    } else {
                        player.sendMessage(ChatUtils.format("&aThis player isn't found in the server."));
                    }
                }
            }
        }
        return false;
    }
}
