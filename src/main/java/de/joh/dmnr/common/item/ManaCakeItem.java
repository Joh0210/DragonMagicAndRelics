package de.joh.dmnr.common.item;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import de.joh.dmnr.api.item.BaseDragonMagicItem;
import de.joh.dmnr.common.init.ItemInit;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A cake to eat endlessly with mana consumption.
 */
public class ManaCakeItem extends BaseDragonMagicItem {
    /**
     * Mana cost to "repair" the cake
     */
    private static final int MANA_COSTS = 40;

    /**
     * How much nutrition does the cake give?
     */
    private static final int NUTRITION = 4;

    /**
     * How much saturation does the cake give?
     */
    private static final float SATURATION = 0.4f;

    public ManaCakeItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON)
                .food((new FoodProperties.Builder()).nutrition(NUTRITION).saturationMod(SATURATION).alwaysEat().build()));
    }

    /**
     * If null is returned, the item will be consumed because there was not enough mana
     * Call from the game itself.
     */
    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemstack, @NotNull Level world, @NotNull LivingEntity entity) {
        ItemStack retval = new ItemStack(ItemInit.MANA_CAKE.get());
        super.finishUsingItem(itemstack, world, entity);
        IPlayerMagic magic = entity.getCapability(PlayerMagicProvider.MAGIC).orElse(null);

        if (this.hasDragonMagic(entity)) {
            entity.addEffect(new MobEffectInstance(MobEffects.SATURATION, 600, 0, false, false));
        }

        if (itemstack.isEmpty() && entity instanceof Player && magic != null && magic.getCastingResource().hasEnoughAbsolute(entity, MANA_COSTS)) {
            magic.getCastingResource().consume(entity, MANA_COSTS);
            return retval;
        } else {
            return itemstack;
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("item.dmnr.mana_cake.description").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("  "));
        super.appendHoverText(stack, world, tooltip, flag);
    }

    @Override
    public String dragonMagicID() {
        return "item.dmnr.mana_cake.dragonmagic";
    }
}
