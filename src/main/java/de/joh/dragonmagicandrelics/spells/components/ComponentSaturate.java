package de.joh.dragonmagicandrelics.spells.components;

import com.ma.api.affinity.Affinity;
import com.ma.api.capabilities.Faction;
import com.ma.api.spells.ComponentApplicationResult;
import com.ma.api.spells.SpellPartTags;
import com.ma.api.spells.attributes.AttributeValuePair;
import com.ma.api.spells.base.IModifiedSpellPart;
import com.ma.api.spells.parts.Component;
import com.ma.api.spells.targeting.SpellContext;
import com.ma.api.spells.targeting.SpellSource;
import com.ma.api.spells.targeting.SpellTarget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

/**
 * This spell satiates the user.
 * @author Joh0210
 */
public class ComponentSaturate extends Component {
    public ComponentSaturate(ResourceLocation registryName, ResourceLocation guiIcon) {
        super(registryName, guiIcon, new AttributeValuePair[0]);
    }

    public int requiredXPForRote() {
        return 200;
    }

    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<Component> modificationData, SpellContext context) {
        if(target.getEntity() instanceof PlayerEntity){
            ((PlayerEntity)target.getEntity()).getFoodStats().addStats(5, 6);
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
