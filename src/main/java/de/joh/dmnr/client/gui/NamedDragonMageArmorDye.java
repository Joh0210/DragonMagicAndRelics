package de.joh.dmnr.client.gui;

import de.joh.dmnr.common.util.FactionArmorColors;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * Opens the dyeing GUI of the Dragon Mage Armor.
 * @see ContainerDragonMageArmorDye
 * @author Joh0210
 */
public class NamedDragonMageArmorDye implements MenuProvider {
    private final InteractionHand hand;
    private final FactionArmorColors.Colors defaultColors;

    public NamedDragonMageArmorDye(InteractionHand hand, FactionArmorColors.Colors defaultColors) {
        this.hand = hand;
        this.defaultColors = defaultColors;
    }

    @Nullable
    public AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        return new ContainerDragonMageArmorDye(i, this.hand, this.defaultColors);
    }

    /**
     * Writes the data the client needs to create the menu, pass it to {@code NetworkHooks.openScreen}.
     */
    public void writeOpenData(FriendlyByteBuf buffer) {
        ContainerDragonMageArmorDye.writeOpenData(buffer, this.hand, this.defaultColors);
    }

    public @NotNull Component getDisplayName() {
        return Component.translatable("gui.dmnr.dragon_mage_armor_dye");
    }
}
