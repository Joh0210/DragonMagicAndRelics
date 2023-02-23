package de.joh.dragonmagicandrelics.block;

import com.mna.gui.containers.providers.NamedRift;
import de.joh.dragonmagicandrelics.block.entity.BlockEntitieInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class RiftEmitter extends BaseEntityBlock {
    public RiftEmitter(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState blockstate, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        super.use(blockstate, world, pos, player, hand, hit);
        if (!player.level.isClientSide) {
            NetworkHooks.openGui((ServerPlayer)player, new NamedRift());
        }

        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntitieInit.RIFT_EMITTER_ENTITY.get().create(pPos, pState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}
