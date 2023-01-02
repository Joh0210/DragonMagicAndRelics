package de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonfullyequipped;

import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgrade;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraft.world.entity.player.Player;

/**
 * Upgrades of this type add a permanent effect to the wearer.
 * However, there is an error with these effects. Further information on this:
 * @see de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonarmortick.ArmorUpgradeFly
 *
 * @see DragonMageArmor
 * @see de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit
 * @author Joh0210
 */
public abstract class IArmorUpgradeOnFullyEquipped extends ArmorUpgrade {
    public IArmorUpgradeOnFullyEquipped(String upgradeId, int maxUpgradeLevel) {
        super(upgradeId, maxUpgradeLevel);
    }

    /**
     * This feature adds the permanent effect when a player fully equips the Dragon Mage armor.
     * @see DragonMageArmor
     * @param player Wearer of the Dragon Mage Armor. Player exclusive, no other entities.
     * @param level The level of the installed upgrade.
     */
    public abstract void applySetBonus(Player player, int level);

    /**
     * This feature removes the permanent effect from the player when the wearer unequips at least one piece of armor.
     * @param player Wearer of the Dragon Mage Armor. Player exclusive, no other entities.
     */
    public abstract void removeSetBonus(Player player);
}
