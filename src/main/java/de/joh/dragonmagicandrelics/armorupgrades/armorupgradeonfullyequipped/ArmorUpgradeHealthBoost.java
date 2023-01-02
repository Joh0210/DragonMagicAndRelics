package de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonfullyequipped;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

//todo
/**
 * If upgrades of this type are installed, the player receives a certain number of additional HP.
 * Function and constructor are initialized versions of parent class function/constructor.
 * However, there is currently an error:
 * When you log in to the world again, the additional HP is no longer available.
 * You have to take your armor off and on again to get your life back.
 * The error is fixed at times, but the effect has been swapped out as a potion effect for the time being.
 * @see de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit
 * @author Joh0210
 */
public class ArmorUpgradeHealthBoost extends IArmorUpgradeOnFullyEquipped {
    private static final AttributeModifier healthBoost1 = new AttributeModifier("mma_armor_health_boost_1", 6, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier healthBoost2 = new AttributeModifier("mma_armor_health_boost_2", 4, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier healthBoost3 = new AttributeModifier("mma_armor_health_boost_3", 6, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier healthBoost4 = new AttributeModifier("mma_armor_health_boost_4", 4, AttributeModifier.Operation.ADDITION);

    public ArmorUpgradeHealthBoost(String upgradeId, int maxUpgradeLevel) {
        super(upgradeId, maxUpgradeLevel);
    }

    @Override
    public void applySetBonus(Player player, int level) {
        if (!player.getAttribute(Attributes.MAX_HEALTH).hasModifier(healthBoost1) && level >= 1){
            player.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(healthBoost1);

            if(!player.getAttribute(Attributes.MAX_HEALTH).hasModifier(healthBoost2) && level >= 2){
                player.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(healthBoost2);

                if(!player.getAttribute(Attributes.MAX_HEALTH).hasModifier(healthBoost3) && level >= 3){
                    player.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(healthBoost3);

                    if(!player.getAttribute(Attributes.MAX_HEALTH).hasModifier(healthBoost4) && level >= 4){
                        player.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(healthBoost4);
                    }
                }
            }
        } else {
            player.getAttribute(Attributes.MAX_HEALTH).removeModifier(healthBoost1);
            player.getAttribute(Attributes.MAX_HEALTH).removeModifier(healthBoost2);
            player.getAttribute(Attributes.MAX_HEALTH).removeModifier(healthBoost3);
            player.getAttribute(Attributes.MAX_HEALTH).removeModifier(healthBoost4);
        }
    }

    @Override
    public void removeSetBonus(Player player) {
        player.getAttribute(Attributes.MAX_HEALTH).removeModifier(healthBoost1);
        player.getAttribute(Attributes.MAX_HEALTH).removeModifier(healthBoost2);
        player.getAttribute(Attributes.MAX_HEALTH).removeModifier(healthBoost3);
        player.getAttribute(Attributes.MAX_HEALTH).removeModifier(healthBoost4);
    }
}
