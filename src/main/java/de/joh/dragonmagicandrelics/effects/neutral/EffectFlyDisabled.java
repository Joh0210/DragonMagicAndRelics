package de.joh.dragonmagicandrelics.effects.neutral;

import de.joh.dragonmagicandrelics.armorupgrades.init.ArmorUpgradeFly;
import de.joh.dragonmagicandrelics.item.items.dragonmagearmor.DragonMageArmor;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * This effect disables the Flight and Elytra effects of the Dragon Mage Armor
 * @see ArmorUpgradeFly
 * @see DragonMageArmor
 * @see de.joh.dragonmagicandrelics.utils.KeybindInit
 * @see de.joh.dragonmagicandrelics.effects.EffectInit
 * @author Joh0210
 */
public class EffectFlyDisabled extends MobEffect {
    public EffectFlyDisabled() {
        super(MobEffectCategory.NEUTRAL, -2448096);
    }
}
