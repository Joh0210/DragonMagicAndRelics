package de.joh.dmnr.client.gui;

import net.minecraft.world.item.ItemStack;

public class NamedRingOfPower extends NamedSpellRingProvider {
    public NamedRingOfPower(ItemStack stack) {
        super(stack, ContainerRingOfPower::new);
    }
}
