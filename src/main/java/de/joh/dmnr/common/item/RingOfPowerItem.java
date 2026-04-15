package de.joh.dmnr.common.item;

import com.mna.api.items.TieredItem;
import de.joh.dmnr.common.init.EffectInit;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

// todo: on Attack self spell
public class RingOfPowerItem extends TieredItem implements ICurioItem {
    public RingOfPowerItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant());
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ICurioItem.super.onUnequip(slotContext, newStack, stack);

        slotContext.entity().removeEffect(EffectInit.SORCERERS_PRIDE.get());
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ICurioItem.super.curioTick(slotContext, stack);

        LivingEntity livingEntity = slotContext.entity();
        MobEffectInstance regen = livingEntity.getEffect(EffectInit.SORCERERS_PRIDE.get());
        if(regen == null /*|| regen.getAmplifier() > (level-1)*/) {
            livingEntity.addEffect(new MobEffectInstance(EffectInit.SORCERERS_PRIDE.get(), -1, 0, false, false));
        }
    }


    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.dmnr.ring_of_power.tooltip.one"));
        tooltip.add(Component.translatable("tooltip.dmnr.ring_of_power.tooltip.two"));
        super.appendHoverText(stack, world, tooltip, flag);
    }
}
