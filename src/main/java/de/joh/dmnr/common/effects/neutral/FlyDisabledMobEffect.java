package de.joh.dmnr.common.effects.neutral;

import de.joh.dmnr.common.armorupgrade.FlyArmorUpgrade;
import de.joh.dmnr.common.init.EffectInit;
import de.joh.dmnr.api.item.DragonMageArmorItem;
import de.joh.dmnr.common.init.KeybindInit;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * This effect disables the Flight and Elytra effects of the Dragon Mage Armor
 * @see FlyArmorUpgrade
 * @see DragonMageArmorItem
 * @see KeybindInit
 * @see EffectInit
 * @author Joh0210
 */
public class FlyDisabledMobEffect extends MobEffect {
    public FlyDisabledMobEffect() {
        super(MobEffectCategory.NEUTRAL, -2448096);
    }
}
