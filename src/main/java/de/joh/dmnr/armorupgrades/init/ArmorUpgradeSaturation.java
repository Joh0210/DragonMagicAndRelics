package de.joh.dmnr.armorupgrades.init;

import com.mna.api.capabilities.IPlayerMagic;
import de.joh.dmnr.armorupgrades.types.ArmorUpgradeOnTick;
import de.joh.dmnr.config.CommonConfigs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Feeds the wearer of Dragon Mage Armor, but requires a certain amount of mana to do so.
 * Function and constructor are initialized versions of parent class function/constructor.
 * Configurable in the CommonConfigs.
 * @see CommonConfigs
 * @see de.joh.dmnr.armorupgrades.ArmorUpgradeInit
 * @author Joh0210
 */
public class ArmorUpgradeSaturation extends ArmorUpgradeOnTick {

    public ArmorUpgradeSaturation(@NotNull ResourceLocation registryName, int upgradeCost) {
        super(registryName, 1, false, upgradeCost);
    }

    @Override
    public void onTick(Level world, Player player, int level, IPlayerMagic magic) {
        if(level > 0){
            // Only if the wearer is actually hungry
            if (player.canEat(false) && magic != null && magic.getCastingResource().hasEnoughAbsolute(player, CommonConfigs.SATURATION_MANA_PER_NUTRITION.get())) {
                player.getFoodData().eat(1, 1);
                magic.getCastingResource().consume(player, CommonConfigs.SATURATION_MANA_PER_NUTRITION.get());
            }
        }
    }
}