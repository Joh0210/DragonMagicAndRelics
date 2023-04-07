package de.joh.dragonmagicandrelics;

import de.joh.dragonmagicandrelics.item.ItemInit;
import net.minecraft.world.item.ItemStack;

/**
 * Creation of a creative tap for the items of this mod.
 * Called when the constructor of this mod's items is called.
 */
public class CreativeModeTab {
    public static final net.minecraft.world.item.CreativeModeTab CreativeModeTab = new net.minecraft.world.item.CreativeModeTab("dragonmagicandrelics"){
        @Override
        public ItemStack makeIcon(){
            return new ItemStack(ItemInit.INFERNAL_DRAGON_MAGE_HELMET.get());
        }
    };
}
