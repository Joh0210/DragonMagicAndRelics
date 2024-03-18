package de.joh.dmnr.common.effects.harmful;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SpellStoringCooldownMobEffect extends MobEffect {
    public SpellStoringCooldownMobEffect() {
        super(MobEffectCategory.HARMFUL, -2448096);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}