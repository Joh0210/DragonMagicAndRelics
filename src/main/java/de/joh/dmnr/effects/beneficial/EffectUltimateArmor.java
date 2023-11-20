package de.joh.dmnr.effects.beneficial;

import de.joh.dmnr.armorupgrades.ArmorUpgradeInit;
import de.joh.dmnr.capabilities.dragonmagic.ArmorUpgradeHelper;
import de.joh.dmnr.item.items.dragonmagearmor.DragonMageArmor;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

/**
 * This effect ensures that if you are wearing full armor, you have every upgrade installed at max level.
 * This effect ensures that if you are wearing full armor, you have every upgrade installed at max level.
 * @see DragonMageArmor
 * @see ArmorUpgradeInit
 * @author Joh0210
 */
public class EffectUltimateArmor extends MobEffect {
    public EffectUltimateArmor() {
        super(MobEffectCategory.BENEFICIAL, -2448096);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity livingEntity, AttributeMap attributeMap, int amplifier) {
        if (livingEntity instanceof Player){
            ArmorUpgradeHelper.ultimateArmorFin((Player) livingEntity);
        }

        super.addAttributeModifiers(livingEntity, attributeMap, amplifier);
    }

    @Override
    public void addAttributeModifiers(LivingEntity livingEntity, AttributeMap attributeMap, int amplifier) {
        if (livingEntity instanceof Player){
            ArmorUpgradeHelper.ultimateArmorStart((Player) livingEntity);
        }

        super.addAttributeModifiers(livingEntity, attributeMap, amplifier);
    }
}
