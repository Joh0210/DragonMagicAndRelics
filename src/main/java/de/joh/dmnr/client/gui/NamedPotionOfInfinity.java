package de.joh.dmnr.client.gui;

import com.mna.inventory.ItemInventoryBase;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class NamedPotionOfInfinity implements MenuProvider {
    private final ItemStack stack;

    public NamedPotionOfInfinity(ItemStack stack) {
        this.stack = stack;
    }

    @Nullable
    public AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        return new ContainerPotionOfInfinity(i, inventory, new ItemInventoryBase(this.stack, 1));
    }

    public @NotNull Component getDisplayName() {
        return Component.literal("");
    }
}
