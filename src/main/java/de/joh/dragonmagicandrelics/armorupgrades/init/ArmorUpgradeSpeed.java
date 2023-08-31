package de.joh.dragonmagicandrelics.armorupgrades.init;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.armorupgrades.types.ArmorUpgrade;
import de.joh.dragonmagicandrelics.armorupgrades.types.IArmorUpgradeOnTick;
import de.joh.dragonmagicandrelics.config.CommonConfigs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Makes the wearer of the Dragon Mage Armor significantly faster when sprinting.
 * However, this consumes a small amount of mana.
 * Function and constructor are initialized versions of parent class function/constructor.
 * Configurable in the CommonConfigs.
 * @see CommonConfigs
 * @see de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit
 * @author Joh0210
 */
public class ArmorUpgradeSpeed extends IArmorUpgradeOnTick {
    private static final AttributeModifier runSpeed1 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_speed_bonus_1", 0.025f, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier runSpeed2 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_speed_bonus_2", 0.025f, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier runSpeed3 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_speed_bonus_3", 0.025f, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier runSpeed4 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_speed_bonus_4", 0.025f, AttributeModifier.Operation.ADDITION);

    public ArmorUpgradeSpeed(ResourceLocation upgradeId, int upgradeCost) {
        super(upgradeId, 3, false, true, upgradeCost);
    }

    @Override
    public @Nullable ArmorUpgrade getStrongerAlternative() {
        return ArmorUpgradeInit.BURNING_FRENZY;
    }

    @Override
    public void onTick(Level world, Player player, int level, IPlayerMagic magic) {
        if (player.isSprinting() && magic != null && magic.getCastingResource().hasEnoughAbsolute(player, CommonConfigs.getSpeedManaCostPerTickPerLevel() * level)) {
            magic.getCastingResource().consume(player, CommonConfigs.getSpeedManaCostPerTickPerLevel() * level);
            if (!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(runSpeed1)){
                if(level >= 1){
                    player.playSound(SFX.Event.Artifact.DEMON_ARMOR_SPRINT_START, 1.0F, 0.8F);
                    player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(runSpeed1);

                    if(!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(runSpeed2) && level >= 2){
                        player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(runSpeed2);

                        if(!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(runSpeed3) &&  level >= 3){
                            player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(runSpeed3);

                            if(!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(runSpeed4) &&  level >= 4){
                                player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(runSpeed4);
                            }
                        }
                    }
                }
            }
        }else{
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(runSpeed1);
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(runSpeed2);
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(runSpeed3);
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(runSpeed4);
        }
    }
}
