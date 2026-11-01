package de.joh.dmnr.common.dragonmagic;

import com.mna.items.ItemInit;
import de.joh.dmnr.common.util.CommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;

/**
 * Dragon Magic of the Antidote Bracelet of Mana and Artifice, which removes Poison from its wearer for 1 charge.
 * <br> Dragon Magic: the bracelet turns Poison into Regeneration of the same level, lasting the percentage of the
 * remaining duration set in {@link CommonConfig#ANTIDOTE_BRACELET_DM_REGENERATION_DURATION} (50% by default). This uses
 * no charge, but like the bracelet itself it only works while it has charge left.
 * @author Joh0210
 */
public class AntidoteBraceletDragonMagic extends ExternalDragonMagic {
    public AntidoteBraceletDragonMagic() {
        super(new ResourceLocation("mna", "antidote_bracelet"));
    }

    /**
     * Runs at the start of the player tick, before Curios ticks the bracelet (Mana and Artifice would remove the Poison
     * and consume charge) and before the Poison deals damage.
     */
    public static void eventHandlePoisonConversion(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (event.phase != TickEvent.Phase.START || player.level().isClientSide()) {
            return;
        }

        MobEffectInstance poison = player.getEffect(MobEffects.POISON);
        if (poison != null
                && ItemInit.ANTIDOTE_BRACELET.get().isEquippedAndHasMana(player, 1.0f, false)
                && ExternalDragonMagicInit.ANTIDOTE_BRACELET.hasDragonMagic(player)
                && player.removeEffect(MobEffects.POISON)) {
            int duration = poison.isInfiniteDuration()
                    ? MobEffectInstance.INFINITE_DURATION
                    : (int) (poison.getDuration() * (CommonConfig.ANTIDOTE_BRACELET_DM_REGENERATION_DURATION.get() / 100f));
            if (duration != 0) {
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, duration, poison.getAmplifier(),
                        poison.isAmbient(), poison.isVisible(), poison.showIcon()));
            }
        }
    }
}
