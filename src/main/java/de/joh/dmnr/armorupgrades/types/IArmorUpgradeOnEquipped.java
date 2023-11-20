package de.joh.dmnr.armorupgrades.types;

import de.joh.dmnr.item.items.dragonmagearmor.DragonMageArmor;
import net.minecraft.world.entity.player.Player;

public interface IArmorUpgradeOnEquipped {
    /**
     * This feature adds the permanent effect when a player fully equips the Dragon Mage armor.
     * @see DragonMageArmor
     * @param player Wearer of the Dragon Mage Armor. Player exclusive, no other entities.
     * @param level The level of the installed upgrade.
     */
    void onEquip(Player player, int level);

    ArmorUpgrade getArmorUpgrade();
}
