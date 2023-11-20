package de.joh.dmnr.armorupgrades.init;

import de.joh.dmnr.armorupgrades.types.ArmorUpgradePotionEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import org.jetbrains.annotations.NotNull;

public class ArmorUpgradeRegeneration extends ArmorUpgradePotionEffect {
    public ArmorUpgradeRegeneration(@NotNull ResourceLocation registryName, int maxUpgradeLevel, int upgradeCost) {
        super(registryName, maxUpgradeLevel, true, upgradeCost);
    }

    @Override
    public MobEffect getMobEffect() {
        return  MobEffects.REGENERATION;
    }
}
