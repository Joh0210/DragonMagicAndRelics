package de.joh.dmnr.common.item.spellstoring;

import com.mna.spells.crafting.SpellRecipe;
import de.joh.dmnr.client.gui.NamedRingOfCooldownSpellStoring;
import de.joh.dmnr.common.init.EffectInit;
import de.joh.dmnr.common.util.CommonConfig;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * {@link RingOfSpellStoringItem} which uses no Mana but its Cooldowns is multiplied by 5
 * @see RingOfSpellStoringItem
 * @author Joh0210
 */
public class RingOfCooldownSpellStoringItem extends RingOfSpellStoringItem{
    public RingOfCooldownSpellStoringItem(Properties itemProperties) {
        super(itemProperties);
    }

    protected boolean canUse(Player player, ItemStack ring, SpellRecipe recipe) {
        return !player.getCooldowns().isOnCooldown(this);
    }

    protected void onUse(Player player, ItemStack ring, SpellRecipe recipe) {
        player.getCooldowns().addCooldown(this, recipe.getCooldown(player) * CommonConfig.SPELL_STORING_COOLDOWN_FACTOR.get());
        player.addEffect(new MobEffectInstance(EffectInit.SPELL_STORING_COOLDOWN.get(), recipe.getCooldown(player) * CommonConfig.SPELL_STORING_COOLDOWN_FACTOR.get(), 0, false, false, true));
    }

    @Override
    public MenuProvider getProvider(ItemStack itemStack) {
        return new NamedRingOfCooldownSpellStoring(itemStack);
    }
}
