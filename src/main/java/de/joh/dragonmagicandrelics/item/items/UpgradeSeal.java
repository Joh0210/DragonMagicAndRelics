package de.joh.dragonmagicandrelics.item.items;

import de.joh.dragonmagicandrelics.CreativeModeTab;
import de.joh.dragonmagicandrelics.Registries;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.armorupgrades.types.ArmorUpgrade;
import net.minecraft.network.chat.Component;
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
    private final ResourceLocation armorUpgrade;

    /**
     * @param armorUpgrade ID of the armor upgrade that can be installed with this UpgradeSeal
     */
    public UpgradeSeal(ResourceLocation armorUpgrade, Rarity rarity) {
        super(new Item.Properties().tab(CreativeModeTab.ArmorUpgradeModeTab).stacksTo(1).rarity(rarity));
        this.armorUpgrade = armorUpgrade;
    }

    /**
     * Adds a tooltip (when hovering over the item) to the item.
     * Shows which Ritual Tier you need to install the upgrade.
     * Call from the game itself.
     */
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        //todo: Display point cost
        TranslatableComponent component = new TranslatableComponent("tooltip.dragonmagicandrelics.upgradeseal");
        //tooltip.add(new TextComponent(component.getString() + " " + tier));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@NotNull ItemStack itemstack) {
        return true;
    }

    public ArmorUpgrade getArmorUpgrade(){
        return Registries.ARMOR_UPGRADE.get().getValue(armorUpgrade);
    }
}
