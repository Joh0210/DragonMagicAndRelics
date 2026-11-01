package de.joh.dmnr.common.dragonmagic;

import com.mna.effects.EffectInit;
import com.mna.items.ItemInit;
import de.joh.dmnr.common.util.CommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.TickEvent;
import top.theillusivec4.curios.api.CuriosApi;

/**
 * Dragon Magic of the Arcane Crown of Mana and Artifice, which keeps Mana Boost II (+100 maximum mana) on its wearer.
 * <br> Dragon Magic: the crown keeps Mana Boost of the level set in {@link CommonConfig#ARCANE_CROWN_DM_MANA_BOOST_LEVEL}
 * instead (IV = +200 maximum mana by default). The crown only renews its own Mana Boost when the current one is weaker
 * than level II or about to expire, so it does not replace the stronger one.
 * @author Joh0210
 */
public class ArcaneCrownDragonMagic extends ExternalDragonMagic {
    private static final int DURATION = 100;
    private static final int RENEW_BELOW = 40;

    public ArcaneCrownDragonMagic() {
        super(new ResourceLocation("mna", "arcane_crown"));
    }

    public static void eventHandleManaBoost(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (event.phase != TickEvent.Phase.END || player.level().isClientSide() || player.tickCount % 10 != 0) {
            return;
        }

        int amplifier = CommonConfig.ARCANE_CROWN_DM_MANA_BOOST_LEVEL.get() - 1;
        MobEffect manaBoost = EffectInit.MANA_BOOST.get();
        MobEffectInstance current = player.getEffect(manaBoost);
        if ((current == null || current.getAmplifier() < amplifier || (current.getAmplifier() == amplifier && current.getDuration() < RENEW_BELOW))
                && isWorn(player)
                && ExternalDragonMagicInit.ARCANE_CROWN.hasDragonMagic(player)) {
            player.addEffect(new MobEffectInstance(manaBoost, DURATION, amplifier, false, false, false));
        }
    }

    private static boolean isWorn(Player player) {
        Item crown = ItemInit.ARCANE_CROWN.get();
        return player.getItemBySlot(EquipmentSlot.HEAD).is(crown)
                || CuriosApi.getCuriosInventory(player).map(curios -> curios.findFirstCurio(crown).isPresent()).orElse(false);
    }
}
