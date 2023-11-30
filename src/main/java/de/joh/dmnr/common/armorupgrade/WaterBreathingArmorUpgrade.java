package de.joh.dmnr.common.armorupgrade;

import com.mna.api.capabilities.IPlayerMagic;
import de.joh.dmnr.api.armorupgrade.OnTickArmorUpgrade;
import de.joh.dmnr.common.init.ArmorUpgradeInit;
import de.joh.dmnr.common.util.CommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Allows the wearer of Dragon Mage Armor to breathe underwater.
 * Level 1 consumes mana while level 2 is free underwater breathing.
 * Function and constructor are initialized versions of parent class function/constructor.Configurable in the CommonConfigs.
 * @see CommonConfig
 * @see ArmorUpgradeInit
 * @author Joh0210
 */
public class WaterBreathingArmorUpgrade extends OnTickArmorUpgrade {
    public WaterBreathingArmorUpgrade(@NotNull ResourceLocation registryName, int maxUpgradeLevel, int upgradeCost) {
        super(registryName, maxUpgradeLevel, true, upgradeCost);
    }

    @Override
    public void onTick(Level world, Player player, int level, IPlayerMagic magic) {
        //25 is 1 bubble but fits like this
        if(level == 1){
            if (player.getAirSupply()  < 230 && magic != null && magic.getCastingResource().hasEnoughAbsolute(player, CommonConfig.WATERBREATHING_MANA_PRO_OXIGEN_BUBBLE.get())) {
                player.setAirSupply(player.getAirSupply() + 25);
                magic.getCastingResource().consume(player, CommonConfig.WATERBREATHING_MANA_PRO_OXIGEN_BUBBLE.get());
            }
        }
        else if(level == 2){
            if (player.getAirSupply()  < 230) {
                player.setAirSupply(player.getAirSupply() + 25);
            }
        }
    }
}
