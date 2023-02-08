package de.joh.dragonmagicandrelics.spells.components;

import com.ma.api.affinity.Affinity;
import com.ma.api.spells.ComponentApplicationResult;
import com.ma.api.spells.SpellPartTags;
import com.ma.api.spells.attributes.AttributeValuePair;
import com.ma.api.spells.base.IModifiedSpellPart;
import com.ma.api.spells.parts.Component;
import com.ma.api.spells.targeting.SpellContext;
import com.ma.api.spells.targeting.SpellSource;
import com.ma.api.spells.targeting.SpellTarget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

/**
 * This spell stops rain/snowfall
 * @author Joh0210
 */
public class ComponentBanishRain extends Component {
    public ComponentBanishRain(final ResourceLocation registryName, final ResourceLocation guiIcon) {
        super(registryName, guiIcon, new AttributeValuePair[0]);
    }

    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<Component> modificationData, SpellContext context) {
        context.getWorld().setWeather(6000, 0, false, false);
        return ComponentApplicationResult.SUCCESS;
    }
    @Override
    public SoundEvent SoundEffect() {
        return SoundEvents.ENTITY_BLAZE_BURN;
    }

    public Affinity getAffinity() {
        return Affinity.FIRE;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }

    public float initialComplexity() {
        return 250.0f;
    }

    public int requiredXPForRote() {
        return 100;
    }
}
