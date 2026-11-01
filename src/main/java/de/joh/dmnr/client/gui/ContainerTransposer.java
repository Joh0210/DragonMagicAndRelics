package de.joh.dmnr.client.gui;

import de.joh.dmnr.client.init.ContainerInit;
import de.joh.dmnr.common.block.entity.TransposerBlockEntity;
import de.joh.dmnr.common.block.entity.TransposerStatus;
import de.joh.dmnr.common.init.BlockInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

/**
 * Menu of the Transposer: a slot for the Rune of Marking, a slot for the Ritual Focus and the inventory of the player.
 * <br>The status of the Transposer and the size of its area are synchronized as {@link ContainerData}, since only the
 * server can check the other Transposer and the size depends on the config of the server.
 * @see GuiTransposer
 * @see TransposerBlockEntity
 * @author Joh0210
 */
public class ContainerTransposer extends AbstractContainerMenu {
    // Positions of the slots, shared with the GUI
    public static final int RUNE_SLOT_X = 17;
    public static final int RUNE_SLOT_Y = 23;
    public static final int FOCUS_SLOT_X = 17;
    public static final int FOCUS_SLOT_Y = 47;
    public static final int INVENTORY_X = 0;
    public static final int INVENTORY_Y = 116;

    private static final int INVENTORY_START = TransposerBlockEntity.SLOT_COUNT;
    private static final int INVENTORY_END = INVENTORY_START + 36;

    private final ContainerData data;
    private final ContainerLevelAccess access;

    public ContainerTransposer(int id, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(id, playerInventory, TransposerBlockEntity.createInventory(() -> {}),
                new SimpleContainerData(TransposerBlockEntity.DATA_COUNT), ContainerLevelAccess.NULL);
    }

    public ContainerTransposer(int id, Inventory playerInventory, IItemHandler inventory, ContainerData data, ContainerLevelAccess access) {
        super(ContainerInit.TRANSPOSER.get(), id);
        this.data = data;
        this.access = access;

        addSlot(new SlotItemHandler(inventory, TransposerBlockEntity.SLOT_RUNE, RUNE_SLOT_X, RUNE_SLOT_Y));
        addSlot(new SlotItemHandler(inventory, TransposerBlockEntity.SLOT_FOCUS, FOCUS_SLOT_X, FOCUS_SLOT_Y));

        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                addSlot(new Slot(playerInventory, column + row * 9 + 9, INVENTORY_X + 8 + column * 18, INVENTORY_Y + 8 + row * 18));
            }
        }
        for (int column = 0; column < 9; ++column) {
            addSlot(new Slot(playerInventory, column, INVENTORY_X + 8 + column * 18, INVENTORY_Y + 66));
        }

        addDataSlots(data);
    }

    public @NotNull TransposerStatus getStatus() {
        return TransposerStatus.byOrdinal(data.get(TransposerBlockEntity.DATA_STATUS));
    }

    /**
     * @return edge length of the area, 0 if there is no area (no Ritual Focus, and the config requires one)
     */
    public int getAreaSize() {
        return data.get(TransposerBlockEntity.DATA_AREA_SIZE);
    }

    public @NotNull ItemStack getRune() {
        return getSlot(TransposerBlockEntity.SLOT_RUNE).getItem();
    }

    public @NotNull ItemStack getFocus() {
        return getSlot(TransposerBlockEntity.SLOT_FOCUS).getItem();
    }

    /**
     * Moves runes and foci from the inventory of the player into their slot, and both back into the inventory.
     */
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        Slot slot = slots.get(pIndex);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (pIndex < INVENTORY_START) {
            if (!moveItemStackTo(stack, INVENTORY_START, INVENTORY_END, true)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(stack, 0, INVENTORY_START, false)) {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        if (stack.getCount() == original.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTake(pPlayer, stack);
        return original;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(access, pPlayer, BlockInit.TRANSPOSER.get());
    }
}
