package de.joh.dmnr.gui;

import com.mna.inventory.ItemInventoryBase;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class NamedBraceletOfFriendship implements MenuProvider {
    private final ItemStack stack;

    public NamedBraceletOfFriendship(ItemStack stack) {
        this.stack = stack;
    }

    @Nullable
    public AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        return new ContainerBraceletOfFriendship(i, inventory, new ItemInventoryBase(this.stack, 6));
    }

    public @NotNull Component getDisplayName() {
        return new TextComponent("");
    }
}
