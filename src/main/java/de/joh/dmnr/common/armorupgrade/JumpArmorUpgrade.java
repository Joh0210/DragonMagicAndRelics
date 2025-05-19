package de.joh.dmnr.common.armorupgrade;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.sound.SFX;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.api.armorupgrade.OnTickArmorUpgrade;
import de.joh.dmnr.common.event.CommonEventHandler;
import de.joh.dmnr.common.util.CommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

/**
 * This upgrade increases the jump height of the wearer of the Dragon Mage Armor.
 * Effect only applies while sprinting.
 * @see CommonEventHandler
 * @author Joh0210
 */
public class JumpArmorUpgrade extends OnTickArmorUpgrade {
    private static final AttributeModifier stepMod1 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_step_bonus_1", 0.5f, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier stepMod2 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_step_bonus_2", 0.5f, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier stepMod3 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_step_bonus_3", 0.5f, AttributeModifier.Operation.ADDITION);

    public JumpArmorUpgrade(@NotNull ResourceLocation registryName, RegistryObject<Item> upgradeSealItem, int upgradeCost) {
        super(registryName, 2, upgradeSealItem, false, true, upgradeCost); //false --> onTick would have to be reworked.
    }

    @Override
    public void onTick(Level world, Player player, int level, IPlayerMagic magic) {
        if (player.isSprinting() && magic != null && magic.getCastingResource().hasEnoughAbsolute(player, CommonConfig.getSpeedManaCostPerTickPerLevel() * level)) {
            magic.getCastingResource().consume(player, CommonConfig.getSpeedManaCostPerTickPerLevel() * level);
            if (!player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).hasModifier(stepMod1)){
                if(level >= 1){

                    player.level().playSeededSound(null, player.getX(), player.getY(), player.getZ(), SFX.Event.Artifact.DEMON_ARMOR_SPRINT_START, SoundSource.PLAYERS, 1f, 0.8F, 0);
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
