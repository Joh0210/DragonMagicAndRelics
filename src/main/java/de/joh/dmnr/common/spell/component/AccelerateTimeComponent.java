package de.joh.dmnr.common.spell.component;

import com.mna.api.affinity.Affinity;
import com.mna.api.sound.SFX;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellCraftingContext;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.effects.EffectInit;
import de.joh.dmnr.api.spell.ChronomancyProgress;
import de.joh.dmnr.api.util.ModTags;
import de.joh.dmnr.common.util.CommonConfig;
import de.joh.dmnr.common.util.TimeAccelerationData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;

/**
 * This spell accelerates the time of the targeted block: its block entity (furnace, machine, ...) is ticked additionally,
 * with Precision also its random ticks (crops, saplings, ...).
 * <br> - MAGNITUDE: every level doubles the speed (the maximum is set in the spell part config of M&A)
 * <br> - DURATION: duration of the acceleration
 * <br> - PRECISION: also accelerates the random ticks, very expensive
 * <br> Afterwards the caster suffers from Chrono Exhaustion, which prevents casting it (and Chrono Anchor) again directly.
 * <br> Silver spell: it becomes rote by bending time, see {@link ChronomancyProgress}.
 * @see TimeAccelerationData
 * @author Joh0210
 */
public class AccelerateTimeComponent extends SpellEffect {
    /**
     * Blocks whose time can't be accelerated
     */
    public static final TagKey<Block> BLACKLIST = BlockTags.create(ModTags.Blocks.ACCELERATE_TIME_BLACKLIST);

    public AccelerateTimeComponent(final ResourceLocation guiIcon) {
        super(guiIcon,
                new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 4.0F, 1.0F, 25.0F),
                new AttributeValuePair(Attribute.DURATION, 15.0F, 5.0F, 60.0F, 5.0F, 5.0F),
                new AttributeValuePair(Attribute.PRECISION, 0.0F, 0.0F, 1.0F, 1.0F, 100.0F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        LivingEntity caster = source.getCaster();
        if (!CommonConfig.ACCELERATE_TIME_ENABLED.get() || !target.isBlock() || caster == null || !(context.getLevel() instanceof ServerLevel level)) {
            return ComponentApplicationResult.FAIL;
        }

        if (caster.hasEffect(EffectInit.CHRONO_EXHAUSTION.get())) {
            if (caster instanceof Player player) {
                player.displayClientMessage(Component.translatable("dmnr.feedback.chrono_exhaustion"), true);
            }
            return ComponentApplicationResult.FAIL;
        }

        BlockPos pos = target.getBlock();
        boolean randomTicks = modificationData.getValue(Attribute.PRECISION) >= 1.0F;
        if (!TimeAccelerationData.canAccelerate(level, pos, randomTicks)) {
            return ComponentApplicationResult.FAIL;
        }

        // every Magnitude level doubles the speed: 2x, 4x, 8x, 16x (limited to 30 levels against an overflow)
        int magnitude = Math.round(modificationData.getValue(Attribute.MAGNITUDE));
        int extraTicks = (1 << Math.min(magnitude, 30)) - 1;
        int durationTicks = Math.round(modificationData.getValue(Attribute.DURATION) * 20);
        TimeAccelerationData.accelerate(level, pos, extraTicks, durationTicks, randomTicks);

        int exhaustion = CommonConfig.ACCELERATE_TIME_EXHAUSTION.get();
        if (exhaustion > 0) {
            caster.addEffect(new MobEffectInstance(EffectInit.CHRONO_EXHAUSTION.get(), exhaustion * 20));
        }
        return ComponentApplicationResult.SUCCESS;
    }

    @Override
    public boolean isCraftable(SpellCraftingContext context) {
        return CommonConfig.ACCELERATE_TIME_ENABLED.get();
    }

    @Override
    public boolean isSilverSpell() {
        return true;
    }

    @Override
    public int requiredXPForRote() {
        return 150;
    }

    @Override
    public SoundEvent SoundEffect() {
        return SFX.Spell.Buff.ARCANE;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ARCANE;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }

    @Override
    public float initialComplexity() {
        return 50.0F;
    }

    @Override
    public boolean canBeChanneled() {
        return false;
    }

    @Override
    public boolean canBeOnRandomStaff() {
        return false;
    }

    @Override
    public boolean targetsBlocks() {
        return true;
    }

    @Override
    public boolean targetsEntities() {
        return false;
    }
}
