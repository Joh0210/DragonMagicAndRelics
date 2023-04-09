package de.joh.dragonmagicandrelics.item.items;

import com.mna.api.items.TieredItem;
import com.mna.blocks.BlockInit;
import com.mna.blocks.ritual.ChalkRuneBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;

public class BrimstoneChalk extends TieredItem {

    public BrimstoneChalk(Properties itemProperties) {
        super(itemProperties);
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    public int getItemEnchantability(ItemStack stack) {
        return 0;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    public boolean isEnchantable(ItemStack p_77616_1_) {
        return false;
    }

    public InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide()) {
            if (context.getLevel().getBlockState(context.getClickedPos()).isFaceSturdy(context.getLevel(), context.getClickedPos(), Direction.UP) && context.getLevel().isEmptyBlock(context.getClickedPos().above())) {
                context.getLevel().setBlockAndUpdate(context.getClickedPos().above(), BlockInit.CHALK_RUNE.get().defaultBlockState().setValue(ChalkRuneBlock.RUNEINDEX, (int)Math.floor(Math.random() * (double)(ChalkRuneBlock.RUNEINDEX.getPossibleValues().size() - 1))).setValue(ChalkRuneBlock.METAL, false).setValue(ChalkRuneBlock.ACTIVATED, false));
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            return InteractionResult.SUCCESS;
        }
    }
}
