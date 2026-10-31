package de.joh.dmnr.client.gui;

import com.mna.gui.GuiTextures;
import com.mna.gui.base.GuiJEIDisable;
import com.mojang.blaze3d.systems.RenderSystem;
import de.joh.dmnr.client.gui.widgets.DyeColorPicker;
import de.joh.dmnr.client.item.WingRenderLayer;
import de.joh.dmnr.common.item.DragonMageArmorItem;
import de.joh.dmnr.common.item.IWingItem;
import de.joh.dmnr.common.util.FactionArmorColors;
import de.joh.dmnr.networking.ModMessages;
import de.joh.dmnr.networking.packet.DyeDragonMageArmorC2SPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Dyeing GUI of the Dragon Mage Armor, in the style of the M&A GUIs: a vellum with a preview of the player wearing the
 * dyed armor, a toggle for each piece of armor, and a color picker with a hex input for each of the two colors.
 * <br>Two tabs switch the color pickers between the colors of the armor and those of the wings of the chestplate. The
 * wings tab also shows the choice of the wing design and a switch to show the cape on the wings instead, it is only
 * available if the chestplate is held or worn.
 * <br>The colors are only applied when the player confirms them, closing the GUI in any other way discards them.
 * The reset buttons return to the colors of the faction of the player ({@link FactionArmorColors}).
 * @see ContainerDragonMageArmorDye
 * @see DyeDragonMageArmorC2SPacket
 * @author Joh0210
 */
public class GuiDragonMageArmorDye extends GuiJEIDisable<ContainerDragonMageArmorDye> {
    private static final int TEXT_COLOR = FastColor.ARGB32.color(255, 49, 49, 49);
    private static final int FRAME_COLOR = FastColor.ARGB32.color(255, 98, 70, 34);
    private static final int FRAME_COLOR_UNSELECTED = FastColor.ARGB32.color(255, 166, 132, 80);
    private static final int SLOT_COLOR = FastColor.ARGB32.color(255, 226, 199, 145);
    private static final int SLOT_COLOR_UNSELECTED = FastColor.ARGB32.color(255, 196, 160, 98);
    private static final int FADE_COLOR = FastColor.ARGB32.color(150, 196, 160, 98);

    // The vellum of M&A is 213x256, it is squeezed to the height of this GUI.
    private static final int VELLUM_WIDTH = 213;
    private static final int VELLUM_HEIGHT = 256;
    private static final int GUI_HEIGHT = 214;

    private static final int PREVIEW_X = 54;
    private static final int PREVIEW_Y = 128;
    private static final int PREVIEW_SCALE = 46;
    private static final int PREVIEW_LEFT = 8;
    private static final int PREVIEW_TOP = 18;
    private static final int PREVIEW_RIGHT = 100;
    private static final int PREVIEW_BOTTOM = 132;

    private static final int PIECES_X = 18;
    private static final int PIECES_Y = 136;
    private static final int PIECE_SIZE = 18;

    // Design button and cape switch share a row, centered under the pieces of armor
    private static final int WING_DESIGN_X = 8;
    private static final int WING_DESIGN_Y = 160;
    private static final int WING_DESIGN_WIDTH = 72;
    private static final int CAPE_TOGGLE_X = WING_DESIGN_X + WING_DESIGN_WIDTH + 2;
    private static final int CAPE_TOGGLE_Y = WING_DESIGN_Y + 1;

    private static final int COLUMN_X = 110;
    private static final int TABS_Y = 19;
    private static final int TAB_WIDTH = (DyeColorPicker.WIDTH - 1) / 2;
    private static final int TAB_HEIGHT = 13;
    private static final int PRIMARY_Y = 38;
    private static final int SECONDARY_Y = 111;
    private static final int PICKER_OFFSET_Y = 17;
    private static final int HEX_BOX_X = 159;
    private static final int HEX_BOX_WIDTH = 44;
    private static final int HEX_BOX_HEIGHT = 12;
    private static final int SWATCH_BORDER = 3;

    private static final int BUTTONS_Y = 186;

    // Rotation of the preview when switching tabs: the front for the armor, the back for the wings
    private static final float ARMOR_PREVIEW_ROTATION = -25.0F;
    private static final float WINGS_PREVIEW_ROTATION = 155.0F;

