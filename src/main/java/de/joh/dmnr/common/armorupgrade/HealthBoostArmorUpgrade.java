package de.joh.dmnr.common.armorupgrade;

import de.joh.dmnr.api.armorupgrade.OnEquippedArmorUpgrade;
import de.joh.dmnr.common.init.ArmorUpgradeInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

/**
 * If upgrades of this type are installed, the player receives a certain number of additional HP.
 * Function and constructor are initialized versions of parent class function/constructor.
 * @see ArmorUpgradeInit
 * @author Joh0210
 */
public class HealthBoostArmorUpgrade extends OnEquippedArmorUpgrade {
    private static final AttributeModifier healthBoost1 = new AttributeModifier("mma_armor_health_boost_1", 4, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier healthBoost2 = new AttributeModifier("mma_armor_health_boost_2", 4, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier healthBoost3 = new AttributeModifier("mma_armor_health_boost_3", 4, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier healthBoost4 = new AttributeModifier("mma_armor_health_boost_4", 4, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier healthBoost5 = new AttributeModifier("mma_armor_health_boost_5", 4, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier healthBoost6 = new AttributeModifier("mma_armor_health_boost_6", 4, AttributeModifier.Operation.ADDITION);

    public HealthBoostArmorUpgrade(@NotNull ResourceLocation registryName, RegistryObject<Item> upgradeSealItem, int upgradeCost) {
        super(registryName, 5, upgradeSealItem, false, true, upgradeCost);
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

                            if(!player.getAttribute(Attributes.MAX_HEALTH).hasModifier(healthBoost6) && level >= 6){
                                player.getAttribute(Attributes.MAX_HEALTH).addTransientModifier(healthBoost6);
                            }
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
        player.getAttribute(Attributes.MAX_HEALTH).removeModifier(healthBoost6);

        player.setHealth(Math.min(player.getMaxHealth(), player.getHealth()));
    }
}
