package de.joh.dmnr.common.armorupgrade;

import de.joh.dmnr.api.armorupgrade.OnEquippedArmorUpgrade;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;

/**
 * Increases the range in which the user can interact
 * @author Joh0210
 */
public class ReachDistanceArmorUpgrade extends OnEquippedArmorUpgrade {
    private static final AttributeModifier reachBoost1 = new AttributeModifier("mma_armor_reach_boost_1", 1, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier reachBoost2 = new AttributeModifier("mma_armor_reach_boost_2", 1, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier reachBoost3 = new AttributeModifier("mma_armor_reach_boost_3", 1, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier reachBoost4 = new AttributeModifier("mma_armor_reach_boost_4", 1, AttributeModifier.Operation.ADDITION);

    private static final AttributeModifier attackRangBoost1 = new AttributeModifier("mma_armor_attack_range_boost_1", 1, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier attackRangBoost2 = new AttributeModifier("mma_armor_attack_range_boost_2", 1, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier attackRangBoost3 = new AttributeModifier("mma_armor_attack_range_boost_3", 1, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier attackRangBoost4 = new AttributeModifier("mma_armor_attack_range_boost_4", 1, AttributeModifier.Operation.ADDITION);


    public ReachDistanceArmorUpgrade(@NotNull ResourceLocation registryName, int upgradeCost) {
        super(registryName, 2, false, true, upgradeCost);
    }

    @Override
    public void onEquip(Player player, int level) {
        AttributeInstance reach = player.getAttribute(ForgeMod.BLOCK_REACH.get());

        if (!reach.hasModifier(reachBoost1) && level >= 1) {
            reach.addTransientModifier(reachBoost1);

            if (!reach.hasModifier(reachBoost2) && level >= 2) {
                reach.addTransientModifier(reachBoost2);

                if (!reach.hasModifier(reachBoost3) && level >= 3) {
                    reach.addTransientModifier(reachBoost3);

                    if (!reach.hasModifier(reachBoost4) && level >= 4) {
                        reach.addTransientModifier(reachBoost4);
                    }
                }
            }
        }

        AttributeInstance attackRange = player.getAttribute(ForgeMod.ENTITY_REACH.get());

        if (!attackRange.hasModifier(attackRangBoost1) && level >= 1) {
            attackRange.addTransientModifier(attackRangBoost1);

            if (!attackRange.hasModifier(attackRangBoost2) && level >= 2) {
                attackRange.addTransientModifier(attackRangBoost2);

                if (!attackRange.hasModifier(attackRangBoost3) && level >= 3) {
                    attackRange.addTransientModifier(attackRangBoost3);

                    if (!attackRange.hasModifier(attackRangBoost4) && level >= 4) {
                        attackRange.addTransientModifier(attackRangBoost4);
                    }
                }
            }
        }
    }

    @Override
    public void onRemove(Player player) {
        AttributeInstance reach = player.getAttribute(ForgeMod.BLOCK_REACH.get());
        AttributeInstance attackRange = player.getAttribute(ForgeMod.ENTITY_REACH.get());

        reach.removeModifier(reachBoost1);
        reach.removeModifier(reachBoost2);
        reach.removeModifier(reachBoost3);
        reach.removeModifier(reachBoost4);
        attackRange.removeModifier(attackRangBoost1);
        attackRange.removeModifier(attackRangBoost2);
        attackRange.removeModifier(attackRangBoost3);
        attackRange.removeModifier(attackRangBoost4);
    }
}