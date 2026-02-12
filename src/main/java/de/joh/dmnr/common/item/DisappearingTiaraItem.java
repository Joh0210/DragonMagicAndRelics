package de.joh.dmnr.common.item;

import com.mna.api.faction.IFaction;
import com.mna.api.items.IFactionSpecific;
import com.mna.api.items.ITieredItem;
import com.mna.effects.EffectInit;
import com.mna.factions.Factions;
import de.joh.dmnr.common.item.material.ArmorMaterials;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.function.Consumer;

public class DisappearingTiaraItem extends ArmorItem implements ITieredItem<DisappearingTiaraItem>, IFactionSpecific, ICurioItem {
        private int tier = -1;

        public DisappearingTiaraItem() {
            super(ArmorMaterials.DISAPPEARING_TIARA_MATERIAL, Type.HELMET, new Item.Properties().stacksTo(1));
        }

        public void tick(LivingEntity livingEntity) {
            if(livingEntity.isShiftKeyDown() && !livingEntity.hasEffect(EffectInit.GREATER_INVISIBILITY.get())) {
                if(livingEntity instanceof Player) {
                    this.usedByPlayer((Player) livingEntity);
                }
                livingEntity.addEffect(new MobEffectInstance(EffectInit.GREATER_INVISIBILITY.get(), -1, 0, false, false));
            } else if(!livingEntity.isShiftKeyDown() && livingEntity.hasEffect(EffectInit.GREATER_INVISIBILITY.get())) {
                livingEntity.removeEffect(EffectInit.GREATER_INVISIBILITY.get());
            }
        }

        public float getMaxIre() {
        return 0.002F;
    }

        public IFaction getFaction() {
        return Factions.FEY;
    }

        public void unequip(LivingEntity livingEntity) {
            livingEntity.removeEffect(EffectInit.GREATER_INVISIBILITY.get());
        }

        @Override
        public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
            ICurioItem.super.onUnequip(slotContext, newStack, stack);
            this.unequip(slotContext.entity());
        }

        public void onDiscard(LivingEntity entity) {
            this.unequip(entity);
        }

        @Override
        public void onArmorTick(ItemStack stack, Level level, Player player) {
            super.onArmorTick(stack, level, player);
            this.tick(player);
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            ICurioItem.super.curioTick(slotContext, stack);
            this.tick(slotContext.entity());
        }

        @Override
        public void setCachedTier(int tier) {
            this.tier = tier;
        }

        @Override
        public int getCachedTier() {
            return tier;
        }

        /**
         * Crown does not break
         */
        @Override
        public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
            return super.damageItem(stack, 0, entity, onBroken);
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
            tooltip.add(Component.translatable("item.dmnr.disappearing_tiara.description").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
            super.appendHoverText(stack, world, tooltip, flag);
        }
    }
