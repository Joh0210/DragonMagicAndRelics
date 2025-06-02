package de.joh.dmnr.common.item;

import com.mna.api.faction.IFaction;
import com.mna.api.items.IFactionSpecific;
import com.mna.api.items.ITieredItem;
import com.mna.factions.Factions;
import de.joh.dmnr.common.item.material.ArmorMaterials;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.function.Consumer;

/**
 * Grants the User Night Vision and prevents Blindness and Confusion
 * <br>When unequiped will Confuse the user for a few seconds
 * @author Joh0210
 */

public class NightGogglesItem extends ArmorItem implements ITieredItem<HydraCrownItem>, IFactionSpecific, ICurioItem {
    private int tier = -1;

    public NightGogglesItem() {
        super(ArmorMaterials.NIGHT_GOGGLES_MATERIAL, Type.HELMET, new Item.Properties().stacksTo(1));
    }

    public void tick(LivingEntity livingEntity) {
        MobEffectInstance vision = livingEntity.getEffect(MobEffects.NIGHT_VISION);
        if(vision == null) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, -1, 0, false, false));
        }
        livingEntity.removeEffect(MobEffects.BLINDNESS);
        livingEntity.removeEffect(MobEffects.CONFUSION);
    }

    public void unequip(LivingEntity livingEntity) {
        livingEntity.removeEffect(MobEffects.NIGHT_VISION);

        MobEffectInstance vision = livingEntity.getEffect(MobEffects.CONFUSION);
        if(vision == null) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0, false, false));
        }
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
    public IFaction getFaction() {
        return  Factions.UNDEAD;
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

    @Override
    public boolean makesPiglinsNeutral(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return true;
    }
}


