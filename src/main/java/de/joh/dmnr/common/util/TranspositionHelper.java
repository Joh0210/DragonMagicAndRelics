package de.joh.dmnr.common.util;

import de.joh.dmnr.api.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.level.BlockEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Swaps two areas of the same size in one level, together with their block entities and (if enabled in the config)
 * the entities in them. Used by the Transposer.
 * <p>
 * The swap happens in phases, so that no block is updated while the areas are only half swapped (a torch would fall
 * off a wall that has not been moved yet): first all blocks and block entities are read, then all blocks are placed
 * without updates, then the block entities are restored, and only then the moved blocks and their neighbours are
 * updated.
 * <br>Blocks that can't be broken (bedrock, end portal frames, ...) and blocks of the tag
 * {@code dmnr:transposer_blacklist} stay where they are, together with the block they would be swapped with.
 * Entities of the entity type tag {@code dmnr:transposer_blacklist} stay as well.
 * @see de.joh.dmnr.common.block.entity.TransposerBlockEntity
 * @author Joh0210
 */
public class TranspositionHelper {
    public static final TagKey<Block> BLOCK_BLACKLIST = BlockTags.create(ModTags.Blocks.TRANSPOSER_BLACKLIST);
    public static final TagKey<EntityType<?>> ENTITY_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, ModTags.EntityTypes.TRANSPOSER_BLACKLIST);

    // The blocks are placed without updates, the updates follow once both areas are complete.
    private static final int PLACE_FLAGS = Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_MOVE_BY_PISTON;

    /**
     * Content of a position before the swap.
     * @param blockEntity saved block entity (with its ID), if the block has one
     */
    private record Snapshot(@NotNull BlockState state, @Nullable CompoundTag blockEntity) {
    }

    /**
     * @return the area above the given Transposer: a cube with an edge length of {@code 2 * radius + 1}, centered
     * horizontally on the Transposer and starting right above it
     */
    public static @NotNull BoundingBox getArea(@NotNull BlockPos transposer, int radius) {
        return new BoundingBox(
                transposer.getX() - radius, transposer.getY() + 1, transposer.getZ() - radius,
                transposer.getX() + radius, transposer.getY() + 1 + 2 * radius, transposer.getZ() + radius);
    }

    /**
     * Asks the claim and protection mods whether all the given players may change the area, through the
     * {@link BlockEvent.BreakEvent} they listen to (like M&A does for its spells, which also respects its own wards and
     * boss arenas). No block is broken.
     * <br>Every position of the area is checked, air included, so entities in protected land are protected as well.
     * @return whether none of the players is denied any position of the area
     */
    public static boolean mayChange(@NotNull ServerLevel level, @NotNull BoundingBox area, @NotNull Collection<? extends Player> players) {
        for (BlockPos mutablePos : BlockPos.betweenClosed(area.minX(), area.minY(), area.minZ(), area.maxX(), area.maxY(), area.maxZ())) {
            if (!isInWorld(level, mutablePos)) {
                continue;
            }

            BlockPos pos = mutablePos.immutable();
            BlockState state = level.getBlockState(pos);
            for (Player player : players) {
                if (MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(level, pos, state, player))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Swaps the area above {@code first} with the area above {@code second}. Chunks that are not loaded are loaded;
     * their entities are loaded asynchronously, so the entities of an unloaded area stay where they are.
     * <br>Does not check whether anyone may change the areas, see {@link #mayChange}.
     * @param radius radius of both areas, see {@link #getArea}
     * @throws IllegalArgumentException if the areas overlap
     */
    public static void swap(@NotNull ServerLevel level, @NotNull BlockPos first, @NotNull BlockPos second, int radius) {
        BoundingBox firstArea = getArea(first, radius);
        BoundingBox secondArea = getArea(second, radius);
        if (firstArea.intersects(secondArea)) {
            throw new IllegalArgumentException("The areas above " + first + " and " + second + " overlap");
        }
        BlockPos offset = second.subtract(first);

        // Collected before anything moves, so no entity is swapped twice
        boolean moveEntities = CommonConfig.TRANSPOSER_MOVE_ENTITIES.get();
        List<Entity> firstEntities = moveEntities ? collectEntities(level, firstArea) : List.of();
        List<Entity> secondEntities = moveEntities ? collectEntities(level, secondArea) : List.of();

        swapBlocks(level, firstArea, offset);

        BlockPos backOffset = BlockPos.ZERO.subtract(offset);
        firstEntities.forEach(entity -> teleport(level, entity, offset));
        secondEntities.forEach(entity -> teleport(level, entity, backOffset));

        playEffects(level, firstArea);
        playEffects(level, secondArea);
    }

    private static void swapBlocks(ServerLevel level, BoundingBox area, BlockPos offset) {
        // New content of every changed position
        Map<BlockPos, Snapshot> moved = new LinkedHashMap<>();
        for (BlockPos mutablePos : BlockPos.betweenClosed(area.minX(), area.minY(), area.minZ(), area.maxX(), area.maxY(), area.maxZ())) {
            BlockPos pos = mutablePos.immutable();
            BlockPos other = pos.offset(offset);
            if (canSwap(level, pos, other)) {
                moved.put(other, snapshot(level, pos));
                moved.put(pos, snapshot(level, other));
            }
        }

        // Without their block entities, the replaced blocks can't drop their contents
        moved.keySet().forEach(level::removeBlockEntity);
        moved.forEach((pos, snapshot) -> level.setBlock(pos, snapshot.state(), PLACE_FLAGS));
        moved.forEach((pos, snapshot) -> {
            if (snapshot.blockEntity() != null) {
                BlockState state = level.getBlockState(pos);
                BlockEntity blockEntity = BlockEntity.loadStatic(pos, state, snapshot.blockEntity());
                if (blockEntity != null) {
                    level.setBlockEntity(blockEntity);
                    // Also sends the block entity if the block itself did not change (e.g. two chests swapped)
                    level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
                }
            }
        });

        // The moved blocks adapt to their new neighbours (e.g. fences at the border, torches that lost their wall)...
        for (BlockPos pos : moved.keySet()) {
            BlockState state = level.getBlockState(pos);
            Block.updateOrDestroy(state, Block.updateFromNeighbourShapes(state, level, pos), level, pos, Block.UPDATE_ALL);
        }
        // ...and the neighbours to the moved blocks (shapes, redstone, falling blocks, ...)
        for (BlockPos pos : moved.keySet()) {
            BlockState state = level.getBlockState(pos);
            state.updateNeighbourShapes(level, pos, Block.UPDATE_ALL);
            level.updateNeighborsAt(pos, state.getBlock());
        }
    }

    private static boolean canSwap(ServerLevel level, BlockPos pos, BlockPos other) {
        if (!isInWorld(level, pos) || !isInWorld(level, other)) {
            return false;
        }

        BlockState state = level.getBlockState(pos);
        BlockState otherState = level.getBlockState(other);
        // Nothing would change
        if (state == otherState && !state.hasBlockEntity()) {
            return false;
        }
        return isMovable(level, pos, state) && isMovable(level, other, otherState);
    }

    private static boolean isInWorld(ServerLevel level, BlockPos pos) {
        return !level.isOutsideBuildHeight(pos) && level.getWorldBorder().isWithinBounds(pos);
    }

    private static boolean isMovable(ServerLevel level, BlockPos pos, BlockState state) {
        return state.getDestroySpeed(level, pos) >= 0 && !state.is(BLOCK_BLACKLIST);
    }

    private static Snapshot snapshot(ServerLevel level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return new Snapshot(level.getBlockState(pos), blockEntity == null ? null : blockEntity.saveWithId());
    }

    /**
     * @return the entities whose position is inside the area. Passengers are moved with their vehicle.
     */
    private static List<Entity> collectEntities(ServerLevel level, BoundingBox area) {
        AABB box = AABB.of(area);
        return level.getEntitiesOfClass(Entity.class, box, entity -> box.contains(entity.position()) && canTeleport(entity));
    }

    private static boolean canTeleport(Entity entity) {
        // Entities that can't use portals (e.g. the Ender Dragon) can't be transposed either
        return !(entity instanceof PartEntity<?>) && !entity.isPassenger() && !entity.isSpectator()
                && entity.canChangeDimensions() && !entity.getType().is(ENTITY_BLACKLIST);
    }

    private static void teleport(ServerLevel level, Entity entity, BlockPos offset) {
        if (!entity.isRemoved()) {
            entity.teleportTo(level, entity.getX() + offset.getX(), entity.getY() + offset.getY(), entity.getZ() + offset.getZ(),
                    Set.of(), entity.getYRot(), entity.getXRot());
        }
    }

    private static void playEffects(ServerLevel level, BoundingBox area) {
        Vec3 center = AABB.of(area).getCenter();
        double spread = area.getXSpan() / 2.0;
        level.playSound(null, center.x, center.y, center.z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.sendParticles(ParticleTypes.PORTAL, center.x, center.y, center.z, 32 * area.getXSpan(), spread, spread, spread, 0.5);
    }
}
