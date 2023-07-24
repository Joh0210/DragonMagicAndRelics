package de.joh.dragonmagicandrelics.armorupgrades.init;

import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.armorupgrades.types.ArmorUpgrade;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This upgrade protects you from fire damage. Level 1 Consumes mana instead. Level 2 protects you completely.
 * Increasing the maximum level has no effect without further adjustments.
 * @see de.joh.dragonmagicandrelics.events.DamageEventHandler
 * @author Joh0210
 */
public class ArmorUpgradeFireResistance extends ArmorUpgrade {
    public final boolean hasStrongerAlternative;

    /**
     * @param registryName ID under which the upgrade can be recognized.
     * @param maxUpgradeLevel Maximum upgrade level that can be installed for this type.
     */
    public ArmorUpgradeFireResistance(@NotNull ResourceLocation registryName, int maxUpgradeLevel, boolean hasStrongerAlternative) {
        super(registryName, maxUpgradeLevel, false);
        this.hasStrongerAlternative = hasStrongerAlternative;
    }

    @Override
    public @Nullable ArmorUpgrade getStrongerAlternative() {
        return hasStrongerAlternative ? ArmorUpgradeInit.MAJOR_FIRE_RESISTANCE : null;
    }
}
