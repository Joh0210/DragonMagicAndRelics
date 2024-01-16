package de.joh.dmnr.client.gui;

import com.mna.gui.containers.HeldContainerBase;
import com.mna.gui.containers.slots.BaseSlot;
import com.mna.gui.containers.slots.ItemFilterSlot;
import com.mna.inventory.ItemInventoryBase;
import de.joh.dmnr.client.init.ContainerInit;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.api.util.PlayerCharmFilter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class ContainerBraceletOfFriendship extends HeldContainerBase {
    public ContainerBraceletOfFriendship(int i, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(i, playerInventory, new ItemInventoryBase(new ItemStack(ItemInit.BRACELET_OF_FRIENDSHIP.get(), 6), 6));
    }

    public ContainerBraceletOfFriendship(int i, Inventory playerInv, ItemInventoryBase basebag) {
        super(ContainerInit.BRACELET_OF_FRIENDSHIP.get(), i, playerInv, basebag);
    }

    protected void initializeSlots(Inventory playerInv) {
        this.addSlot(this.slot(this.inventory, 0, 7+24, 7));
        this.addSlot(this.slot(this.inventory, 1, 104+24, 7));
        this.addSlot(this.slot(this.inventory, 2, 30+24, 46));
        this.addSlot(this.slot(this.inventory, 3, 82+24, 46));
        this.addSlot(this.slot(this.inventory, 4, 7+24, 103));
        this.addSlot(this.slot(this.inventory, 5, 104+24, 103));
        int slotIndex = 6;

        int xpos;
        for(xpos = 0; xpos < 3; ++xpos) {
            for(int ypos = 0; ypos < 9; ++ypos) {
                ++slotIndex;
                this.addSlot(new Slot(playerInv, ypos + xpos * 9 + 9, 8 + ypos * 18, 140 + xpos * 18));
            }
        }

        for(xpos = 0; xpos < 9; ++xpos) {
            if (xpos == playerInv.selected) {
                this.mySlot = slotIndex;
            }

            this.addSlot(new Slot(playerInv, xpos, 8 + xpos * 18, 198));
            ++slotIndex;
        }
    }

    public BaseSlot slot(IItemHandler inv, int index, int x, int y) {
        return (new ItemFilterSlot(inv, index, x, y, PlayerCharmFilter.ANY_PLAYER_CHARM)).setMaxStackSize(1);
    }

    protected int slotsPerRow() {
        return 1;
    }

    protected int numRows() {
        return 6;
    }

    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        try {
            if (this.getSlot(slotId).getItem().hashCode() == this.bagHash) {
                return;
            }
        } catch (Exception var6) {
        }

        super.clicked(slotId, dragType, clickTypeIn, player);
    }

    @Nonnull
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < this.numRows() * this.slotsPerRow()) {
                if (!this.moveItemStackTo(itemstack1, this.numRows() * this.slotsPerRow(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.numRows() * this.slotsPerRow(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }
}
