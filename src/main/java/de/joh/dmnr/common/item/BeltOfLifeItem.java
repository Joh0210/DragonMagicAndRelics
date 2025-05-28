package de.joh.dmnr.common.item;

import com.mna.api.items.TieredItem;
import de.joh.dmnr.DragonMagicAndRelics;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

/**
 * Increases Max-HP of the user.
 * Increase is exponential to the level.
 * @author Joh0210
 */
public class BeltOfLifeItem extends TieredItem implements ICurioItem {
    private final AttributeModifier healthMod;

    public BeltOfLifeItem(int level) {
        super(new Item.Properties().stacksTo(1));
        healthMod = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_health_belt", 5F * Math.pow(2, level-1), AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        ICurioItem.super.onEquip(slotContext, prevStack, stack);
        AttributeInstance healthAttribute = slotContext.entity().getAttribute(Attributes.MAX_HEALTH);
        if(healthAttribute != null && !healthAttribute.hasModifier(healthMod)) {
            healthAttribute.addTransientModifier(healthMod);
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ICurioItem.super.onUnequip(slotContext, newStack, stack);
        AttributeInstance healthAttribute = slotContext.entity().getAttribute(Attributes.MAX_HEALTH);
        if(healthAttribute != null) {
            healthAttribute.removeModifier(healthMod);
        }
    }

    @Override
    public boolean makesPiglinsNeutral(SlotContext slotContext, ItemStack stack) {
        return true;
    }
}
