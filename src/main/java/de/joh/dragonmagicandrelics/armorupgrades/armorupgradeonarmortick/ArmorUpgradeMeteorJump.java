package de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonarmortick;

import com.ma.api.capabilities.IPlayerMagic;
import com.ma.api.particles.MAParticleType;
import com.ma.api.particles.ParticleInit;
import com.ma.api.sound.SFX;
import com.ma.config.GeneralModConfig;
import com.ma.entities.utility.MAExplosion;
import de.joh.dragonmagicandrelics.config.CommonConfigs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * If the wielder shifts during a sprint jump, it lands like a metor on the ground and creates an explosion
 * This consumes a small amount of mana
 * Function and constructor are initialized versions of parent class function/constructor.
 * Configurable in the CommonConfigs.
 * @see CommonConfigs
 * @see de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit
 * @author Joh0210
 */
public class ArmorUpgradeMeteorJump extends IArmorUpgradeOnArmorTick {
    private static final int reqHeight = 5;

    public ArmorUpgradeMeteorJump(String upgradeId, int maxUpgradeLevel) {
        super(upgradeId, maxUpgradeLevel);
    }

    @Override
    public void onArmorTick(World world, PlayerEntity player, int level, IPlayerMagic magic) {
        if (!player.isOnGround() && player.getMotion().y < 0.0D && player.isCrouching() && magic.getCastingResource().getAmount() > CommonConfigs.METEOR_JUMP_MANA_COST.get() && !player.getPersistentData().contains("dmnr_meteor_jumping") && player.isSprinting()) {
            int heightAboveGround = 0;
            for(BlockPos pos = player.getPosition(); player.world.isAirBlock(pos) && heightAboveGround < reqHeight; ++heightAboveGround) {
                pos = pos.down();
            }

            if (heightAboveGround >= reqHeight) {
                player.getPersistentData().putBoolean("dmnr_meteor_jumping", true);
                player.playSound(SFX.Event.Artifact.METEOR_JUMP, 0.25F, 0.8F);
                magic.getCastingResource().consume(CommonConfigs.METEOR_JUMP_MANA_COST.get());
            }
        }

        if (player.getPersistentData().contains("dmnr_meteor_jumping")) {
            if (player.isOnGround()) {
                handlePlayerMeteorJumpImpact(player);
            }

            player.addVelocity(0.0D, -0.05D, 0.0D);
            if (player.world.isRemote) {
                for(int i = 0; i < 25; ++i) {
                    player.world.addParticle(new MAParticleType(ParticleInit.FLAME.get()), player.getPosX() - 0.5D + Math.random() * 0.5D, player.getPosY() + Math.random(), player.getPosZ() - 0.5D + Math.random() * 0.5D, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    /**
     * Produces the explosion upon player impact.
     */
    private void handlePlayerMeteorJumpImpact(PlayerEntity player) {
        if (player.getPersistentData().contains("dmnr_meteor_jumping")) {
            player.getPersistentData().remove("dmnr_meteor_jumping");
            player.setSprinting(false);
            if (!player.world.isRemote) {
                MAExplosion.make(player, (ServerWorld) player.world, player.getPosX(), player.getPosY(), player.getPosZ(), CommonConfigs.METEOR_JUMP_IMPACT.get(), CommonConfigs.METEOR_JUMP_IMPACT.get()*4, GeneralModConfig.MA_METEOR_JUMP.get() && ((ServerWorld)player.world).getServer().getGameRules().getBoolean(GameRules.MOB_GRIEFING) ? Explosion.Mode.BREAK : Explosion.Mode.NONE);
            }
        }
    }
}