    private static final Map<ArmorItem.Type, ResourceLocation> EMPTY_SLOT_ICONS = Map.of(
            ArmorItem.Type.HELMET, InventoryMenu.EMPTY_ARMOR_SLOT_HELMET,
            ArmorItem.Type.CHESTPLATE, InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE,
            ArmorItem.Type.LEGGINGS, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS,
            ArmorItem.Type.BOOTS, InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS);

    private final Map<ArmorItem.Type, ItemStack> targets;
    private final EnumSet<ArmorItem.Type> selected = EnumSet.noneOf(ArmorItem.Type.class);
    private final ColorControl primary;
    private final ColorControl secondary;
    private final ColorControl wingPrimary;
    private final ColorControl wingSecondary;
    private DragonMageArmorItem.WingDesign wingDesign;
    private boolean wingCape;
    private Tab tab = Tab.ARMOR;
    private CycleButton<DragonMageArmorItem.WingDesign> wingDesignButton;
    private Button applyButton;

    private float previewRotation = ARMOR_PREVIEW_ROTATION;
    private boolean rotatingPreview = false;

    public GuiDragonMageArmorDye(ContainerDragonMageArmorDye container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.imageWidth = VELLUM_WIDTH;
        this.imageHeight = GUI_HEIGHT;

        Player player = inventory.player;
        this.targets = DragonMageArmorItem.getDyeTargets(player, container.getHand());
        this.selected.addAll(this.targets.keySet());

        ItemStack held = player.getItemInHand(container.getHand());
        ItemStack colorSource = held.getItem() instanceof DragonMageArmorItem ? held : targets.values().stream().findFirst().orElse(ItemStack.EMPTY);
        FactionArmorColors.Colors defaults = container.getDefaultColors();
        this.primary = new ColorControl(() -> Component.translatable("gui.dmnr.dragon_mage_armor_dye.primary"), PRIMARY_Y,
                defaults.primary(), colorSource.isEmpty() ? defaults.primary() : DragonMageArmorItem.getPrimaryColor(colorSource));
        this.secondary = new ColorControl(() -> Component.translatable("gui.dmnr.dragon_mage_armor_dye.secondary"), SECONDARY_Y,
                defaults.secondary(), colorSource.isEmpty() ? defaults.secondary() : DragonMageArmorItem.getSecondaryColor(colorSource));

        ItemStack chestplate = targets.getOrDefault(ArmorItem.Type.CHESTPLATE, ItemStack.EMPTY);
        this.wingDesign = DragonMageArmorItem.getWingDesign(chestplate);
        this.wingCape = DragonMageArmorItem.isWingCapeEnabled(chestplate);
        this.wingPrimary = new ColorControl(() -> Component.translatable("gui.dmnr.dragon_mage_armor_dye.wings." + wingDesign.getName() + ".primary"), PRIMARY_Y,
                defaults.primary(), chestplate.isEmpty() ? defaults.primary() : DragonMageArmorItem.getWingPrimaryColor(chestplate));
        this.wingSecondary = new ColorControl(() -> Component.translatable("gui.dmnr.dragon_mage_armor_dye.wings." + wingDesign.getName() + ".secondary"), SECONDARY_Y,
                defaults.secondary(), chestplate.isEmpty() ? defaults.secondary() : DragonMageArmorItem.getWingSecondaryColor(chestplate));
    }

