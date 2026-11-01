package de.joh.dmnr.common.dragonmagic;

import com.mna.items.ItemInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.living.EnderManAngerEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import top.theillusivec4.curios.api.CuriosApi;

/**
 * Dragon Magic of the Enderguard Amulet of Mana and Artifice, which teleports a creeper that explodes within 5 blocks of
 * its wearer away for 50 charge, where it explodes instead.
 * <br> Dragon Magic: the amulet never runs out of charge, and Endermen no longer get angry when its wearer looks at
 * them, as if the wearer had a carved pumpkin on.
 * @author Joh0210
 */
public class EnderguardAmuletDragonMagic extends ExternalDragonMagic {
    /**
     * Distance from the bounding box of the creeper within which Mana and Artifice looks for a player with the amulet.
     */
    private static final double CREEPER_RANGE = 5.0;

    public EnderguardAmuletDragonMagic() {
        super(new ResourceLocation("mna", "enderguard_amulet"));
    }

    /**
     * Refills the amulets of all players around an exploding creeper, so Mana and Artifice always finds the charge it
     * consumes.
     * <br> Must run before Mana and Artifice, which teleports the creeper in the same event.
     */
    public static void eventHandleRefill(ExplosionEvent.Start event) {
        if (!event.getLevel().isClientSide()
                && event.getExplosion().getDirectSourceEntity() instanceof Creeper creeper) {
            for (Player player : creeper.level().getEntitiesOfClass(Player.class, creeper.getBoundingBox().inflate(CREEPER_RANGE))) {
                if (ExternalDragonMagicInit.ENDERGUARD_AMULET.hasDragonMagic(player)) {
                    refillEquippedCurio(ItemInit.ENDERGUARD_AMULET.get(), player);
                }
            }
        }
    }

    /**
     * Keeps Endermen calm when the wearer looks at them. Needs no charge, since the amulet never runs out of it.
     */
    public static void eventHandleEnderManAnger(EnderManAngerEvent event) {
        Player player = event.getPlayer();
        Item amulet = ItemInit.ENDERGUARD_AMULET.get();
        if (CuriosApi.getCuriosInventory(player).map(curios -> curios.findFirstCurio(amulet).isPresent()).orElse(false)
                && ExternalDragonMagicInit.ENDERGUARD_AMULET.hasDragonMagic(player)) {
            event.setCanceled(true);
        }
    }
}
