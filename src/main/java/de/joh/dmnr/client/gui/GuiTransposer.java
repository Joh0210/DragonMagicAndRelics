package de.joh.dmnr.client.gui;

import com.mna.gui.GuiTextures;
import com.mna.items.ItemInit;
import com.mna.items.runes.ItemRuneMarking;
import com.mojang.blaze3d.systems.RenderSystem;
import de.joh.dmnr.common.block.entity.TransposerBlockEntity;
import de.joh.dmnr.common.block.entity.TransposerStatus;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * GUI of the Transposer, in the style of the M&A GUIs: a vellum with the slots for the Rune of Marking and the
 * Ritual Focus, the marked position, the size of the area and the status of the Transposer. The inventory of the
 * player is below it.
 * @see ContainerTransposer
 * @author Joh0210
 */
public class GuiTransposer extends AbstractContainerScreen<ContainerTransposer> {
    private static final int TEXT_COLOR = FastColor.ARGB32.color(255, 49, 49, 49);
    private static final int INFO_COLOR = FastColor.ARGB32.color(255, 98, 70, 34);
    private static final int FRAME_COLOR = FastColor.ARGB32.color(255, 98, 70, 34);
    private static final int SLOT_COLOR = FastColor.ARGB32.color(255, 226, 199, 145);
    private static final int FADE_COLOR = FastColor.ARGB32.color(170, 226, 199, 145);
    private static final int READY_COLOR = FastColor.ARGB32.color(255, 38, 110, 42);
    private static final int WARNING_COLOR = FastColor.ARGB32.color(255, 150, 95, 0);
    private static final int ERROR_COLOR = FastColor.ARGB32.color(255, 150, 30, 30);

    // The vellum of M&A is 213x256, it is squeezed into the part of this GUI above the inventory.
    private static final int VELLUM_WIDTH = 213;
    private static final int VELLUM_HEIGHT = 256;
    private static final int GUI_WIDTH = 176;
    private static final int VELLUM_GUI_HEIGHT = 112;
    private static final int INVENTORY_WIDTH = 176;
    private static final int INVENTORY_HEIGHT = 90;

    private static final int TITLE_Y = 8;
    private static final int SLOT_SIZE = 18;
    private static final int TEXT_X = 40;
    private static final int STATUS_Y = 72;
    private static final int STATUS_WIDTH = 148;
    private static final int TOOLTIP_WIDTH = 160;

    private final ItemStack runeIcon = new ItemStack(ItemInit.RUNE_MARKING.get());
    private final ItemStack focusIcon = new ItemStack(ItemInit.RITUAL_FOCUS_MINOR.get());

    public GuiTransposer(ContainerTransposer container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.imageWidth = GUI_WIDTH;
        this.imageHeight = ContainerTransposer.INVENTORY_Y + INVENTORY_HEIGHT;
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        this.renderBackground(pGuiGraphics);
        RenderSystem.enableBlend();
        pGuiGraphics.blit(GuiTextures.Items.SPELL_RECIPE, leftPos, topPos, GUI_WIDTH, VELLUM_GUI_HEIGHT, 0, 0, VELLUM_WIDTH, VELLUM_HEIGHT, 256, 256);
        RenderSystem.disableBlend();
        pGuiGraphics.blit(GuiTextures.Widgets.STANDALONE_INVENTORY_TEXTURE, leftPos + ContainerTransposer.INVENTORY_X, topPos + ContainerTransposer.INVENTORY_Y,
                0, 0, INVENTORY_WIDTH, INVENTORY_HEIGHT, INVENTORY_WIDTH, INVENTORY_HEIGHT);

        renderSlotFrame(pGuiGraphics, ContainerTransposer.RUNE_SLOT_X, ContainerTransposer.RUNE_SLOT_Y, menu.getRune(), runeIcon);
        renderSlotFrame(pGuiGraphics, ContainerTransposer.FOCUS_SLOT_X, ContainerTransposer.FOCUS_SLOT_Y, menu.getFocus(), focusIcon);
    }

