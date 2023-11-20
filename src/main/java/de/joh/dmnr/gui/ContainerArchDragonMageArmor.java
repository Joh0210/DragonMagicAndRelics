package de.joh.dmnr.gui;

import com.mna.inventory.ItemInventoryBase;
import de.joh.dmnr.item.ItemInit;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class ContainerArchDragonMageArmor extends ContainerDragonMageArmor{
    public ContainerArchDragonMageArmor(int i, Inventory playerInventory) {
        this(i, playerInventory, new ItemInventoryBase(new ItemStack(ItemInit.ARCH_DRAGON_MAGE_CHESTPLATE.get(), 2), 2));
    }

    public ContainerArchDragonMageArmor(int i, Inventory playerInv, ItemInventoryBase basebag) {
        super(ContainerInit.ARCH_DRAGON_MAGE_CHESTPLATE, i, playerInv, basebag);
    }
}
