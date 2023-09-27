package de.joh.dragonmagicandrelics.armorupgrades.types;

import de.joh.dragonmagicandrelics.armorupgrades.init.ArmorUpgradeFly;
import de.joh.dragonmagicandrelics.item.items.dragonmagearmor.DragonMageArmor;
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
public abstract class ArmorUpgradeOnEquipped extends ArmorUpgrade implements IArmorUpgradeOnEquipped{
    public ArmorUpgradeOnEquipped(@NotNull ResourceLocation registryName, int maxUpgradeLevel, boolean isInfStackable, boolean supportsOnExtraLevel, int upgradeCost) {
        super(registryName, maxUpgradeLevel, isInfStackable, supportsOnExtraLevel, upgradeCost);
    }

    public ArmorUpgradeOnEquipped(@NotNull ResourceLocation registryName, int maxUpgradeLevel, boolean isInfStackable, int upgradeCost) {
        super(registryName, maxUpgradeLevel, isInfStackable, upgradeCost);
    }

    @Override
    public ArmorUpgrade getArmorUpgrade() {
        return this;
    }
}
