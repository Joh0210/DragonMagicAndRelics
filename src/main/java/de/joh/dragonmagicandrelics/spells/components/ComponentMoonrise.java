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
import com.mna.entities.EntityInit;
import com.mna.entities.rituals.EntityTimeChangeBall;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;

/**
 * This spell turns day into night.
 * @author Joh0210
 */
public class ComponentMoonrise extends SpellEffect {
    public ComponentMoonrise(final ResourceLocation registryName, final ResourceLocation guiIcon) {
        super(registryName, guiIcon, new AttributeValuePair[0]);
    }

    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (!context.getWorld().isDay()) {
            return ComponentApplicationResult.FAIL;
        } else {

            BlockPos blockPos = (target.isBlock()) ? (new BlockPos(target.getBlock().getX(), (target.getBlock().getY() + 3), target.getBlock().getZ())) : (new BlockPos(target.getEntity().getOnPos().getX(), (target.getEntity().getOnPos().getY() + 3), target.getEntity().getOnPos().getZ()));

            Entity auroraBall = EntityInit.STARBALL_ENTITY.get().spawn(context.getWorld(), null, null, blockPos, MobSpawnType.TRIGGERED, true, false);
            if (auroraBall != null && auroraBall instanceof EntityTimeChangeBall) {
                ((EntityTimeChangeBall) auroraBall).setTimeChangeType(EntityTimeChangeBall.TIME_CHANGE_NIGHT);
                return ComponentApplicationResult.SUCCESS;
            }
        }
        return ComponentApplicationResult.FAIL;
    }

    public Affinity getAffinity() {
        return Affinity.ENDER;
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
