package de.joh.dmnr.common.item;

import com.mna.api.items.ITieredItem;
import com.mna.inventory.ItemInventoryBase;
import com.mna.items.base.ItemBagBase;
import com.mna.items.filters.ItemFilterGroup;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.api.util.PotionFilter;
import de.joh.dmnr.client.gui.NamedPotionOfInfinity;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Provides a way to infinitely drink a potion
 * @author Joh0210
 */
public class PotionOfInfinityItem extends ItemBagBase implements ITieredItem<PotionOfInfinityItem> {
    private int _tier = -1;

    public PotionOfInfinityItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON)
                .food((new FoodProperties.Builder()).nutrition(1).saturationMod(1).alwaysEat().build()));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("item.dmnr.potion_of_infinity.description"));
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemstack, @NotNull Level world, @NotNull LivingEntity entity) {
        super.finishUsingItem(itemstack, world, entity);

        DragonMagicAndRelics.LOGGER.warn("Potion of Infinity Ite0");
        if (!world.isClientSide) {
            ItemInventoryBase inv = new ItemInventoryBase(itemstack);
            ItemStack potion = inv.getStackInSlot(0);
            if (potion.getItem() instanceof PotionItem) {
                for(MobEffectInstance mobEffectInstance : PotionUtils.getMobEffects(potion)) {
                    if (mobEffectInstance.getEffect().isInstantenous()) {
                        mobEffectInstance.getEffect().applyInstantenousEffect(entity, entity, entity, mobEffectInstance.getAmplifier(), 1.0F);
                    } else {
                        entity.addEffect(new MobEffectInstance(mobEffectInstance));
                    }
                }
            }
        }
        if (entity instanceof Player) ((Player) entity).getCooldowns().addCooldown(this, 1200);

        itemstack.grow(1);
        return itemstack;
    }

    @Override
    public @NotNull SoundEvent getEatingSound() {
        return super.getDrinkingSound();
    }



    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (this.openGuiIfModifierPressed(itemstack, player, world)) {
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);
    }

    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public ItemFilterGroup filterGroup() {
        return PotionFilter.ANY_POTION;
    }

    @Override
    public MenuProvider getProvider(ItemStack itemStack) {
        return new NamedPotionOfInfinity(itemStack);
    }


    @Override
    public void setCachedTier(int tier) {
        this._tier = tier;
    }

    @Override
    public int getCachedTier() {
        return this._tier;
    }

}

