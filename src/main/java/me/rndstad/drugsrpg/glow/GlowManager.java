package me.rndstad.drugsrpg.glow;

import me.rndstad.drugsrpg.DrugsPlugin;
import me.rndstad.drugsrpg.common.Module;
import me.rndstad.drugsrpg.glow.objects.Glow;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;

public class GlowManager extends Module {

    private final DrugsPlugin drugsrpg;

    public GlowManager(DrugsPlugin drugsrpg) {
        super("Glow Manager");
        this.drugsrpg = drugsrpg;

        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Glow glow = new Glow(70);
            Enchantment.registerEnchantment(glow);
        } catch (IllegalArgumentException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
