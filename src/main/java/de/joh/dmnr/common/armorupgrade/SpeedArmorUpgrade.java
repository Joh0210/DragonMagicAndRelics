package de.joh.dmnr.common.armorupgrade;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.sound.SFX;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.init.ArmorUpgradeInit;
import de.joh.dmnr.api.armorupgrade.ArmorUpgrade;
import de.joh.dmnr.api.armorupgrade.OnTickArmorUpgrade;
import de.joh.dmnr.common.util.CommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

/**
 * Makes the wearer of the Dragon Mage Armor significantly faster when sprinting.
 * However, this consumes a small amount of mana.
 * Function and constructor are initialized versions of parent class function/constructor.
 * Configurable in the CommonConfigs.
 * @see CommonConfig
 * @see ArmorUpgradeInit
 * @author Joh0210
 */
public class SpeedArmorUpgrade extends OnTickArmorUpgrade {
    private static final AttributeModifier runSpeed1 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_speed_bonus_1", 0.025f, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier runSpeed2 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_speed_bonus_2", 0.025f, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier runSpeed3 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_speed_bonus_3", 0.025f, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier runSpeed4 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_speed_bonus_4", 0.025f, AttributeModifier.Operation.ADDITION);

    public SpeedArmorUpgrade(ResourceLocation upgradeId, RegistryObject<Item> upgradeSealItem, int upgradeCost) {
        super(upgradeId, 3, upgradeSealItem, false, true, upgradeCost);
    }

    @Override
    public @Nullable ArmorUpgrade getStrongerAlternative() {
        return ArmorUpgradeInit.BURNING_FRENZY;
    }

    @Override
    public void onTick(Level world, Player player, int level, IPlayerMagic magic) {
        if (player.isSprinting() && magic != null && magic.getCastingResource().hasEnoughAbsolute(player, CommonConfig.getSpeedManaCostPerTickPerLevel() * level)) {
            magic.getCastingResource().consume(player, CommonConfig.getSpeedManaCostPerTickPerLevel() * level);
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
