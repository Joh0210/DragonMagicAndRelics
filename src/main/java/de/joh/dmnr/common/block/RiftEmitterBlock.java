package de.joh.dmnr.common.block;

import com.mna.gui.containers.providers.NamedRift;
import de.joh.dmnr.common.init.BlockEntitieInit;
import de.joh.dmnr.common.init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * This block gives access to the Rift inventory.
 * @see com.mna.spells.components.ComponentRift
 * @author Joh0210
 */
public class RiftEmitterBlock extends BaseEntityBlock {
    public RiftEmitterBlock(Properties pProperties) {
        super(pProperties);
    }

    /**
     * Interaction with the block to open the rift
     */
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState blockstate, @NotNull Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        super.use(blockstate, world, pos, player, hand, hit);
        if (!player.level.isClientSide) {
            NetworkHooks.openScreen((ServerPlayer)player, new NamedRift());
        }

        return InteractionResult.SUCCESS;
    }

    /**
     * Makes the block drop the appropriate item.
     */
    @Override
    public @NotNull List<ItemStack> getDrops(@NotNull BlockState state, LootContext.@NotNull Builder builder) {
        return Collections.singletonList(new ItemStack(ItemInit.RIFT_EMITTER_ITEM.get()));
    }

    /**
     * Instead of the block, a BlockEntity is created that uses the GeckoLib model.
     */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return BlockEntitieInit.RIFT_EMITTER_ENTITY.get().create(pPos, pState);
    }

    /**
     * Instead of the block, a BlockEntity is created that uses the GeckoLib model.
     */
    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}
