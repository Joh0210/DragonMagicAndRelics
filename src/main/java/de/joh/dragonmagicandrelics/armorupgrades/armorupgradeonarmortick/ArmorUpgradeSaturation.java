package de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonarmortick;

import com.mna.api.capabilities.IPlayerMagic;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Feeds the wearer of Dragon Mage Armor, but requires a certain amount of mana to do so.
 * Function and constructor are initialized versions of parent class function/constructor.
 * @see de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit
 * @author Joh0210
 */
public class ArmorUpgradeSaturation extends IArmorUpgradeOnArmorTick {
    /**
     * The amount of saturation that fills up when the wearer's hunger fills up.
     */
    private static int ARMOR_SATURATION = 6;
    private static int MANA_PER_NUTRITION = 15;

    public ArmorUpgradeSaturation(String upgradeId, int maxUpgradeLevel) {
        super(upgradeId, maxUpgradeLevel);
    }

    @Override
    public void onArmorTick(Level world, Player player, int level, IPlayerMagic magic) {
        if(level > 0){
            // Only if the wearer is actually hungry
            if (player.getFoodData().getFoodLevel()  < 20 && magic != null && magic.getCastingResource().hasEnoughAbsolute(player, MANA_PER_NUTRITION)) {
                player.getFoodData().eat(1, ARMOR_SATURATION);
                magic.getCastingResource().consume(player, MANA_PER_NUTRITION);
            }
        }
    }
}
