package de.joh.dragonmagicandrelics.armorupgrades.init;

import de.joh.dragonmagicandrelics.armorupgrades.types.IArmorUpgradePotionEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import org.jetbrains.annotations.NotNull;

public class ArmorUpgradeRegeneration extends IArmorUpgradePotionEffect {
    public ArmorUpgradeRegeneration(@NotNull ResourceLocation registryName, int maxUpgradeLevel, int upgradeCost) {
        super(registryName, maxUpgradeLevel, true, upgradeCost);
    }

    @Override
    public MobEffect getMobEffect() {
        return  MobEffects.REGENERATION;
    }
}
