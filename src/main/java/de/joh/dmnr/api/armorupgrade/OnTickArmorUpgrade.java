package de.joh.dmnr.api.armorupgrade;

import com.mna.api.capabilities.IPlayerMagic;
import de.joh.dmnr.common.init.ArmorUpgradeInit;
import de.joh.dmnr.api.item.DragonMageArmorItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Each upgrade of this type has a function that is performed on each tick of the Dragon Mage Armor.
 * @see DragonMageArmorItem
 * @see ArmorUpgradeInit
 * @author Joh0210
 */
public abstract class OnTickArmorUpgrade extends ArmorUpgrade {
    public OnTickArmorUpgrade(@NotNull ResourceLocation registryName, int maxUpgradeLevel, boolean isInfStackable, boolean supportsOnExtraLevel, int upgradeCost) {
        super(registryName, maxUpgradeLevel, isInfStackable, supportsOnExtraLevel, upgradeCost);
    }

    public OnTickArmorUpgrade(@NotNull ResourceLocation registryName, int maxUpgradeLevel, boolean isInfStackable, int upgradeCost) {
        super(registryName, maxUpgradeLevel, isInfStackable, upgradeCost);
    }

    /**
     * This feature of any upgrade (of this type) is performed every tick, if the player is wearing the full armor.
     * @param world The world in which the tick is triggered.
     * @param player Wearer of the Dragon Mage Armor. Player exclusive, no other entities.
     * @param level The level of the installed upgrade.
     * @param magic Player's magic parameters.
     * @see DragonMageArmorItem
     * @see ArmorUpgradeInit
     */
    public abstract void onTick(Level world, Player player, int level, IPlayerMagic magic);
}
