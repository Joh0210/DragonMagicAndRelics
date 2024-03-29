package de.joh.dmnr.common.effects.beneficial;

import de.joh.dmnr.common.init.ArmorUpgradeInit;
import de.joh.dmnr.capabilities.dragonmagic.ArmorUpgradeHelper;
import de.joh.dmnr.api.item.DragonMageArmorItem;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

/**
 * This effect ensures that if you are wearing full armor, you have every upgrade installed at max level.
 * This effect ensures that if you are wearing full armor, you have every upgrade installed at max level.
 * @see DragonMageArmorItem
 * @see ArmorUpgradeInit
 * @author Joh0210
 */
public class UltimateArmorMobEffect extends MobEffect {
    public UltimateArmorMobEffect() {
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
