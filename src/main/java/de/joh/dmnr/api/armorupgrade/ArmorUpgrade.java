package de.joh.dmnr.api.armorupgrade;

import de.joh.dmnr.common.init.ArmorUpgradeInit;
import de.joh.dmnr.api.item.DragonMageArmorItem;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.common.util.RLoc;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is the base class for any upgrade that can be installed on the Dragon Mage Armor.
 * Upgrades only work if full armor is worn and the upgrade is installed.
 * @see DragonMageArmorItem
 * @see ArmorUpgradeInit
 * @author Joh0210
 */
public class ArmorUpgrade {
    public static ArmorUpgrade INSTANCE = new ArmorUpgrade(RLoc.create("armorupgrade/none"), 0, ItemInit.BLANK_UPGRADE_SEAL, false, false, 0);

    public final int upgradeCost;

    /**
     * Maximum upgrade level that can be installed for this type.
     */
    public final int maxUpgradeLevel;

    private final RegistryObject<Item> upgradeSealItem;

    public final boolean supportsOnExtraLevel;

    /**
     * Can you upgrade the armor an infinite number of times with this upgrade?
     */
    public final boolean isInfStackable;
    private final ResourceLocation registryName;

    /**
     * @param registryName ID under which the upgrade can be recognized.
     * @param maxUpgradeLevel Maximum upgrade level that can be installed for this type.
     * @param isInfStackable Can you upgrade the armor an infinite number of times with this upgrade?
     */
    public ArmorUpgrade(ResourceLocation registryName, int maxUpgradeLevel, RegistryObject<Item> upgradeSealItem, boolean isInfStackable, boolean supportsOnExtraLevel, int upgradeCost){
        this.registryName = registryName;
        this.maxUpgradeLevel = maxUpgradeLevel;
        this.isInfStackable = isInfStackable;
        this.upgradeCost = upgradeCost;
        this.supportsOnExtraLevel = supportsOnExtraLevel;
        this.upgradeSealItem = upgradeSealItem;
    }

    public ArmorUpgrade(ResourceLocation registryName, int maxUpgradeLevel, RegistryObject<Item> upgradeSealItem, boolean isInfStackable, int upgradeCost){
        this.registryName = registryName;
        this.maxUpgradeLevel = maxUpgradeLevel;
        this.isInfStackable = isInfStackable;
        this.upgradeCost = upgradeCost;
        this.supportsOnExtraLevel = isInfStackable;
        this.upgradeSealItem = upgradeSealItem;
    }

    public @NotNull Item getSeal(){
        return upgradeSealItem.get();
    }

    /**
     * This feature removes the permanent effect from the player when the wearer unequips at least one piece of armor.
     * @param player Wearer of the Dragon Mage Armor. Player exclusive, no other entities.
     */
    public void onRemove(Player player){
    }

    /**
     * Is there another version of this upgrade that is significantly stronger.
     */
    public final boolean hasStrongerAlternative() {
        return getStrongerAlternative() != null;
    }

    @Nullable
    public ArmorUpgrade getStrongerAlternative() {
        return null;
    }

    public String getSourceID(int level) {
        return registryName.toString() + "_" + level;
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }
}
