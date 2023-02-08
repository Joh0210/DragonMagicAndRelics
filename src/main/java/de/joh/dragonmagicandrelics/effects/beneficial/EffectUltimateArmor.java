package de.joh.dragonmagicandrelics.effects.beneficial;

import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

/**
 * This effect ensures that if you are wearing full armor, you have every upgrade installed at max level.
 * This effect ensures that if you are wearing full armor, you have every upgrade installed at max level.
 * @see DragonMageArmor
 * @see ArmorUpgradeInit
 * @author Joh0210
 */
public class EffectUltimateArmor extends Effect {
    public EffectUltimateArmor() {
        super(EffectType.BENEFICIAL, -2448096);
    }
}
