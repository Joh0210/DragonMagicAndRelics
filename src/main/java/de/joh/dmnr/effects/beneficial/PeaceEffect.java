package de.joh.dmnr.effects.beneficial;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * This effect is given by the Faction Amulet against when an opponent of another faction wants to attack you, the player and ensures that the monsters of other factions no longer attack.
 * @see de.joh.dmnr.item.items.FactionAmulet
 * @author Joh0210
 */
public class PeaceEffect extends MobEffect {
    public PeaceEffect() {
        super(MobEffectCategory.BENEFICIAL, 16777215);
    }
}
