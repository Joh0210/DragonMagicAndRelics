package de.joh.dragonmagicandrelics.item.items;

import de.joh.dragonmagicandrelics.CreativeModeTab;
import de.joh.dragonmagicandrelics.Registries;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.armorupgrades.types.ArmorUpgrade;
import de.joh.dragonmagicandrelics.item.items.dragonmagearmor.DragonMageArmor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    private final ResourceLocation armorUpgradeRL;

    /**
     * @param armorUpgrade ID of the armor upgrade that can be installed with this UpgradeSeal
     */
    public UpgradeSeal(ResourceLocation armorUpgrade) {
        super(new Item.Properties().tab(CreativeModeTab.ArmorUpgradeModeTab).stacksTo(1).rarity(Rarity.RARE)); //.rarity(rarity));
        this.armorUpgradeRL = armorUpgrade;
    }

    /**
     * Adds a tooltip (when hovering over the item) to the item.
     * Shows which Ritual Tier you need to install the upgrade.
     * Call from the game itself.
     */
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            ArmorUpgrade armorUpgrade = getArmorUpgrade();
            if (armorUpgrade != null) {
                tooltip.add(new TranslatableComponent(armorUpgrade.getRegistryName().toString() + ".description"));
                TranslatableComponent component0 = new TranslatableComponent("tooltip.dragonmagicandrelics.upgradeseal.max_level");
                tooltip.add(new TextComponent(component0.getString() + " " + armorUpgrade.maxUpgradeLevel));
                TranslatableComponent component1 = new TranslatableComponent("tooltip.dragonmagicandrelics.upgradeseal");
                tooltip.add(new TextComponent(component1.getString() + " " + armorUpgrade.upgradeCost));
                if (armorUpgrade.hasStrongerAlternative()) {
                    tooltip.add(new TextComponent("  "));
                    TranslatableComponent component2 = new TranslatableComponent("tooltip.dragonmagicandrelics.upgradeseal.stronger_version");
                    TranslatableComponent component3 = new TranslatableComponent(String.valueOf(armorUpgrade.getStrongerAlternative().getRegistryName()));
                    tooltip.add(new TextComponent(component2.getString() + " " + component3.getString()));
                }

                if (!armorUpgrade.supportsOnExtraLevel) {
                    tooltip.add(new TextComponent("  "));
                    tooltip.add(new TranslatableComponent("tooltip.dragonmagicandrelics.upgradeseal.not_supports_extra_level"));
                }
            }
        }
        else{
            tooltip.add(new TranslatableComponent("tooltip.dragonmagicandrelics.armor.tooltip"));
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@NotNull ItemStack itemstack) {
        return true;
    }

    @Nullable
    public ArmorUpgrade getArmorUpgrade(){
        return Registries.ARMOR_UPGRADE.get().getValue(armorUpgradeRL);
    }
}
