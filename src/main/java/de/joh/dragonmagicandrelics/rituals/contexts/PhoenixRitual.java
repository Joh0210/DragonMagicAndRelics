package de.joh.dragonmagicandrelics.rituals.contexts;

import com.ma.api.rituals.IRitualContext;
import com.ma.api.rituals.RitualEffect;
import com.ma.effects.EffectInit;
import com.ma.tools.TeleportHelper;
import de.joh.dragonmagicandrelics.capabilities.secondchance.PlayerSecondChance;
import de.joh.dragonmagicandrelics.capabilities.secondchance.PlayerSecondChanceProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This ritual teleports the caster to its previous death point.
 * @see de.joh.dragonmagicandrelics.events.CommonEventHandler
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
        PlayerEntity caster = context.getCaster();

        caster.getCapability(PlayerSecondChanceProvider.PLAYER_SECOND_CHANCE).ifPresent(secondChance -> {
            RegistryKey<World> dimension = secondChance.getDimension();
            if(!caster.getEntityWorld().getDimensionKey().getLocation().toString().equals(dimension.getLocation().toString())){
                Vector3d targetPosition = new Vector3d(secondChance.getPosition().getX()+0.5,secondChance.getPosition().getY(),secondChance.getPosition().getZ()+0.5);
                TeleportHelper.teleportEntity(caster, dimension, targetPosition);
            } else {
                caster.teleportKeepLoaded(secondChance.getPosition().getX()+0.5,secondChance.getPosition().getY(),secondChance.getPosition().getZ()+0.5);
            }
        });

        caster.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 300));
        caster.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 300, 2));
        caster.addPotionEffect(new EffectInstance(EffectInit.LEVITATION.get(), 300));

        return true;
    }

    @Override
    public boolean applyStartCheckInCreative() {
        return true;
    }

    @Override
    public TextComponent canRitualStart(IRitualContext context) {
        AtomicBoolean isValid = new AtomicBoolean(false);

        context.getCaster().getCapability(PlayerSecondChanceProvider.PLAYER_SECOND_CHANCE).ifPresent(secondChance -> isValid.set(secondChance.isValid()));

        return isValid.get() ? null : new TranslationTextComponent("dragonmagicandrelics.ritual.output.secondchanceritual.missing.nbt.error");
    }

    @Override
    protected int getApplicationTicks(IRitualContext iRitualContext) {
        return 0;
    }
}
