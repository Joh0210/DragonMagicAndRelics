package de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonarmortick;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

/**
 * Makes the wearer of the Dragon Mage Armor significantly faster when sprinting.
 * However, this consumes a small amount of mana.
 * Function and constructor are initialized versions of parent class function/constructor.
 * @see de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit
 * @author Joh0210
 */
public class ArmorUpgradeSpeed extends IArmorUpgradeOnArmorTick {
    private static final AttributeModifier runSpeed1 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_speed_bonus_1", 0.05f, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier runSpeed2 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_speed_bonus_2", 0.05f, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier runSpeed3 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_speed_bonus_3", 0.05f, AttributeModifier.Operation.ADDITION);
    private static final float MANA_PRO_SPEED_LEVEL = 0.25f;

    public ArmorUpgradeSpeed(String upgradeId, int maxUpgradeLevel) {
        super(upgradeId, maxUpgradeLevel);
    }

    @Override
    public void onArmorTick(Level world, Player player, int level, IPlayerMagic magic) {
        boolean showParticles = false;

        if (player.isSprinting() && magic != null && magic.getCastingResource().hasEnoughAbsolute(player, MANA_PRO_SPEED_LEVEL * level)) {
            magic.getCastingResource().consume(player, MANA_PRO_SPEED_LEVEL * level);
            if (!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(runSpeed1)){
                if(level >= 1){
                    player.playSound(SFX.Event.Artifact.DEMON_ARMOR_SPRINT_START, 1.0F, 0.8F);
                    showParticles = true;
                    player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(runSpeed1);

                    if(!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(runSpeed2) && level >= 2){
                        player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(runSpeed2);

                        if(!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(runSpeed3) &&  level >= 3){
                            player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(runSpeed3);
                        }
                    }
                }
            } else{
                showParticles = true;
            }

            //Particles
            if (world.isClientSide && showParticles) {
                Vec3 motion = player.getDeltaMovement();
                Vec3 look = player.getForward().cross(new Vec3(0.0D, 1.0D, 0.0D));
                float offset = (float)(Math.random() * 0.2D);
                float yOffset = 0.2F;
                look = look.scale((double)offset);

                for(int i = 0; i < 5; ++i) {
                    world.addParticle(new MAParticleType(ParticleInit.FLAME.get()), player.getX() + look.x + Math.random() * motion.x * 2.0D, player.getY() + (double)yOffset + Math.random() * motion.y * 2.0D, player.getZ() + look.z + Math.random() * motion.z * 2.0D, 0.0D, 0.0D, 0.0D);
                    world.addParticle(new MAParticleType(ParticleInit.FLAME.get()), player.getX() - look.x + Math.random() * motion.x * 2.0D, player.getY() + (double)yOffset + Math.random() * motion.y * 2.0D, player.getZ() - look.z + Math.random() * motion.z * 2.0D, 0.0D, 0.0D, 0.0D);
                }
            }
        }else{
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(runSpeed1);
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(runSpeed2);
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(runSpeed3);
        }
    }
}
