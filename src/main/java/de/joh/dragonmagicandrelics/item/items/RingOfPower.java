package de.joh.dragonmagicandrelics.item.items;

import de.joh.dragonmagicandrelics.CreativeModeTab;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.Registries;
import de.joh.dragonmagicandrelics.armorupgrades.types.ArmorUpgrade;
import de.joh.dragonmagicandrelics.capabilities.dragonmagic.PlayerDragonMagicProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class RingOfPower extends DragonMageCurios{
    public RingOfPower() {
        super(16, "ring_of_power", new Item.Properties().tab(CreativeModeTab.CreativeModeTab).rarity(Rarity.EPIC).fireResistant());
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
                        playerCapability.addUpgrade(sourceExtension + armorUpgrade.getSourceID(level + 1), armorUpgrade, level + 1, player);
                    }
                }
            });
        }
    }

    @Override
    public void removeDragonMagic(ItemStack itemStack, Player player, String sourceExtension){
        if(itemStack.hasTag() && itemStack.getTag().contains(DragonMagicAndRelics.MOD_ID + "armor_upgrade")){
            CompoundTag nbt = itemStack.getTag().getCompound(DragonMagicAndRelics.MOD_ID + "armor_upgrade");
            player.getCapability(PlayerDragonMagicProvider.PLAYER_DRAGON_MAGIC).ifPresent((playerCapability) -> {
                for(String upgradeKey : nbt.getAllKeys()){
                    ArmorUpgrade armorUpgrade = Registries.ARMOR_UPGRADE.get().getValue(new ResourceLocation(upgradeKey));
                    if(armorUpgrade != null){
                        playerCapability.removeUpgrade(sourceExtension + armorUpgrade.getSourceID(nbt.getInt(upgradeKey) + 1), player);
                    }
                }
            });
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(new TranslatableComponent("tooltip.dragonmagicandrelics.ring_of_power.tooltip.one"));
        tooltip.add(new TranslatableComponent("tooltip.dragonmagicandrelics.ring_of_power.tooltip.two"));
    }
}
