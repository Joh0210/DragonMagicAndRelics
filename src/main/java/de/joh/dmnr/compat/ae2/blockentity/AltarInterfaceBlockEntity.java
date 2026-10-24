package de.joh.dmnr.compat.ae2.blockentity;

import appeng.api.inventories.InternalInventory;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.IUpgradeableObject;
import appeng.api.util.IConfigManager;
import appeng.api.util.IConfigurableObject;
import appeng.blockentity.grid.AENetworkBlockEntity;
import de.joh.dmnr.compat.ae2.AE2Init;
import de.joh.dmnr.compat.ae2.logic.AltarInterfaceLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Block entity of the {@link de.joh.dmnr.compat.ae2.block.AltarInterfaceBlock}: a node of the ME network, which
 * supplies the Altar of Arcana of its Rune of Marking with items from the network.
 * @see AltarInterfaceLogic
 * @author Joh0210
 */
public class AltarInterfaceBlockEntity extends AENetworkBlockEntity implements IUpgradeableObject, IConfigurableObject {
    private final AltarInterfaceLogic logic = new AltarInterfaceLogic(getMainNode(), this);
    /**
     * The altar remembers the item handlers of the inventories it found, so there must be one instance for the whole
     * lifetime of the block entity.
     */
    private final IItemHandler altarItemHandler = new ExtractOnlyItemHandler(logic.getStorage().toItemHandler());
    private LazyOptional<IItemHandler> itemHandlerCapability = LazyOptional.of(() -> altarItemHandler);

    public AltarInterfaceBlockEntity(BlockPos pos, BlockState state) {
        super(AE2Init.ALTAR_INTERFACE_ENTITY.get(), pos, state);
    }

    public @NotNull AltarInterfaceLogic getLogic() {
        return logic;
    }

    @Override
    public @NotNull IUpgradeInventory getUpgrades() {
        return logic.getUpgrades();
    }

    @Override
    public @NotNull IConfigManager getConfigManager() {
        return logic.getConfigManager();
    }

    @Override
    public void saveAdditional(CompoundTag data) {
        super.saveAdditional(data);
        logic.writeToNBT(data);
    }

    @Override
    public void loadTag(CompoundTag data) {
        super.loadTag(data);
        logic.readFromNBT(data);
    }

    @Override
    public void addAdditionalDrops(Level level, BlockPos pos, List<ItemStack> drops) {
        super.addAdditionalDrops(level, pos, drops);
        logic.addDrops(drops);
    }

    @Override
    public void clearContent() {
        super.clearContent();
        logic.clearContent();
    }

    @Override
    public @Nullable InternalInventory getSubInventory(ResourceLocation id) {
        if (id.equals(UPGRADES)) {
            return logic.getUpgrades();
        }
        if (id.equals(STORAGE)) {
            return logic.getStorage();
        }
        return super.getSubInventory(id);
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        // Without a side too, because the altar asks for the inventories like that.
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return itemHandlerCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandlerCapability.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        itemHandlerCapability = LazyOptional.of(() -> altarItemHandler);
    }

    /**
     * Others may take items out (the altar does), but only the interface fills the inventory.
     */
    private record ExtractOnlyItemHandler(IItemHandler delegate) implements IItemHandler {
        @Override
        public int getSlots() {
            return delegate.getSlots();
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int slot) {
            return delegate.getStackInSlot(slot);
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return delegate.extractItem(slot, amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot) {
            return delegate.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return false;
        }
    }
}
