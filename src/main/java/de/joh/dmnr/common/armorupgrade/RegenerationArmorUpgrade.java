package de.joh.dmnr.common.armorupgrade;

import de.joh.dmnr.api.armorupgrade.PotionEffectArmorUpgrade;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

/**
 * Lets the wearer regenerate really fast
 * @author Joh021
 */
public class RegenerationArmorUpgrade extends PotionEffectArmorUpgrade {
    public RegenerationArmorUpgrade(@NotNull ResourceLocation registryName, int maxUpgradeLevel, RegistryObject<Item> upgradeSealItem, int upgradeCost) {
        super(registryName, maxUpgradeLevel, upgradeSealItem, true, upgradeCost);
    }

    @NotNull
    @Override
    public MobEffect getMobEffect() {
        return  MobEffects.REGENERATION;
    }
}
