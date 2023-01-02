package de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonarmortick;

import com.mna.api.capabilities.IPlayerMagic;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgrade;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Each upgrade of this type has a function that is performed on each tick of the Dragon Mage Armor.
 * @see DragonMageArmor
 * @see de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit
 * @author Joh0210
 */
public abstract class IArmorUpgradeOnArmorTick extends ArmorUpgrade {
    public IArmorUpgradeOnArmorTick(String upgradeId, int maxUpgradeLevel) {
        super(upgradeId, maxUpgradeLevel);
    }

    /**
     * This feature of any upgrade (of this type) is performed every tick, if the player is wearing the full armor.
     * @param world The world in which the tick is triggered.
     * @param player Wearer of the Dragon Mage Armor. Player exclusive, no other entities.
     * @param level The level of the installed upgrade.
     * @param magic Player's magic parameters.
     * @see DragonMageArmor
     * @see de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit
     */
    public abstract void onArmorTick(Level world, Player player, int level, IPlayerMagic magic);
}
