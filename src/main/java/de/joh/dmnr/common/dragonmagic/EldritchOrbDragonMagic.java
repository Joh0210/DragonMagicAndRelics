package de.joh.dmnr.common.dragonmagic;

import com.mna.items.ItemInit;
import de.joh.dmnr.common.util.CommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Dragon Magic of the Eldritch Orb of Mana and Artifice, which hits random hostile mobs within 5 blocks of its wearer
 * about once per second with an arcane bolt for 1 damage.
 * <br> Dragon Magic: the bolts deal {@link CommonConfig#ELDRITCH_ORB_DM_DAMAGE_MULTIPLIER} times as much damage (3 by
 * default).
 * <br> Mana and Artifice deals the damage as an ordinary player attack, without an entity, damage type or event of its
 * own. So a hit counts as a bolt if it is a player attack for exactly 1 damage on a mob in range of the orb, and the
 * player has not attacked in the same tick (a real attack fires {@link AttackEntityEvent} first, which also covers its
 * sweep hits). A bolt in the same tick as an attack of the player stays unchanged.
 * @author Joh0210
 */
public class EldritchOrbDragonMagic extends ExternalDragonMagic {
    private static final float BOLT_DAMAGE = 1.0f;
    private static final double RANGE = 5.0;
    private static final Map<Player, Long> LAST_ATTACK = new WeakHashMap<>();

    public EldritchOrbDragonMagic() {
        super(new ResourceLocation("mna", "eldritch_orb"));
    }

    public static void eventHandleAttack(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide()) {
            LAST_ATTACK.put(player, player.level().getGameTime());
        }
    }

    /**
     * Must run before the other handlers change the damage.
     */
    public static void eventHandleBoltDamage(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        if (event.getAmount() == BOLT_DAMAGE
                && source.is(DamageTypes.PLAYER_ATTACK)
                && source.getDirectEntity() instanceof Player player
                && event.getEntity() instanceof Mob mob
                && player.getBoundingBox().inflate(RANGE).intersects(mob.getBoundingBox())
                && !attackedThisTick(player)
                && isEquipped(player)
                && ExternalDragonMagicInit.ELDRITCH_ORB.hasDragonMagic(player)) {
            event.setAmount(event.getAmount() * CommonConfig.ELDRITCH_ORB_DM_DAMAGE_MULTIPLIER.get());
        }
    }

    private static boolean attackedThisTick(Player player) {
        Long lastAttack = LAST_ATTACK.get(player);
        return lastAttack != null && lastAttack == player.level().getGameTime();
    }

    private static boolean isEquipped(Player player) {
        Item orb = ItemInit.ELDRITCH_ORB.get();
        return CuriosApi.getCuriosInventory(player).map(curios -> curios.findFirstCurio(orb).isPresent()).orElse(false);
    }
}
