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

public class NamedSpellRingProvider implements MenuProvider {
    private final ItemStack stack;
    private final ContainerFactory factory;

    public NamedSpellRingProvider(ItemStack stack, ContainerFactory factory) {
        this.stack = stack;
        this.factory = factory;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        return factory.create(i, inventory, new ItemInventoryBase(this.stack, 1));
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("");
    }

    @FunctionalInterface
    public interface ContainerFactory {
        AbstractContainerMenu create(int id, Inventory inv, ItemInventoryBase inventory);
    }
}
