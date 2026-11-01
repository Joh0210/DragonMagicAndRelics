package de.joh.dmnr.common.dragonmagic;

import com.mna.api.events.ComponentApplyingEvent;
import com.mna.api.spells.collections.Components;
import com.mna.items.ItemInit;
import com.mna.items.artifice.SpellModifierRing;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

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
            SpellModifierRing ring = ItemInit.SILK_TOUCH_RING.get();
            CuriosApi.getCuriosInventory(player).ifPresent(curios -> curios.findFirstCurio(ring)
                    .ifPresent(slot -> refill(ring, slot.stack(), player)));
        }
    }

    private static void refill(SpellModifierRing ring, ItemStack stack, Player player) {
        // refundMana does nothing for a ring that has never been charged and has no tag yet
        stack.getOrCreateTag();
        ring.refundMana(stack, ring.getMaxMana() - ring.getMana(stack), player);
    }
}
