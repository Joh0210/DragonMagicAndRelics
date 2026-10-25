package de.joh.dmnr.compat.create.blockentity;

import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.particles.types.movers.ParticleSphereOrbitMover;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import de.joh.dmnr.compat.create.CreateInit;
import de.joh.dmnr.compat.create.block.EngineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Block entity of the {@link EngineBlock}: a Create generator that turns permanently at the speed and with the stress
 * capacity of its block.
 * <p>
 * The values are taken over from the config on the server whenever the block entity is loaded, and sent to the
 * clients, so the goggle tooltip is correct even if their config differs.
 * @author Joh0210
 */
public class EngineBlockEntity extends GeneratingKineticBlockEntity {
    /**
     * Radius of the orbits of the particles around the crystal, in blocks. The Eldrin Matrix of M&A uses 0.1 for a
     * crystal two thirds the size.
     */
    private static final double ORBIT_RADIUS = 0.15;

    /**
     * Generated rotation speed in RPM, always positive.
     */
    private int generatedSpeed = 1;
    /**
     * Stress capacity in SU, the same at every speed.
     */
    private int stressUnits = 0;

    public EngineBlockEntity(BlockPos pos, BlockState state) {
        super(CreateInit.ENGINE_ENTITY.get(), pos, state);
        updateValues();
    }

    /**
     * Takes over the speed and stress capacity of the block.
     * @return true if one of them has changed
     */
    private boolean updateValues() {
        if (!(getBlockState().getBlock() instanceof EngineBlock engine)) {
            return false;
        }

        int speed = Math.max(1, engine.getSpeed());
        int stress = Math.max(0, engine.getStressUnits());
        boolean changed = speed != generatedSpeed || stress != stressUnits;
        generatedSpeed = speed;
        stressUnits = stress;
        return changed;
    }

    @Override
    public void initialize() {
        boolean changed = !level.isClientSide() && updateValues();
        super.initialize();

        if (changed) {
            setChanged();
            sendData();
        }
        // A changed value also has to reach a network that was running before.
        if (changed || !hasSource() || getGeneratedSpeed() > getTheoreticalSpeed()) {
            updateGeneratedRotation();
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!level.isClientSide()) {
            return;
        }

        if (getBlockState().is(CreateInit.ARCANE_ENGINE.get())) {
            spawnOrbitingParticle(ParticleInit.ARCANE.get());
            playAmbientSound(SoundEvents.AMETHYST_BLOCK_CHIME, 0.8F);
        } else if (getBlockState().is(CreateInit.BRIMSTONE_ENGINE.get())) {
            spawnOrbitingParticle(ParticleInit.FLAME.get());
            playAmbientSound(SoundEvents.FIRE_AMBIENT, 0.6F);
        }
    }

    /**
     * Now and then the crystal makes a sound, on average every five seconds and each time a little higher or lower: it
     * shimmers like amethyst at the Arcane Engine and crackles like fire at the Brimstone Engine.
     * @param minPitch lowest pitch, the highest is 0.4 above it
     */
    private void playAmbientSound(SoundEvent sound, float minPitch) {
        RandomSource random = level.getRandom();
        if (random.nextInt(100) != 0) {
            return;
        }

        level.playLocalSound(worldPosition, sound, SoundSource.BLOCKS, 1.0F, minPitch + random.nextFloat() * 0.4F, false);
    }

    /**
     * Particles circle the crystal in every direction, like the ones around the crystal of an Eldrin Matrix of M&A:
     * arcane ones around the Arcane Engine, flames around the Brimstone Engine like at a Fire Matrix.
     * @param type type of the particle
     */
    private void spawnOrbitingParticle(MAParticleType type) {
        Vec3 center = Vec3.atCenterOf(worldPosition);
        // The orbit of every particle is tilted a little more than the one before, so together they form a sphere.
        MAParticleType particle = new MAParticleType(type)
                .setMaxAge(200)
                .setMover(new ParticleSphereOrbitMover(center.x, center.y, center.z, 0.05, Math.toDegrees(level.getGameTime() % 360), ORBIT_RADIUS));
        level.addParticle(particle, center.x, center.y, center.z, 0, 0, 0);
    }

    @Override
    public float getGeneratedSpeed() {
        if (!(getBlockState().getBlock() instanceof EngineBlock)) {
            return 0;
        }
        return generatedSpeed;
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
    protected void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("GeneratedSpeed", generatedSpeed);
        compound.putInt("StressUnits", stressUnits);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        // Without saved values, the ones of the block remain.
        if (compound.contains("GeneratedSpeed")) {
            generatedSpeed = Math.max(1, compound.getInt("GeneratedSpeed"));
            stressUnits = Math.max(0, compound.getInt("StressUnits"));
        }
        super.read(compound, clientPacket);
    }
}
