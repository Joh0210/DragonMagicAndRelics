package de.joh.dmnr.item.items;

import de.joh.dmnr.CreativeModeTab;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.Registries;
import de.joh.dmnr.armorupgrades.types.ArmorUpgrade;
import de.joh.dmnr.capabilities.dragonmagic.ArmorUpgradeHelper;
import de.joh.dmnr.capabilities.dragonmagic.PlayerDragonMagicProvider;
import de.joh.dmnr.item.items.dragonmagearmor.DragonMageArmor;
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
        super(16, "ring_of_ruling", new Properties().tab(CreativeModeTab.CreativeModeTab).stacksTo(1).rarity(Rarity.EPIC).fireResistant());
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
        tooltip.add(new TranslatableComponent("tooltip.dmnr.ring_of_ruling.tooltip.one"));
        tooltip.add(new TranslatableComponent("tooltip.dmnr.ring_of_ruling.tooltip.two"));
        super.appendHoverText(stack, world, tooltip, flag);
    }
}
