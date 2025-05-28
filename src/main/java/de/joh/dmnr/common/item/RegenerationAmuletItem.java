package de.joh.dmnr.common.item;

import com.mna.api.items.TieredItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

/**
 * Gives the User a permanent Regeneration Effect
 * @author Joh0210
 */
public class RegenerationAmuletItem extends TieredItem implements ICurioItem {
    private final int level;

    public RegenerationAmuletItem(int level) {
        super(new Properties().stacksTo(1).rarity(Rarity.EPIC));
        this.level = level;
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ICurioItem.super.onUnequip(slotContext, newStack, stack);

        slotContext.entity().removeEffect(MobEffects.REGENERATION);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ICurioItem.super.curioTick(slotContext, stack);

        LivingEntity livingEntity = slotContext.entity();
        MobEffectInstance regen = livingEntity.getEffect(MobEffects.REGENERATION);
        if(regen == null || regen.getAmplifier() > (level-1)) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, -1, (level-1)));
        }
    }

    @Override
    public boolean makesPiglinsNeutral(SlotContext slotContext, ItemStack stack) {
        return true;
    }
}
