package de.joh.dragonmagicandrelics.item.util;

import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.Registries;
import de.joh.dragonmagicandrelics.armorupgrades.types.ArmorUpgrade;
import de.joh.dragonmagicandrelics.capabilities.dragonmagic.PlayerDragonMagicProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * DragonMagicContainers have the power to hold Dragon Magic (Armor Upgrades).
 * Drawing on the item transfers the power to the player.
 * If he has full Dragon Mage Armor on, he can use these powers.
 * @see de.joh.dragonmagicandrelics.item.items.DragonMageArmor
 * @see ArmorUpgrade
 * @see de.joh.dragonmagicandrelics.capabilities.dragonmagic.PlayerDragonMagic
 * @author Joh0210
 */
public interface DragonMagicContainer {
    //todo add max Upgrade number per Item
    /**
     * What is the Dragon Magic budget of this item
     */
    int getMaxDragonMagic(ItemStack itemStack);

    /**
     * @return The level of the wanted upgrade in the container (0 = absent)
     */
    default int getUpgradeLevel(ItemStack itemStack, ArmorUpgrade armorUpgrade){
        if(itemStack.hasTag() && itemStack.getTag().contains(DragonMagicAndRelics.MOD_ID + "armor_upgrade")){
            CompoundTag nbt = itemStack.getTag().getCompound(DragonMagicAndRelics.MOD_ID + "armor_upgrade");
            for(String upgradeKey : nbt.getAllKeys()){
                int level = nbt.getInt(upgradeKey);
                ArmorUpgrade installedArmorUpgrade = Registries.ARMOR_UPGRADE.get().getValue(new ResourceLocation(upgradeKey));
                if(armorUpgrade.equals(installedArmorUpgrade)){
                    return level;
                }
            }
        }
        return 0;
    }

    /**
     * Installs a new upgrade in the container
     * @param force Ignore the maximum number of upgrades
     * @return false = The upgrade could not be added
     */
    default boolean addDragonMagicToItem(ItemStack itemStack, ArmorUpgrade armorUpgrade, int level, boolean force){
        if(itemStack.hasTag()){
            if(!force){
                level = Math.min(level, armorUpgrade.maxUpgradeLevel);
            } else if(!armorUpgrade.isInfStackable){
                level = Math.min(level, armorUpgrade.supportsOnExtraLevel ? armorUpgrade.maxUpgradeLevel+1 : armorUpgrade.maxUpgradeLevel);
            }

            CompoundTag nbt = new CompoundTag();

            if(itemStack.getTag().contains(DragonMagicAndRelics.MOD_ID + "armor_upgrade")){
                nbt = itemStack.getTag().getCompound(DragonMagicAndRelics.MOD_ID + "armor_upgrade");
            }

            int spentPoints = 0;
            for(String key : nbt.getAllKeys()){
                ArmorUpgrade savedUpgrade = Registries.ARMOR_UPGRADE.get().getValue(new ResourceLocation(key));
                if(savedUpgrade != armorUpgrade && savedUpgrade != null){
                    spentPoints += savedUpgrade.upgradeCost * nbt.getInt(key);
                }
            }

            spentPoints += armorUpgrade.upgradeCost * level;

            if(spentPoints < getMaxDragonMagic(itemStack) || force){
                itemStack.getTag().remove(DragonMagicAndRelics.MOD_ID + "armor_upgrade");
                nbt.putInt(armorUpgrade.getRegistryName().toString(), level);
                itemStack.getTag().putInt(DragonMagicAndRelics.MOD_ID + "spent_dp", spentPoints);
                itemStack.getTag().put(DragonMagicAndRelics.MOD_ID + "armor_upgrade", nbt);
                return true;
            }
        }
        return false;
    }

    default int getSpentDragonPoints(ItemStack itemStack){
        if(itemStack.hasTag()){
            return itemStack.getTag().getInt(DragonMagicAndRelics.MOD_ID + "spent_dp");
        }
        return 0;
    }

    /**
     * This function must be called by the item that implements the interface!
     * Adds the container's upgrades to the player.
     */
    default void addDragonMagic(ItemStack itemStack, Player player, String sourceExtension){
        if(itemStack.hasTag() && itemStack.getTag().contains(DragonMagicAndRelics.MOD_ID + "armor_upgrade")){
            CompoundTag nbt = itemStack.getTag().getCompound(DragonMagicAndRelics.MOD_ID + "armor_upgrade");
            player.getCapability(PlayerDragonMagicProvider.PLAYER_DRAGON_MAGIC).ifPresent((playerCapability) -> {
                for(String upgradeKey : nbt.getAllKeys()){
                    int level = nbt.getInt(upgradeKey);
                    ArmorUpgrade armorUpgrade = Registries.ARMOR_UPGRADE.get().getValue(new ResourceLocation(upgradeKey));
                    if(armorUpgrade != null){
                        playerCapability.addUpgrade(sourceExtension + armorUpgrade.getSourceID(level), armorUpgrade, level, player);
                    }
                }
            });
        }
    }

    /**
     * This function must be called by the item that implements the interface!
     * Removes the container's upgrades to the player.
     */
    default void removeDragonMagic(ItemStack itemStack, Player player, String sourceExtension){
        if(itemStack.hasTag() && itemStack.getTag().contains(DragonMagicAndRelics.MOD_ID + "armor_upgrade")){
            CompoundTag nbt = itemStack.getTag().getCompound(DragonMagicAndRelics.MOD_ID + "armor_upgrade");
            player.getCapability(PlayerDragonMagicProvider.PLAYER_DRAGON_MAGIC).ifPresent((playerCapability) -> {
                for(String upgradeKey : nbt.getAllKeys()){
                    ArmorUpgrade armorUpgrade = Registries.ARMOR_UPGRADE.get().getValue(new ResourceLocation(upgradeKey));
                    if(armorUpgrade != null){
                        playerCapability.removeUpgrade(sourceExtension + armorUpgrade.getSourceID(nbt.getInt(upgradeKey)), player);
                    }
                }
            });
        }
    }
}
