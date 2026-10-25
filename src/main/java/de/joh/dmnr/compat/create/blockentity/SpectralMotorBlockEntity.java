package de.joh.dmnr.compat.create.blockentity;

import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import de.joh.dmnr.compat.create.CreateInit;
import de.joh.dmnr.compat.create.block.SpectralMotorBlock;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Block entity of the {@link SpectralMotorBlock}: a Create generator with the values of the spell that conjured it,
 * which removes its block once its duration has run out.
 * @author Joh0210
 */
public class SpectralMotorBlockEntity extends GeneratingKineticBlockEntity {
    /**
     * Pastel colours of the spectral texture of M&A, for the sparkles.
     */
    private static final int[][] SPARKLE_COLORS = {{244, 201, 244}, {201, 244, 228}, {244, 238, 201}, {201, 228, 244}, {220, 201, 244}};
    /**
     * Distance of the tips of the gear from the axis, in blocks.
     */
    private static final double GEAR_RADIUS = 0.48;

    /**
     * Generated rotation speed in RPM, always positive.
     */
    private int generatedSpeed = 16;
    /**
     * Stress capacity in SU, the same at every speed.
     */
    private int stressUnits = 256;
    /**
     * If true, the motor turns in the negative direction of its axis.
     */
    private boolean reversed = false;
    private int remainingTicks = 600;

    public SpectralMotorBlockEntity(BlockPos pos, BlockState state) {
        super(CreateInit.SPECTRAL_MOTOR_ENTITY.get(), pos, state);
    }

    /**
     * Applies the values of a spell and restarts the duration.
     * @param speed rotation speed in RPM
     * @param stressUnits stress capacity in SU
     * @param reversed should the motor turn in the negative direction of its axis?
     * @param durationTicks lifetime in ticks, starting now
     */
    public void configure(int speed, int stressUnits, boolean reversed, int durationTicks) {
        this.generatedSpeed = Math.max(1, speed);
        this.stressUnits = Math.max(0, stressUnits);
        this.reversed = reversed;
        this.remainingTicks = durationTicks;
        setChanged();

        // A freshly placed motor starts turning in initialize(), a running one has to inform its network.
        if (getTheoreticalSpeed() != 0) {
            updateGeneratedRotation();
        }
        sendData();
    }

    @Override
    public void initialize() {
        super.initialize();
        if (!hasSource() || getGeneratedSpeed() > getTheoreticalSpeed()) {
            updateGeneratedRotation();
        }
    }

    @Override
    public void tick() {
        super.tick();

        // Also counted on the client, for the goggle tooltip.
        remainingTicks--;
        if (level.isClientSide()) {
            spawnSparkle();
        } else if (remainingTicks <= 0) {
            level.destroyBlock(worldPosition, false);
        }
    }

    /**
     * Now and then a sparkle drifts off the tips of the gear, a little more often at high speed.
     */
    private void spawnSparkle() {
        RandomSource random = level.getRandom();
        if (random.nextFloat() > 0.02F + 0.05F * Math.abs(getSpeed()) / 256.0F) {
            return;
        }

        // Direction from the axis to a tip, within the plane of the gear
        double angle = random.nextDouble() * Math.PI * 2;
        double u = Math.cos(angle), v = Math.sin(angle);
        Vec3 direction = switch (getBlockState().getValue(SpectralMotorBlock.AXIS)) {
            case X -> new Vec3(0, u, v);
            case Y -> new Vec3(u, 0, v);
            case Z -> new Vec3(u, v, 0);
        };
        Vec3 position = Vec3.atCenterOf(worldPosition).add(direction.scale(GEAR_RADIUS));
        Vec3 velocity = direction.scale(0.01 + random.nextDouble() * 0.01);
        int[] color = SPARKLE_COLORS[random.nextInt(SPARKLE_COLORS.length)];

        // Larger and longer-lived than the default sparkle of M&A, which is barely visible
        MAParticleType sparkle = new MAParticleType(ParticleInit.SPARKLE_VELOCITY.get())
                .setColor(color[0], color[1], color[2])
                .setScale(0.08F)
                .setMaxAge(40);
        level.addParticle(sparkle, position.x, position.y, position.z, velocity.x, velocity.y, velocity.z);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        // Otherwise the chunk might be saved with an outdated duration.
        setChanged();
    }

    @Override
    public float getGeneratedSpeed() {
        if (!getBlockState().is(CreateInit.SPECTRAL_MOTOR.get())) {
            return 0;
        }
        return reversed ? -generatedSpeed : generatedSpeed;
    }

    /**
     * Create multiplies the capacity of a generator with its speed, so dividing by the speed keeps the stress
     * capacity independent of it.
     */
    @Override
    public float calculateAddedStressCapacity() {
        float capacity = (float) stressUnits / generatedSpeed;
        this.lastCapacityProvided = capacity;
        return capacity;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        // A LangBuilder appends to its component, so every line needs a new one.
        new LangBuilder("block.dmnr")
                .translate("spectral_motor.remaining", Math.max(0, remainingTicks) / 20)
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        return true;
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("GeneratedSpeed", generatedSpeed);
        compound.putInt("StressUnits", stressUnits);
        compound.putBoolean("Reversed", reversed);
        compound.putInt("RemainingTicks", remainingTicks);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        generatedSpeed = Math.max(1, compound.getInt("GeneratedSpeed"));
        stressUnits = compound.getInt("StressUnits");
        reversed = compound.getBoolean("Reversed");
        remainingTicks = compound.getInt("RemainingTicks");
        super.read(compound, clientPacket);
    }
}
