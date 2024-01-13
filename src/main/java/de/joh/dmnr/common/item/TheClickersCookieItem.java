package de.joh.dmnr.common.item;

import de.joh.dmnr.api.util.CreativeModeTab;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * Spawns a Cookie in 2 Blocks Radius when Clicked
 * <br>Is a Relict
 * @author Joh0210
 */
public class TheClickersCookieItem extends Item {
    public TheClickersCookieItem() {
        super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC).tab(CreativeModeTab.CreativeModeTab));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player user, @NotNull InteractionHand hand) {
        InteractionResultHolder<ItemStack> ar = super.use(world, user, hand);
        user.getCooldowns().addCooldown(this, 10);

        //Random Pos:
        for(int i = 0; i < 6; i++){
            Random random = new Random();
            int x = random.nextInt(5) - 2;
            int y = random.nextInt(5) - 2;
            int z = random.nextInt(4) - 1;
            if (!world.isEmptyBlock(new BlockPos(user.getX()+x, user.getY()+y, user.getZ()+z))) {
                ItemEntity item = new ItemEntity(world, user.getX()+x, user.getY()+y, user.getZ()+z, new ItemStack(Items.COOKIE));
                world.addFreshEntity(item);
                return ar;  // Only Spawn 1
            }
        }

        // tries to Force:
        for (int xOffset = -2; xOffset <= 2; xOffset++) {
            for (int yOffset = -2; yOffset <= 2; yOffset++) {
                for (int zOffset = -1; zOffset <= 2; zOffset++) {
                    if (world.isEmptyBlock(new BlockPos(user.getX()+xOffset, user.getY()+yOffset, user.getZ()+zOffset))) {
                        ItemEntity item = new ItemEntity(world, user.getX()+xOffset, user.getY()+yOffset, user.getZ()+zOffset, new ItemStack(Items.COOKIE));
                        world.addFreshEntity(item);
                        return ar;  // Only Spawn 1
                    }
                }
            }
        }

        return ar;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@NotNull ItemStack itemStack){
        return true;
    }
}
