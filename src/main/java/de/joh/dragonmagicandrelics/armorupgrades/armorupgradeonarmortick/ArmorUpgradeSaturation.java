package de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonarmortick;

import com.ma.api.capabilities.IPlayerMagic;
import de.joh.dragonmagicandrelics.config.CommonConfigs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

/**
 * Feeds the wearer of Dragon Mage Armor, but requires a certain amount of mana to do so.
 * Function and constructor are initialized versions of parent class function/constructor.
 * Configurable in the CommonConfigs.
 * @see CommonConfigs
 * @see de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit
 * @author Joh0210
 */
public class ArmorUpgradeSaturation extends IArmorUpgradeOnArmorTick {

    public ArmorUpgradeSaturation(String upgradeId, int maxUpgradeLevel) {
        super(upgradeId, maxUpgradeLevel);
    }

    @Override
    public void onArmorTick(World world, PlayerEntity player, int level, IPlayerMagic magic) {
        if(level > 0){
            // Only if the wearer is actually hungry
            if (player.getFoodStats().getFoodLevel()  < 20 && magic != null && magic.getCastingResource().getAmount() > CommonConfigs.SATURATION_MANA_PER_NUTRITION.get()) {
                player.getFoodStats().addStats(1, 1);
                magic.getCastingResource().consume(CommonConfigs.SATURATION_MANA_PER_NUTRITION.get());
            }
        }
    }
}
