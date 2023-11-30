package de.joh.dmnr.capabilities.dragonmagic;


import com.mna.api.capabilities.IPlayerMagic;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import de.joh.dmnr.common.util.Registries;
import de.joh.dmnr.api.armorupgrade.ArmorUpgrade;
import de.joh.dmnr.api.armorupgrade.OnTickArmorUpgrade;
import de.joh.dmnr.api.armorupgrade.IOnEquippedArmorUpgrade;
import de.joh.dmnr.common.init.EffectInit;
import de.joh.dmnr.common.util.CapabilityException;
import de.joh.dmnr.api.item.DragonMageArmorItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Helper functions for the Dragon Magic effects.
 * @author Joh0210
 */
public class ArmorUpgradeHelper {
    public static int getUpgradeLevel(Player player, ArmorUpgrade armorUpgrade){
        if(armorUpgrade.getStrongerAlternative() != null && getUpgradeLevel(player, armorUpgrade.getStrongerAlternative()) > 0){
            return 0;
        }

        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);

        AtomicInteger level = new AtomicInteger(0);

        if(!chest.isEmpty() && chest.getItem() instanceof DragonMageArmorItem dragonMageArmor && dragonMageArmor.isSetEquipped(player)){
            if(player.hasEffect(EffectInit.ULTIMATE_ARMOR.get())){
                return armorUpgrade.maxUpgradeLevel;
            }

            player.getCapability(PlayerDragonMagicProvider.PLAYER_DRAGON_MAGIC).ifPresent((playerCapability) -> {
                if(armorUpgrade instanceof OnTickArmorUpgrade){
                    for(Pair<OnTickArmorUpgrade, Integer> equippedAU : playerCapability.onTickUpgrade.values()){
                        if(armorUpgrade.equals(equippedAU.getA())){
                            level.set(Math.max(level.get(), equippedAU.getB()));
                        }
                    }
                }
                else {
                    for(Pair<ArmorUpgrade, Integer> equippedAU : playerCapability.onEventUpgrade.values()){
                        if(armorUpgrade.equals(equippedAU.getA())){
                            level.set(Math.max(level.get(), equippedAU.getB()));
                        }
                    }
                }
            });
        }

        player.getCapability(PlayerDragonMagicProvider.PLAYER_DRAGON_MAGIC).ifPresent((playerCapability) -> {
            if(armorUpgrade instanceof OnTickArmorUpgrade){
                for (Pair<OnTickArmorUpgrade, Integer> equippedAU : playerCapability.onTickPermaUpgrade.values()) {
                    if (armorUpgrade.equals(equippedAU.getA())) {
                        level.set(Math.max(level.get(), equippedAU.getB()));
                    }
                }
            }
            else {
                for (Pair<ArmorUpgrade, Integer> equippedAU : playerCapability.onEventPermaUpgrade.values()) {
                    if (armorUpgrade.equals(equippedAU.getA())) {
                        level.set(Math.max(level.get(), equippedAU.getB()));
                    }
                }
            }
        });

