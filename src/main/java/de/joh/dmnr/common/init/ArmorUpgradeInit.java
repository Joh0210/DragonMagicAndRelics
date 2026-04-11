package de.joh.dmnr.common.init;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.util.Registries;
import de.joh.dmnr.api.armorupgrade.ArmorUpgrade;
import de.joh.dmnr.common.event.DamageEventHandler;
import de.joh.dmnr.common.util.CommonConfig;
import de.joh.dmnr.common.event.CommonEventHandler;
import de.joh.dmnr.api.item.DragonMageArmorItem;
import de.joh.dmnr.common.util.RLoc;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

/**
 * An initialization of all upgrades for the Dragon Mage Armor.
 * Each new upgrade must also be entered getAllUpgrades or in one of the arrays!
 * Each new upgrade must also be listed in the configs for the initial upgrades!
 * @see DragonMageArmorItem
 * @see CommonConfig
 * @author Joh0210
 */
@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ArmorUpgradeInit {
    /**
     * This upgrade reduces the damage the wearer receives, regardless of the source. Currently 20% reduction per level.
     * @see DamageEventHandler
     */
    public static ArmorUpgrade DAMAGE_RESISTANCE;
    /**
     * This upgrade increases the damage dealt by the wearer of the Dragon Mage Armor. Both spells and normal weapons.
     * @see DamageEventHandler
     */
    public static ArmorUpgrade DAMAGE_BOOST;

    /**
     * This upgrade reflects projectiles when they try to hit you.
     * With each upgrade, a projectile can be reflected more before it needs to recharge.
     * @see DamageEventHandler
     */
    public static ArmorUpgrade PROJECTILE_REFLECTION;

    /**
     * Increases castet Spells Attributes:
     * <br> - MAGNITUDE +0.5
     * <br> - DAMAGE    +3
     * <br> - DURATION: +30%
     * @see CommonEventHandler
     */
    public static ArmorUpgrade SORCERERS_PRIDE;

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(Registries.ARMOR_UPGRADE.get().getRegistryKey(), (helper) -> {
            helper.register(ArmorUpgradeInit.SORCERERS_PRIDE.getRegistryName(), ArmorUpgradeInit.SORCERERS_PRIDE);
            helper.register(ArmorUpgradeInit.DAMAGE_RESISTANCE.getRegistryName(), ArmorUpgradeInit.DAMAGE_RESISTANCE);
            helper.register(ArmorUpgradeInit.DAMAGE_BOOST.getRegistryName(), ArmorUpgradeInit.DAMAGE_BOOST);
            helper.register(ArmorUpgradeInit.PROJECTILE_REFLECTION.getRegistryName(), ArmorUpgradeInit.PROJECTILE_REFLECTION);
        });
    }

    static {
        ArmorUpgradeInit.DAMAGE_RESISTANCE = new ArmorUpgrade(RLoc.create("armorupgrade/damage_resistance"), 3, ItemInit.UPGRADE_SEAL_DAMAGE_RESISTANCE, true, 8);
        ArmorUpgradeInit.DAMAGE_BOOST = new ArmorUpgrade(RLoc.create("armorupgrade/damage_boost"), 4, ItemInit.UPGRADE_SEAL_DAMAGE_BOOST, true, 7);
        ArmorUpgradeInit.PROJECTILE_REFLECTION = new ArmorUpgrade(RLoc.create("armorupgrade/projectile_reflection"), 3, ItemInit.UPGRADE_SEAL_PROJECTILE_REFLECTION, true, 2);
        ArmorUpgradeInit.SORCERERS_PRIDE = new ArmorUpgrade(RLoc.create("armorupgrade/sorcerers_pride"), 3, ItemInit.UPGRADE_SEAL_SORCERERS_PRIDE, true, 5);
    }
}
