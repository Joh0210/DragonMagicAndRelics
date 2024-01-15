package de.joh.dmnr.common.item;

import de.joh.dmnr.api.armorupgrade.ArmorUpgrade;
import de.joh.dmnr.api.item.DragonMageArmorItem;
import de.joh.dmnr.common.init.ArmorUpgradeInit;
import de.joh.dmnr.common.ritual.UpgradeRitual;
import de.joh.dmnr.common.util.Registries;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
 * @see DragonMageArmorItem
 * @see UpgradeRitual
 * @see ArmorUpgradeInit
 * @author Joh0210
 */
public class UpgradeSealItem extends Item {
    /**
     * ID of the armor upgrade that can be installed with this UpgradeSeal
     */
    private final ResourceLocation armorUpgradeRL;

    /**
     * @param armorUpgrade ID of the armor upgrade that can be installed with this UpgradeSeal
     */
    public UpgradeSealItem(ResourceLocation armorUpgrade) {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)); //.rarity(rarity));
        this.armorUpgradeRL = armorUpgrade;
    }

    /**
     * Adds a tooltip (when hovering over the item) to the item.
     * Shows which Ritual Tier you need to install the upgrade.
     * Call from the game itself.
     */
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            ArmorUpgrade armorUpgrade = getArmorUpgrade();
            if (armorUpgrade != null) {
                tooltip.add(Component.translatable(armorUpgrade.getRegistryName().toString() + ".description"));

                MutableComponent component0 = Component.translatable("tooltip.dmnr.upgradeseal.max_level");
                tooltip.add(Component.literal(component0.getString() + " " + armorUpgrade.maxUpgradeLevel));

                MutableComponent component1 = Component.translatable("tooltip.dmnr.upgradeseal");
                tooltip.add(Component.literal(component1.getString() + " " + armorUpgrade.upgradeCost));

                if (armorUpgrade.getStrongerAlternative() != null) {
                    tooltip.add(Component.literal("  "));
                    MutableComponent component2 = Component.translatable("tooltip.dmnr.upgradeseal.stronger_version");
                    MutableComponent component3 = Component.translatable(String.valueOf(armorUpgrade.getStrongerAlternative().getRegistryName()));
                    tooltip.add(Component.literal(component2.getString() + " " + component3.getString()));
                }

                if (!armorUpgrade.supportsOnExtraLevel) {
                    tooltip.add(Component.literal("  "));
                    tooltip.add(Component.translatable("tooltip.dmnr.upgradeseal.not_supports_extra_level"));
                }
            }
        }
        else{
            tooltip.add(Component.translatable("tooltip.dmnr.armor.tooltip"));
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
