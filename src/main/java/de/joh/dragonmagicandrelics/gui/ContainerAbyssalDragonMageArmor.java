package de.joh.dragonmagicandrelics.gui;

import com.mna.inventory.ItemInventoryBase;
import de.joh.dragonmagicandrelics.item.ItemInit;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class ContainerAbyssalDragonMageArmor extends ContainerDragonMageArmor{
    public ContainerAbyssalDragonMageArmor(int i, Inventory playerInventory) {
        this(i, playerInventory, new ItemInventoryBase(new ItemStack(ItemInit.ABYSSAL_DRAGON_MAGE_CHESTPLATE.get(), 2), 2));
    }

    public ContainerAbyssalDragonMageArmor(int i, Inventory playerInv, ItemInventoryBase basebag) {
        super(ContainerInit.ABYSSAL_DRAGON_MAGE_CHESTPLATE, i, playerInv, basebag);
    }
}
