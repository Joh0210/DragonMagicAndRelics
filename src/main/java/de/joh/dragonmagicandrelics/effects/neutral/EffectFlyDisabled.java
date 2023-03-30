package de.joh.dragonmagicandrelics.effects.neutral;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * This effect disables the Flight and Elytra effects of the Dragon Mage Armor
 * @see de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonarmortick.ArmorUpgradeFly
 * @see de.joh.dragonmagicandrelics.item.items.DragonMageArmor
 * @see de.joh.dragonmagicandrelics.utils.KeybindInit
 * @see de.joh.dragonmagicandrelics.effects.EffectInit
 * @author Joh0210
 */
public class EffectFlyDisabled extends MobEffect {
    public EffectFlyDisabled() {
        super(MobEffectCategory.NEUTRAL, -2448096);
    }
}
