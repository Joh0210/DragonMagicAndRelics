package de.joh.dmnr.common.item;

import com.mna.api.items.TieredItem;
import de.joh.dmnr.api.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.NotNull;


/**
 * A powder that converts minecraft plants into other minecraft plants. The Wither Rose cannot mutate.
 * @author Joh0210
 */
public class MutandisItem extends TieredItem {
    private final boolean isPurified;

    public MutandisItem(boolean isPurified, Properties pProperties) {
        super(pProperties);
        this.isPurified = isPurified;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide()) {
            if (context.getLevel().getBlockState(context.getClickedPos()).getBlock() != Blocks.WITHER_ROSE && ModTags.isBlockIn(context.getLevel().getBlockState(context.getClickedPos()).getBlock(), ModTags.Blocks.MUTANDIS_PLANTS)) {
                BlockPos blockPos = context.getClickedPos();
                if(ModTags.isBlockIn(context.getLevel().getBlockState(blockPos).getBlock(), ModTags.Blocks.TALL_FLOWERS)){
                    if (ModTags.isBlockIn(context.getLevel().getBlockState(blockPos.below()).getBlock(), ModTags.Blocks.TALL_FLOWERS)){
                        blockPos = blockPos.below();
                    }
                }

                Block block;
                do {
                    block = ModTags.getRandomBlock(isPurified ? ModTags.Blocks.MNA_FLOWERS : ModTags.Blocks.MUTANDIS_PLANTS);
                    if(block == null){
                        return InteractionResult.FAIL;
                    }
                } while (block == Blocks.WITHER_ROSE || (ModTags.isBlockIn(block, ModTags.Blocks.TALL_FLOWERS)
                                && !(context.getLevel().getBlockState(blockPos.above()).getBlock() == Blocks.AIR
                                || context.getLevel().getBlockState(blockPos.above()).getBlock() == Blocks.CAVE_AIR)));

                context.getLevel().setBlockAndUpdate(blockPos, block.defaultBlockState());
                if(ModTags.isBlockIn(block, ModTags.Blocks.TALL_FLOWERS)){
                    context.getLevel().setBlockAndUpdate(blockPos.above(), block.defaultBlockState().setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER));
                }
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            if (context.getLevel().getBlockState(context.getClickedPos()).getBlock() != Blocks.WITHER_ROSE && ModTags.isBlockIn(context.getLevel().getBlockState(context.getClickedPos()).getBlock(), ModTags.Blocks.MUTANDIS_PLANTS)) {
                BoneMealItem.addGrowthParticles(context.getLevel(), context.getClickedPos(), 8);
            }
            return InteractionResult.SUCCESS;
        }
    }

}
