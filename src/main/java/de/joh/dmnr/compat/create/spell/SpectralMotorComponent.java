package de.joh.dmnr.compat.create.spell;

import com.mna.api.affinity.Affinity;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.simibubi.create.infrastructure.config.AllConfigs;
import de.joh.dmnr.compat.create.CreateInit;
import de.joh.dmnr.compat.create.blockentity.SpectralMotorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * This spell conjures a temporary {@link de.joh.dmnr.compat.create.block.SpectralMotorBlock} at the targeted block,
 * similar to the Spectral Crafting Table and Anvil of M&A.
 * <ul>
 *     <li>Speed: rotation speed, from 16 to 256 RPM in steps of 16</li>
 *     <li>Magnitude: stress capacity, 256 SU doubled with every additional point (7 = 16,384 SU)</li>
 *     <li>Duration: lifetime in seconds</li>
 *     <li>Precision: 1 reverses the direction of rotation</li>
 * </ul>
 * Cast on an existing Spectral Motor, the spell applies its values to it and restarts its duration.
 * @author Joh0210
 */
public class SpectralMotorComponent extends SpellEffect {
    /**
     * Stress capacity at Magnitude 1.
     */
    private static final int BASE_STRESS_UNITS = 256;
    /**
     * Upper bound of the Magnitude for the calculation, so the stress capacity cannot overflow even if multipliers
     * or the M&A config raise it.
     */
    private static final int MAX_MAGNITUDE = 24;

    public SpectralMotorComponent(final ResourceLocation guiIcon) {
        super(guiIcon,
                new AttributeValuePair(Attribute.SPEED, 16.0F, 16.0F, 256.0F, 16.0F, 1.5F),
                new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 7.0F, 1.0F, 15.0F),
                new AttributeValuePair(Attribute.DURATION, 30.0F, 15.0F, 600.0F, 15.0F, 1F),
                new AttributeValuePair(Attribute.PRECISION, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        // Like the Spectral Crafting Table: one motor per cast, even if the spell hits several blocks.
        if (!target.isBlock() || context.hasEntityBeenAffected(this, source.getCaster())) {
            return ComponentApplicationResult.FAIL;
        }
        context.addAffectedEntity(this, source.getCaster());

        ServerLevel level = context.getServerLevel();
        BlockPos motorPos = target.getBlock();
        if (!level.getBlockState(motorPos).is(CreateInit.SPECTRAL_MOTOR.get())) {
            BlockPlaceResult result = tryPlaceBlock(source.getPlayer(), level, CreateInit.SPECTRAL_MOTOR.get(), target.getBlock(), target.getBlockFace(this), false);
            if (!result.success) {
                return ComponentApplicationResult.FAIL;
            }
            motorPos = result.position;
        }

        // Not there if the placement was cancelled, e.g. by a claim.
        if (!(level.getBlockEntity(motorPos) instanceof SpectralMotorBlockEntity motor)) {
            return ComponentApplicationResult.FAIL;
        }

        int speed = Mth.clamp(Math.round(modificationData.getValue(Attribute.SPEED)), 1, AllConfigs.server().kinetics.maxRotationSpeed.get());
        int magnitude = Mth.clamp(Math.round(modificationData.getValue(Attribute.MAGNITUDE)), 1, MAX_MAGNITUDE);
        int durationTicks = Math.max(20, Math.round(modificationData.getValue(Attribute.DURATION) * 20));
        boolean reversed = modificationData.getValue(Attribute.PRECISION) >= 0.5F;

        motor.configure(speed, BASE_STRESS_UNITS << (magnitude - 1), reversed, durationTicks);
        return ComponentApplicationResult.SUCCESS;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, @Nullable LivingEntity caster, @Nullable ISpellDefinition recipe) {
        if (age > 1 || recipe == null) {
            return;
        }

        BlockPos impact = BlockPos.containing(impact_position);
        for (int i = 0; i < 50; ++i) {
            world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_GRAVITY.get()), caster),
                    impact.getX() + 0.5, impact.getY() + 0.5, impact.getZ() + 0.5,
                    -0.25 + Math.random() * 0.5, Math.random() * 0.25, -0.25 + Math.random() * 0.5);
        }
    }

    /**
     * The sound M&A plays when a spectral creature is summoned.
     */
    @Override
    public SoundEvent SoundEffect() {
        return SFX.Entity.SpectralHorse.SUMMON;
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
        return 17.5F;
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public boolean targetsEntities() {
        return false;
    }

    @Override
    public boolean canBeChanneled() {
        return false;
    }

    @Override
    public boolean canBeOnRandomStaff() {
        return false;
    }
}
