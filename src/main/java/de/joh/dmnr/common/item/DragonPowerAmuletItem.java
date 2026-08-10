package de.joh.dmnr.common.item;

import de.joh.dmnr.api.item.BaseDragonMagicItem;
import de.joh.dmnr.common.init.EffectInit;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class DragonPowerAmuletItem extends BaseDragonMagicItem implements ICurioItem {
    public DragonPowerAmuletItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant());
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ICurioItem.super.onUnequip(slotContext, newStack, stack);

        slotContext.entity().removeEffect(EffectInit.SORCERERS_PRIDE.get());
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@NotNull ItemStack pStack) {
        return false;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ICurioItem.super.curioTick(slotContext, stack);

        LivingEntity livingEntity = slotContext.entity();
        MobEffectInstance regen = livingEntity.getEffect(EffectInit.SORCERERS_PRIDE.get());

        int amplifier = this.hasDragonMagic(livingEntity) ? 2 : 1;

        if(regen == null || regen.getAmplifier() < amplifier) {
            livingEntity.addEffect(new MobEffectInstance(EffectInit.SORCERERS_PRIDE.get(), -1, amplifier, false, false));
        }
    }

    @Override
    public String dragonMagicID() {
        return "item.dmnr.dragon_power_amulet.dragonmagic";
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("item.dmnr.amulet_of_dragon_power.description").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("  "));
        super.appendHoverText(stack, world, tooltip, flag);
    }
}
