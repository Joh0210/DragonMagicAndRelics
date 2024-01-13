package de.joh.dmnr.common.init;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.util.Registries;
import de.joh.dmnr.api.armorupgrade.ArmorUpgrade;
import de.joh.dmnr.common.armorupgrade.*;
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
    public static ArmorUpgrade REACH_DISTANCE;
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
    public static ArmorUpgrade MINOR_FIRE_RESISTANCE;
    public static ArmorUpgrade MAJOR_FIRE_RESISTANCE;
    /**
     * This upgrade protects you from fall- and kinetic damage.
     * Increasing the maximum level has no effect without further adjustments.
     * @see DamageEventHandler
     */
    public static ArmorUpgrade KINETIC_RESISTANCE;

    /**
     * This upgrade protects you from explosion damage.
     * Increasing the maximum level has no effect without further adjustments.
     * @see DamageEventHandler
     */
    public static ArmorUpgrade EXPLOSION_RESISTANCE;

    /**
     * This upgrade reflects projectiles when they try to hit you.
     * With each upgrade, a projectile can be reflected more before it needs to recharge.
     * @see DamageEventHandler
     */
    public static ArmorUpgrade PROJECTILE_REFLECTION;

    public static ArmorUpgrade MIST_FORM;

    public static ArmorUpgrade JUMP;

    /**
     * This upgrade allows the wearer to use Elytra Flight. Level 2 gives a permanent boost, but needs mana.
     * Increasing the maximum level has no effect without further adjustments.
     * @see DragonMageArmorItem
     */
    public static ArmorUpgrade ELYTRA;
    public static ArmorUpgrade ANGEL_FLIGHT;

    public static ArmorUpgrade NIGHT_VISION;

    public static ArmorUpgrade BURNING_FRENZY;
    public static ArmorUpgrade MAJOR_MANA_BOOST;

    /**
     * Increases castet Spells Attributes:
     * <br> - MAGNITUDE +0.5
     * <br> - DAMAGE    +3
     * <br> - DURATION: +30%
     * @see CommonEventHandler
     */
    public static ArmorUpgrade SORCERERS_PRIDE;

    public static ArmorUpgrade INSIGHT;

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(Registries.ARMOR_UPGRADE.get().getRegistryKey(), (helper) -> {
            helper.register(ArmorUpgradeInit.FLY.getRegistryName(), ArmorUpgradeInit.FLY);
            helper.register(ArmorUpgradeInit.SATURATION.getRegistryName(), ArmorUpgradeInit.SATURATION);
            helper.register(ArmorUpgradeInit.MOVEMENT_SPEED.getRegistryName(), ArmorUpgradeInit.MOVEMENT_SPEED);
            helper.register(ArmorUpgradeInit.WATER_BREATHING.getRegistryName(), ArmorUpgradeInit.WATER_BREATHING);
            helper.register(ArmorUpgradeInit.METEOR_JUMP.getRegistryName(), ArmorUpgradeInit.METEOR_JUMP);
            helper.register(ArmorUpgradeInit.SORCERERS_PRIDE.getRegistryName(), ArmorUpgradeInit.SORCERERS_PRIDE);
            helper.register(ArmorUpgradeInit.BURNING_FRENZY.getRegistryName(), ArmorUpgradeInit.BURNING_FRENZY);


            helper.register(ArmorUpgradeInit.DOLPHINS_GRACE.getRegistryName(), ArmorUpgradeInit.DOLPHINS_GRACE);
            helper.register(ArmorUpgradeInit.REGENERATION.getRegistryName(), ArmorUpgradeInit.REGENERATION);
            helper.register(ArmorUpgradeInit.MANA_BOOST.getRegistryName(), ArmorUpgradeInit.MANA_BOOST);
            helper.register(ArmorUpgradeInit.MAJOR_MANA_BOOST.getRegistryName(), ArmorUpgradeInit.MAJOR_MANA_BOOST);

            helper.register(ArmorUpgradeInit.MANA_REGEN.getRegistryName(), ArmorUpgradeInit.MANA_REGEN);
            helper.register(ArmorUpgradeInit.HEALTH_BOOST.getRegistryName(), ArmorUpgradeInit.HEALTH_BOOST);
            helper.register(ArmorUpgradeInit.DAMAGE_RESISTANCE.getRegistryName(), ArmorUpgradeInit.DAMAGE_RESISTANCE);
            helper.register(ArmorUpgradeInit.DAMAGE_BOOST.getRegistryName(), ArmorUpgradeInit.DAMAGE_BOOST);
            helper.register(ArmorUpgradeInit.REACH_DISTANCE.getRegistryName(), ArmorUpgradeInit.REACH_DISTANCE);

            helper.register(ArmorUpgradeInit.MINOR_FIRE_RESISTANCE.getRegistryName(), ArmorUpgradeInit.MINOR_FIRE_RESISTANCE);
            helper.register(ArmorUpgradeInit.MAJOR_FIRE_RESISTANCE.getRegistryName(), ArmorUpgradeInit.MAJOR_FIRE_RESISTANCE);
            helper.register(ArmorUpgradeInit.KINETIC_RESISTANCE.getRegistryName(), ArmorUpgradeInit.KINETIC_RESISTANCE);
            helper.register(ArmorUpgradeInit.EXPLOSION_RESISTANCE.getRegistryName(), ArmorUpgradeInit.EXPLOSION_RESISTANCE);
            helper.register(ArmorUpgradeInit.PROJECTILE_REFLECTION.getRegistryName(), ArmorUpgradeInit.PROJECTILE_REFLECTION);

            helper.register(ArmorUpgradeInit.MIST_FORM.getRegistryName(), ArmorUpgradeInit.MIST_FORM);
            helper.register(ArmorUpgradeInit.JUMP.getRegistryName(), ArmorUpgradeInit.JUMP);
            helper.register(ArmorUpgradeInit.ELYTRA.getRegistryName(), ArmorUpgradeInit.ELYTRA);
            helper.register(ArmorUpgradeInit.ANGEL_FLIGHT.getRegistryName(), ArmorUpgradeInit.ANGEL_FLIGHT);
            helper.register(ArmorUpgradeInit.NIGHT_VISION.getRegistryName(), ArmorUpgradeInit.NIGHT_VISION);
            helper.register(ArmorUpgradeInit.INSIGHT.getRegistryName(), ArmorUpgradeInit.INSIGHT);
        });
    }

    static {
        ArmorUpgradeInit.REACH_DISTANCE = new ReachDistanceArmorUpgrade(RLoc.create("armorupgrade/reach_distance"), 8);
        ArmorUpgradeInit.FLY = new FlyArmorUpgrade(RLoc.create("armorupgrade/fly"), 7);
        ArmorUpgradeInit.SATURATION = new SaturationArmorUpgrade(RLoc.create("armorupgrade/saturation"), 8);
        ArmorUpgradeInit.MOVEMENT_SPEED = new SpeedArmorUpgrade(RLoc.create("armorupgrade/movement_speed"), 4);
        ArmorUpgradeInit.WATER_BREATHING = new WaterBreathingArmorUpgrade(RLoc.create("armorupgrade/water_breathing"), 2, 2);
        ArmorUpgradeInit.JUMP = new JumpArmorUpgrade(RLoc.create("armorupgrade/jump"), 2);
        ArmorUpgradeInit.METEOR_JUMP = new MeteorJumpArmorUpgrade(RLoc.create("armorupgrade/meteor_jump"), 3);
        ArmorUpgradeInit.DOLPHINS_GRACE = new SwimSpeedArmorUpgrade(RLoc.create("armorupgrade/dolphins_grace"), 2, 2);
        ArmorUpgradeInit.REGENERATION = new RegenerationArmorUpgrade(RLoc.create("armorupgrade/regeneration"), 1, 10);
        ArmorUpgradeInit.MANA_BOOST = new ManaBoostArmorUpgrade(RLoc.create("armorupgrade/mana_boost"), false, 4);
        ArmorUpgradeInit.MAJOR_MANA_BOOST = new ManaBoostArmorUpgrade(RLoc.create("armorupgrade/major_mana_boost"), true, 5);
        ArmorUpgradeInit.MANA_REGEN = new ManaRegenerationArmorUpgrade(RLoc.create("armorupgrade/mana_regen"), 3, 5);
        ArmorUpgradeInit.HEALTH_BOOST = new HealthBoostArmorUpgrade(RLoc.create("armorupgrade/health_boost"), 2);
        ArmorUpgradeInit.DAMAGE_RESISTANCE = new ArmorUpgrade(RLoc.create("armorupgrade/damage_resistance"), 3, true, 8);
        ArmorUpgradeInit.DAMAGE_BOOST = new ArmorUpgrade(RLoc.create("armorupgrade/damage_boost"), 4, true, 7);
        ArmorUpgradeInit.MINOR_FIRE_RESISTANCE = new FireResistanceArmorUpgrade(RLoc.create("armorupgrade/minor_fire_resistance"), true, 4);
        ArmorUpgradeInit.MAJOR_FIRE_RESISTANCE = new FireResistanceArmorUpgrade(RLoc.create("armorupgrade/major_fire_resistance"), false, 2);
        ArmorUpgradeInit.KINETIC_RESISTANCE = new ArmorUpgrade(RLoc.create("armorupgrade/kinetic_resistance"), 1, false, 4);
        ArmorUpgradeInit.EXPLOSION_RESISTANCE = new ArmorUpgrade(RLoc.create("armorupgrade/explosion_resistance"), 1, false, 4);
        ArmorUpgradeInit.PROJECTILE_REFLECTION = new ArmorUpgrade(RLoc.create("armorupgrade/projectile_reflection"), 3, true, 2);
        ArmorUpgradeInit.MIST_FORM = new MistFormArmorUpgrade(RLoc.create("armorupgrade/mist_form"), 1, false, 8);
        ArmorUpgradeInit.ELYTRA = new ElytraArmorUpgrade(RLoc.create("armorupgrade/elytra"), 1, true, 5);
        ArmorUpgradeInit.ANGEL_FLIGHT = new ElytraArmorUpgrade(RLoc.create("armorupgrade/angel_flight"), 2, false, 5);
        ArmorUpgradeInit.NIGHT_VISION = new NightVisionArmorUpgrade(RLoc.create("armorupgrade/night_vision"), 2);
        ArmorUpgradeInit.BURNING_FRENZY = new BurningFrenzyArmorUpgrade(RLoc.create("armorupgrade/burning_frenzy"), 6);
        ArmorUpgradeInit.SORCERERS_PRIDE = new ArmorUpgrade(RLoc.create("armorupgrade/sorcerers_pride"), 3, true, 5);
        ArmorUpgradeInit.INSIGHT = new InsightArmorUpgrade(RLoc.create("armorupgrade/insight"), 1);
    }
}
