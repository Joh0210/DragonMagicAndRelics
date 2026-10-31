package de.joh.dmnr.common.item;

import de.joh.dmnr.common.util.CommonConfig;
import de.joh.tnl.api.item.BaseDragonMagicItem;
import de.joh.dmnr.DragonMagicAndRelics;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

/**
 * Curio that grants bonus armor and armor toughness.
 * <br> Dragon Magic: reduces all incoming damage by the percentage set in {@link CommonConfig#DEFENSE_BRACELET_DM_DAMAGE_REDUCTION}.
 * @author Joh0210
 */
public class DefenseBraceletItem extends BaseDragonMagicItem implements ICurioItem, IDamageAdjustmentItem {
    private final AttributeModifier armorMod;
    private final AttributeModifier toughnessMod;

    public DefenseBraceletItem(int armor, int toughness) {
        super(new Item.Properties().stacksTo(1));
        armorMod = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_defense_bracelet", armor, AttributeModifier.Operation.ADDITION);
        toughnessMod = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_defense_bracelet", toughness, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        ICurioItem.super.onEquip(slotContext, prevStack, stack);
        AttributeInstance armorAttribute = slotContext.entity().getAttribute(Attributes.ARMOR);
        if(armorAttribute != null && !armorAttribute.hasModifier(armorMod)) {
            armorAttribute.addTransientModifier(armorMod);
        }

        AttributeInstance toughnessAttribute = slotContext.entity().getAttribute(Attributes.ARMOR_TOUGHNESS);
        if(toughnessAttribute != null && !toughnessAttribute.hasModifier(toughnessMod)) {
            toughnessAttribute.addTransientModifier(toughnessMod);
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ICurioItem.super.onUnequip(slotContext, newStack, stack);
        AttributeInstance armorAttribute = slotContext.entity().getAttribute(Attributes.ARMOR);
        if(armorAttribute != null) {
            armorAttribute.removeModifier(armorMod);
        }

        AttributeInstance toughnessAttribute = slotContext.entity().getAttribute(Attributes.ARMOR_TOUGHNESS);
        if(toughnessAttribute != null) {
            toughnessAttribute.removeModifier(toughnessMod);
        }
    }

    @Override
    public boolean canAdjustDefending(LivingHurtEvent event, Player defender, ItemStack damageAdjustmentItem) {
        // Only the first equipped Bracelet of Defense applies the reduction, so several bracelets do not stack
        return CommonConfig.DEFENSE_BRACELET_DM_DAMAGE_REDUCTION.get() > 0 && hasDragonMagic(defender);
    }

    @Override
    public float adjustDefending(LivingHurtEvent event, Player defender, ItemStack damageAdjustmentItem, float amount) {
        return amount * (1 - CommonConfig.DEFENSE_BRACELET_DM_DAMAGE_REDUCTION.get() / 100f);
    }

    @Override
    public boolean canAdjustAttacking(LivingHurtEvent event, Player attacker, ItemStack damageAdjustmentItem) {
        return false;
    }

    @Override
    public float adjustAttacking(LivingHurtEvent event, Player attacker, ItemStack damageAdjustmentItem, float amount) {
        return amount;
    }

    @Override
    public String dragonMagicID() {
        return "item.dmnr.defense_bracelet.dragonmagic";
    }



    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("item.dmnr.defense_bracelet.description").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("  "));
        super.appendHoverText(stack, world, tooltip, flag);
    }
}
