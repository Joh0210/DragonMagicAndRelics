package de.joh.dmnr.common.armorupgrade;

import de.joh.dmnr.api.armorupgrade.OnEquippedArmorUpgrade;
import de.joh.dmnr.common.event.DamageEventHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

/**
 * This upgrade lets you enter Mist Form when you are about to die.
 * <br>When the Player is an Undead, it also makes {@link com.mna.spells.components.ComponentMistForm MistForm} toggable
 * <br>Increasing the maximum level has no effect without further adjustments.
 * @see DamageEventHandler
 * @author Joh0210
 */
public class MistFormArmorUpgrade extends OnEquippedArmorUpgrade {
    public MistFormArmorUpgrade(@NotNull ResourceLocation registryName, int maxUpgradeLevel, RegistryObject<Item> upgradeSealItem, boolean isInfStackable, int upgradeCost) {
        super(registryName, maxUpgradeLevel, upgradeSealItem, isInfStackable, upgradeCost);
    }

    @Override
    public void onEquip(Player player, int level) {
        player.getPersistentData().putBoolean("bone_armor_set_bonus", true);
    }

    @Override
    public void onRemove(Player player) {
        player.getPersistentData().putBoolean("bone_armor_set_bonus", false);
    }
}
