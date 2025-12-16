package de.joh.dmnr.common.spell.component;

import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.factions.Factions;
import com.mna.spells.components.PotionEffectComponent;
import de.joh.dmnr.common.init.EffectInit;
import net.minecraft.resources.ResourceLocation;

/**
 * This spell adds the Hellfire potion effect to the target.
 * @see de.joh.dmnr.common.effects.harmful.HellfireMobEffect
 * @author Joh0210
 */
public class HellfireComponent extends PotionEffectComponent {
    public HellfireComponent(final ResourceLocation guiIcon) {
        super(guiIcon, EffectInit.HELLFIRE_EFFECT, new AttributeValuePair(Attribute.DURATION, 15.0F, 5.0F, 300.0F, 5.0F, 2.0F));
    }

    public int requiredXPForRote() {
        return 200;
    }

    public Affinity getAffinity() {
        return Affinity.FIRE;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.HARMFUL;
    }

    public float initialComplexity() {
        return 40.0F;
    }

    public IFaction getFactionRequirement() {
        return Factions.DEMONS;
    }
}
