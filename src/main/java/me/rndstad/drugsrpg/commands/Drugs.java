package me.rndstad.drugsrpg.commands;

import me.rndstad.drugsrpg.Core;
import me.rndstad.drugsrpg.menus.DrugInventory;
import me.rndstad.drugsrpg.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Drugs implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (p.hasPermission("drugs.admin")) {
			if (args.length == 0) {
				if(p.hasPermission("drugs.admin")) {
					DrugInventory.INVENTORY.open(p);
				} else {
                    p.sendMessage(StringUtils.format("&8&m-----------------------"));
                    p.sendMessage(StringUtils.format("&6&lDrugsRPG &8- &7v" + Core.getInstance().getDescription().getVersion()));
                    p.sendMessage(StringUtils.format("&7Author: &fTostiTegen"));
                    p.sendMessage(StringUtils.format("&8&m-----------------------"));
                }
				return true;
			}
		}
		return false;
	}
}