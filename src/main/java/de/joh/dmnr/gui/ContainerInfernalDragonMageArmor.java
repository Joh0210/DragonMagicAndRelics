package de.joh.dmnr.gui;

import com.mna.inventory.ItemInventoryBase;
import de.joh.dmnr.item.ItemInit;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class ContainerInfernalDragonMageArmor extends ContainerDragonMageArmor{
    public ContainerInfernalDragonMageArmor(int i, Inventory playerInventory) {
        this(i, playerInventory, new ItemInventoryBase(new ItemStack(ItemInit.INFERNAL_DRAGON_MAGE_CHESTPLATE.get(), 2), 2));
    }

    public ContainerInfernalDragonMageArmor(int i, Inventory playerInv, ItemInventoryBase basebag) {
        super(ContainerInit.INFERNAL_DRAGON_MAGE_CHESTPLATE, i, playerInv, basebag);
    }
}
