package de.joh.dmnr.common.block.entity;

import de.joh.dmnr.common.init.BlockEntitieInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

/**
 * BlockEntity of the Rift Emitter. It holds no data, it only exists for the RiftEmitterRenderer.
 * @see de.joh.dmnr.common.block.RiftEmitterBlock
 * @author Joh0210
 */
public class RiftEmitterBlockEntity extends BlockEntity {
    public RiftEmitterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitieInit.RIFT_EMITTER_ENTITY.get(), pPos, pBlockState);
    }

    /**
     * The bottle on the table reaches above the block.
     */
    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(worldPosition).expandTowards(0, 0.5, 0);
    }
}
