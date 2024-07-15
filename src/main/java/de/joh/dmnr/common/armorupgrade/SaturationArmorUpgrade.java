package de.joh.dmnr.common.armorupgrade;

import com.mna.api.capabilities.IPlayerMagic;
import de.joh.dmnr.api.armorupgrade.OnTickArmorUpgrade;
import de.joh.dmnr.common.init.ArmorUpgradeInit;
import de.joh.dmnr.common.util.CommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

/**
 * Feeds the wearer of Dragon Mage Armor, but requires a certain amount of mana to do so.
 * Function and constructor are initialized versions of parent class function/constructor.
 * Configurable in the CommonConfigs.
 * @see CommonConfig
 * @see ArmorUpgradeInit
 * @author Joh0210
 */
public class SaturationArmorUpgrade extends OnTickArmorUpgrade {

    public SaturationArmorUpgrade(@NotNull ResourceLocation registryName, RegistryObject<Item> upgradeSealItem, int upgradeCost) {
        super(registryName, 1, upgradeSealItem, false, upgradeCost);
    }

    @Override
    public void onTick(Level world, Player player, int level, IPlayerMagic magic) {
        if(level > 0){
            // Only if the wearer is actually hungry
            if (player.canEat(false) && magic != null && magic.getCastingResource().hasEnoughAbsolute(player, CommonConfig.SATURATION_MANA_PER_NUTRITION.get())) {
                player.getFoodData().eat(1, 1);
                magic.getCastingResource().consume(player, CommonConfig.SATURATION_MANA_PER_NUTRITION.get());
            }
        }
    }
}
