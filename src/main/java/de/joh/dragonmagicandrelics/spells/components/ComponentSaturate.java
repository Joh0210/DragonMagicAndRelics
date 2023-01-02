package de.joh.dragonmagicandrelics.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.Faction;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * This spell satiates the user.
 * @author Joh0210
 */
public class ComponentSaturate extends SpellEffect {
    public ComponentSaturate(ResourceLocation registryName, ResourceLocation guiIcon) {
        super(registryName, guiIcon, new AttributeValuePair[0]);
    }

    public int requiredXPForRote() {
        return 200;
    }

    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if(target.getEntity() instanceof Player player){
            player.getFoodData().eat(5, 6);
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
    public Faction getFactionRequirement() {
        return Faction.FEY_COURT;
    }

    public float initialComplexity() {
        return 50.0F;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.FRIENDLY;
    }
}