    @Override
    protected void init() {
        super.init();
        primary.init();
        secondary.init();
        wingPrimary.init();
        wingSecondary.init();

        wingDesignButton = addRenderableWidget(CycleButton.<DragonMageArmorItem.WingDesign>builder(design -> Component.translatable("gui.dmnr.dragon_mage_armor_dye.wings." + design.getName()))
                .withValues(DragonMageArmorItem.WingDesign.values())
                .withInitialValue(wingDesign)
                .displayOnlyValue()
                .withTooltip(design -> Tooltip.create(Component.translatable("gui.dmnr.dragon_mage_armor_dye.wings.tooltip")))
                .create(leftPos + WING_DESIGN_X, topPos + WING_DESIGN_Y, WING_DESIGN_WIDTH, 20,
                        Component.translatable("gui.dmnr.dragon_mage_armor_dye.wings"), (button, design) -> wingDesign = design));

        addRenderableWidget(Button.builder(Component.translatable("gui.dmnr.dragon_mage_armor_dye.reset"), b -> {
                    for (ColorControl control : getColorControls()) {
                        if (control.visible) {
                            control.setColor(control.defaultColor);
                        }
                    }
                })
                .bounds(leftPos + PIECES_X, topPos + BUTTONS_Y, PIECE_SIZE * 4, 20)
                .tooltip(Tooltip.create(Component.translatable("gui.dmnr.dragon_mage_armor_dye.reset.tooltip")))
                .build());

        applyButton = addRenderableWidget(Button.builder(Component.translatable("gui.dmnr.dragon_mage_armor_dye.apply"), b -> apply())
                .bounds(leftPos + COLUMN_X, topPos + BUTTONS_Y, DyeColorPicker.WIDTH, 20)
                .tooltip(Tooltip.create(Component.translatable("gui.dmnr.dragon_mage_armor_dye.apply.tooltip")))
                .build());
        applyButton.active = !selected.isEmpty();

        updateTabWidgets();
    }

    private void apply() {
        ModMessages.sendToServer(new DyeDragonMageArmorC2SPacket(primary.color, secondary.color,
                wingDesign, wingPrimary.color, wingSecondary.color, wingCape, selected));
        onClose();
    }

    private List<ColorControl> getColorControls() {
        return List.of(primary, secondary, wingPrimary, wingSecondary);
    }

    /**
     * Shows the widgets of the current tab and hides those of the other one.
     */
    private void updateTabWidgets() {
        primary.setVisible(tab == Tab.ARMOR);
        secondary.setVisible(tab == Tab.ARMOR);
        wingPrimary.setVisible(tab == Tab.WINGS);
        wingSecondary.setVisible(tab == Tab.WINGS);
        wingDesignButton.visible = tab == Tab.WINGS;
    }

    private boolean isTabAvailable(Tab t) {
        return t != Tab.WINGS || targets.containsKey(ArmorItem.Type.CHESTPLATE);
    }

    /**
     * @return a copy of the piece of armor with the colors (and for the chestplate the wings) currently chosen in the GUI
     */
    private ItemStack dyedCopy(ItemStack stack) {
        ItemStack copy = stack.copy();
        DragonMageArmorItem.setColors(copy, primary.color, secondary.color);
        DragonMageArmorItem.setWings(copy, wingDesign, wingPrimary.color, wingSecondary.color, wingCape);
        return copy;
    }

    /**
     * @return how the piece of the given type looks after applying: dyed if selected, unchanged otherwise
     */
    private ItemStack previewStack(ArmorItem.Type type) {
        ItemStack target = targets.get(type);
        return selected.contains(type) ? dyedCopy(target) : target;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        this.renderBackground(pGuiGraphics);
        RenderSystem.enableBlend();
        pGuiGraphics.blit(GuiTextures.Items.SPELL_RECIPE, leftPos, topPos, imageWidth, imageHeight, 0, 0, VELLUM_WIDTH, VELLUM_HEIGHT, 256, 256);
        RenderSystem.disableBlend();

        renderPreview(pGuiGraphics);
        renderPieces(pGuiGraphics);
        renderTabs(pGuiGraphics);
        if (tab == Tab.WINGS) {
            renderCapeToggle(pGuiGraphics);
        }
        for (ColorControl control : getColorControls()) {
            control.renderSwatch(pGuiGraphics);
        }
    }

