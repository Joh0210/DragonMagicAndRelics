package de.joh.dmnr.common.item;

import com.mna.api.items.TieredItem;
import de.joh.dmnr.DragonMagicAndRelics;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
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

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("item.dmnr.reach_ring.description").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("  "));
        super.appendHoverText(stack, world, tooltip, flag);
    }
}
