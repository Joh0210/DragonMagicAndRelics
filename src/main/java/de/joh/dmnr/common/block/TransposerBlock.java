package de.joh.dmnr.common.block;

import com.mna.api.affinity.Affinity;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import de.joh.dmnr.common.block.entity.TransposerBlockEntity;
import de.joh.dmnr.common.init.BlockEntitieInit;
import de.joh.dmnr.common.util.TranspositionHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * When this block receives a redstone signal, it swaps the blocks and entities above it with those above another
 * Transposer. The other Transposer is chosen with a Rune of Marking, the size of the area with an optional Ritual
 * Focus (a single block without one); both are inserted through the GUI of the block.
 * <br>The block shows its content: the seal on top and the channel on the sides glow while a bound rune is inserted,
 * the points at the bottom of the sides show the tier of the Ritual Focus. The top is usually covered by the swapped
 * blocks, so the sides carry everything that matters.
 * @see TransposerBlockEntity
 * @author Joh0210
 */
public class TransposerBlock extends Block implements EntityBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    /** Is a Rune of Marking inserted that is bound to another position? */
    public static final BooleanProperty LINKED = BooleanProperty.create("linked");
    /** Tier of the inserted Ritual Focus: 0 none, 1 minor, 2 lesser, 3 greater */
    public static final IntegerProperty FOCUS = IntegerProperty.create("focus", 0, 3);

    /** Block event sent to both Transposers after a transposition, its parameter is the radius of the area. */
    public static final int EVENT_TRANSPOSED = 0;

    // Colors of the swap seal, used for the particles
    private static final int[] CYAN = {107, 215, 233};
    private static final int[] VIOLET = {166, 120, 241};

    public TransposerBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(stateDefinition.any().setValue(POWERED, false).setValue(LINKED, false).setValue(FOCUS, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(POWERED, LINKED, FOCUS);
    }

    /**
     * A Transposer placed next to an active redstone signal does not swap until the signal is turned on again.
     */
    @Override
    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        return defaultBlockState().setValue(POWERED, pContext.getLevel().hasNeighborSignal(pContext.getClickedPos()));
    }

    /**
     * The Transposer remembers who placed it, for the protection checks and the same-owner rule of the config.
     */
    @Override
    public void setPlacedBy(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @Nullable LivingEntity pPlacer, @NotNull ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        if (!pLevel.isClientSide() && pPlacer instanceof Player player && pLevel.getBlockEntity(pPos) instanceof TransposerBlockEntity transposer) {
            transposer.setOwner(player.getGameProfile());
        }
    }

    /**
     * Only a rising redstone signal activates the Transposer. Like the dispenser, it swaps in a scheduled tick instead
     * of during the block updates.
     * <br>Signals that change while the Transposer takes part in a transposition don't activate it, see
     * {@link TransposerBlockEntity#transpose}.
     */
    @Override
    public void neighborChanged(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Block pNeighborBlock, @NotNull BlockPos pNeighborPos, boolean pMovedByPiston) {
        if (pLevel.isClientSide()) {
            return;
        }

        boolean transposing = pLevel.getBlockEntity(pPos) instanceof TransposerBlockEntity transposer && transposer.isTransposing();
        updatePowered(pLevel, pPos, pState, !transposing);
    }

    /**
     * Adopts the current redstone signal of the Transposer.
     * @param canActivate may a rising signal activate the Transposer? If not, it only remembers the signal, so it
     *                    needs a new rising signal to be activated.
     */
    public static void updatePowered(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, boolean canActivate) {
        boolean powered = level.hasNeighborSignal(pos);
        if (powered != state.getValue(POWERED)) {
            if (powered && canActivate) {
                level.scheduleTick(pos, state.getBlock(), 1);
            }
            level.setBlock(pos, state.setValue(POWERED, powered), Block.UPDATE_INVISIBLE);
        }
    }

    @Override
    public void tick(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
        if (pLevel.getBlockEntity(pPos) instanceof TransposerBlockEntity transposer) {
            transposer.transpose();
        }
    }

    /**
     * After a transposition, the server sends {@link #EVENT_TRANSPOSED} to both Transposers, so a single packet
     * shows the effect to every player nearby.
     */
    @Override
    public boolean triggerEvent(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, int pId, int pParam) {
        if (pId != EVENT_TRANSPOSED) {
            return super.triggerEvent(pState, pLevel, pPos, pId, pParam);
        }
        if (pLevel.isClientSide()) {
            spawnTranspositionParticles(pLevel, pPos, pParam);
        }
        return true;
    }

    /**
     * Sparkles in the colors of the seal trace the edges of the swapped area, so its size is visible, and ender
     * particles fill it.
     * @param radius radius of the area, see {@link TranspositionHelper#getArea}
     */
    private static void spawnTranspositionParticles(@NotNull Level level, @NotNull BlockPos pos, int radius) {
        BoundingBox area = TranspositionHelper.getArea(pos, radius);
        double[] min = {area.minX(), area.minY(), area.minZ()};
        double[] max = {area.maxX() + 1, area.maxY() + 1, area.maxZ() + 1};
        int size = 2 * radius + 1;
        RandomSource random = level.getRandom();

        MAParticleType cyan = new MAParticleType(ParticleInit.SPARKLE_VELOCITY.get()).setColor(CYAN[0], CYAN[1], CYAN[2]);
        MAParticleType violet = new MAParticleType(ParticleInit.SPARKLE_VELOCITY.get()).setColor(VIOLET[0], VIOLET[1], VIOLET[2]);
        double[] particle = new double[3];
        for (int axis = 0; axis < 3; axis++) {
            // The four edges parallel to the axis: the two bits of the corner pick min or max on the other axes
            for (int corner = 0; corner < 4; corner++) {
                for (int i = 0; i < 3 * size; i++) {
                    for (int other = 0, bit = 0; other < 3; other++) {
                        particle[other] = other == axis
                                ? min[other] + random.nextDouble() * size
                                : ((corner >> bit++) & 1) == 0 ? min[other] : max[other];
                    }
                    level.addParticle(random.nextBoolean() ? cyan : violet, particle[0], particle[1], particle[2], 0, 0.02, 0);
                }
            }
        }

        MAParticleType ender = new MAParticleType(ParticleInit.ENDER_VELOCITY.get()).setColor(Affinity.ENDER);
        for (int i = 0; i < 8 + 2 * size * size; i++) {
            level.addParticle(ender, min[0] + random.nextDouble() * size, min[1] + random.nextDouble() * size, min[2] + random.nextDouble() * size,
                    (random.nextDouble() - 0.5) * 0.04, 0.03, (random.nextDouble() - 0.5) * 0.04);
        }
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        if (pPlayer instanceof ServerPlayer serverPlayer && pLevel.getBlockEntity(pPos) instanceof TransposerBlockEntity transposer) {
            NetworkHooks.openScreen(serverPlayer, transposer);
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Override
    public void onRemove(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pNewState, boolean pMovedByPiston) {
        if (!pState.is(pNewState.getBlock()) && pLevel.getBlockEntity(pPos) instanceof TransposerBlockEntity transposer) {
            transposer.dropContents();
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable BlockGetter pLevel, @NotNull List<Component> pTooltip, @NotNull TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        pTooltip.add(Component.translatable("item.dmnr.transposer.description").withStyle(ChatFormatting.GRAY));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return BlockEntitieInit.TRANSPOSER_ENTITY.get().create(pPos, pState);
    }
}
