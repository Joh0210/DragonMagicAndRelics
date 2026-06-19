package de.joh.dmnr.client.gui;

import com.mna.inventory.ItemInventoryBase;
import de.joh.dmnr.client.init.ContainerInit;
import de.joh.dmnr.common.init.ItemInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class ContainerRingOfRuling extends ContainerSpellRingBase {
    public ContainerRingOfRuling(int i, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(i, playerInventory, new ItemInventoryBase(new ItemStack(ItemInit.RING_OF_RULING.get(), 1), 1));
    }

    public ContainerRingOfRuling(int i, Inventory playerInv, ItemInventoryBase basebag) {
        super(ContainerInit.RING_OF_RULING.get(), i, playerInv, basebag);
    }
}
