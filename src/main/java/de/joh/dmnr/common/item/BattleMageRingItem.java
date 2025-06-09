package de.joh.dmnr.common.item;

import com.mna.api.items.IRelic;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This item increases the dmg of magic by the attack_dmg attribute of the player.
 * Since the attack_dmg attribute is meant for sword_attacks, most effects provide a flat-bonus to it. E.g. Potion of Str.
 * However, this item also boosts small, repeating dmg, like poison. For this reason the flat-bonus is recalculated in a relative function.
 * @author Joh0210
 */
public class BattleMageRingItem extends Item implements IDamageAdjustmentItem, IRelic {
    public BattleMageRingItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public boolean canAdjustAttacking(LivingHurtEvent event, Player attacker, ItemStack damageAdjustmentItem) {
        return event.getSource().is(DamageTypeTags.BYPASSES_ARMOR); // More or less IS_MAGIC

    }

    @Override
    public float adjustAttacking(LivingHurtEvent event, Player attacker, ItemStack damageAdjustmentItem, float amount) {
        AttributeInstance attackAttribute = attacker.getAttribute(Attributes.ATTACK_DAMAGE);
        int attack = (attackAttribute != null) ? (int) Math.round(attackAttribute.getValue()) : 1;

        // shift default value of the attack from 1 to 0;
        return amount * (1.0f + (attack-1.0f)/15.0f);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.dmnr.battle_mage_ring.lore_1").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.dmnr.battle_mage_ring.lore_2").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("  "));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean canAdjustDefending(LivingHurtEvent event, Player defender, ItemStack damageAdjustmentItem) {
        return false;
    }

    @Override
    public float adjustDefending(LivingHurtEvent event, Player defender, ItemStack damageAdjustmentItem, float amount) {
        return amount;
    }
}
