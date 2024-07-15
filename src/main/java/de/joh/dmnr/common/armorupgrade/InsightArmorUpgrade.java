package de.joh.dmnr.common.armorupgrade;

import com.mna.effects.EffectInit;
import de.joh.dmnr.api.armorupgrade.PotionEffectArmorUpgrade;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

/**
 * Makes the wearer see damage and healing numbers permanently
 * @author Joh0210
 */
public class InsightArmorUpgrade extends PotionEffectArmorUpgrade {
    public InsightArmorUpgrade(@NotNull ResourceLocation registryName, RegistryObject<Item> upgradeSealItem, int upgradeCost) {
        super(registryName, 1, upgradeSealItem, false, upgradeCost);
    }

    @NotNull
    @Override
    public MobEffect getMobEffect() {
        return EffectInit.INSIGHT.get();
    }
}
