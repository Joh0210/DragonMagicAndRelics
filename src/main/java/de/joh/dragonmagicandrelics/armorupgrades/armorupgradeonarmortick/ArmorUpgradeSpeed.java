package de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonarmortick;

import com.mna.api.capabilities.IPlayerMagic;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * Makes the wearer of the Dragon Mage Armor significantly faster when sprinting.
 * However, this consumes a small amount of mana
 * Function and constructor are initialized versions of parent class function/constructor.
 * @see de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit
 * @author Joh0210
 */
public class ArmorUpgradeSpeed extends IArmorUpgradeOnArmorTick {
    private static final AttributeModifier runSpeed1 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_speed_bonus_1", 0.05f, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier runSpeed2 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_speed_bonus_2", 0.05f, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier runSpeed3 = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_armor_speed_bonus_3", 0.05f, AttributeModifier.Operation.ADDITION);
    private static float MANA_PRO_SPEED_LEVEL = 0.25f;

    public ArmorUpgradeSpeed(String upgradeId, int maxUpgradeLevel) {
        super(upgradeId, maxUpgradeLevel);
    }

    @Override
    public void onArmorTick(Level world, Player player, int level, IPlayerMagic magic) {
        if (player.isSprinting() && magic != null && magic.getCastingResource().hasEnoughAbsolute(player, MANA_PRO_SPEED_LEVEL)) {
            magic.getCastingResource().consume(player, MANA_PRO_SPEED_LEVEL * level);
            if (!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(runSpeed1) && level >= 1){
                player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(runSpeed1);

                if(!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(runSpeed2) && level >= 2){
                    player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(runSpeed2);

                    if(!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(runSpeed3) &&  level >= 3){
                        player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(runSpeed3);
                    }
                }
            }
        }else{
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(runSpeed1);
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(runSpeed2);
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(runSpeed3);
        }
    }
}
