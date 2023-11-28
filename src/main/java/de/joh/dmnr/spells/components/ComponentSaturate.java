package de.joh.dmnr.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.factions.Factions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * This spell satiates the user.
 * @author Joh0210
 */
public class ComponentSaturate extends SpellEffect {
    public ComponentSaturate(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 5.0F, 1.0F, 10F));
    }

    public int requiredXPForRote() {
        return 200;
    }

    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        int magnitude = (int) modificationData.getValue(Attribute.MAGNITUDE);
        if(target.getEntity() instanceof Player player){
            player.getFoodData().eat(magnitude, 0.1f * magnitude);
            return ComponentApplicationResult.SUCCESS;
        }

        return ComponentApplicationResult.FAIL;
    }

    @Override
    public boolean targetsBlocks() {
        return false;
    }

    public Affinity getAffinity() {
        return Affinity.EARTH;
    }

    @Override
    public IFaction getFactionRequirement() {
        return Factions.FEY;
    }

    public float initialComplexity() {
        return 30.0F;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.FRIENDLY;
    }
}
