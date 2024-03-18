package de.joh.dmnr.client.gui;

import com.mna.inventory.ItemInventoryBase;
import de.joh.dmnr.common.init.ItemInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class ContainerRingOfNormalSpellStoring extends ContainerRingOfSpellStoring {
    public ContainerRingOfNormalSpellStoring(int i, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(i, playerInventory, new ItemInventoryBase(new ItemStack(ItemInit.RING_OF_SPELL_STORING.get(), 1), 2));
    }

    public ContainerRingOfNormalSpellStoring(int i, Inventory playerInv, ItemInventoryBase basebag) {
        super(i, playerInv, basebag);
    }
}
