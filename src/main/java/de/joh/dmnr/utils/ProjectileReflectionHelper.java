package de.joh.dmnr.utils;

import de.joh.dmnr.armorupgrades.ArmorUpgradeInit;
import de.joh.dmnr.capabilities.dragonmagic.ArmorUpgradeHelper;
import de.joh.dmnr.config.CommonConfigs;
import de.joh.dmnr.item.items.dragonmagearmor.DragonMageArmor;
import net.minecraft.world.entity.player.Player;

/**
 * This Helper is used to reflects projectiles
 * @see de.joh.dmnr.events.DamageEventHandler
 * @author Joh0210
 */
public class ProjectileReflectionHelper {

    /**
     * This function is called by the Dragon Mage Armor every tick (if the upgrade is installed) to recharge the reflections
     * @see DragonMageArmor
     * @param player wearer of the armor
     */
    public static void tickReflectCharges(Player player) {
        int[] reflections = getReflectCharges(player);

        for(int i = 0; i < reflections.length; ++i) {
            if (reflections[i] > 0) {
                reflections[i]--;
            }
        }

        updateReflectCharges(player, reflections);
    }

    /**
     * If yes, start the cooldown of the reflection.
     * @see de.joh.dmnr.events.DamageEventHandler
     * @param player wearer of the armor
     * @return Can the player reflect a projectile?
     */
    public static boolean consumeReflectCharge(Player player) {
        int[] reflections = getReflectCharges(player);

        for(int i = 0; i < reflections.length; ++i) {
            if (reflections[i] <= 0) {
                reflections[i] = CommonConfigs.PROJECTILE_REFLECTION_TICKS_PER_CHARGE.get();
                updateReflectCharges(player, reflections);
                return true;
            }
        }

        return false;
    }

    private static int[] getReflectCharges(Player player) {
        int[] reflections;
        if (!player.getPersistentData().contains("dragon_mage_armor_reflect_counters")) {
            reflections = new int[ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.PROJECTILE_REFLECTION)];
        } else {
            reflections = player.getPersistentData().getIntArray("dragon_mage_armor_reflect_counters");
        }

        if (reflections.length != ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.PROJECTILE_REFLECTION)) {
            reflections = new int[(Integer)ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.PROJECTILE_REFLECTION)];
        }
        return reflections;
    }

    private static void updateReflectCharges(Player player, int[] reflections) {
        player.getPersistentData().putIntArray("dragon_mage_armor_reflect_counters", reflections);
    }
}
