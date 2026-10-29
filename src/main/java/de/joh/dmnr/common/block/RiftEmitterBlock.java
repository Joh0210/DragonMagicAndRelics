package de.joh.dmnr.common.block;

import com.mna.api.blocks.WizardLabBlock;
import com.mna.gui.containers.providers.NamedRift;
import de.joh.dmnr.common.init.BlockEntitieInit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * This block gives access to the Rift inventory.
 * <br> It is a table of the M&A wizard lab: it faces the player who places it and connects to the neighbouring wizard
 * lab blocks (Inscription Table, Thesis Desk, Altar of Arcana, ...) on its left and right.
 * @see com.mna.spells.components.ComponentRift
 * @author Joh0210
 */
public class RiftEmitterBlock extends WizardLabBlock implements EntityBlock {
    public RiftEmitterBlock(Properties pProperties) {
        super(pProperties);
    }

    /**
     * Interaction with the block opens the Rift
     */
    @Override
    protected MenuProvider getProvider(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return new NamedRift();
    }

    /**
     * The Rift menu does not need any data of the block.
     */
    @Override
    protected Consumer<FriendlyByteBuf> getContainerBufferWriter(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return buffer -> {};
    }

    /**
     * The BlockEntity only exists for the RiftEmitterRenderer (vortex and content of the bottle).
     */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return BlockEntitieInit.RIFT_EMITTER_ENTITY.get().create(pPos, pState);
    }
}
