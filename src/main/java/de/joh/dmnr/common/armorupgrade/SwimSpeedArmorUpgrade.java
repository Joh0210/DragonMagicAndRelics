package de.joh.dmnr.common.armorupgrade;

import de.joh.dmnr.api.armorupgrade.OnEquippedArmorUpgrade;
import de.joh.dmnr.common.init.ArmorUpgradeInit;
import de.joh.dmnr.common.util.CommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

/**
 * Makes the wearer of the Dragon Mage Armor significantly faster when swimming.
 * However, this consumes a small amount of mana.
 * Function and constructor are initialized versions of parent class function/constructor.
 * Configurable in the CommonConfigs.
 * @see CommonConfig
 * @see ArmorUpgradeInit
 * @author Joh0210
 */
public class SwimSpeedArmorUpgrade extends OnEquippedArmorUpgrade {
    private static final AttributeModifier swimBoost1 = new AttributeModifier("mma_armor_swim_boost_1", 0.5, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier swimBoost2 = new AttributeModifier("mma_armor_swim_boost_2", 0.5, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier swimBoost3 = new AttributeModifier("mma_armor_swim_boost_3", 0.5, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier swimBoost4 = new AttributeModifier("mma_armor_swim_boost_4", 0.5, AttributeModifier.Operation.ADDITION);

    public SwimSpeedArmorUpgrade(@NotNull ResourceLocation registryName, int maxUpgradeLevel, RegistryObject<Item> upgradeSealItem, int upgradeCost) {
        super(registryName, maxUpgradeLevel, upgradeSealItem, false, true, upgradeCost);
    }

    @Override
    public void onEquip(Player player, int level) {
        AttributeInstance swimSpeed = player.getAttribute(ForgeMod.SWIM_SPEED.get());

        if (!swimSpeed.hasModifier(swimBoost1) && level >= 1){
            swimSpeed.addTransientModifier(swimBoost1);

            if(!swimSpeed.hasModifier(swimBoost2) && level >= 2){
                swimSpeed.addTransientModifier(swimBoost2);

                if(!swimSpeed.hasModifier(swimBoost3) && level >= 3){
                    swimSpeed.addTransientModifier(swimBoost3);

                    if(!swimSpeed.hasModifier(swimBoost4) && level >= 4){
                        swimSpeed.addTransientModifier(swimBoost4);
                    }
                }
            }
        }
    }

    @Override
    public void onRemove(Player player) {
        AttributeInstance swimSpeed = player.getAttribute(ForgeMod.SWIM_SPEED.get());

        swimSpeed.removeModifier(swimBoost1);
        swimSpeed.removeModifier(swimBoost2);
        swimSpeed.removeModifier(swimBoost3);
        swimSpeed.removeModifier(swimBoost4);
    }
}
