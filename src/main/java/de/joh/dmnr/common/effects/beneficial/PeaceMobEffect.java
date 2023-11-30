package de.joh.dmnr.common.effects.beneficial;

import de.joh.dmnr.common.item.FactionAmuletItem;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * This effect is given by the Faction Amulet against when an opponent of another faction wants to attack you, the player and ensures that the monsters of other factions no longer attack.
 * @see FactionAmuletItem
 * @author Joh0210
 */
public class PeaceMobEffect extends MobEffect {
    public PeaceMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 16777215);
    }
}
