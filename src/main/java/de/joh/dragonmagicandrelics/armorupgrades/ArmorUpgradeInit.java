package de.joh.dragonmagicandrelics.armorupgrades;

import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.armorupgrades.init.*;
import de.joh.dragonmagicandrelics.armorupgrades.types.ArmorUpgrade;
import de.joh.dragonmagicandrelics.armorupgrades.types.ArmorUpgradePotionEffect;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import de.joh.dragonmagicandrelics.utils.RLoc;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * An initialization of all upgrades for the Dragon Mage Armor.
 * Each new upgrade must also be entered getAllUpgrades or in one of the arrays!
 * Each new upgrade must also be listed in the configs for the initial upgrades!
 * @see DragonMageArmor
 * @see de.joh.dragonmagicandrelics.config.CommonConfigs
 * @author Joh0210
 */
@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ArmorUpgradeInit {
    public static ArmorUpgrade FLY;
    public static ArmorUpgrade SATURATION;
    public static ArmorUpgrade MOVEMENT_SPEED;
    public static ArmorUpgrade WATER_BREATHING;
    public static ArmorUpgrade METEOR_JUMP;
    public static ArmorUpgrade DOLPHINS_GRACE;
    public static ArmorUpgrade REGENERATION;
    public static ArmorUpgrade MANA_BOOST;
    public static ArmorUpgrade MANA_REGEN;
    public static ArmorUpgrade HEALTH_BOOST;
    /**
     * This upgrade reduces the damage the wearer receives, regardless of the source. Currently 20% reduction per level.
     * @see de.joh.dragonmagicandrelics.events.DamageEventHandler
     */
    public static ArmorUpgrade DAMAGE_RESISTANCE;
    /**
     * This upgrade increases the damage dealt by the wearer of the Dragon Mage Armor. Both spells and normal weapons.
     * @see de.joh.dragonmagicandrelics.events.DamageEventHandler
     */
    public static ArmorUpgrade DAMAGE_BOOST;
    /**
     * This upgrade protects you from fire damage. Level 1 Consumes mana instead. Level 2 protects you completely.
     * Increasing the maximum level has no effect without further adjustments.
     * @see de.joh.dragonmagicandrelics.events.DamageEventHandler
     */
    public static ArmorUpgrade FIRE_RESISTANCE;
    /**
     * This upgrade protects you from fall- and kinetic damage.
     * Increasing the maximum level has no effect without further adjustments.
     * @see de.joh.dragonmagicandrelics.events.DamageEventHandler
     */
    public static ArmorUpgrade KINETIC_RESISTANCE;

    /**
     * This upgrade protects you from explosion damage.
     * Increasing the maximum level has no effect without further adjustments.
     * @see de.joh.dragonmagicandrelics.events.DamageEventHandler
     */
    public static ArmorUpgrade EXPLOSION_RESISTANCE;

    /**
     * This upgrade reflects projectiles when they try to hit you.
     * With each upgrade, a projectile can be reflected more before it needs to recharge.
     * @see de.joh.dragonmagicandrelics.events.DamageEventHandler
     */
    public static ArmorUpgrade PROJECTILE_REFLECTION;

    /**
     * This upgrade lets you enter Mist Form when you are about to die.
     * Increasing the maximum level has no effect without further adjustments.
     * @see de.joh.dragonmagicandrelics.events.DamageEventHandler
     */
    public static ArmorUpgrade MIST_FORM;

    //todo
    /**
     * This upgrade increases the jump height of the wearer of the Dragon Mage Armor.
     * Effect only applies while sprinting.
     */
    public static ArmorUpgrade JUMP;

    //todo
    /**
     * This upgrade allows the wearer to use Elytra Flight. Level 2 gives a permanent boost, but needs mana.
     * Increasing the maximum level has no effect without further adjustments.
     * @see DragonMageArmor
     */
    public static ArmorUpgrade ELYTRA;

    public static ArmorUpgrade NIGHT_VISION;
    
    @SubscribeEvent
    public static void registerArmorUpgrades(final RegistryEvent.Register<ArmorUpgrade> event) {
        event.getRegistry().register(ArmorUpgradeInit.FLY);
        event.getRegistry().register(ArmorUpgradeInit.SATURATION);
        event.getRegistry().register(ArmorUpgradeInit.MOVEMENT_SPEED);
        event.getRegistry().register(ArmorUpgradeInit.WATER_BREATHING);

        event.getRegistry().register(ArmorUpgradeInit.METEOR_JUMP);
        event.getRegistry().register(ArmorUpgradeInit.DOLPHINS_GRACE);
        event.getRegistry().register(ArmorUpgradeInit.REGENERATION);
        event.getRegistry().register(ArmorUpgradeInit.MANA_BOOST);

        event.getRegistry().register(ArmorUpgradeInit.MANA_REGEN);
        event.getRegistry().register(ArmorUpgradeInit.HEALTH_BOOST);
        event.getRegistry().register(ArmorUpgradeInit.DAMAGE_RESISTANCE);
        event.getRegistry().register(ArmorUpgradeInit.DAMAGE_BOOST);

        event.getRegistry().register(ArmorUpgradeInit.FIRE_RESISTANCE);
        event.getRegistry().register(ArmorUpgradeInit.KINETIC_RESISTANCE);
        event.getRegistry().register(ArmorUpgradeInit.EXPLOSION_RESISTANCE);
        event.getRegistry().register(ArmorUpgradeInit.PROJECTILE_REFLECTION);

        event.getRegistry().register(ArmorUpgradeInit.MIST_FORM);
        event.getRegistry().register(ArmorUpgradeInit.JUMP);
        event.getRegistry().register(ArmorUpgradeInit.ELYTRA);
        event.getRegistry().register(ArmorUpgradeInit.NIGHT_VISION);
    }

    static {
        ArmorUpgradeInit.FLY = new ArmorUpgradeFly(RLoc.create("armorupgrade/fly"), 2);
        ArmorUpgradeInit.SATURATION = new ArmorUpgradeSaturation(RLoc.create("armorupgrade/saturation"), 1);
        ArmorUpgradeInit.MOVEMENT_SPEED = new ArmorUpgradeSpeed(RLoc.create("armorupgrade/movement_speed"), 3);
        ArmorUpgradeInit.WATER_BREATHING = new ArmorUpgradeWaterbreathing(RLoc.create("armorupgrade/water_breathing"), 2);

        ArmorUpgradeInit.METEOR_JUMP = new ArmorUpgradeMeteorJump(RLoc.create("armorupgrade/meteor_jump"), 1);
        ArmorUpgradeInit.DOLPHINS_GRACE = new ArmorUpgradePotionEffect(RLoc.create("armorupgrade/dolphins_grace"), 2, MobEffects.DOLPHINS_GRACE);
        ArmorUpgradeInit.REGENERATION = new ArmorUpgradePotionEffect(RLoc.create("armorupgrade/regeneration"), 1, MobEffects.REGENERATION);
        ArmorUpgradeInit.MANA_BOOST = new ArmorUpgradeManaBoost(RLoc.create("armorupgrade/mana_boost"), 5);

        ArmorUpgradeInit.MANA_REGEN = new ArmorUpgradeManaRegeneration(RLoc.create("armorupgrade/mana_regen"), 5);
        ArmorUpgradeInit.HEALTH_BOOST = new ArmorUpgradeHealthBoost(RLoc.create("armorupgrade/health_boost"), 5);
        ArmorUpgradeInit.DAMAGE_RESISTANCE = new ArmorUpgrade(RLoc.create("armorupgrade/damage_resistance"), 3);
        ArmorUpgradeInit.DAMAGE_BOOST = new ArmorUpgrade(RLoc.create("armorupgrade/damage_boost"), 4);

        ArmorUpgradeInit.FIRE_RESISTANCE = new ArmorUpgrade(RLoc.create("armorupgrade/fire_resistance"), 2);
        ArmorUpgradeInit.KINETIC_RESISTANCE = new ArmorUpgrade(RLoc.create("armorupgrade/kinetic_resistance"), 1);
        ArmorUpgradeInit.EXPLOSION_RESISTANCE = new ArmorUpgrade(RLoc.create("armorupgrade/explosion_resistance"), 1);
        ArmorUpgradeInit.PROJECTILE_REFLECTION = new ArmorUpgrade(RLoc.create("armorupgrade/projectile_reflection"), 3);

        ArmorUpgradeInit.MIST_FORM = new ArmorUpgrade(RLoc.create("armorupgrade/mist_form"), 1);
        ArmorUpgradeInit.JUMP = new ArmorUpgrade(RLoc.create("armorupgrade/jump"), 3);
        ArmorUpgradeInit.ELYTRA = new ArmorUpgrade(RLoc.create("armorupgrade/elytra"), 2);
        ArmorUpgradeInit.NIGHT_VISION = new ArmorUpgradeNightVision(RLoc.create("armorupgrade/night_vision"), 1);
    }
}
