package de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonarmortick;

import com.ma.api.capabilities.IPlayerMagic;
import com.ma.api.particles.MAParticleType;
import com.ma.api.particles.ParticleInit;
import com.ma.api.sound.SFX;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.config.CommonConfigs;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

/**
 * Makes the wearer of the Dragon Mage Armor significantly faster when sprinting.
 * However, this consumes a small amount of mana.
 * Function and constructor are initialized versions of parent class function/constructor.
 * Configurable in the CommonConfigs.
 * @see CommonConfigs
 * @see de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit
 * @author Joh0210
 */
public class ArmorUpgradeSpeed extends IArmorUpgradeOnArmorTick {
    private static final AttributeModifier runSpeed1 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_speed_bonus_1", 0.05f, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier runSpeed2 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_speed_bonus_2", 0.05f, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier runSpeed3 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_speed_bonus_3", 0.05f, AttributeModifier.Operation.ADDITION);

    public ArmorUpgradeSpeed(String upgradeId, int maxUpgradeLevel) {
        super(upgradeId, maxUpgradeLevel);
    }

    @Override
    public void onArmorTick(World world, PlayerEntity player, int level, IPlayerMagic magic) {
        boolean showParticles = false;

        if (player.isSprinting() && magic != null && magic.getCastingResource().getAmount() > CommonConfigs.getSpeedManaCostPerTickPerLevel() * level) {
            magic.getCastingResource().consume(CommonConfigs.getSpeedManaCostPerTickPerLevel() * level);
            if (!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(runSpeed1)){
                if(level >= 1){
                    player.playSound(SFX.Event.Artifact.DEMON_ARMOR_SPRINT_START, 1.0F, 0.8F);
                    showParticles = true;
                    player.getAttribute(Attributes.MOVEMENT_SPEED).applyNonPersistentModifier(runSpeed1);

                    if(!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(runSpeed2) && level >= 2){
                        player.getAttribute(Attributes.MOVEMENT_SPEED).applyNonPersistentModifier(runSpeed2);

                        if(!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(runSpeed3) &&  level >= 3){
                            player.getAttribute(Attributes.MOVEMENT_SPEED).applyNonPersistentModifier(runSpeed3);
                        }
                    }
                }
            } else{
                showParticles = true;
            }

            //Particles
            if (world.isRemote && showParticles) {
                Vector3d motion = player.getMotion();
                Vector3d look = player.getForward().crossProduct(new Vector3d(0.0D, 1.0D, 0.0D));
                float offset = (float)(Math.random() * 0.2D);
                float yOffset = 0.2F;
                look = look.scale(offset);

                for(int i = 0; i < 5; ++i) {
                    world.addParticle(new MAParticleType(ParticleInit.FLAME.get()), player.getPosX() + look.x + Math.random() * motion.x * 2.0D, player.getPosY() + (double)yOffset + Math.random() * motion.y * 2.0D, player.getPosZ() + look.z + Math.random() * motion.z * 2.0D, 0.0D, 0.0D, 0.0D);
                    world.addParticle(new MAParticleType(ParticleInit.FLAME.get()), player.getPosX() - look.x + Math.random() * motion.x * 2.0D, player.getPosY() + (double)yOffset + Math.random() * motion.y * 2.0D, player.getPosZ() - look.z + Math.random() * motion.z * 2.0D, 0.0D, 0.0D, 0.0D);
                }
            }
        }else{
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(runSpeed1);
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(runSpeed2);
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(runSpeed3);
        }
    }
}