    /**
     * Frame of a slot of the Transposer. An empty slot shows a faded icon of the item it takes.
     */
    private void renderSlotFrame(GuiGraphics pGuiGraphics, int slotX, int slotY, ItemStack content, ItemStack icon) {
        int x = leftPos + slotX - 1;
        int y = topPos + slotY - 1;
        pGuiGraphics.fill(x, y, x + SLOT_SIZE, y + SLOT_SIZE, FRAME_COLOR);
        pGuiGraphics.fill(x + 1, y + 1, x + SLOT_SIZE - 1, y + SLOT_SIZE - 1, SLOT_COLOR);

        if (content.isEmpty()) {
            pGuiGraphics.renderFakeItem(icon, x + 1, y + 1);
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate(0, 0, 200);
            pGuiGraphics.fill(x + 1, y + 1, x + SLOT_SIZE - 1, y + SLOT_SIZE - 1, FADE_COLOR);
            pGuiGraphics.pose().popPose();
        }
    }

    /**
     * Title, marked position, size of the area and status. The inventory has no label, like the other M&A GUIs.
     */
    @Override
    protected void renderLabels(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(font, title, (imageWidth - font.width(title)) / 2, TITLE_Y, TEXT_COLOR, false);

        pGuiGraphics.drawString(font, Component.translatable("gui.dmnr.transposer.target"), TEXT_X, ContainerTransposer.RUNE_SLOT_Y, TEXT_COLOR, false);
        pGuiGraphics.drawString(font, getTargetText(), TEXT_X, ContainerTransposer.RUNE_SLOT_Y + font.lineHeight, INFO_COLOR, false);
        pGuiGraphics.drawString(font, Component.translatable("gui.dmnr.transposer.area"), TEXT_X, ContainerTransposer.FOCUS_SLOT_Y, TEXT_COLOR, false);
        pGuiGraphics.drawString(font, getAreaText(), TEXT_X, ContainerTransposer.FOCUS_SLOT_Y + font.lineHeight, INFO_COLOR, false);

        TransposerStatus status = menu.getStatus();
        int color = status == TransposerStatus.READY ? READY_COLOR : status.canActivate() ? WARNING_COLOR : ERROR_COLOR;
        List<FormattedCharSequence> lines = font.split(status.getText(), STATUS_WIDTH);
        for (int i = 0; i < lines.size(); i++) {
            FormattedCharSequence line = lines.get(i);
            pGuiGraphics.drawString(font, line, (imageWidth - font.width(line)) / 2, STATUS_Y + i * font.lineHeight, color, false);
        }
    }

    private Component getTargetText() {
        ItemStack rune = menu.getRune();
        BlockPos target = rune.getItem() instanceof ItemRuneMarking marking ? marking.getLocation(rune) : null;
        return target == null
                ? Component.translatable("gui.dmnr.transposer.target.none")
                : Component.translatable("gui.dmnr.transposer.target.position", target.getX(), target.getY(), target.getZ());
    }

    private Component getAreaText() {
        int size = menu.getAreaSize();
        return size <= 0
                ? Component.translatable("gui.dmnr.transposer.area.none")
                : Component.translatable("gui.dmnr.transposer.area.size", size, size, size);
    }

    /**
     * Empty slots explain which item they take.
     */
    @Override
    protected void renderTooltip(@NotNull GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderTooltip(pGuiGraphics, pX, pY);

        if (!menu.getCarried().isEmpty() || hoveredSlot == null || hoveredSlot.hasItem()) {
            return;
        }
        String key = switch (hoveredSlot.index) {
            case TransposerBlockEntity.SLOT_RUNE -> "gui.dmnr.transposer.rune.tooltip";
            case TransposerBlockEntity.SLOT_FOCUS -> "gui.dmnr.transposer.focus.tooltip";
            default -> null;
        };
        if (key != null) {
            pGuiGraphics.renderTooltip(font, font.split(Component.translatable(key), TOOLTIP_WIDTH), pX, pY);
        }
    }
}
