package de.joh.dmnr.common.item;

import com.mna.api.faction.IFaction;
import com.mna.api.items.IFactionSpecific;
import com.mna.api.items.ITieredItem;
import com.mna.factions.Factions;
import de.joh.dmnr.common.init.EffectInit;
import de.joh.dmnr.common.item.material.ArmorMaterials;
import net.minecraft.world.effect.MobEffect;
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
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class HydraCrownItem extends ArmorItem implements ITieredItem<HydraCrownItem>, IFactionSpecific, ICurioItem {
    private int tier = -1;

    public HydraCrownItem() {
        super(ArmorMaterials.HYDRA_CROWN_MATERIAL, Type.HELMET, new Item.Properties().stacksTo(1));
    }

    Map<MobEffect, TreeMap<Integer, Integer>> effects = Map.of(
            MobEffects.REGENERATION, new TreeMap<>(Map.of(
                    10, 0
            )),
            MobEffects.DAMAGE_RESISTANCE, new TreeMap<>(Map.of(
                    30, 1,
                    50, 0
            )),
            EffectInit.ULTIMATE_ARMOR.get(), new TreeMap<>(Map.of(
                    10, 3,
                    20, 2,
                    35, 1,
                    50, 0
            )),
            MobEffects.MOVEMENT_SPEED, new TreeMap<>(Map.of(
                    20, 2,
                    30, 2,
                    50, 1,
                    70, 0
            )),
            MobEffects.JUMP, new TreeMap<>(Map.of(
                    30, 1,
                    70, 0
            )),
            MobEffects.DAMAGE_BOOST, new TreeMap<>(Map.of(
                    10, 4,
                    20, 3,
                    30, 2,
                    40, 1,
                    50, 0
            ))
    );

    public void tick(LivingEntity livingEntity) {
        boolean applied = false;

        int hp_percent = (int)(100 * livingEntity.getHealth() / livingEntity.getMaxHealth());

        for (Map.Entry<MobEffect, TreeMap<Integer, Integer>> effect : effects.entrySet()) {
            // look for the corresponding hp_percent - Level entry
            Map.Entry<Integer, Integer> thresholdEntry = effect.getValue().ceilingEntry(hp_percent);
            if (thresholdEntry != null) {
                MobEffectInstance current = livingEntity.getEffect(effect.getKey());

                // apply the effect, if the effect is better than the current instance, or if the player does not have the effect
                if(current == null ||current.getAmplifier() < thresholdEntry.getValue() || (current.getAmplifier() == thresholdEntry.getValue() && current.getDuration() < 40)){
                    livingEntity.addEffect(new MobEffectInstance(effect.getKey(), 300, thresholdEntry.getValue(), false, false, false));
                    applied = true;
                }
            }
        }

        if (applied && livingEntity instanceof Player) {
            this.usedByPlayer((Player) livingEntity);
        }
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
