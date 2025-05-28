package de.joh.dmnr.common.item;

import com.mna.api.items.TieredItem;
import de.joh.dmnr.DragonMagicAndRelics;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

/**
 * Increases the Users Reach-Distance (for placing and attacking).
 * Higher Levels will add mor reach.
 * @author Joh0210
 */
public class ReachRingItem extends TieredItem implements ICurioItem {
    private final AttributeModifier reachMod;

    public ReachRingItem(int level) {
        super(new Item.Properties().stacksTo(1));
        reachMod = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_reach_ring", level, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        ICurioItem.super.onEquip(slotContext, prevStack, stack);
        AttributeInstance entityReachAttribute = slotContext.entity().getAttribute(ForgeMod.BLOCK_REACH.get());
        if(entityReachAttribute != null && !entityReachAttribute.hasModifier(reachMod)) {
            entityReachAttribute.addTransientModifier(reachMod);
        }

        AttributeInstance blockReachAttribute = slotContext.entity().getAttribute(ForgeMod.ENTITY_REACH.get());
        if(blockReachAttribute != null && !blockReachAttribute.hasModifier(reachMod)) {
            blockReachAttribute.addTransientModifier(reachMod);
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ICurioItem.super.onUnequip(slotContext, newStack, stack);
        AttributeInstance entityReachAttribute = slotContext.entity().getAttribute(ForgeMod.BLOCK_REACH.get());
        if(entityReachAttribute != null) {
            entityReachAttribute.removeModifier(reachMod);
        }

        AttributeInstance blockReachAttribute = slotContext.entity().getAttribute(ForgeMod.ENTITY_REACH.get());
        if(blockReachAttribute != null) {
            blockReachAttribute.removeModifier(reachMod);
        }
    }
}
