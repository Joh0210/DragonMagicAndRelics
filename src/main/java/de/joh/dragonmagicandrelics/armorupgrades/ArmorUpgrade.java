package de.joh.dragonmagicandrelics.armorupgrades;

import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;

/**
 * This class is the base class for any upgrade that can be installed on the Dragon Mage Armor.
 * Upgrades only work if full armor is worn and the upgrade is installed.
 * @see DragonMageArmor
 * @see ArmorUpgradeInit
 * @author Joh0210
 */
public class ArmorUpgrade {
    /**
     * ID under which the upgrade can be recognized.
     */
    private final String upgradeId;

    /**
     * Maximum upgrade level that can be installed for this type.
     */
    private final int maxUpgradeLevel;

    /**
     * @param upgradeId ID under which the upgrade can be recognized.
     * @param maxUpgradeLevel Maximum upgrade level that can be installed for this type.
     */
    public ArmorUpgrade(String upgradeId, int maxUpgradeLevel){
        this.upgradeId = upgradeId;
        this.maxUpgradeLevel = maxUpgradeLevel;
    }

    public String getUpgradeId(){
        return upgradeId;
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
}
