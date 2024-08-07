package de.joh.dmnr.common.armorupgrade;

import de.joh.dmnr.api.armorupgrade.ArmorUpgrade;
import de.joh.dmnr.api.armorupgrade.PotionEffectArmorUpgrade;
import de.joh.dmnr.common.effects.beneficial.ElytraMobEffect;
import de.joh.dmnr.common.init.ArmorUpgradeInit;
import de.joh.dmnr.common.init.EffectInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Gives the Player the ELYTRA Effect at Tier level +1 (-> Creative Flight + Boosted Elytra Flight)
 * @see ElytraMobEffect
 * @author Joh0210
 */
public class ElytraArmorUpgrade extends PotionEffectArmorUpgrade {
    private final boolean hasStrongerAlternative;
    public ElytraArmorUpgrade(@NotNull ResourceLocation registryName, int maxUpgradeLevel, RegistryObject<Item> upgradeSealItem, boolean hasStrongerAlternative, int upgradeCost) {
        super(registryName, maxUpgradeLevel, upgradeSealItem, !hasStrongerAlternative, upgradeCost);
        this.hasStrongerAlternative = hasStrongerAlternative;
    }

    @Override
    public @Nullable ArmorUpgrade getStrongerAlternative() {
        return hasStrongerAlternative ? ArmorUpgradeInit.ANGEL_FLIGHT : null;
    }

    @NotNull
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
