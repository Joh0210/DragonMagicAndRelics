package de.joh.dmnr.compat.create.block;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Base of the Create blocks of this mod that generate rotation themselves: a spinning shaft whose sides grip like a
 * small cogwheel. The rotor is drawn by the block entity renderer.
 * @param <T> block entity of the block, which generates the rotation
 * @see SpectralMotorBlock
 * @see EngineBlock
 * @author Joh0210
 */
public abstract class AbstractMotorBlock<T extends GeneratingKineticBlockEntity> extends RotatedPillarKineticBlock implements IBE<T>, ICogWheel, ProperWaterloggedBlock {
    protected AbstractMotorBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(WATERLOGGED));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return withWater(defaultBlockState().setValue(AXIS, getAxisForPlacement(context)), context);
    }

    /**
     * Like a cogwheel, the motor meshes with a small cogwheel it is placed against and connects to adjacent shafts.
     * Otherwise its shaft points into the targeted block.
     */
    private Direction.Axis getAxisForPlacement(BlockPlaceContext context) {
        BlockState placedAgainst = context.getLevel().getBlockState(context.getClickedPos().relative(context.getClickedFace().getOpposite()));
        if (ICogWheel.isSmallCog(placedAgainst)) {
            return ((IRotate) placedAgainst.getBlock()).getRotationAxis(placedAgainst);
        }

        Direction.Axis preferredAxis = getPreferredAxis(context);
        return preferredAxis != null ? preferredAxis : context.getClickedFace().getAxis();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return CogWheelBlock.isValidCogwheelPosition(false, level, pos, state.getValue(AXIS));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AllShapes.SMALL_GEAR.get(state.getValue(AXIS));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return fluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level,
                                  BlockPos pos, BlockPos neighborPos) {
        updateWater(level, state, pos);
        return state;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    // IRotate:

    @Override
    public boolean hasShaftTowards(LevelReader level, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(AXIS);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(AXIS);
    }

    @Override
    public boolean hideStressImpact() {
        return true;
    }
}
