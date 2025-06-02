package de.joh.dmnr.client.gui;

import com.mna.gui.GuiTextures;
import com.mna.gui.base.GuiBagBase;
import de.joh.dmnr.common.util.RLoc;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GuiPotionOfInfinity extends GuiBagBase<ContainerPotionOfInfinity> {
    public GuiPotionOfInfinity(ContainerPotionOfInfinity inventorySlotsIn, Inventory inv, Component comp) {
        super(inv, inventorySlotsIn);
        this.imageWidth = 176;
        this.imageHeight = 209;
    }

    public ResourceLocation texture() {
        return RLoc.create("textures/gui/potion_of_infinity.png");
    }

    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        this.renderBackground(pGuiGraphics);
        // RenderSystem.setShaderTexture(0, this.texture());
        int i = this.leftPos + 24;
        int j = this.topPos;
        pGuiGraphics.blit(this.texture(), i, j, 0, 0, 128, 128);
        // RenderSystem.setShaderTexture(0, GuiTextures.Widgets.STANDALONE_INVENTORY_TEXTURE);
        i = this.leftPos;
        j = this.topPos + 132;
        pGuiGraphics.blit(GuiTextures.Widgets.STANDALONE_INVENTORY_TEXTURE, i, j, 0.0F, 0.0F, 176, 90, 176, 90);
    }

    public String name() {
        return "Potion of Infinity";
    }
}
