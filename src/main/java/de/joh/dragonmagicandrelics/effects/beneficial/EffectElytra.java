package de.joh.dragonmagicandrelics.effects.beneficial;

import com.mna.ManaAndArtifice;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

/**
 * This effect gives an elytra fly.
 * <br>Level 0: Default elytra fly.
 * <br>Level 1: With speed boost and creative fly, but mana consumption.
 * <br>Level 2: With speed boost, creative fly and no mana consumption.
 * @see de.joh.dragonmagicandrelics.events.CommonEventHandler
 * @author Joh0210
 */
public class EffectElytra extends MobEffect {
    public EffectElytra() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity living, AttributeMap attributemods, int p_111187_3_) {
        super.removeAttributeModifiers(living, attributemods, p_111187_3_);
        if (living instanceof ServerPlayer) {
            ManaAndArtifice.instance.proxy.setFlightEnabled((ServerPlayer)living, false);
        } else if (living instanceof Player && living.level.isClientSide) {
            ManaAndArtifice.instance.proxy.setFlySpeed((Player)living, 0.05F);
        }

    }
}
