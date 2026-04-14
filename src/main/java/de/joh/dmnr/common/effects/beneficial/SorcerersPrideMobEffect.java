package de.joh.dmnr.common.effects.beneficial;

import com.mna.api.events.SpellCastEvent;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.Shape;
import de.joh.dmnr.common.init.EffectInit;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

/**
 * This Effect Boosts the Spells of the user.
 * Boosts per Level:
 * <br> - MAGNITUDE +0.5
 * <br> - DAMAGE    +3
 * <br> - DURATION: +30%
 * @author Joh0210
 */
public class SorcerersPrideMobEffect extends MobEffect {
    public SorcerersPrideMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -2448096);
    }

    public static void spellBoost(SpellCastEvent event) {
        LivingEntity caster = event.getSource().getCaster();
        if (caster != null) {
            IModifiedSpellPart<Shape> shape = event.getSpell().getShape();

            int level = caster.hasEffect(EffectInit.SORCERERS_PRIDE.get()) ? caster.getEffect(EffectInit.SORCERERS_PRIDE.get()).getAmplifier() + 1 : 0;
            if (level > 0 && shape != null) {
                shape.getContainedAttributes().stream()
                        .filter(attribute -> attribute == Attribute.MAGNITUDE)
                        .forEach(attribute -> shape.setValue(attribute, shape.getValue(attribute) + Math.round(level * 0.5f)));
                shape.getContainedAttributes().stream()
                        .filter(attribute -> attribute == Attribute.DAMAGE)
                        .forEach(attribute -> shape.setValue(attribute, shape.getValue(attribute) + level * 3));
                shape.getContainedAttributes().stream()
                        .filter(attribute -> attribute == Attribute.DURATION)
                        .forEach(attribute -> shape.setValue(attribute, shape.getValue(attribute) * (1 + level * 0.3f)));

                event.getSpell().getComponents().forEach(modifiedSpellPart -> modifiedSpellPart.getContainedAttributes().stream()
                        .filter(attribute -> attribute == Attribute.MAGNITUDE)
                        .forEach(attribute -> modifiedSpellPart.setValue(attribute, modifiedSpellPart.getValue(attribute) + Math.round(level * 0.5f))));
                event.getSpell().getComponents().forEach(modifiedSpellPart -> modifiedSpellPart.getContainedAttributes().stream()
                        .filter(attribute -> attribute == Attribute.DAMAGE)
                        .forEach(attribute -> modifiedSpellPart.setValue(attribute, modifiedSpellPart.getValue(attribute) + level * 3)));
                event.getSpell().getComponents().forEach(modifiedSpellPart -> modifiedSpellPart.getContainedAttributes().stream()
                        .filter(attribute -> attribute == Attribute.DURATION)
                        .forEach(attribute -> modifiedSpellPart.setValue(attribute, modifiedSpellPart.getValue(attribute) * (1 + level * 0.3f))));
            }
        }
    }
}
