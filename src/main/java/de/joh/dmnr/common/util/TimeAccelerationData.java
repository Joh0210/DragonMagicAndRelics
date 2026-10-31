package de.joh.dmnr.common.util;

import com.mna.api.affinity.Affinity;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.spell.component.AccelerateTimeComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Blocks of one dimension whose time is accelerated by Accelerate Time. Saved with the dimension, so an acceleration
 * survives a restart. The blocks are only ticked while their chunk is ticking, but the duration runs out anyway.
 * @see AccelerateTimeComponent
 * @author Joh0210
 */
public class TimeAccelerationData extends SavedData {
    private static final String NAME = DragonMagicAndRelics.MOD_ID + "_time_acceleration";

    private final Map<BlockPos, Acceleration> accelerations = new HashMap<>();

    /**
     * @param extraTicks Additional ticks per game tick
     * @param endTime Game time at which the acceleration ends
     * @param randomTicks Are the random ticks accelerated as well?
     */
    private record Acceleration(int extraTicks, long endTime, boolean randomTicks) {
    }

    /**
     * Accelerates the time of the block. Replaces a running acceleration of the same block.
     */
    public static void accelerate(ServerLevel level, BlockPos pos, int extraTicks, int durationTicks, boolean randomTicks) {
        TimeAccelerationData data = level.getDataStorage().computeIfAbsent(TimeAccelerationData::load, TimeAccelerationData::new, NAME);
        data.accelerations.put(pos.immutable(), new Acceleration(extraTicks, level.getGameTime() + durationTicks, randomTicks));
        data.setDirty();
    }

    /**
     * Ticks all accelerated blocks of the dimension once. Called at the end of every level tick.
     */
    public static void tick(ServerLevel level) {
        // get instead of computeIfAbsent: dimensions without accelerations don't need a file
        TimeAccelerationData data = level.getDataStorage().get(TimeAccelerationData::load, NAME);
        if (data == null || data.accelerations.isEmpty()) {
            return;
        }

        long gameTime = level.getGameTime();
        // copy: a ticked block entity could cast Accelerate Time itself
        for (Map.Entry<BlockPos, Acceleration> entry : new ArrayList<>(data.accelerations.entrySet())) {
            BlockPos pos = entry.getKey();
            if (gameTime >= entry.getValue().endTime() || level.getBlockState(pos).is(AccelerateTimeComponent.BLACKLIST)) {
                data.accelerations.remove(pos);
                data.setDirty();
            } else if (level.shouldTickBlocksAt(pos)) {
                tickBlock(level, pos, entry.getValue());
                if (gameTime % 10 == 0) {
                    spawnParticles(level, pos, entry.getValue());
                }
            }
        }
    }

    /**
     * Arcane trails and sparkles that orbit the block like clock hands, plus rising arcane sparkles.
     * The number and speed of the orbits grow with the factor: slow and few at 2x, fast and many at 16x.
     */
    private static void spawnParticles(ServerLevel level, BlockPos pos, Acceleration acceleration) {
        int power = Math.max(1, Math.min(31 - Integer.numberOfLeadingZeros(acceleration.extraTicks() + 1), 6));
        double centerX = pos.getX() + 0.5;
        double centerZ = pos.getZ() + 0.5;

        // count 0: the client uses the offsets as the velocity of the particle;
        // for orbiting particles of M&A: angular speed per tick, upward speed per tick, radius.
        // The trails are flat horizontal ribbons, almost invisible when looking at the block from the front,
        // so the orbiting sparkles (which always face the camera) carry the effect from that side.
        MAParticleType trail = new MAParticleType(ParticleInit.TRAIL_ORBIT.get()).setColor(Affinity.ARCANE);
        MAParticleType orbitSparkle = new MAParticleType(ParticleInit.BLUE_SPARKLE_ORBIT.get()).setColor(Affinity.ARCANE);
        for (int i = 0; i < power; i++) {
            level.sendParticles(trail, centerX, pos.getY() + 0.1 + level.random.nextDouble() * 0.8, centerZ, 0, 0.08 * power, 0.01, 0.6, 1.0);
            for (int j = 0; j < 2; j++) {
                level.sendParticles(orbitSparkle, centerX, pos.getY() + 0.1 + level.random.nextDouble() * 0.8, centerZ, 0, 0.08 * power, 0.01, 0.6, 1.0);
            }
        }

        MAParticleType sparkle = new MAParticleType(ParticleInit.ARCANE.get()).setColor(Affinity.ARCANE);
        for (int i = 0; i < 2; i++) {
            double x = pos.getX() + level.random.nextDouble();
            double z = pos.getZ() + level.random.nextDouble();
            level.sendParticles(sparkle, x, pos.getY() + 0.8, z, 0, 0.0, 0.03, 0.0, 1.0);
        }
    }

