package de.joh.dragonmagicandrelics.gui;

import com.mna.gui.containers.HeldContainerBase;
import com.mna.gui.containers.slots.BaseSlot;
import com.mna.gui.containers.slots.ItemFilterSlot;
import com.mna.inventory.ItemInventoryBase;
import com.mna.items.filters.ItemFilterGroup;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ContainerDragonMageArmor extends HeldContainerBase {
    public ContainerDragonMageArmor(@Nullable MenuType<?> type, int i, Inventory playerInv, ItemInventoryBase basebag) {
        super(type, i, playerInv, basebag);
    }

    protected void initializeSlots(Inventory playerInv) {
        this.addSlot(this.slot(this.inventory, 0, 4+24, 100));
        this.addSlot(this.slot(this.inventory, 1, 104+24, 100));
        int slotIndex = 2;

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
        return (new ItemFilterSlot(inv, index, x, y, ItemFilterGroup.ANY_SPELL)).setMaxStackSize(1);
    }

    protected int slotsPerRow() {
        return 1;
    }

    protected int numRows() {
        return 2;
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
