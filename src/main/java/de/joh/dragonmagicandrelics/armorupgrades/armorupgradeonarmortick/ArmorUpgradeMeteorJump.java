package de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonarmortick;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.config.GeneralModConfig;
import com.mna.entities.utility.MAExplosion;
import de.joh.dragonmagicandrelics.config.CommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

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
    public void onArmorTick(Level world, Player player, int level, IPlayerMagic magic) {
        if (level > 0 && !player.isOnGround() && player.getDeltaMovement().y < 0.0D && !player.isFallFlying() && player.isCrouching() && magic.getCastingResource().hasEnough(player, CommonConfigs.METEOR_JUMP_MANA_COST.get()) && !player.getPersistentData().contains("dmnr_meteor_jumping") && player.isSprinting()) {
            int heightAboveGround = 0;
            for(BlockPos pos = player.blockPosition(); player.level.isEmptyBlock(pos) && heightAboveGround < reqHeight; ++heightAboveGround) {
                pos = pos.below();
            }

            if (heightAboveGround >= reqHeight) {
                player.getPersistentData().putBoolean("dmnr_meteor_jumping", true);
                player.playSound(SFX.Event.Artifact.METEOR_JUMP, 0.25F, 0.8F);
                magic.getCastingResource().consume(player, CommonConfigs.METEOR_JUMP_MANA_COST.get());
            }
        }

        if (player.getPersistentData().contains("dmnr_meteor_jumping")) {
            if (player.isOnGround()) {
                handlePlayerMeteorJumpImpact(player);
            }

            player.push(0.0D, -0.05D, 0.0D);
            if (player.level.isClientSide) {
                for(int i = 0; i < 25; ++i) {
                    player.level.addParticle(new MAParticleType(ParticleInit.FLAME.get()), player.getX() - 0.5D + Math.random() * 0.5D, player.getY() + Math.random(), player.getZ() - 0.5D + Math.random() * 0.5D, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    /**
     * Produces the explosion upon player impact.
     */
    private void handlePlayerMeteorJumpImpact(Player player) {
        if (player.getPersistentData().contains("dmnr_meteor_jumping")) {
            player.getPersistentData().remove("dmnr_meteor_jumping");
            player.setSprinting(false);
            if (!player.level.isClientSide) {
                MAExplosion.make(player, (ServerLevel)player.level, player.getX(), player.getY(), player.getZ(), CommonConfigs.METEOR_JUMP_IMPACT.get(), CommonConfigs.METEOR_JUMP_IMPACT.get()*4, true, GeneralModConfig.MA_METEOR_JUMP.get() && ((ServerLevel)player.level).getServer().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? Explosion.BlockInteraction.BREAK : Explosion.BlockInteraction.NONE);

            }
        }
    }
}
