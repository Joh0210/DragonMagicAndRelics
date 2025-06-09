package de.joh.dmnr.common.effects.beneficial;

import com.mna.ManaAndArtifice;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import de.joh.dmnr.common.util.CommonConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.caelus.api.CaelusApi;

/**
 * This effect gives an elytra fly.
 * <br>Level 0: Default elytra fly.
 * <br>Level 1: With speed boost and creative fly, but mana consumption.
 * <br>Level 2: With speed boost, creative fly and no mana consumption.
 * <br>This effect takes into account the speed or slow effect.
 * todo: refactor
 * @author Joh0210
 */
public class ElytraMobEffect extends MobEffect {
    public ElytraMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        if(livingEntity instanceof Player player) {
            if (!player.hasEffect(de.joh.dmnr.common.init.EffectInit.FLY_DISABLED.get()) && amplifier>= 1) {
                ManaAndArtifice.instance.proxy.setFlightEnabled(player, true);
                if (!player.isCreative() && !player.isSpectator()) {
                    ManaAndArtifice.instance.proxy.setFlySpeed(player, Math.max((amplifier + 1-1) * CommonConfig.getFlySpeedPerLevel(player), 0.05f));
                } else {
                    ManaAndArtifice.instance.proxy.setFlySpeed(player, 0.05f);
                }


                if (player.isFallFlying()) {
                    Vec3 look = player.getLookAngle();
                    Vec3 pos;
                    float maxLength;
                    double lookScale;
                    Vec3 scaled_look;
                    if (!player.isShiftKeyDown()) {
                        pos = player.getDeltaMovement();

                        // effect Level * (Speed_Attribute * ConfigValue) * Config Value adjustment for Elytra-Flight
                        float speedboost = (amplifier + 1-1) * CommonConfig.getFlySpeedPerLevel(player) * 45;

                        maxLength = Math.max(1.75F * speedboost, 0.5F);
                        lookScale = Math.max(0.06D * speedboost, 0.02D);

                        scaled_look = look.scale(lookScale);
                        pos = pos.add(scaled_look);
                        if (pos.length() > (double) maxLength) {
                            pos = pos.scale((double) maxLength / pos.length());
                        }
                        player.setDeltaMovement(pos);
                    } else {
                        pos = player.getDeltaMovement();
                        maxLength = 0.1F;
                        lookScale = -0.01D;
                        scaled_look = look.scale(lookScale);
                        pos = pos.add(scaled_look);
                        if (pos.length() < (double) maxLength) {
                            pos = pos.scale((double) maxLength / pos.length());
                        }
                        player.setDeltaMovement(pos);
                    }

                    if (player.level().isClientSide) {
                        pos = player.position().add(look.scale(3.0D));
                        for (int i = 0; i < 5; ++i) {
                            player.level().addParticle((new MAParticleType(ParticleInit.AIR_VELOCITY.get())).setScale(0.2F).setColor(10, 10, 10), pos.x - 0.5D + Math.random(), pos.y - 0.5D + Math.random(), pos.z - 0.5D + Math.random(), -look.x, -look.y, -look.z);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity living, @NotNull AttributeMap attributemods, int p_111187_3_) {
        super.removeAttributeModifiers(living, attributemods, p_111187_3_);
        if (living instanceof ServerPlayer) {
            ManaAndArtifice.instance.proxy.setFlightEnabled((ServerPlayer)living, false);
        } else if (living instanceof Player && living.level().isClientSide) {
            ManaAndArtifice.instance.proxy.setFlySpeed((Player)living, 0.05F);
        }
    }

    /**
     * If the player has the Elytra effect, they can fly through this event like with an Elytra.
     */
    public static void eventHandleElytraFly(TickEvent.PlayerTickEvent event){
        if(event.player.hasEffect(de.joh.dmnr.common.init.EffectInit.ELYTRA.get())
                && !event.player.hasEffect(de.joh.dmnr.common.init.EffectInit.FLY_DISABLED.get())) {
            AttributeInstance attributeInstance = event.player.getAttribute(CaelusApi.getInstance().getFlightAttribute());
            if(attributeInstance != null && !attributeInstance.hasModifier(CaelusApi.getInstance().getElytraModifier()))
                attributeInstance.addTransientModifier(CaelusApi.getInstance().getElytraModifier());
        }
    }
}
