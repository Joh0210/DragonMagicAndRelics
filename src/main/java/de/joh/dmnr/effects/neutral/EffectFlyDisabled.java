package de.joh.dmnr.effects.neutral;

import de.joh.dmnr.armorupgrades.init.ArmorUpgradeFly;
import de.joh.dmnr.item.items.dragonmagearmor.DragonMageArmor;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * This effect disables the Flight and Elytra effects of the Dragon Mage Armor
 * @see ArmorUpgradeFly
 * @see DragonMageArmor
 * @see de.joh.dmnr.utils.KeybindInit
 * @see de.joh.dmnr.effects.EffectInit
 * @author Joh0210
 */
public class EffectFlyDisabled extends MobEffect {
    public EffectFlyDisabled() {
        super(MobEffectCategory.NEUTRAL, -2448096);
    }
}