    private static void tickBlock(ServerLevel level, BlockPos pos, Acceleration acceleration) {
        boolean randomTicks = acceleration.randomTicks() && CommonConfig.ACCELERATE_TIME_RANDOM_TICKS.get();
        int randomTickSpeed = level.getGameRules().getInt(GameRules.RULE_RANDOMTICKING);

        for (int i = 0; i < acceleration.extraTicks(); i++) {
            // read again every tick: the ticker can change the block (e.g. a furnace that starts burning)
            BlockState state = level.getBlockState(pos);
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity != null && !blockEntity.isRemoved() && blockEntity.getType().isValid(state)) {
                BlockEntityTicker<BlockEntity> ticker = getTicker(level, state, blockEntity);
                if (ticker != null) {
                    ticker.tick(level, pos, state, blockEntity);
                }
            }

            // vanilla picks randomTickSpeed of the 4096 blocks of a section per tick
            if (randomTicks && level.random.nextInt(4096) < randomTickSpeed) {
                state = level.getBlockState(pos);
                if (state.isRandomlyTicking()) {
                    state.randomTick(level, pos, level.random);
                }
            }
        }
    }

    /**
     * @return Can Accelerate Time affect the block at all: does it have a ticking block entity, or random ticks if they are accelerated?
     */
    public static boolean canAccelerate(ServerLevel level, BlockPos pos, boolean randomTicks) {
        BlockState state = level.getBlockState(pos);
        if (state.is(AccelerateTimeComponent.BLACKLIST)) {
            return false;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity != null && getTicker(level, state, blockEntity) != null) {
            return true;
        }

        return randomTicks && CommonConfig.ACCELERATE_TIME_RANDOM_TICKS.get() && state.isRandomlyTicking();
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private static <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, T blockEntity) {
        return state.getTicker(level, (BlockEntityType<T>) blockEntity.getType());
    }

    private static TimeAccelerationData load(CompoundTag nbt) {
        TimeAccelerationData data = new TimeAccelerationData();
        for (Tag tag : nbt.getList("accelerations", Tag.TAG_COMPOUND)) {
            CompoundTag entry = (CompoundTag) tag;
            data.accelerations.put(BlockPos.of(entry.getLong("pos")),
                    new Acceleration(entry.getInt("extra_ticks"), entry.getLong("end_time"), entry.getBoolean("random_ticks")));
        }
        return data;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag nbt) {
        ListTag list = new ListTag();
        for (Map.Entry<BlockPos, Acceleration> entry : accelerations.entrySet()) {
            CompoundTag tag = new CompoundTag();
            tag.putLong("pos", entry.getKey().asLong());
            tag.putInt("extra_ticks", entry.getValue().extraTicks());
            tag.putLong("end_time", entry.getValue().endTime());
            tag.putBoolean("random_ticks", entry.getValue().randomTicks());
            list.add(tag);
        }
        nbt.put("accelerations", list);
        return nbt;
    }
}
