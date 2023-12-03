package de.joh.dragonmagicandrelics.armorupgrades.init;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.sound.SFX;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.armorupgrades.types.ArmorUpgrade;
import de.joh.dragonmagicandrelics.armorupgrades.types.ArmorUpgradeOnTick;
import de.joh.dragonmagicandrelics.config.CommonConfigs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This upgrade increases the jump height of the wearer of the Dragon Mage Armor.
 * Effect only applies while sprinting.
 * @see de.joh.dragonmagicandrelics.events.CommonEventHandler
 * @author Joh0210
 */
public class ArmorUpgradeJump extends ArmorUpgradeOnTick {
    private static final AttributeModifier stepMod1 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_step_bonus_1", 0.5f, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier stepMod2 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_step_bonus_2", 0.5f, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier stepMod3 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_step_bonus_3", 0.5f, AttributeModifier.Operation.ADDITION);

    @Override
    public @Nullable ArmorUpgrade getStrongerAlternative() {
        return ArmorUpgradeInit.BURNING_FRENZY;
    }

    public ArmorUpgradeJump(@NotNull ResourceLocation registryName, int upgradeCost) {
        super(registryName, 2, false, true, upgradeCost); //false --> onTick would have to be reworked.
    }

    @Override
    public void onTick(Level world, Player player, int level, IPlayerMagic magic) {
        if (player.isSprinting() && magic != null && magic.getCastingResource().hasEnoughAbsolute(player, CommonConfigs.getSpeedManaCostPerTickPerLevel() * level)) {
            magic.getCastingResource().consume(player, CommonConfigs.getSpeedManaCostPerTickPerLevel() * level);
            if (!player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).hasModifier(stepMod1)){
                if(level >= 1){
                    player.playSound(SFX.Event.Artifact.DEMON_ARMOR_SPRINT_START, 1.0F, 0.8F);
                    player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).addTransientModifier(stepMod1);

                    if(!player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).hasModifier(stepMod2) && level >= 2){
                        player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).addTransientModifier(stepMod2);

                        if(!player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).hasModifier(stepMod3) &&  level >= 3){
                            player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).addTransientModifier(stepMod3);
                        }
                    }
                }
            } 
        }else{
            player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).removeModifier(stepMod1);
            player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).removeModifier(stepMod2);
            player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).removeModifier(stepMod3);
        }
    }
}