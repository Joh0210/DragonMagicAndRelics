package de.joh.dmnr.gui;

import com.mna.gui.GuiTextures;
import com.mna.gui.base.GuiBagBase;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.joh.dmnr.utils.RLoc;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class GuiDragonMageArmor<T extends AbstractContainerMenu> extends GuiBagBase<T>{
    public GuiDragonMageArmor(T inventorySlotsIn, Inventory inv, Component comp) {
        super(inv, inventorySlotsIn);
        this.imageWidth = 176;
        this.imageHeight = 209;
    }

    public ResourceLocation texture() {
        return RLoc.create("textures/gui/draconic_spells.png");
    }

    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        this.renderBackground(matrixStack);
        RenderSystem.setShaderTexture(0, this.texture());
        int i = this.leftPos + 24;
        int j = this.topPos;
        this.blit(matrixStack, i, j, 0, 0, 128, 128);
        RenderSystem.setShaderTexture(0, GuiTextures.Widgets.STANDALONE_INVENTORY_TEXTURE);
        i = this.leftPos;
        j = this.topPos + 132;
        blit(matrixStack, i, j, 0.0F, 0.0F, 176, 90, 176, 90);
    }

    public String name() {
        return "Bracelet of Friendship";
    }
}
