package de.joh.dmnr.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.entities.EntityInit;
import com.mna.entities.rituals.TimeChangeBall;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;

/**
 * This spell turns night into day.
 * @author Joh0210
 */
public class ComponentSunrise extends SpellEffect {
    public ComponentSunrise(final ResourceLocation guiIcon) {
        super(guiIcon);
    }

    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (!context.getWorld().isNight()) {
            return ComponentApplicationResult.FAIL;
        } else {

            BlockPos blockPos = (target.isBlock()) ? (new BlockPos(target.getBlock().getX(), (target.getBlock().getY() + 3), target.getBlock().getZ())) : (new BlockPos(target.getEntity().getOnPos().getX(), (target.getEntity().getOnPos().getY() + 3), target.getEntity().getOnPos().getZ()));

            Entity auroraBall = EntityInit.STARBALL_ENTITY.get().spawn((ServerLevel)context.getWorld(), null, null, blockPos, MobSpawnType.TRIGGERED, true, false);
            if (auroraBall instanceof TimeChangeBall) {
                ((TimeChangeBall) auroraBall).setTimeChangeType(TimeChangeBall.TIME_CHANGE_DAY);
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
        return 75.0f;
    }

    public int requiredXPForRote() {
        return 150;
    }

    public boolean canBeChanneled() {
        return false;
    }

    @Override
    public boolean canBeOnRandomStaff() {
        return false;
    }
}