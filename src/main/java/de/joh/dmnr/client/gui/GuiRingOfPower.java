package de.joh.dmnr.client.gui;

import de.joh.dmnr.common.util.RLoc;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class GuiRingOfPower<T extends AbstractContainerMenu> extends GuiSpellRingBase<T> {
    public GuiRingOfPower(T inventorySlotsIn, Inventory inv, Component comp) {
        super(inventorySlotsIn, inv, comp);
    }

    @Override
    public ResourceLocation texture() {
        return RLoc.create("textures/gui/draconic_spells.png");
    }

    @Override
    public String name() {
        return "Ring Of Power";
    }
}
