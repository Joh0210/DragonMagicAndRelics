package de.joh.dragonmagicandrelics.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

/**
 * This spell makes it rain. A Magnitude can summons a storm instead.
 * @author Joh0210
 */
public class ComponentConjureStorm extends SpellEffect {
    public ComponentConjureStorm(final ResourceLocation registryName, final ResourceLocation guiIcon) {
        super(registryName, guiIcon, new AttributeValuePair[]{new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 2.0F, 1.0F, 50.0F)});
    }

    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        context.getWorld().setWeatherParameters(0, 6000, true, 1.5F <= (modificationData.getValue(Attribute.MAGNITUDE)));

        return ComponentApplicationResult.SUCCESS;
    }

    @Override
    public SoundEvent SoundEffect() {
        return SoundEvents.LIGHTNING_BOLT_THUNDER;
    }

    public Affinity getAffinity() {
        return Affinity.WATER;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }

    public float initialComplexity() {
        return 75.0f;
    }

    public int requiredXPForRote() {
        return 150;
    }
}
