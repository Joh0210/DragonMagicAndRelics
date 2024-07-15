package de.joh.dmnr.api.item;

import de.joh.dmnr.DragonMagicAndRelics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Allows the user to Change the Weather
 * <br>Is a Relict
 * @author Joh0210
 */
public interface ScrollableItem {
    int getIteratorSize(Player player);

    default int incrementIterator(ItemStack stack, boolean inverted, Player player){
        if(!player.level().isClientSide()){
            AtomicInteger wildMagicIterator = new AtomicInteger(0);
            AtomicBoolean isInverted = new AtomicBoolean(inverted);
            if(stack.getTag() != null && stack.getTag().contains(DragonMagicAndRelics.MOD_ID + "_iterator")){
                wildMagicIterator.set(stack.getTag().getInt(DragonMagicAndRelics.MOD_ID + "_iterator"));
                stack.getTag().remove(DragonMagicAndRelics.MOD_ID + "_iterator");
            }

            wildMagicIterator.set(adjustWildMagicIterator(wildMagicIterator.get() + (isInverted.get() ? -1 : +1), player));

            CompoundTag nbtData = new CompoundTag();
            nbtData.putInt(DragonMagicAndRelics.MOD_ID + "_iterator", wildMagicIterator.get());
            if(stack.getTag() == null){
                stack.setTag(nbtData);
            } else {
                stack.getTag().merge(nbtData);
            }

            return stack.getTag().getInt(DragonMagicAndRelics.MOD_ID + "_iterator");
        }
        return 0;
    }

    default int getIterator(ItemStack stack){
        if(stack.getTag() == null){
            return 0;
        }
        else {
            return stack.getTag().getInt(DragonMagicAndRelics.MOD_ID + "_iterator");
        }
    }

    default int adjustWildMagicIterator(int iterator, Player player){
        int iteratorSize = getIteratorSize(player);
        if(iteratorSize <= 0){
            return 0;
        }
        else {
            while (iterator < 0){
                iterator += iteratorSize;
            }

            return iterator % iteratorSize;
        }
    }
}
