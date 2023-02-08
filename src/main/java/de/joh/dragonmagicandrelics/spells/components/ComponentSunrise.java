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
import com.ma.entities.EntityInit;
import com.ma.entities.rituals.EntityTimeChangeBall;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

/**
 * This spell turns night into day.
 * @author Joh0210
 */
public class ComponentSunrise extends Component {
    public ComponentSunrise(final ResourceLocation registryName, final ResourceLocation guiIcon) {
        super(registryName, guiIcon, new AttributeValuePair[0]);
    }

    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<Component> modificationData, SpellContext context) {
        if (!context.getWorld().isNightTime()) {
            return ComponentApplicationResult.FAIL;
        } else {

            BlockPos blockPos = (target.isBlock()) ? (new BlockPos(target.getBlock().getX(), (target.getBlock().getY() + 3), target.getBlock().getZ())) : (new BlockPos(target.getEntity().getPosition().getX(), (target.getEntity().getPosition().getY() + 3), target.getEntity().getPosition().getZ()));

            Entity auroraBall = EntityInit.STARBALL_ENTITY.get().spawn(context.getWorld(), null, null, blockPos, SpawnReason.TRIGGERED, true, false);
            if (auroraBall != null && auroraBall instanceof EntityTimeChangeBall) {
                ((EntityTimeChangeBall) auroraBall).setTimeChangeType(EntityTimeChangeBall.TIME_CHANGE_DAY);
                return ComponentApplicationResult.SUCCESS;
            }
        }
        return ComponentApplicationResult.FAIL;
    }

    public Affinity getAffinity() {
        return Affinity.ARCANE;
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
