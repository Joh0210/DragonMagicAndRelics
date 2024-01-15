package de.joh.dmnr.common.ritual;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.effects.EffectInit;
import com.mna.tools.TeleportHelper;
import de.joh.dmnr.capabilities.secondchance.PlayerSecondChance;
import de.joh.dmnr.capabilities.secondchance.PlayerSecondChanceProvider;
import de.joh.dmnr.common.event.CommonEventHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This ritual teleports the caster to its previous death point.
 * @see CommonEventHandler
 * @see PlayerSecondChance
 * @see PlayerSecondChanceProvider
 * @author Joh0210
 */
public class PhoenixRitual extends RitualEffect {

    public PhoenixRitual(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        Player caster = context.getCaster();

        caster.getCapability(PlayerSecondChanceProvider.PLAYER_SECOND_CHANCE).ifPresent(secondChance -> {
            ResourceKey<Level> dimension = secondChance.getDimension();
            if(!caster.level().dimension().location().toString().equals(dimension.location().toString())){
                Vec3 targetPosition = new Vec3(secondChance.getPosition().getX()+0.5,secondChance.getPosition().getY(),secondChance.getPosition().getZ()+0.5);
                TeleportHelper.teleportEntity(caster, dimension, targetPosition);
            } else {
                caster.teleportTo(secondChance.getPosition().getX()+0.5,secondChance.getPosition().getY(),secondChance.getPosition().getZ()+0.5);
            }
        });

        caster.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300));
        caster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 2));
        caster.addEffect(new MobEffectInstance(EffectInit.LEVITATION.get(), 300));

        return true;
    }

    @Override
    public boolean applyStartCheckInCreative() {
        return true;
    }

    @Override
    public Component canRitualStart(IRitualContext context) {
        AtomicBoolean isValid = new AtomicBoolean(false);

        context.getCaster().getCapability(PlayerSecondChanceProvider.PLAYER_SECOND_CHANCE).ifPresent(secondChance -> isValid.set(secondChance.isValid()));

        return isValid.get() ? null : Component.translatable("dmnr.ritual.output.secondchanceritual.missing.nbt.error");
    }

    @Override
    protected int getApplicationTicks(IRitualContext iRitualContext) {
        return 0;
    }
}
