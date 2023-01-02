package de.joh.dragonmagicandrelics.spells.components;

import com.mna.api.spells.SpellPartTags;
import com.mna.api.affinity.Affinity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.attributes.AttributeValuePair;
import net.minecraft.resources.ResourceLocation;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.entities.EntityInit;
import com.mna.entities.rituals.EntityTimeChangeBall;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * This spell turns night into day.
 * @author Joh0210
 */
public class ComponentSunrise extends SpellEffect {
    public ComponentSunrise(final ResourceLocation registryName, final ResourceLocation guiIcon) {
        super(registryName, guiIcon, new AttributeValuePair[0]);
    }

    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (!context.getWorld().isNight()) {
            return ComponentApplicationResult.FAIL;
        } else {

            BlockPos blockPos = (target.isBlock()) ? (new BlockPos(target.getBlock().getX(), (target.getBlock().getY() + 3), target.getBlock().getZ())) : (new BlockPos(target.getEntity().getOnPos().getX(), (target.getEntity().getOnPos().getY() + 3), target.getEntity().getOnPos().getZ()));

            Entity auroraBall = ((EntityType) EntityInit.STARBALL_ENTITY.get()).spawn((ServerLevel) context.getWorld(), (ItemStack) null, (Player) null, blockPos, MobSpawnType.TRIGGERED, true, false);
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
