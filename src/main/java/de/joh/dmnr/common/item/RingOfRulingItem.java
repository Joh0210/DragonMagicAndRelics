package de.joh.dmnr.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// todo: on Attack revenge spell
public class RingOfRulingItem extends DragonMageCuriosItem {
    public RingOfRulingItem() {
        super(16, "ring_of_ruling", new Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.dmnr.ring_of_ruling.tooltip.one"));
        tooltip.add(Component.translatable("tooltip.dmnr.ring_of_ruling.tooltip.two"));
        super.appendHoverText(stack, world, tooltip, flag);
    }
}
