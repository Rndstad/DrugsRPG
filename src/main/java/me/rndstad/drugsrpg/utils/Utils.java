package me.rndstad.drugsrpg.utils;

import java.lang.reflect.Field;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Utils {
	
	public Utils() {
		
	}
	
	public void registerGlow() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Glow glow = new Glow(70);
            Enchantment.registerEnchantment(glow);
        }
        catch (IllegalArgumentException e){
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
	
	public void removeItem(PlayerInventory inv, Material type, int amount) {
	    for (ItemStack is : inv.getContents()) {
	        if (is != null && is.getType() == type) {
	            int newamount = is.getAmount() - amount;
	            if (newamount > 0) {
	                is.setAmount(newamount);
	                break;
	            } else {
	                inv.remove(is);
	                amount = -newamount;
	                if (amount == 0) break;
	            }
	        }
	    }
	}

}
