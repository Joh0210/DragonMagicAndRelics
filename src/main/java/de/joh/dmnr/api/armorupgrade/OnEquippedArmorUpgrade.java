package de.joh.dmnr.api.armorupgrade;

import de.joh.dmnr.common.armorupgrade.FlyArmorUpgrade;
import de.joh.dmnr.common.init.ArmorUpgradeInit;
import de.joh.dmnr.api.item.DragonMageArmorItem;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * Upgrades of this type add a permanent effect to the wearer.
 * @see FlyArmorUpgrade
 *
 * @see DragonMageArmorItem
 * @see ArmorUpgradeInit
 * @author Joh0210
 */
public abstract class OnEquippedArmorUpgrade extends ArmorUpgrade implements IOnEquippedArmorUpgrade {
    public OnEquippedArmorUpgrade(@NotNull ResourceLocation registryName, int maxUpgradeLevel, boolean isInfStackable, boolean supportsOnExtraLevel, int upgradeCost) {
        super(registryName, maxUpgradeLevel, isInfStackable, supportsOnExtraLevel, upgradeCost);
    }

    public OnEquippedArmorUpgrade(@NotNull ResourceLocation registryName, int maxUpgradeLevel, boolean isInfStackable, int upgradeCost) {
        super(registryName, maxUpgradeLevel, isInfStackable, upgradeCost);
    }

    @Override
    public ArmorUpgrade getArmorUpgrade() {
        return this;
    }
}
