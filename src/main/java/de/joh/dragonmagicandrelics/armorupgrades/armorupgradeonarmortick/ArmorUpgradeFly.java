package de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonarmortick;

import com.ma.ManaAndArtifice;
import com.ma.api.capabilities.IPlayerMagic;
import com.ma.api.particles.MAParticleType;
import com.ma.api.particles.ParticleInit;
import com.ma.capabilities.playerdata.magic.PlayerMagicProvider;
import com.ma.effects.EffectInit;
import de.joh.dragonmagicandrelics.config.CommonConfigs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

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
    public void onArmorTick(World world, PlayerEntity player, int level, IPlayerMagic magic) {
        if (0 < level) {
            player.getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> {
                //Creation of particles
                if (player.abilities.isFlying && !player.isPotionActive((Effect)EffectInit.LEVITATION.get())) {
                    if (world.isRemote) {
                        Vector3d look = player.getForward().crossProduct(new Vector3d(0.0D, 1.0D, 0.0D));
                        float offset = (float)(Math.random() * 0.2D);
                        look = look.scale(offset);
                        world.addParticle(new MAParticleType(ParticleInit.ARCANE.get()), player.getPosX() + look.x, player.getPosY(), player.getPosZ() + look.z, 0.0D, -0.05000000074505806D, 0.0D);
                        world.addParticle(new MAParticleType(ParticleInit.ARCANE.get()), player.getPosX() - look.x, player.getPosY(), player.getPosZ() - look.z, 0.0D, -0.05000000074505806D, 0.0D);
                    } else {
                        m.getCastingResource().consume(CommonConfigs.getFlyManaCostPerTick());
                    }
                }

                //Actual flying
                if (m.getCastingResource().getAmount() < CommonConfigs.getFlyManaCostPerTick()) {
                    ManaAndArtifice.instance.proxy.setFlightEnabled(player, false);
                } else {
                    ManaAndArtifice.instance.proxy.setFlightEnabled(player, true);
                    if (!player.isCreative() && !player.isSpectator()) {
                        ManaAndArtifice.instance.proxy.setFlySpeed(player, level*CommonConfigs.getFlySpeedPerLevel());
                        if (!CommonConfigs.FLY_ALLOW_SPRTINTING_WHILE_FLYING.get() && player.abilities.isFlying) {
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
