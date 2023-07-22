package de.joh.dragonmagicandrelics.armorupgrades.types;

import de.joh.dragonmagicandrelics.armorupgrades.init.ArmorUpgradeFly;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Upgrades of this type add a permanent effect to the wearer.
 * @see ArmorUpgradeFly
 *
 * @see DragonMageArmor
 * @see de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit
 * @author Joh0210
 */
public abstract class IArmorUpgradeOnEquipped extends ArmorUpgrade {
    public IArmorUpgradeOnEquipped(@NotNull ResourceLocation registryName, int maxUpgradeLevel) {
        super(registryName, maxUpgradeLevel);
    }

    /**
     * This feature adds the permanent effect when a player fully equips the Dragon Mage armor.
     * @see DragonMageArmor
     * @param player Wearer of the Dragon Mage Armor. Player exclusive, no other entities.
     * @param level The level of the installed upgrade.
     */
    public abstract void onEquip(Player player, int level);
}