    /**
     * Draws the cape switch like the toggles of the pieces of armor, with the outside of the cape of the player as
     * icon, or an Elytra if the player has no cape.
     */
    private void renderCapeToggle(GuiGraphics pGuiGraphics) {
        int x = leftPos + CAPE_TOGGLE_X;
        int y = topPos + CAPE_TOGGLE_Y;
        pGuiGraphics.fill(x, y, x + PIECE_SIZE, y + PIECE_SIZE, wingCape ? FRAME_COLOR : FRAME_COLOR_UNSELECTED);
        pGuiGraphics.fill(x + 1, y + 1, x + PIECE_SIZE - 1, y + PIECE_SIZE - 1, wingCape ? SLOT_COLOR : SLOT_COLOR_UNSELECTED);

        Player player = this.minecraft.player;
        if (player instanceof AbstractClientPlayer clientPlayer && clientPlayer.isCapeLoaded() && clientPlayer.getCloakTextureLocation() != null) {
            // The outside of the cape is 10x16 pixels at (1, 1) of the 64x32 cape texture
            pGuiGraphics.blit(clientPlayer.getCloakTextureLocation(), x + 4, y + 1, 10, 16, 1, 1, 10, 16, 64, 32);
        } else {
            pGuiGraphics.renderItem(new ItemStack(Items.ELYTRA), x + 1, y + 1);
        }

        if (!wingCape) {
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate(0, 0, 200);
            pGuiGraphics.fill(x + 1, y + 1, x + PIECE_SIZE - 1, y + PIECE_SIZE - 1, FADE_COLOR);
            pGuiGraphics.pose().popPose();
        }
    }

    /**
     * Draws the tabs like the toggles of the pieces of armor, an unavailable tab is faded.
     */
    private void renderTabs(GuiGraphics pGuiGraphics) {
        for (Tab t : Tab.values()) {
            int x = tabX(t);
            int y = topPos + TABS_Y;
            boolean isSelected = t == tab;
            pGuiGraphics.fill(x, y, x + TAB_WIDTH, y + TAB_HEIGHT, isSelected ? FRAME_COLOR : FRAME_COLOR_UNSELECTED);
            pGuiGraphics.fill(x + 1, y + 1, x + TAB_WIDTH - 1, y + TAB_HEIGHT - 1, isSelected ? SLOT_COLOR : SLOT_COLOR_UNSELECTED);

            float scale = Math.min(1.0F, (float) (TAB_WIDTH - 4) / font.width(t.label));
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate(x + (TAB_WIDTH - font.width(t.label) * scale) / 2.0F, y + 3 + (1 - scale) * 4, 0);
            pGuiGraphics.pose().scale(scale, scale, 1.0F);
            pGuiGraphics.drawString(font, t.label, 0, 0, TEXT_COLOR, false);
            pGuiGraphics.pose().popPose();

            if (!isTabAvailable(t)) {
                pGuiGraphics.fill(x + 1, y + 1, x + TAB_WIDTH - 1, y + TAB_HEIGHT - 1, FADE_COLOR);
            }
        }
    }

    /**
     * Renders the player wearing the preview of the armor. The armor slots of the client player are swapped for the
     * duration of the render call only.
     */
    private void renderPreview(GuiGraphics pGuiGraphics) {
        Player player = this.minecraft.player;
        if (player == null) {
            return;
        }

        NonNullList<ItemStack> armor = player.getInventory().armor;
        ItemStack[] original = new ItemStack[armor.size()];
        for (int i = 0; i < armor.size(); i++) {
            original[i] = armor.get(i);
        }

        float yBodyRot = player.yBodyRot;
        float yRot = player.getYRot();
        float xRot = player.getXRot();
        float yHeadRotO = player.yHeadRotO;
        float yHeadRot = player.yHeadRot;
        try {
            targets.keySet().forEach(type -> armor.set(type.getSlot().getIndex(), previewStack(type)));

            player.yBodyRot = 180.0F + previewRotation;
            player.setYRot(180.0F + previewRotation);
            player.setXRot(0.0F);
            player.yHeadRot = player.getYRot();
            player.yHeadRotO = player.getYRot();
            InventoryScreen.renderEntityInInventory(pGuiGraphics, leftPos + PREVIEW_X, topPos + PREVIEW_Y, PREVIEW_SCALE,
                    new Quaternionf().rotateZ((float) Math.PI), null, player);
        } finally {
            for (int i = 0; i < armor.size(); i++) {
                armor.set(i, original[i]);
            }
            player.yBodyRot = yBodyRot;
            player.setYRot(yRot);
            player.setXRot(xRot);
            player.yHeadRotO = yHeadRotO;
            player.yHeadRot = yHeadRot;
        }
    }

