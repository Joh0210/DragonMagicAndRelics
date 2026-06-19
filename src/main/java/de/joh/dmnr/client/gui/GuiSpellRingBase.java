package de.joh.dmnr.client.gui;

import com.mna.gui.GuiTextures;
import com.mna.gui.base.GuiBagBase;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class GuiSpellRingBase<T extends AbstractContainerMenu> extends GuiBagBase<T>{
    public GuiSpellRingBase(T inventorySlotsIn, Inventory inv, Component comp) {
        super(inv, inventorySlotsIn);
        this.imageWidth = 176;
        this.imageHeight = 209;
    }

    public abstract ResourceLocation texture();

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        this.renderBackground(pGuiGraphics);
        int i = this.leftPos + 24;
        int j = this.topPos;
        pGuiGraphics.blit(this.texture(), i, j, 0, 0, 128, 128);
        i = this.leftPos;
        j = this.topPos + 132;
        pGuiGraphics.blit(GuiTextures.Widgets.STANDALONE_INVENTORY_TEXTURE, i, j, 0.0F, 0.0F, 176, 90, 176, 90);
    }

    public abstract String name();
}
