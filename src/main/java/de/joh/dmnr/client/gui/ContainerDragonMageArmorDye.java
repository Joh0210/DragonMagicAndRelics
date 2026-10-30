package de.joh.dmnr.client.gui;

import de.joh.dmnr.client.init.ContainerInit;
import de.joh.dmnr.common.item.DragonMageArmorItem;
import de.joh.dmnr.common.util.FactionArmorColors;
import de.joh.dmnr.networking.packet.DyeDragonMageArmorC2SPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Menu of the dyeing GUI of the Dragon Mage Armor. It has no slots: the screen only chooses the colors, the server
 * applies them when it receives a {@link DyeDragonMageArmorC2SPacket} while this menu is open.
 * <br>The server sends the colors of the faction of the player along when it opens the menu, since only the server
 * knows them.
 * @see GuiDragonMageArmorDye
 * @see DragonMageArmorItem#getDyeTargets
 * @author Joh0210
 */
public class ContainerDragonMageArmorDye extends AbstractContainerMenu {
    private final InteractionHand hand;
    private final FactionArmorColors.Colors defaultColors;

    public ContainerDragonMageArmorDye(int id, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(id, buffer.readEnum(InteractionHand.class), FactionArmorColors.Colors.read(buffer));
    }

    public ContainerDragonMageArmorDye(int id, InteractionHand hand, FactionArmorColors.Colors defaultColors) {
        super(ContainerInit.DRAGON_MAGE_ARMOR_DYE.get(), id);
        this.hand = hand;
        this.defaultColors = defaultColors;
    }

    /**
     * Writes the data the client needs to create this menu.
     */
    public static void writeOpenData(FriendlyByteBuf buffer, InteractionHand hand, FactionArmorColors.Colors defaultColors) {
        buffer.writeEnum(hand);
        defaultColors.write(buffer);
    }

    /**
     * @return the hand holding the piece of armor the GUI was opened with
     */
    public InteractionHand getHand() {
        return hand;
    }

    /**
     * @return the colors of the faction of the player, which the GUI resets to
     */
    public FactionArmorColors.Colors getDefaultColors() {
        return defaultColors;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return player.isAlive() && player.getItemInHand(hand).getItem() instanceof DragonMageArmorItem;
    }
}
