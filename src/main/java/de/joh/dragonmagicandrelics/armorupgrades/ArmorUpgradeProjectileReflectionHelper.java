package de.joh.dragonmagicandrelics.armorupgrades;

import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.config.CommonConfigs;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * This Helper is used to reflects projectiles
 * @see de.joh.dragonmagicandrelics.events.DamageEventHandler
 */
public class ArmorUpgradeProjectileReflectionHelper {

    /**
     * This function is called by the Dragon Mage Armor every tick (if the upgrade is installed) to recharge the reflections
     * @see de.joh.dragonmagicandrelics.item.items.DragonMageArmor
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
     * @see de.joh.dragonmagicandrelics.events.DamageEventHandler
     * @param player wearer of the armor
     * @return Can the player reflect a projectile?
     */
    public static boolean consumeReflectCharge(Player player) {
        int[] reflections = getReflectCharges(player);

        DragonMagicAndRelics.LOGGER.info("NEXT");
        for(int i = 0; i < reflections.length; ++i) {
            DragonMagicAndRelics.LOGGER.info(Integer.toString(reflections[i]));
        }

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
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if (!chest.isEmpty() && chest.getItem() instanceof DragonMageArmor dragonMageArmor && dragonMageArmor.isSetEquipped(player)) {
            if (!player.getPersistentData().contains("dragon_mage_armor_reflect_counters")) {
                reflections = new int[(Integer) dragonMageArmor.getUpgradeLevel(ArmorUpgradeInit.PROJECTILE_REFLECTION, player)];
            } else {
                reflections = player.getPersistentData().getIntArray("dragon_mage_armor_reflect_counters");
            }

            if (reflections.length != dragonMageArmor.getUpgradeLevel(ArmorUpgradeInit.PROJECTILE_REFLECTION, player)) {
                reflections = new int[(Integer)dragonMageArmor.getUpgradeLevel(ArmorUpgradeInit.PROJECTILE_REFLECTION, player)];
            }
        } else {
            reflections = new int[]{CommonConfigs.PROJECTILE_REFLECTION_TICKS_PER_CHARGE.get()};
        }
        return reflections;
    }

    private static void updateReflectCharges(Player player, int[] reflections) {
        player.getPersistentData().putIntArray("dragon_mage_armor_reflect_counters", reflections);
    }
}
