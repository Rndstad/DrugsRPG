package me.rndstad.drugsrpg.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class StringUtils {
	
	public static String format(String string){
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public static String message(Player p, String string){
		p.sendMessage(format(string));
		return string;
	}
}