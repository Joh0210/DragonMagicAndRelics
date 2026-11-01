package de.joh.dmnr.common.dragonmagic;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.capabilities.resource.CastingResourceIDs;
import com.mna.api.capabilities.resource.ICastingResource;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.items.ItemInit;
import de.joh.dmnr.common.util.CommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.Event;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Dragon Magic of the Ring of Brittle Bone of Mana and Artifice, which multiplies the souls its Undead wearer gains by
 * 2.25 for 3.5 charge.
 * <br> Dragon Magic: the wearer gains the percentage set in {@link CommonConfig#BRITTLE_BONE_RING_DM_SOUL_BONUS} of
 * these souls once more (100% by default, i.e. twice as many). Mana and Artifice grants the souls without an event of
 * its own, inside LivingDeathEvent (kills) and LivingDamageEvent (life drain of the Witherbone Armor). So the souls of
 * the attacker are measured before and after Mana and Artifice handles these events.
 * @author Joh0210
 */
public class RingOfBrittleBoneDragonMagic extends ExternalDragonMagic {
    private static final float CHARGE_PER_USE = 3.5f;
    private static final Map<Event, Float> SOULS_BEFORE = new IdentityHashMap<>();

    public RingOfBrittleBoneDragonMagic() {
        super(new ResourceLocation("mna", "bone_ring"));
    }

    /**
     * Must run before Mana and Artifice, which consumes the charge of the ring in the same event.
     */
    public static void eventHandleSoulsBefore(LivingDeathEvent event) {
        measureBefore(event, event.getSource());
    }

    /**
     * @see #eventHandleSoulsBefore(LivingDeathEvent)
     */
    public static void eventHandleSoulsBefore(LivingDamageEvent event) {
        measureBefore(event, event.getSource());
    }

    /**
     * Must run after Mana and Artifice, also for canceled events, so no measurement is left behind.
     */
    public static void eventHandleSoulsAfter(LivingDeathEvent event) {
        grantBonus(event, event.getSource());
    }

    /**
     * @see #eventHandleSoulsAfter(LivingDeathEvent)
     */
    public static void eventHandleSoulsAfter(LivingDamageEvent event) {
        grantBonus(event, event.getSource());
    }

    private static void measureBefore(Event event, DamageSource source) {
        if (source.getEntity() instanceof Player player
                && !player.level().isClientSide()
                && ItemInit.BONE_RING.get().isEquippedAndHasMana(player, CHARGE_PER_USE, false)
                && ExternalDragonMagicInit.RING_OF_BRITTLE_BONE.hasDragonMagic(player)) {
            getSouls(player).ifPresent(souls -> SOULS_BEFORE.put(event, souls.getAmount()));
        }
    }

    private static void grantBonus(Event event, DamageSource source) {
        Float before = SOULS_BEFORE.remove(event);
        if (before != null && source.getEntity() instanceof Player player) {
            getSouls(player).ifPresent(souls -> {
                float gained = souls.getAmount() - before;
                if (gained > 0) {
                    souls.restore(gained * CommonConfig.BRITTLE_BONE_RING_DM_SOUL_BONUS.get() / 100f);
                    souls.setNeedsSync();
                }
            });
        }
    }

    private static Optional<ICastingResource> getSouls(Player player) {
        return player.getCapability(PlayerMagicProvider.MAGIC)
                .map(IPlayerMagic::getCastingResource)
                .filter(resource -> CastingResourceIDs.SOULS.equals(resource.getRegistryName()));
    }
}
