package de.joh.dmnr.client.gui.widgets;

import com.mna.gui.GuiTextures;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntConsumer;

/**
 * RGB color picker in the style of the color picker of the M&A Book of Rote ({@link com.mna.gui.widgets.RGBPicker}),
 * drawn from the same texture but without the alpha channel.
 * <br>Each channel has a reset button (the colored dot, back to the default color), nudge buttons (with Shift in steps
 * of 10), a slider, and can be changed with the mouse wheel.
 * @author Joh0210
 */
public class DyeColorPicker extends AbstractWidget {
    public static final int WIDTH = 95;
    public static final int HEIGHT = 50;

    private static final int TEXTURE_U = 161;
    // The texture has four rows, the background is cut after the third one and closed with the bottom edge.
    private static final int TOP_PART_HEIGHT = 44;
    private static final int BOTTOM_EDGE_V = 54;
    private static final int BOTTOM_EDGE_HEIGHT = 6;

    private static final int CHANNELS = 3;
    private static final int ROW_OFFSET = 6;
    private static final int ROW_SPACING = 13;
    private static final int ROW_HEIGHT = 8;
    private static final int RESET_START_X = 6;
    private static final int RESET_END_X = 14;
    private static final int NUDGE_DOWN_X = 15;
    private static final int NUDGE_UP_X = 38;
    private static final int NUDGE_WIDTH = 5;
    private static final int VALUE_CENTER_X = 29;
    private static final int SLIDER_START_X = 48;
    private static final int SLIDER_LENGTH = 42;
    private static final int TEXT_COLOR = FastColor.ARGB32.color(255, 49, 49, 49);

    private final int defaultColor;
    private final IntConsumer onChange;
    private final int[] values = new int[CHANNELS];
    private int dragChannel = -1;

    /**
     * @param defaultColor RGB color the reset buttons return to
     * @param name name of the color, used for narration
     * @param onChange called with the new RGB color whenever the player changes it
     */
    public DyeColorPicker(int x, int y, int defaultColor, Component name, IntConsumer onChange) {
        super(x, y, WIDTH, HEIGHT, name);
        this.defaultColor = defaultColor;
        this.onChange = onChange;
        setColor(defaultColor);
    }

    /**
     * @return the RGB color of the picker
     */
    public int getColor() {
        return values[0] << 16 | values[1] << 8 | values[2];
    }

    /**
     * Sets the color without notifying the listener.
     */
    public void setColor(int color) {
        for (int channel = 0; channel < CHANNELS; channel++) {
            values[channel] = channelOf(color, channel);
        }
    }

    private static int channelOf(int color, int channel) {
        return color >> (16 - 8 * channel) & 255;
    }

    private void setValue(int channel, int value) {
        value = Mth.clamp(value, 0, 255);
        if (values[channel] != value) {
            values[channel] = value;
            onChange.accept(getColor());
        }
    }

    private static int step() {
        return Screen.hasShiftDown() ? 10 : 1;
    }

    private int rowY(int channel) {
        return getY() + ROW_OFFSET + ROW_SPACING * channel;
    }

    /**
     * @return the channel whose row contains the given height, or -1
     */
    private int getChannelAt(double mouseY) {
        for (int channel = 0; channel < CHANNELS; channel++) {
            if (mouseY >= rowY(channel) && mouseY <= rowY(channel) + ROW_HEIGHT) {
                return channel;
            }
        }
        return -1;
    }

    private void updateDraggedValue(double mouseX) {
        double pct = Mth.clamp((mouseX - getX() - SLIDER_START_X) / SLIDER_LENGTH, 0, 1);
        setValue(dragChannel, (int) Math.round(255 * pct));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.active || !this.visible || !this.isValidClickButton(button) || !this.isMouseOver(mouseX, mouseY)) {
            return false;
        }

        int channel = getChannelAt(mouseY);
        if (channel == -1) {
            return false;
        }

        double relX = mouseX - getX();
        if (relX >= RESET_START_X && relX <= RESET_END_X) {
            setValue(channel, channelOf(defaultColor, channel));
        } else if (relX >= NUDGE_DOWN_X && relX <= NUDGE_DOWN_X + NUDGE_WIDTH) {
            setValue(channel, values[channel] - step());
        } else if (relX >= NUDGE_UP_X && relX <= NUDGE_UP_X + NUDGE_WIDTH) {
            setValue(channel, values[channel] + step());
        } else if (relX >= SLIDER_START_X - 4 && relX <= SLIDER_START_X + SLIDER_LENGTH + 4) {
            dragChannel = channel;
            updateDraggedValue(mouseX);
        } else {
            return false;
        }

        this.playDownSound(Minecraft.getInstance().getSoundManager());
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (!this.active || !this.visible || !this.isValidClickButton(button) || dragChannel == -1) {
            return false;
        }
        updateDraggedValue(mouseX);
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragChannel = -1;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int channel = getChannelAt(mouseY);
        if (!this.active || !this.visible || !this.isMouseOver(mouseX, mouseY) || channel == -1 || delta == 0) {
            return false;
        }
        setValue(channel, values[channel] + (delta > 0 ? step() : -step()));
        return true;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.enableBlend();
        pGuiGraphics.blit(GuiTextures.Items.BOOK_OF_ROTE_EXTRAS, getX(), getY(), TEXTURE_U, 0, WIDTH, TOP_PART_HEIGHT);
        pGuiGraphics.blit(GuiTextures.Items.BOOK_OF_ROTE_EXTRAS, getX(), getY() + TOP_PART_HEIGHT, TEXTURE_U, BOTTOM_EDGE_V, WIDTH, BOTTOM_EDGE_HEIGHT);

        for (int channel = 0; channel < CHANNELS; channel++) {
            int y = rowY(channel);
            int nubbinX = getX() + SLIDER_START_X + Math.round(SLIDER_LENGTH * values[channel] / 255.0F) - 2;
            pGuiGraphics.blit(GuiTextures.Items.BOOK_OF_ROTE_EXTRAS, nubbinX, y, 10, 88, 4, 8);
            pGuiGraphics.blit(GuiTextures.Items.BOOK_OF_ROTE_EXTRAS, getX() + NUDGE_DOWN_X, y, 0, 88, NUDGE_WIDTH, 8);
            pGuiGraphics.blit(GuiTextures.Items.BOOK_OF_ROTE_EXTRAS, getX() + NUDGE_UP_X, y, 5, 88, NUDGE_WIDTH, 8);
        }
        RenderSystem.disableBlend();

        renderValues(pGuiGraphics);
    }

    /**
     * Draws the numbers into the boxes between the nudge buttons, scaled like in M&A.
     */
    private void renderValues(GuiGraphics pGuiGraphics) {
        Minecraft mc = Minecraft.getInstance();
        boolean unicode = mc.options.forceUnicodeFont().get();
        float scale = unicode ? 1.0F : 0.7F;
        Font font = mc.font;

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(scale, scale, scale);
        for (int channel = 0; channel < CHANNELS; channel++) {
            String value = Integer.toString(values[channel]);
            float textX = (getX() + VALUE_CENTER_X + 0.5F) / scale - font.width(value) / 2.0F;
            float textY = (rowY(channel) + (unicode ? 0.0F : 1.5F)) / scale;
            pGuiGraphics.drawString(font, value, (int) textX, (int) textY, TEXT_COLOR, false);
        }
        pGuiGraphics.pose().popPose();
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {
        pNarrationElementOutput.add(NarratedElementType.TITLE, Component.translatable("gui.dmnr.dragon_mage_armor_dye.narration",
                getMessage(), values[0], values[1], values[2]));
    }
}
