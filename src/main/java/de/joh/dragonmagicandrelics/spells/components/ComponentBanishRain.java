package de.joh.dragonmagicandrelics.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
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
 * This spell stops rain/snowfall
 * @author Joh0210
 */
public class ComponentBanishRain extends SpellEffect {
    public ComponentBanishRain(final ResourceLocation registryName, final ResourceLocation guiIcon) {
        super(registryName, guiIcon, new AttributeValuePair[0]);
    }

    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        context.getWorld().setWeatherParameters(6000, 0, false, false);
        return ComponentApplicationResult.SUCCESS;
    }
    @Override
    public SoundEvent SoundEffect() {
        return SoundEvents.BLAZE_BURN;
    }

    public Affinity getAffinity() {
        return Affinity.FIRE;
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
