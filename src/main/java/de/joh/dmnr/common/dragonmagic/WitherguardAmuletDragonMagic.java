package de.joh.dmnr.common.dragonmagic;

import com.mna.items.ItemInit;
import de.joh.dmnr.common.util.CommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

/**
 * Dragon Magic of the Witherguard Amulet of Mana and Artifice, which removes Wither from its wearer for 1 charge.
 * <br> Dragon Magic: every creature that hurts the wearer suffers Wither of the level and duration set in
 * {@link CommonConfig#WITHERGUARD_AMULET_DM_WITHER_LEVEL} and {@link CommonConfig#WITHERGUARD_AMULET_DM_WITHER_DURATION}
 * (II for 5 seconds by default), also through its projectiles. This uses no charge, but like the amulet itself it only
 * works while it has charge left. Creatures that are immune to Wither (e.g. Wither Skeletons) are not affected.
 * @author Joh0210
 */
public class WitherguardAmuletDragonMagic extends ExternalDragonMagic {
    public WitherguardAmuletDragonMagic() {
        super(new ResourceLocation("mna", "witherguard_amulet"));
    }

    public static void eventHandleWitherAttacker(LivingHurtEvent event) {
        int duration = CommonConfig.WITHERGUARD_AMULET_DM_WITHER_DURATION.get() * 20;
        if (duration > 0
                && event.getEntity() instanceof Player player
                && event.getSource().getEntity() instanceof LivingEntity attacker
                && attacker != player
                && ItemInit.WITHERGUARD_AMULET.get().isEquippedAndHasMana(player, 1.0f, false)
                && ExternalDragonMagicInit.WITHERGUARD_AMULET.hasDragonMagic(player)) {
            int amplifier = CommonConfig.WITHERGUARD_AMULET_DM_WITHER_LEVEL.get() - 1;
            attacker.addEffect(new MobEffectInstance(MobEffects.WITHER, duration, amplifier), player);
        }
    }
}