    private void renderPieces(GuiGraphics pGuiGraphics) {
        for (ArmorItem.Type type : ArmorItem.Type.values()) {
            int x = pieceX(type);
            int y = topPos + PIECES_Y;
            boolean isSelected = selected.contains(type);
            pGuiGraphics.fill(x, y, x + PIECE_SIZE, y + PIECE_SIZE, isSelected ? FRAME_COLOR : FRAME_COLOR_UNSELECTED);
            pGuiGraphics.fill(x + 1, y + 1, x + PIECE_SIZE - 1, y + PIECE_SIZE - 1, isSelected ? SLOT_COLOR : SLOT_COLOR_UNSELECTED);

            if (!targets.containsKey(type)) {
                RenderSystem.enableBlend();
                pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 0.4F);
                pGuiGraphics.blit(x + 1, y + 1, 0, 16, 16, this.minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(EMPTY_SLOT_ICONS.get(type)));
                pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.disableBlend();
                continue;
            }

            pGuiGraphics.renderItem(previewStack(type), x + 1, y + 1);
            if (!isSelected) {
                pGuiGraphics.pose().pushPose();
                pGuiGraphics.pose().translate(0, 0, 200);
                pGuiGraphics.fill(x + 1, y + 1, x + PIECE_SIZE - 1, y + PIECE_SIZE - 1, FADE_COLOR);
                pGuiGraphics.pose().popPose();
            }
        }
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(this.font, this.title, (this.imageWidth - this.font.width(this.title)) / 2, 7, TEXT_COLOR, false);
        for (ColorControl control : getColorControls()) {
            control.renderLabel(pGuiGraphics);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);

