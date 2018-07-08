package me.rndstad.drugsrpg.commands;

import me.rndstad.drugsrpg.Core;
import me.rndstad.drugsrpg.managers.MenuManager;
import me.rndstad.drugsrpg.utils.StringUtils;
import me.rndstad.drugsrpg.utils.YAML_API;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Drugs implements CommandExecutor {

	YAML_API config = Core.getInstance().getConfigData();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (p.hasPermission("drugs.admin")) {
			if (args.length == 0) {
				if(p.hasPermission("Drugs.Admin")) {
					MenuManager.getInstance().getMenu().show(p);
				} else {
					p.sendMessage(StringUtils.format("&8&m-----------------------"));
					p.sendMessage(StringUtils.format("&6&lDrugs &8- &7v1.15"));
					p.sendMessage(StringUtils.format("&7Author: &fTostiTegen"));
					p.sendMessage(StringUtils.format("&8&m-----------------------"));
				}
				return true;
			}
		}
		return false;
	}
}