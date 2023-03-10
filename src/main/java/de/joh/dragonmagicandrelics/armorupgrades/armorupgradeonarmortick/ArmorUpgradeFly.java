package de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonarmortick;

import com.mna.ManaAndArtifice;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.effects.EffectInit;
import de.joh.dragonmagicandrelics.config.CommonConfigs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Allows the wearer of Dragon Mage Armor to fly on every tick.
 * However, this requires a little bit of mana.
 * Function and constructor are initialized versions of parent class function/constructor.
 * Configurable in the CommonConfigs.
 * @see CommonConfigs
 * @see de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit
 * @author Joh0210
 */
public class ArmorUpgradeFly extends IArmorUpgradeOnArmorTick {
    public ArmorUpgradeFly(String upgradeId, int maxUpgradeLevel) {
        super(upgradeId, maxUpgradeLevel);
    }

    @Override
    public void onArmorTick(Level world, Player player, int level, IPlayerMagic magic) {
        if (0 < level) {
            player.getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> {
                //Creation of particles
                if (player.getAbilities().flying && !player.hasEffect(EffectInit.LEVITATION.get())) {
                    if (world.isClientSide) {
                        Vec3 look = player.getForward().cross(new Vec3(0.0D, 1.0D, 0.0D));
                        float offset = (float)(Math.random() * 0.2D);
                        look = look.scale(offset);
                        world.addParticle(new MAParticleType(ParticleInit.ARCANE.get()), player.getX() + look.x, player.getY(), player.getZ() + look.z, 0.0D, -0.05000000074505806D, 0.0D);
                        world.addParticle(new MAParticleType(ParticleInit.ARCANE.get()), player.getX() - look.x, player.getY(), player.getZ() - look.z, 0.0D, -0.05000000074505806D, 0.0D);
                    } else {
                        m.getCastingResource().consume(player, CommonConfigs.getFlyManaCostPerTick());
                    }
                }

                //Actual flying
                if (!m.getCastingResource().hasEnoughAbsolute(player, CommonConfigs.getFlyManaCostPerTick())) {
                    ManaAndArtifice.instance.proxy.setFlightEnabled(player, false);
                } else {
                    ManaAndArtifice.instance.proxy.setFlightEnabled(player, true);
                    if (!player.isCreative() && !player.isSpectator()) {
                        ManaAndArtifice.instance.proxy.setFlySpeed(player, level*CommonConfigs.getFlySpeedPerLevel());
                        if (!CommonConfigs.FLY_ALLOW_SPRTINTING_WHILE_FLYING.get() && player.getAbilities().flying) {
                            player.setSprinting(false);
                        }
                    } else {
                        ManaAndArtifice.instance.proxy.setFlySpeed(player, 0.05F);
                    }
                }
            });
        } else {
            player.getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> ManaAndArtifice.instance.proxy.setFlightEnabled(player, false));
        }
    }
}
