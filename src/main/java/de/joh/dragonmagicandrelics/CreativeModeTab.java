package de.joh.dragonmagicandrelics;

import de.joh.dragonmagicandrelics.item.ItemInit;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

/**
 * Creation of a creative tap for the items of this mod.
 * Called when the constructor of this mod's items is called.
 */
public class CreativeModeTab extends ItemGroup {
    public static final ItemGroup CreativeModeTab = new CreativeModeTab("itemGroup.dragonmagicandrelicstab");
    

    private CreativeModeTab(String label) {
        super(label);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ItemInit.INFERNAL_DRAGON_MAGE_CHESTPLATE.get());
    }
}
