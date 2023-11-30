package de.joh.dmnr.api.armorupgrade;

import de.joh.dmnr.api.item.DragonMageArmorItem;
import net.minecraft.world.entity.player.Player;

public interface IOnEquippedArmorUpgrade {
    /**
     * This feature adds the permanent effect when a player fully equips the Dragon Mage armor.
     * @see DragonMageArmorItem
     * @param player Wearer of the Dragon Mage Armor. Player exclusive, no other entities.
     * @param level The level of the installed upgrade.
     */
    void onEquip(Player player, int level);

    ArmorUpgrade getArmorUpgrade();
}
