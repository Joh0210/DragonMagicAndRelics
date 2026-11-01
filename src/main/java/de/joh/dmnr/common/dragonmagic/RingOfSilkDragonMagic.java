package de.joh.dmnr.common.dragonmagic;

import com.mna.api.events.ComponentApplyingEvent;
import com.mna.api.spells.collections.Components;
import com.mna.items.ItemInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * Dragon Magic of the Ring of Silk of Mana and Artifice, which gives Break spells Silk Touch as long as it has charge,
 * and loses 1 charge for every block broken.
 * <br> Dragon Magic: the ring never runs out of charge. Its charge is refilled right before Break is applied to each
 * block, so even spells that break many blocks at once keep Silk Touch.
 * @author Joh0210
 */
public class RingOfSilkDragonMagic extends ExternalDragonMagic {
    public RingOfSilkDragonMagic() {
        super(new ResourceLocation("mna", "silk_touch_ring"));
    }

    public static void eventHandleRefill(ComponentApplyingEvent event) {
        if (event.getComponent() == Components.BREAK
                && event.getSource().getCaster() instanceof Player player
                && !player.level().isClientSide()
                && ExternalDragonMagicInit.RING_OF_SILK.hasDragonMagic(player)) {
            refillEquippedCurio(ItemInit.SILK_TOUCH_RING.get(), player);
        }
    }
}
