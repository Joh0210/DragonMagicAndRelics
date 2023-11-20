package de.joh.dmnr.effects.harmful;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;
import java.util.ArrayList;
import java.util.List;

/**
 * If a player had the Peace effect and attacks a monster from another faction, the peace will be broken and they will attack normally again.
 * @see de.joh.dmnr.effects.beneficial.PeaceEffect
 * @author Joh0210
 */
public class BrokenPeaceEffect extends MobEffect {
    public BrokenPeaceEffect() {
        super(MobEffectCategory.HARMFUL, 9633792);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
