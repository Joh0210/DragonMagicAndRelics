package de.joh.dragonmagicandrelics.capabilities.dragonmagic;


import com.mna.api.capabilities.IPlayerMagic;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import de.joh.dragonmagicandrelics.Registries;
import de.joh.dragonmagicandrelics.armorupgrades.types.ArmorUpgrade;
import de.joh.dragonmagicandrelics.armorupgrades.types.IArmorUpgradeOnEquipped;
import de.joh.dragonmagicandrelics.armorupgrades.types.IArmorUpgradeOnTick;
import de.joh.dragonmagicandrelics.effects.EffectInit;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import oshi.util.tuples.Pair;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Helper functions for the Dragon Magic effects.
 * @author Joh0210
 */
public class ArmorUpgradeHelper {
    public static int getUpgradeLevel(Player player, ArmorUpgrade armorUpgrade){
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if(!chest.isEmpty() && chest.getItem() instanceof DragonMageArmor dragonMageArmor){
            if(!dragonMageArmor.isSetEquipped(player)){
                return 0;
            }
            if(player.hasEffect(EffectInit.ULTIMATE_ARMOR.get())){
                return armorUpgrade.getMaxUpgradeLevel();
            }

            AtomicInteger level = new AtomicInteger();

            player.getCapability(PlayerDragonMagicProvider.PLAYER_DRAGON_MAGIC).ifPresent((playerCapability) -> {
                for(Pair<ArmorUpgrade, Integer> equippedAU : playerCapability.onEventUpgrade.values()){
                    if(armorUpgrade.equals(equippedAU.getA())){
                        level.set(equippedAU.getB());
                        return;
                    }
                }
                for(Pair<IArmorUpgradeOnTick, Integer> equippedAU : playerCapability.onTickUpgrade.values()){
                    if(armorUpgrade.equals(equippedAU.getA())){
                        level.set(equippedAU.getB());
                        return;
                    }
                }
                for(Pair<IArmorUpgradeOnEquipped, Integer> equippedAU : playerCapability.onEquipUpgrade.values()){
                    if(armorUpgrade.equals(equippedAU.getA())){
                        level.set(equippedAU.getB());
                        return;
                    }
                }
            });

            return level.get();
        }

        return 0;
    }

    public static void applyOnTickUpgrade(Player player){
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if(!chest.isEmpty() && chest.getItem() instanceof DragonMageArmor dragonMageArmor) {
            if (!dragonMageArmor.isSetEquipped(player)) {
                return;
            }

            IPlayerMagic magic = player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);

            if(player.hasEffect(EffectInit.ULTIMATE_ARMOR.get())){
                for(ArmorUpgrade armorUpgrade : Registries.ARMOR_UPGRADE.get().getValues()){
                    if(armorUpgrade instanceof IArmorUpgradeOnTick && !armorUpgrade.hasStrongerAlternative()){
                        ((IArmorUpgradeOnTick)armorUpgrade).onTick(player.getLevel(), player, armorUpgrade.getMaxUpgradeLevel(), magic);
                    }
                }
                return;
            }

            player.getCapability(PlayerDragonMagicProvider.PLAYER_DRAGON_MAGIC).ifPresent((playerCapability) -> {
                for(Pair<IArmorUpgradeOnTick, Integer> pair : playerCapability.onTickUpgrade.values()){
                    if(!pair.getA().hasStrongerAlternative() || getUpgradeLevel(player, pair.getA().getStrongerAlternative()) == 0){
                        pair.getA().onTick(player.getLevel(), player, pair.getB(), magic);
                    }
                }
            });
        }
    }

    public static void deactivateAll(Player player){
        player.getCapability(PlayerDragonMagicProvider.PLAYER_DRAGON_MAGIC).ifPresent((playerCapability) -> {
            for(Pair<ArmorUpgrade, Integer> pair : playerCapability.onEventUpgrade.values()){
                if(!pair.getA().hasStrongerAlternative() || getUpgradeLevel(player, pair.getA().getStrongerAlternative()) == 0){
                    pair.getA().onRemove(player, pair.getB());
                }
            }
            for(Pair<IArmorUpgradeOnEquipped, Integer> pair : playerCapability.onEquipUpgrade.values()){
                if(!pair.getA().hasStrongerAlternative() || getUpgradeLevel(player, pair.getA().getStrongerAlternative()) == 0){
                    pair.getA().onRemove(player, pair.getB());
                }
            }
            for(Pair<IArmorUpgradeOnTick, Integer> pair : playerCapability.onTickUpgrade.values()){
                if(!pair.getA().hasStrongerAlternative() || getUpgradeLevel(player, pair.getA().getStrongerAlternative()) == 0){
                    pair.getA().onRemove(player, pair.getB());
                }
            }
        });
    }

    /**
     * Only activates when the DM Armor is worn
     */
    public static void activateOnEquip(Player player){
        if(player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof DragonMageArmor dma && dma.isSetEquipped(player)) {
            player.getCapability(PlayerDragonMagicProvider.PLAYER_DRAGON_MAGIC).ifPresent((playerCapability) -> {
                for (Pair<IArmorUpgradeOnEquipped, Integer> pair : playerCapability.onEquipUpgrade.values()) {
                    if (!pair.getA().hasStrongerAlternative() || getUpgradeLevel(player, pair.getA().getStrongerAlternative()) == 0) {
                        pair.getA().onEquip(player, pair.getB());
                    }
                }
            });
        }
    }

    //todo: If I plan to adjust that in the future so that you no longer have to wear the DMA, that can cause problems.
    public static void ultimateArmorStart(Player player){
        deactivateAll(player);

        if(player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof DragonMageArmor dma && dma.isSetEquipped(player)){
            for(ArmorUpgrade armorUpgrade: Registries.ARMOR_UPGRADE.get().getValues()){
                if(armorUpgrade instanceof IArmorUpgradeOnEquipped){
                    ((IArmorUpgradeOnEquipped)armorUpgrade).onEquip(player, armorUpgrade.getMaxUpgradeLevel());
                }
            }
        }
    }

    //todo: If I plan to adjust that in the future so that you no longer have to wear the DMA, that can cause problems.
    public static void ultimateArmorFin(Player player){
        for(ArmorUpgrade armorUpgrade: Registries.ARMOR_UPGRADE.get().getValues()){
            armorUpgrade.onRemove(player, armorUpgrade.getMaxUpgradeLevel());
        }

        activateOnEquip(player);
    }
}
