package de.joh.dragonmagicandrelics.armorupgrades.init;

import de.joh.dragonmagicandrelics.armorupgrades.types.IArmorUpgradeOnEquipped;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * If upgrades of this type are installed, the player receives a certain number of additional HP.
 * Function and constructor are initialized versions of parent class function/constructor.
 * @see de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit
 * @author Joh0210
 */
public class ArmorUpgradeHealthBoost extends IArmorUpgradeOnEquipped {
    private static final AttributeModifier healthBoost1 = new AttributeModifier("mma_armor_health_boost_1", 4, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier healthBoost2 = new AttributeModifier("mma_armor_health_boost_2", 4, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier healthBoost3 = new AttributeModifier("mma_armor_health_boost_3", 4, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier healthBoost4 = new AttributeModifier("mma_armor_health_boost_4", 4, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier healthBoost5 = new AttributeModifier("mma_armor_health_boost_5", 4, AttributeModifier.Operation.ADDITION);

    public ArmorUpgradeHealthBoost(@NotNull ResourceLocation registryName, int maxUpgradeLevel) {
        super(registryName, maxUpgradeLevel, false);
    }

    @Override
    public void onEquip(Player player, int level) {
        if (!player.getAttribute(Attributes.MAX_HEALTH).hasModifier(healthBoost1) && level >= 1){
            player.getAttribute(Attributes.MAX_HEALTH).addTransientModifier(healthBoost1);

            if(!player.getAttribute(Attributes.MAX_HEALTH).hasModifier(healthBoost2) && level >= 2){
                player.getAttribute(Attributes.MAX_HEALTH).addTransientModifier(healthBoost2);

                if(!player.getAttribute(Attributes.MAX_HEALTH).hasModifier(healthBoost3) && level >= 3){
                    player.getAttribute(Attributes.MAX_HEALTH).addTransientModifier(healthBoost3);

                    if(!player.getAttribute(Attributes.MAX_HEALTH).hasModifier(healthBoost4) && level >= 4){
                        player.getAttribute(Attributes.MAX_HEALTH).addTransientModifier(healthBoost4);

                        if(!player.getAttribute(Attributes.MAX_HEALTH).hasModifier(healthBoost5) && level >= 5){
                            player.getAttribute(Attributes.MAX_HEALTH).addTransientModifier(healthBoost5);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onRemove(Player player) {
        player.getAttribute(Attributes.MAX_HEALTH).removeModifier(healthBoost1);
        player.getAttribute(Attributes.MAX_HEALTH).removeModifier(healthBoost2);
        player.getAttribute(Attributes.MAX_HEALTH).removeModifier(healthBoost3);
        player.getAttribute(Attributes.MAX_HEALTH).removeModifier(healthBoost4);
        player.getAttribute(Attributes.MAX_HEALTH).removeModifier(healthBoost5);

        player.setHealth(Math.min(player.getMaxHealth(), player.getHealth()));
    }
}
