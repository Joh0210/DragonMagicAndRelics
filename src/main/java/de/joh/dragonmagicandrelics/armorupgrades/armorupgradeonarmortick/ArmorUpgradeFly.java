package de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonarmortick;

import com.mna.ManaAndArtifice;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.effects.EffectInit;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Allows the wearer of Dragon Mage Armor to fly on every tick.
 * However, this requires a little bit of mana.
 * Function and constructor are initialized versions of parent class function/constructor.
 * @see de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit
 * @author Joh0210
 */
public class ArmorUpgradeFly extends IArmorUpgradeOnArmorTick {

    /**
     * Defines whether it is possible to additionally sprint while flying with the Dragon Mage armor
     */
    private static boolean ALLOW_SPRTINTING_WHILE_FLYING = true;
    private static final float FLIGHT_MANA_COST_PER_TICK = 0.75F;
    /**
     * This times level = speed when flying. (0.5 is default creative)
     */
    private static final float SPEED_PER_LEVEL = 0.02F;

    public ArmorUpgradeFly(String upgradeId, int maxUpgradeLevel) {
        super(upgradeId, maxUpgradeLevel);
    }

    @Override
    public void onArmorTick(Level world, Player player, int level, IPlayerMagic magic) {
        if (0 < level) {
            player.getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> {
                //Creation of particles
                if (player.getAbilities().flying && !player.hasEffect((MobEffect) EffectInit.LEVITATION.get())) {
                    if (world.isClientSide) {
                        Vec3 look = player.getForward().cross(new Vec3(0.0D, 1.0D, 0.0D));
                        float offset = (float)(Math.random() * 0.2D);
                        look = look.scale((double)offset);
                        world.addParticle(new MAParticleType((ParticleType) ParticleInit.ARCANE.get()), player.getX() + look.x, player.getY(), player.getZ() + look.z, 0.0D, -0.05000000074505806D, 0.0D);
                        world.addParticle(new MAParticleType((ParticleType)ParticleInit.ARCANE.get()), player.getX() - look.x, player.getY(), player.getZ() - look.z, 0.0D, -0.05000000074505806D, 0.0D);
                    } else {
                        m.getCastingResource().consume(player, FLIGHT_MANA_COST_PER_TICK);
                    }
                }

                //Actual flying
                if (!m.getCastingResource().hasEnoughAbsolute(player, FLIGHT_MANA_COST_PER_TICK)) {
                    ManaAndArtifice.instance.proxy.setFlightEnabled(player, false);
                } else {
                    ManaAndArtifice.instance.proxy.setFlightEnabled(player, true);
                    if (!player.isCreative() && !player.isSpectator()) {
                        ManaAndArtifice.instance.proxy.setFlySpeed(player, level*SPEED_PER_LEVEL);
                        if (!ALLOW_SPRTINTING_WHILE_FLYING && player.getAbilities().flying) {
                            player.setSprinting(false);
                        }
                    } else {
                        ManaAndArtifice.instance.proxy.setFlySpeed(player, 0.05F);
                    }
                }
            });
        } else {
            player.getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> {
                ManaAndArtifice.instance.proxy.setFlightEnabled(player, false);
            });
        }
    }
}