        ArmorItem.Type piece = getPieceAt(mouseX, mouseY);
        Tab hoveredTab = getTabAt(mouseX, mouseY);
        if (piece != null) {
            pGuiGraphics.renderComponentTooltip(this.font, getPieceTooltip(piece), mouseX, mouseY);
        } else if (hoveredTab != null && !isTabAvailable(hoveredTab)) {
            pGuiGraphics.renderTooltip(this.font, Component.translatable("gui.dmnr.dragon_mage_armor_dye.tab.wings.missing"), mouseX, mouseY);
        } else if (isInCapeToggle(mouseX, mouseY)) {
            pGuiGraphics.renderComponentTooltip(this.font, getCapeToggleTooltip(), mouseX, mouseY);
        } else if (!rotatingPreview && isInPreview(mouseX, mouseY)) {
            pGuiGraphics.renderTooltip(this.font, Component.translatable("gui.dmnr.dragon_mage_armor_dye.rotate"), mouseX, mouseY);
        }
    }

    private List<Component> getPieceTooltip(ArmorItem.Type type) {
        List<Component> tooltip = new ArrayList<>();
        ItemStack target = targets.get(type);
        if (target == null) {
            tooltip.add(Component.translatable("gui.dmnr.dragon_mage_armor_dye.piece.missing").withStyle(ChatFormatting.GRAY));
            return tooltip;
        }

        tooltip.add(target.getHoverName());
        tooltip.add(Component.translatable(selected.contains(type) ? "gui.dmnr.dragon_mage_armor_dye.piece.selected" : "gui.dmnr.dragon_mage_armor_dye.piece.unselected")
                .withStyle(ChatFormatting.GRAY));
        return tooltip;
    }

    private int pieceX(ArmorItem.Type type) {
        return leftPos + PIECES_X + type.ordinal() * PIECE_SIZE;
    }

    @Nullable
    private ArmorItem.Type getPieceAt(double mouseX, double mouseY) {
        if (mouseY < topPos + PIECES_Y || mouseY >= topPos + PIECES_Y + PIECE_SIZE) {
            return null;
        }
        for (ArmorItem.Type type : ArmorItem.Type.values()) {
            if (mouseX >= pieceX(type) && mouseX < pieceX(type) + PIECE_SIZE) {
                return type;
            }
        }
        return null;
    }

    private List<Component> getCapeToggleTooltip() {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.translatable("gui.dmnr.dragon_mage_armor_dye.wings.cape"));
        tooltip.add(Component.translatable(wingCape ? "gui.dmnr.dragon_mage_armor_dye.wings.cape.on" : "gui.dmnr.dragon_mage_armor_dye.wings.cape.off")
                .withStyle(ChatFormatting.GRAY));
        // Designs with a layer for the cape keep their shape and frame, the others are replaced by the cape
        boolean capeFill = wingDesign.getLayers(0, 0).stream().anyMatch(IWingItem.WingLayer::capeFill);
        tooltip.add(Component.translatable(capeFill ? "gui.dmnr.dragon_mage_armor_dye.wings.cape.fill" : "gui.dmnr.dragon_mage_armor_dye.wings.cape.replace")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("gui.dmnr.dragon_mage_armor_dye.wings.cape.fallback").withStyle(ChatFormatting.GRAY));
        if (WingRenderLayer.getCapeTexture(this.minecraft.player) == null) {
            tooltip.add(Component.translatable("gui.dmnr.dragon_mage_armor_dye.wings.cape.missing").withStyle(ChatFormatting.GOLD));
        }
        return tooltip;
    }

    /**
     * The cape switch is only shown in the wings tab.
     */
    private boolean isInCapeToggle(double mouseX, double mouseY) {
        int x = leftPos + CAPE_TOGGLE_X;
        int y = topPos + CAPE_TOGGLE_Y;
        return tab == Tab.WINGS && mouseX >= x && mouseX < x + PIECE_SIZE && mouseY >= y && mouseY < y + PIECE_SIZE;
    }

    private int tabX(Tab t) {
        return leftPos + COLUMN_X + t.ordinal() * (TAB_WIDTH + 1);
    }

    @Nullable
    private Tab getTabAt(double mouseX, double mouseY) {
        if (mouseY < topPos + TABS_Y || mouseY >= topPos + TABS_Y + TAB_HEIGHT) {
            return null;
        }
        for (Tab t : Tab.values()) {
            if (mouseX >= tabX(t) && mouseX < tabX(t) + TAB_WIDTH) {
                return t;
            }
        }
        return null;
    }

    private boolean isInPreview(double mouseX, double mouseY) {
        return mouseX >= leftPos + PREVIEW_LEFT && mouseX < leftPos + PREVIEW_RIGHT
                && mouseY >= topPos + PREVIEW_TOP && mouseY < topPos + PREVIEW_BOTTOM;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            ArmorItem.Type piece = getPieceAt(mouseX, mouseY);
            if (piece != null) {
                setFocused(null);
                if (targets.containsKey(piece)) {
                    if (!selected.remove(piece)) {
                        selected.add(piece);
                    }
                    applyButton.active = !selected.isEmpty();
                    this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                }
                return true;
            }

            if (isInCapeToggle(mouseX, mouseY)) {
                setFocused(null);
                wingCape = !wingCape;
                this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            }

            Tab clickedTab = getTabAt(mouseX, mouseY);
            if (clickedTab != null) {
                setFocused(null);
                if (clickedTab != tab && isTabAvailable(clickedTab)) {
                    tab = clickedTab;
                    previewRotation = tab == Tab.WINGS ? WINGS_PREVIEW_ROTATION : ARMOR_PREVIEW_ROTATION;
                    updateTabWidgets();
                    this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                }
                return true;
            }

            if (isInPreview(mouseX, mouseY)) {
                setFocused(null);
                rotatingPreview = true;
                return true;
            }
        }

        // Clicking anywhere else removes the focus from the hex boxes
        if (getFocused() instanceof EditBox box && !box.isMouseOver(mouseX, mouseY)) {
            setFocused(null);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    /**
     * {@link net.minecraft.client.gui.screens.inventory.AbstractContainerScreen} does not pass dragging on to its
     * widgets, which the sliders of the color pickers need.
     */
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (rotatingPreview) {
            previewRotation -= (float) dragX * 2.0F;
            return true;
        }
        if (this.getFocused() != null && this.isDragging() && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            return this.getFocused().mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        rotatingPreview = false;
        // Ends dragging a slider even if the mouse is released outside of its picker
        for (ColorControl control : getColorControls()) {
            control.picker.mouseReleased(mouseX, mouseY, button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    /**
     * While a hex box is focused, it gets all keys, so typing e.g. "E" does not close the GUI like the inventory key.
     */
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (getFocused() instanceof EditBox box && box.isFocused() && keyCode != GLFW.GLFW_KEY_ESCAPE && keyCode != GLFW.GLFW_KEY_TAB) {
            if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
                setFocused(null);
            } else {
                box.keyPressed(keyCode, scanCode, modifiers);
            }
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    /**
     * The two tabs of the color pickers.
     */
    private enum Tab {
        ARMOR(Component.translatable("gui.dmnr.dragon_mage_armor_dye.tab.armor")),
        WINGS(Component.translatable("gui.dmnr.dragon_mage_armor_dye.tab.wings"));

        private final Component label;

        Tab(Component label) {
            this.label = label;
        }
    }

    /**
     * Color picker, hex box and swatch of one of the colors. Only the controls of the current tab are visible.
     */
    private final class ColorControl {
        private final Supplier<Component> label;
        private final int y;
        private final int defaultColor;
        private int color;
        private boolean visible = true;
        private DyeColorPicker picker;
        private EditBox hexBox;
        private boolean updating = false;

        /**
         * @param label the label is requested on every frame, so it can follow the chosen wing design
         */
        private ColorControl(Supplier<Component> label, int y, int defaultColor, int color) {
            this.label = label;
            this.y = y;
            this.defaultColor = defaultColor;
            this.color = color;
        }

        private void init() {
            picker = addRenderableWidget(new DyeColorPicker(leftPos + COLUMN_X, topPos + y + PICKER_OFFSET_Y, defaultColor, label.get(), this::setColor));
            picker.setColor(color);

            hexBox = addRenderableWidget(new EditBox(font, leftPos + HEX_BOX_X, topPos + y, HEX_BOX_WIDTH, HEX_BOX_HEIGHT, label.get()));
            hexBox.setMaxLength(6);
            hexBox.setFilter(text -> text.chars().allMatch(c -> Character.digit(c, 16) != -1));
            hexBox.setValue(toHex(color));
            hexBox.setResponder(text -> {
                if (text.length() == 6) {
                    setColor(Integer.parseInt(text, 16));
                }
            });
            hexBox.setTooltip(Tooltip.create(Component.translatable("gui.dmnr.dragon_mage_armor_dye.hex")));
        }

        private void setColor(int newColor) {
            if (updating) {
                return;
            }
            updating = true;
            color = newColor & 0xFFFFFF;
            picker.setColor(color);
            if (!hexBox.getValue().equalsIgnoreCase(toHex(color))) {
                hexBox.setValue(toHex(color));
            }
            updating = false;
        }

        private static String toHex(int color) {
            return String.format("%06X", color & 0xFFFFFF);
        }

        private void setVisible(boolean visible) {
            this.visible = visible;
            picker.visible = visible;
            hexBox.setVisible(visible);
            if (!visible && getFocused() == hexBox) {
                setFocused(null);
            }
        }

        /**
         * Draws a frame in the current color around the hex box.
         */
        private void renderSwatch(GuiGraphics pGuiGraphics) {
            if (!visible) {
                return;
            }
            int x = leftPos + HEX_BOX_X;
            int boxY = topPos + y;
            pGuiGraphics.fill(x - SWATCH_BORDER, boxY - SWATCH_BORDER, x + HEX_BOX_WIDTH + SWATCH_BORDER, boxY + HEX_BOX_HEIGHT + SWATCH_BORDER, FRAME_COLOR);
            pGuiGraphics.fill(x - SWATCH_BORDER + 1, boxY - SWATCH_BORDER + 1, x + HEX_BOX_WIDTH + SWATCH_BORDER - 1, boxY + HEX_BOX_HEIGHT + SWATCH_BORDER - 1, 0xFF000000 | color);
        }

        /**
         * Draws the label left of the hex box, scaled down if a translation is too long.
         */
        private void renderLabel(GuiGraphics pGuiGraphics) {
            if (!visible) {
                return;
            }
            Component text = label.get();
            int x = COLUMN_X + 1;
            int maxWidth = HEX_BOX_X - SWATCH_BORDER - 2 - x;
            float scale = Math.min(1.0F, (float) maxWidth / font.width(text));
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate(x, y + 2 + (1 - scale) * 4, 0);
            pGuiGraphics.pose().scale(scale, scale, 1.0F);
            pGuiGraphics.drawString(font, text, 0, 0, TEXT_COLOR, false);
            pGuiGraphics.pose().popPose();
        }
    }
}
