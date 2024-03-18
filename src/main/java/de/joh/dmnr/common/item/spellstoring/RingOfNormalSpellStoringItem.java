package de.joh.dmnr.common.item.spellstoring;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import de.joh.dmnr.client.gui.NamedRingOfNormalSpellStoring;
import de.joh.dmnr.common.init.EffectInit;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

/**
 * {@link RingOfSpellStoringItem} which uses Mana and Cooldowns as usual
 * @see RingOfSpellStoringItem
 * @author Joh0210
 */
public class RingOfNormalSpellStoringItem extends RingOfSpellStoringItem{
    public RingOfNormalSpellStoringItem(Properties itemProperties) {
        super(itemProperties);
    }

    protected boolean canUse(Player player, ItemStack ring, SpellRecipe recipe) {
        Optional<IPlayerMagic> magic =  player.getCapability(PlayerMagicProvider.MAGIC).resolve();

        return magic.filter(iPlayerMagic -> !player.getCooldowns().isOnCooldown(this) && iPlayerMagic.getCastingResource().hasEnoughAbsolute(player, recipe.getManaCost())).isPresent();
    }

    protected void onUse(Player player, ItemStack ring, SpellRecipe recipe) {
        SpellCaster.setCooldown(ring, player, recipe.getCooldown(player));
        player.getCooldowns().addCooldown(this, recipe.getCooldown(player));
        player.addEffect(new MobEffectInstance(EffectInit.SPELL_STORING_COOLDOWN.get(), recipe.getCooldown(player), 0, false, false, true));

        player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(magic -> magic.getCastingResource().consume(player, recipe.getManaCost()));
    }

    @Override
    public MenuProvider getProvider(ItemStack itemStack) {
        return new NamedRingOfNormalSpellStoring(itemStack);
    }
}
