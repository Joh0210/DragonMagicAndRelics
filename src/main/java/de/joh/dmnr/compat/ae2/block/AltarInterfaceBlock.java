package de.joh.dmnr.compat.ae2.block;

import appeng.block.AEBaseEntityBlock;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocators;
import de.joh.dmnr.compat.ae2.AE2Init;
import de.joh.dmnr.compat.ae2.blockentity.AltarInterfaceBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Connects an Altar of Arcana to the ME network, similar to the ME Interface: The block supplies the altar with the
 * items it needs. The altar is chosen by inserting a Rune of Marking that is bound to it.
 * @see AltarInterfaceBlockEntity
 * @author Joh0210
 */
public class AltarInterfaceBlock extends AEBaseEntityBlock<AltarInterfaceBlockEntity> {
    public AltarInterfaceBlock() {
        super(metalProps());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("item.dmnr.altar_me_interface.description").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult onActivated(Level level, BlockPos pos, Player player, InteractionHand hand,
                                         @Nullable ItemStack heldItem, BlockHitResult hit) {
        // Sneaking is used by AE2 for wrenches and similar tools.
        if (player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

        AltarInterfaceBlockEntity be = getBlockEntity(level, pos);
        if (be == null) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide()) {
            MenuOpener.open(AE2Init.ALTAR_INTERFACE_MENU.get(), player, MenuLocators.forBlockEntity(be));
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);

        AltarInterfaceBlockEntity be = getBlockEntity(level, pos);
        if (be != null) {
            be.getLogic().onNeighborChanged();
        }
    }
}
