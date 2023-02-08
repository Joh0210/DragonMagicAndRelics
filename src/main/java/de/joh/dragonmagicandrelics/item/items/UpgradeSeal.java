package de.joh.dragonmagicandrelics.item.items;

import de.joh.dragonmagicandrelics.CreativeModeTab;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgrade;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

/**
 * These seals can be used to add an upgrade to the Dragon Mage Armor via the Upgrade Ritual.
 * @see DragonMageArmor
 * @see de.joh.dragonmagicandrelics.rituals.contexts.UpgradeRitual
 * @see ArmorUpgradeInit
 * @author Joh0210
 */
public class UpgradeSeal extends Item {
    /**
     * ID of the armor upgrade that can be installed with this UpgradeSeal
     */
    private final String armorUpgradename;

    /**
     * What is the minimum ritual tier required to add this UpgradeSeal
     */
    private final int tier;

    /**
     * Which upgrade level is saved on the UpgradeSeal?
     */
    private final int upgradeLevel;

    /**
     * @param tier What is the minimum ritual tier required to add this UpgradeSeal
     * @param armorUpgradename ID of the armor upgrade that can be installed with this UpgradeSeal
     * @param maxArmorUpgradeLevel Which upgrade level is saved on the UpgradeSeal?
     */
    public UpgradeSeal(int tier, String armorUpgradename, int maxArmorUpgradeLevel) {
        super(new Item.Properties().group(CreativeModeTab.CreativeModeTab).maxStackSize(1).rarity(getRarity(tier)));
        this.tier = tier;
        this.armorUpgradename = armorUpgradename;
        this.upgradeLevel = maxArmorUpgradeLevel;
    }

    /**
     * @param tier_and_level Tier and level are identical
     * @param armorUpgradename ID of the armor upgrade that can be installed with this UpgradeSeal
     */
    public UpgradeSeal(int tier_and_level, String armorUpgradename) {
        this(tier_and_level, armorUpgradename, tier_and_level);
    }

    /**
     * Adds a tooltip (when hovering over the item) to the item.
     * Shows which Ritual Tier you need to install the upgrade.
     * Call from the game itself.
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        TranslationTextComponent component = new TranslationTextComponent("tooltip.dragonmagicandrelics.upgradeseal");
        tooltip.add(new StringTextComponent(component.getString() + " " + tier));
    }

    /**
     * "Calculation" of the rarity of the item, based on the tier
     */
    private static Rarity getRarity(int tier){
        if (tier == 2){
            return Rarity.UNCOMMON;
        } else if (tier == 3){
            return Rarity.RARE;
        } else if (tier == 4){
            return Rarity.EPIC;
        }
        return Rarity.COMMON;
    }

    public int getTier() {
        return tier;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(ItemStack itemstack) {
        return true;
    }

    public ArmorUpgrade getArmorUpgrade(){
        return ArmorUpgradeInit.getArmorUpgradeFromString(armorUpgradename);
    }

    public int getUpgradeLevel(){
        return upgradeLevel;
    }

}
