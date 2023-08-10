package de.joh.dragonmagicandrelics.item.items;

import de.joh.dragonmagicandrelics.CreativeModeTab;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.Registries;
import de.joh.dragonmagicandrelics.armorupgrades.types.ArmorUpgrade;
import de.joh.dragonmagicandrelics.capabilities.dragonmagic.ArmorUpgradeHelper;
import de.joh.dragonmagicandrelics.capabilities.dragonmagic.PlayerDragonMagicProvider;
import de.joh.dragonmagicandrelics.item.items.dragonmagearmor.DragonMageArmor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class RingOfRuling extends DragonMageCurios{
    public RingOfRuling() {
        super(16, "ring_of_ruling", new Properties().tab(CreativeModeTab.CreativeModeTab).rarity(Rarity.EPIC).fireResistant());
    }

    @Override
    public void addDragonMagic(ItemStack itemStack, Player player, String sourceExtension){
        if(itemStack.hasTag() && itemStack.getTag().contains(DragonMagicAndRelics.MOD_ID + "armor_upgrade")){
            CompoundTag nbt = itemStack.getTag().getCompound(DragonMagicAndRelics.MOD_ID + "armor_upgrade");
            player.getCapability(PlayerDragonMagicProvider.PLAYER_DRAGON_MAGIC).ifPresent((playerCapability) -> {
                for(String upgradeKey : nbt.getAllKeys()){
                    int level = nbt.getInt(upgradeKey);
                    ArmorUpgrade armorUpgrade = Registries.ARMOR_UPGRADE.get().getValue(new ResourceLocation(upgradeKey));
                    if(armorUpgrade != null){
                        playerCapability.addUpgrade(sourceExtension + armorUpgrade.getSourceID(level), armorUpgrade, level, player, true);
                    }
                }
            });

            ArmorUpgradeHelper.deactivateAllPerma(player, false);
            ArmorUpgradeHelper.activateOnEquipPerma(player);
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
            if(!chest.isEmpty() && chest.getItem() instanceof DragonMageArmor dragonMageArmor && dragonMageArmor.isSetEquipped(player)){
                ArmorUpgradeHelper.deactivateAll(player, false);
                ArmorUpgradeHelper.activateOnEquip(player);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(new TranslatableComponent("tooltip.dragonmagicandrelics.ring_of_ruling.tooltip.one"));
        tooltip.add(new TranslatableComponent("tooltip.dragonmagicandrelics.ring_of_ruling.tooltip.two"));
        super.appendHoverText(stack, world, tooltip, flag);
    }
}
