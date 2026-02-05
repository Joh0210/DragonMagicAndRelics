package de.joh.dmnr.common.item;

import com.mna.api.items.TieredItem;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.networking.ModMessages;
import de.joh.dmnr.networking.packet.ToggleWaterBraceletS2CPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

/**
 * Allows the user to breath underwater and increases their swimming_speed.
 * The higher the Item-Level, the faster the speed_boost.
 * The underwater vision is also increased.
 * @author Joh0210
 */
public class WaterBraceletItem extends TieredItem implements ICurioItem {
    private final AttributeModifier swimmingMod;

    public WaterBraceletItem(int level) {
        super(new Item.Properties().stacksTo(1));
        swimmingMod = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_water_bracelet", 1.5F * level, AttributeModifier.Operation.ADDITION);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.dmnr.bracelet_of_water").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("  "));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        ICurioItem.super.onEquip(slotContext, prevStack, stack);
        AttributeInstance swimmingAttribute = slotContext.entity().getAttribute(ForgeMod.SWIM_SPEED.get());
        if(swimmingAttribute != null && !swimmingAttribute.hasModifier(swimmingMod)) {
            swimmingAttribute.addTransientModifier(swimmingMod);
        }
        if(slotContext.entity() instanceof ServerPlayer) {
            ModMessages.sendToPlayer(new ToggleWaterBraceletS2CPacket(true), (ServerPlayer) slotContext.entity());
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ICurioItem.super.onUnequip(slotContext, newStack, stack);
        AttributeInstance swimmingAttribute = slotContext.entity().getAttribute(ForgeMod.SWIM_SPEED.get());
        if(swimmingAttribute != null) {
            swimmingAttribute.removeModifier(swimmingMod);
        }
        if(slotContext.entity() instanceof ServerPlayer) {
            ModMessages.sendToPlayer(new ToggleWaterBraceletS2CPacket(false), (ServerPlayer) slotContext.entity());
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ICurioItem.super.curioTick(slotContext, stack);

        LivingEntity livingEntity = slotContext.entity();
        if(livingEntity.getAirSupply() < livingEntity.getMaxAirSupply()) {
            livingEntity.setAirSupply(livingEntity.getMaxAirSupply());
        }
    }
}
