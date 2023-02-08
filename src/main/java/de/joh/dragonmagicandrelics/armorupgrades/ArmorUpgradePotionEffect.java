package de.joh.dragonmagicandrelics.armorupgrades;

import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

/**
 * Upgrades that add a potion effect to the wearer of Dragon Mage Armor.
 * @see DragonMageArmor
 * @see de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit
 * @author Joh0210
 */
public class ArmorUpgradePotionEffect extends ArmorUpgrade{
    /**
     * Time you want the effect to last. Will be reseted every tick.
     */
    private static final int EFFECT_DURATION = 10;
    private final Effect mobEffect;
    private final int factor;

    /**
     * @param upgradeId ID under which the upgrade can be recognized.
     * @param maxUpgradeLevel Maximum upgrade level that can be installed for this type.
     * @param mobEffect Potion Effect to add to the wearer.
     * @param factor Optional. Factor by which The Potion Effect should be amplified.
     */
    public ArmorUpgradePotionEffect(String upgradeId, int maxUpgradeLevel, Effect mobEffect, int factor) {
        super(upgradeId, maxUpgradeLevel);
        this.mobEffect = mobEffect;
        this.factor = factor;
    }

    /**
     * @param upgradeId ID under which the upgrade can be recognized.
     * @param maxUpgradeLevel Maximum upgrade level that can be installed for this type.
     * @param mobEffect Potion Effect to add to the wearer.
     */
    public ArmorUpgradePotionEffect(String upgradeId, int maxUpgradeLevel, Effect mobEffect) {
        this(upgradeId,maxUpgradeLevel, mobEffect, 1);
    }

    /**
     * Adds or updates the Potion Effect for the Dragon Mage Armor wearer.
     * @see DragonMageArmor
     * @param player Wearer of the Dragon Mage Armor. Player exclusive, no other entities.
     * @param level The level of the installed upgrade.
     */
    public void applyPotionAffect(PlayerEntity player, int level){
        if(player.getActivePotionEffect(mobEffect) == null || player.getActivePotionEffect(mobEffect).getAmplifier() < (level*factor - 1)){
            player.addPotionEffect(new EffectInstance(mobEffect, EFFECT_DURATION, level*factor - 1, false, false, false));
        }
        else{
            //Update the duration of the effect.
            player.getActivePotionEffect(mobEffect).combine(new EffectInstance(mobEffect, EFFECT_DURATION, level*factor - 1, false, false, false));
        }
    }
}
