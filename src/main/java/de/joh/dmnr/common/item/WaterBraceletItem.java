package de.joh.dmnr.common.item;

import com.mna.api.items.TieredItem;
import de.joh.dmnr.DragonMagicAndRelics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

/**
 * Allows the user to breath underwater and increases their swimming_speed.
 * The higher the Item-Level, the faster the speed_boost
 * todo: remove water fog?
 * @author Joh0210
 */
public class WaterBraceletItem extends TieredItem implements ICurioItem {
    private final AttributeModifier swimmingMod;

    public WaterBraceletItem(Properties properties, int level) {
        super(properties);
        swimmingMod = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_water_bracelet", 1.5F * level, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        ICurioItem.super.onEquip(slotContext, prevStack, stack);
        AttributeInstance swimmingAttribute = slotContext.entity().getAttribute(ForgeMod.SWIM_SPEED.get());
        if(swimmingAttribute != null && !swimmingAttribute.hasModifier(swimmingMod)) {
            swimmingAttribute.addTransientModifier(swimmingMod);
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ICurioItem.super.onUnequip(slotContext, newStack, stack);
        AttributeInstance swimmingAttribute = slotContext.entity().getAttribute(ForgeMod.SWIM_SPEED.get());
        if(swimmingAttribute != null) {
            swimmingAttribute.removeModifier(swimmingMod);
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
