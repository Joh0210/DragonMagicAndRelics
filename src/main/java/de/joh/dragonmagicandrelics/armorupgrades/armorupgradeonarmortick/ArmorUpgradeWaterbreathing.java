package de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonarmortick;

import com.mna.api.capabilities.IPlayerMagic;
import de.joh.dragonmagicandrelics.config.CommonConfigs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Allows the wearer of Dragon Mage Armor to breathe underwater.
 * Level 1 consumes mana while level 2 is free underwater breathing.
 * Function and constructor are initialized versions of parent class function/constructor.Configurable in the CommonConfigs.
 * @see CommonConfigs
 * @see de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit
 * @author Joh0210
 */
public class ArmorUpgradeWaterbreathing extends IArmorUpgradeOnArmorTick {
    public ArmorUpgradeWaterbreathing(String upgradeId, int maxUpgradeLevel) {
        super(upgradeId, maxUpgradeLevel);
    }

    @Override
    public void onArmorTick(Level world, Player player, int level, IPlayerMagic magic) {
        //25 is 1 bubble but fits like this
        if(level == 1){
            if (player.getAirSupply()  < 230 && magic != null && magic.getCastingResource().hasEnoughAbsolute(player, CommonConfigs.WATERBREATHING_MANA_PRO_OXIGEN_BUBBLE.get())) {
                player.setAirSupply(player.getAirSupply() + 25);
                magic.getCastingResource().consume(player, CommonConfigs.WATERBREATHING_MANA_PRO_OXIGEN_BUBBLE.get());
            }
        }
        else if(level == 2){
            if (player.getAirSupply()  < 230) {
                player.setAirSupply(player.getAirSupply() + 25);
            }
        }
    }
}
