package de.joh.dragonmagicandrelics.armorupgrades.init;

import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.armorupgrades.types.ArmorUpgrade;
import de.joh.dragonmagicandrelics.armorupgrades.types.IArmorUpgradePotionEffect;
import de.joh.dragonmagicandrelics.effects.EffectInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArmorUpgradeElytra extends IArmorUpgradePotionEffect {
    private final boolean hasStrongerAlternative;
    public ArmorUpgradeElytra(@NotNull ResourceLocation registryName, int maxUpgradeLevel, boolean hasStrongerAlternative, int upgradeCost) {
        super(registryName, maxUpgradeLevel, !hasStrongerAlternative, upgradeCost);
        this.hasStrongerAlternative = hasStrongerAlternative;
    }

    @Override
    public @Nullable ArmorUpgrade getStrongerAlternative() {
        return hasStrongerAlternative ? ArmorUpgradeInit.ANGEL_FLIGHT : null;
    }

    @Override
    public MobEffect getMobEffect() {
        return EffectInit.ELYTRA.get();
    }

    @Override
    public void applyPotionAffect(Player player, int level){
        if(hasStrongerAlternative()){
            if(!player.hasEffect(getMobEffect())){
                player.addEffect(new MobEffectInstance(getMobEffect(), EFFECT_DURATION, 0, false, false, false));
            }
            else{
                //Update the duration of the effect.
                player.getEffect(getMobEffect()).update(new MobEffectInstance(getMobEffect(), EFFECT_DURATION, 0, false, false, false));
            }
        } else {
            super.applyPotionAffect(player, level + 1);
        }
    }
}
