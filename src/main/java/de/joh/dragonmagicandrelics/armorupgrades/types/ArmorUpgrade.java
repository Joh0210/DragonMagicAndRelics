package de.joh.dragonmagicandrelics.armorupgrades.types;

import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import de.joh.dragonmagicandrelics.utils.RLoc;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is the base class for any upgrade that can be installed on the Dragon Mage Armor.
 * Upgrades only work if full armor is worn and the upgrade is installed.
 * @see DragonMageArmor
 * @see ArmorUpgradeInit
 * @author Joh0210
 */
public class ArmorUpgrade extends ForgeRegistryEntry<ArmorUpgrade> {
    public static ArmorUpgrade INSTANCE = new ArmorUpgrade(RLoc.create("armorupgrade/none"), 0, false);

    /**
     * Maximum upgrade level that can be installed for this type.
     */
    private final int maxUpgradeLevel;

    /**
     * Can you upgrade the armor an infinite number of times with this upgrade?
     */
    private final boolean isInfStackable;

    /**
     * @param registryName ID under which the upgrade can be recognized.
     * @param maxUpgradeLevel Maximum upgrade level that can be installed for this type.
     * @param isInfStackable Can you upgrade the armor an infinite number of times with this upgrade?
     */
    public ArmorUpgrade(@NotNull ResourceLocation registryName, int maxUpgradeLevel, boolean isInfStackable){
        this.setRegistryName(registryName);
        this.maxUpgradeLevel = maxUpgradeLevel;
        this.isInfStackable = isInfStackable;
    }

    public int getMaxUpgradeLevel(){
        return maxUpgradeLevel;
    }

    /**
     * @param level Level to check
     * @return Is the input level valid for this upgrade.
     */
    public boolean isLevelCorreckt(int level){
        return (0 <= level && level <= maxUpgradeLevel);
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
    public boolean hasStrongerAlternative() {
        return getStrongerAlternative() != null;
    }

    @Nullable
    public ArmorUpgrade getStrongerAlternative() {
        return null;
    }

    public String getSourceID(int level) {
        return getRegistryName().toString() + "_" + level;
    }
}
