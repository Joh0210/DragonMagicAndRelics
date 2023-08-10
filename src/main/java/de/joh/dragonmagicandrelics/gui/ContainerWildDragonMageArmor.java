package de.joh.dragonmagicandrelics.gui;

import com.mna.inventory.ItemInventoryBase;
import de.joh.dragonmagicandrelics.item.ItemInit;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class ContainerWildDragonMageArmor extends ContainerDragonMageArmor{
    public ContainerWildDragonMageArmor(int i, Inventory playerInventory) {
        this(i, playerInventory, new ItemInventoryBase(new ItemStack(ItemInit.WILD_DRAGON_MAGE_CHESTPLATE.get(), 2), 2));
    }

    public ContainerWildDragonMageArmor(int i, Inventory playerInv, ItemInventoryBase basebag) {
        super(ContainerInit.WILD_DRAGON_MAGE_CHESTPLATE, i, playerInv, basebag);
    }
}
