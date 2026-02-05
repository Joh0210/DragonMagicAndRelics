package de.joh.dmnr.common.item;

import de.joh.dmnr.api.item.BaseTieredItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
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
 * Represents a special type of belt item that modifies incoming and outgoing damage
 * based on the item the entity is wearing (e.g., GlassCannonBelt x2 or SturdyBelt x0.5).
 * @author Joh0210
 */
public class DamageAdjustmentBelt extends BaseTieredItem implements IDamageAdjustmentItem {
    private final float modifier;

    public DamageAdjustmentBelt(float modifier) {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
        this.modifier = modifier;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        if(this.modifier > 1.0f){
            tooltip.add(Component.translatable("tooltip.dmnr.glass_cannon_belt").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        }
        else{
            tooltip.add(Component.translatable("tooltip.dmnr.sturdy_belt").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        }
        tooltip.add(Component.literal("  "));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean canAdjustDefending(LivingHurtEvent event, Player defender, ItemStack damageAdjustmentItem) {
        return true;
    }

    @Override
    public float adjustDefending(LivingHurtEvent event, Player defender, ItemStack damageAdjustmentItem, float amount) {
        return amount * modifier;
    }

    @Override
    public boolean canAdjustAttacking(LivingHurtEvent event, Player attacker, ItemStack damageAdjustmentItem) {
        return true;
    }

    @Override
    public float adjustAttacking(LivingHurtEvent event, Player attacker, ItemStack damageAdjustmentItem, float amount) {
        return amount * modifier;
    }
}
