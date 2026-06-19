package de.joh.dmnr.client.gui;

import net.minecraft.world.item.ItemStack;

public class NamedRingOfRuling extends NamedSpellRingProvider {
    public NamedRingOfRuling(ItemStack stack) {
        super(stack, ContainerRingOfRuling::new);
    }
}
