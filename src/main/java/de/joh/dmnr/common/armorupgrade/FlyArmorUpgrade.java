package de.joh.dmnr.common.armorupgrade;

import com.mna.ManaAndArtifice;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.effects.EffectInit;
import de.joh.dmnr.common.init.ArmorUpgradeInit;
import de.joh.dmnr.api.armorupgrade.ArmorUpgrade;
import de.joh.dmnr.api.armorupgrade.OnTickArmorUpgrade;
import de.joh.dmnr.common.util.CommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Allows the wearer of Dragon Mage Armor to fly on every tick.
 * However, this requires a little bit of mana.
 * Function and constructor are initialized versions of parent class function/constructor.
 * Configurable in the CommonConfigs.
 * @see CommonConfig
 * @see ArmorUpgradeInit
 * @author Joh0210
 */
public class FlyArmorUpgrade extends OnTickArmorUpgrade {
    public FlyArmorUpgrade(@NotNull ResourceLocation registryName, RegistryObject<Item> upgradeSealItem, int upgradeCost) {
        super(registryName, 1, upgradeSealItem, true, upgradeCost);
    }

    @Override
    public @Nullable ArmorUpgrade getStrongerAlternative() {
        return ArmorUpgradeInit.ANGEL_FLIGHT;
    }

    @Override
    public void onTick(Level world, Player player, int level, IPlayerMagic magic) {
        if (0 < level && !player.hasEffect(de.joh.dmnr.common.init.EffectInit.FLY_DISABLED.get())) {
            player.getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> {
                //Creation of particles
                if (player.getAbilities().flying && !player.hasEffect(EffectInit.LEVITATION.get())) {
                    if (world.isClientSide) {
                        Vec3 look = player.getForward().cross(new Vec3(0.0D, 1.0D, 0.0D));
                        float offset = (float)(Math.random() * 0.2D);
                        look = look.scale(offset);
                        world.addParticle(new MAParticleType(ParticleInit.ARCANE.get()), player.getX() + look.x, player.getY(), player.getZ() + look.z, 0.0D, -0.05D, 0.0D);
                        world.addParticle(new MAParticleType(ParticleInit.ARCANE.get()), player.getX() - look.x, player.getY(), player.getZ() - look.z, 0.0D, -0.05D, 0.0D);
                    }
                }

                //Actual flying
                ManaAndArtifice.instance.proxy.setFlightEnabled(player, true);
                if (!player.isCreative() && !player.isSpectator()) {
                    ManaAndArtifice.instance.proxy.setFlySpeed(player, level* CommonConfig.getFlySpeedPerLevel(player));
                    if (!CommonConfig.FLY_ALLOW_SPRTINTING_WHILE_FLYING.get() && player.getAbilities().flying) {
                        player.setSprinting(false);
                    }
                } else {
                    ManaAndArtifice.instance.proxy.setFlySpeed(player, 0.05F);
                }
            });
        }
    }

    @Override
    public void onRemove(Player player) {
        ManaAndArtifice.instance.proxy.setFlySpeed(player, 0.05F);
        ManaAndArtifice.instance.proxy.setFlightEnabled(player, false);
    }
}