        return level.get();
    }

    public static void applyOnTickUpgrade(Player player){
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        HashMap<OnTickArmorUpgrade, Integer> toApply = new HashMap<>();
        IPlayerMagic magic = player.getCapability(PlayerMagicProvider.MAGIC).orElseThrow(CapabilityException::mna_magic);
        if(!chest.isEmpty() && chest.getItem() instanceof DragonMageArmorItem dragonMageArmor && dragonMageArmor.isSetEquipped(player)) {
            if(player.hasEffect(EffectInit.ULTIMATE_ARMOR.get())){
                for(ArmorUpgrade armorUpgrade : Registries.ARMOR_UPGRADE.get().getValues()){
                    if(armorUpgrade instanceof OnTickArmorUpgrade && !armorUpgrade.hasStrongerAlternative()){
                        ((OnTickArmorUpgrade)armorUpgrade).onTick(player.level, player, armorUpgrade.maxUpgradeLevel, magic);
                    }
                }
                return;
            }

            player.getCapability(PlayerDragonMagicProvider.PLAYER_DRAGON_MAGIC).ifPresent((playerCapability) -> {
                for(Pair<OnTickArmorUpgrade, Integer> pair : playerCapability.onTickUpgrade.values()){
                    if(pair.getB() > 0){
                        toApply.put(pair.getA(),  Math.max(pair.getB(), toApply.getOrDefault(pair.getA(), 0)));
                    }
                }
            });
        }

        player.getCapability(PlayerDragonMagicProvider.PLAYER_DRAGON_MAGIC).ifPresent((playerCapability) -> {
            for(Pair<OnTickArmorUpgrade, Integer> pair : playerCapability.onTickPermaUpgrade.values()){
                if(pair.getB() > 0){
                    toApply.put(pair.getA(),  Math.max(pair.getB(), toApply.getOrDefault(pair.getA(), 0)));
                }
            }
        });

        for(Map.Entry<OnTickArmorUpgrade, Integer> entry : toApply.entrySet()){
            if(entry.getKey().getStrongerAlternative() == null || getUpgradeLevel(player, entry.getKey().getStrongerAlternative()) == 0){
                entry.getKey().onTick(player.level, player, entry.getValue(), magic);
            }
        }
    }

    public static void deactivateAll(Player player){
        deactivateAll(player, true);
    }

    public static void deactivateAll(Player player, boolean alsoPerma){
        player.getCapability(PlayerDragonMagicProvider.PLAYER_DRAGON_MAGIC).ifPresent((playerCapability) -> {
            for(Pair<ArmorUpgrade, Integer> pair : playerCapability.onEventUpgrade.values()){
                if(pair.getA().getStrongerAlternative() == null || getUpgradeLevel(player, pair.getA().getStrongerAlternative()) == 0){
                    pair.getA().onRemove(player);
                }
            }
            for(Pair<IOnEquippedArmorUpgrade, Integer> pair : playerCapability.getAllOnEquipUpgrade()){
                if(pair.getA().getArmorUpgrade().getStrongerAlternative() == null || getUpgradeLevel(player, pair.getA().getArmorUpgrade().getStrongerAlternative()) == 0){
                    pair.getA().getArmorUpgrade().onRemove(player);
                }
            }
            for(Pair<OnTickArmorUpgrade, Integer> pair : playerCapability.onTickUpgrade.values()){
                if(pair.getA().getStrongerAlternative() == null || getUpgradeLevel(player, pair.getA().getStrongerAlternative()) == 0){
                    pair.getA().onRemove(player);
                }
            }
        });

        if(alsoPerma){
            deactivateAllPerma(player, false);
            activateOnEquipPerma(player);
        }
    }

    public static void deactivateAllPerma(Player player){
        deactivateAllPerma(player, true);
    }

    public static void deactivateAllPerma(Player player, boolean alsoNormal){
        player.getCapability(PlayerDragonMagicProvider.PLAYER_DRAGON_MAGIC).ifPresent((playerCapability) -> {
            for(Pair<ArmorUpgrade, Integer> pair : playerCapability.onEventPermaUpgrade.values()){
                if(pair.getA().getStrongerAlternative() == null || getUpgradeLevel(player, pair.getA().getStrongerAlternative()) == 0){
                    pair.getA().onRemove(player);
                }
            }
            for(Pair<IOnEquippedArmorUpgrade, Integer> pair : playerCapability.getAllOnEquipPermaUpgrade()){
                if(pair.getA().getArmorUpgrade().getStrongerAlternative() == null || getUpgradeLevel(player, pair.getA().getArmorUpgrade().getStrongerAlternative()) == 0){
                    pair.getA().getArmorUpgrade().onRemove(player);
                }
            }
            for(Pair<OnTickArmorUpgrade, Integer> pair : playerCapability.onTickPermaUpgrade.values()){
                if(pair.getA().getStrongerAlternative() == null || getUpgradeLevel(player, pair.getA().getStrongerAlternative()) == 0){
                    pair.getA().onRemove(player);
                }
            }
        });

        if(alsoNormal && player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof DragonMageArmorItem dragonMageArmor && dragonMageArmor.isSetEquipped(player)){
            deactivateAll(player, false);
            activateOnEquip(player);
        }
    }

    /**
     * Only activates when the DM Armor is worn
     */
    public static void activateOnEquipPerma(Player player){
        player.getCapability(PlayerDragonMagicProvider.PLAYER_DRAGON_MAGIC).ifPresent((playerCapability) -> {
            for (Pair<IOnEquippedArmorUpgrade, Integer> pair : playerCapability.getAllOnEquipPermaUpgrade()) {
                if (pair.getA().getArmorUpgrade().getStrongerAlternative() == null || getUpgradeLevel(player, pair.getA().getArmorUpgrade().getStrongerAlternative()) == 0) {
                    pair.getA().onEquip(player, getUpgradeLevel(player, pair.getA().getArmorUpgrade()));
                }
            }
        });
    }

    public static void activateOnEquip(Player player){
        if(player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof DragonMageArmorItem dma && dma.isSetEquipped(player)) {
            player.getCapability(PlayerDragonMagicProvider.PLAYER_DRAGON_MAGIC).ifPresent((playerCapability) -> {
                for (Pair<IOnEquippedArmorUpgrade, Integer> pair : playerCapability.getAllOnEquipUpgrade()) {
                    if (pair.getA().getArmorUpgrade().getStrongerAlternative() == null || getUpgradeLevel(player, pair.getA().getArmorUpgrade().getStrongerAlternative()) == 0) {
                        pair.getA().onEquip(player, getUpgradeLevel(player, pair.getA().getArmorUpgrade()));
                    }
                }
            });
        }
    }

    public static void ultimateArmorStart(Player player){
        deactivateAll(player);
        deactivateAllPerma(player);

        if(player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof DragonMageArmorItem dma && dma.isSetEquipped(player)){
            for(ArmorUpgrade armorUpgrade: Registries.ARMOR_UPGRADE.get().getValues()){
                if(armorUpgrade instanceof IOnEquippedArmorUpgrade){
                    ((IOnEquippedArmorUpgrade)armorUpgrade).onEquip(player, armorUpgrade.maxUpgradeLevel);
                }
            }
        }
    }

    public static void ultimateArmorFin(Player player){
        for(ArmorUpgrade armorUpgrade: Registries.ARMOR_UPGRADE.get().getValues()){
            armorUpgrade.onRemove(player);
        }
        activateOnEquipPerma(player);
        activateOnEquip(player);
    }
}
