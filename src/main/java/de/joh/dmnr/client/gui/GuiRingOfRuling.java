package de.joh.dmnr.client.gui;

import de.joh.dmnr.common.util.RLoc;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class GuiRingOfRuling<T extends AbstractContainerMenu> extends GuiSpellRingBase<T> {
    public GuiRingOfRuling(T inventorySlotsIn, Inventory inv, Component comp) {
        super(inventorySlotsIn, inv, comp);
    }

    @Override
    public ResourceLocation texture() {
        return RLoc.create("textures/gui/draconic_spells_2.png");
    }

    @Override
    public String name() {
        return "Ring Of Ruling";
    }
}
