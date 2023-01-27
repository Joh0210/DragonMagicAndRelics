package de.joh.dragonmagicandrelics.config;

import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgrade;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import net.minecraftforge.common.ForgeConfigSpec;
import java.util.HashMap;

/**
 * This file includes the initial upgrades that are added to faction armor when upgrading.
 * New upgrades must be listed here accordingly and processed in the DragonMageArmorRitual class.
 * @see de.joh.dragonmagicandrelics.rituals.contexts.DragonMageArmorRitual
 * @author Joh0210
 */
public class InitialUpgradesConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> SPELLWEAVER_DAMAGE_BOOST;
    public static final ForgeConfigSpec.ConfigValue<Integer> SPELLWEAVER_DAMAGE_RESISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> SPELLWEAVER_DOLPHINS_GRACE;
    public static final ForgeConfigSpec.ConfigValue<Integer> SPELLWEAVER_EDLRIN_SIGHT;
    public static final ForgeConfigSpec.ConfigValue<Integer> SPELLWEAVER_ELYTRA;
    public static final ForgeConfigSpec.ConfigValue<Integer> SPELLWEAVER_EXPLOSION_RESISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> SPELLWEAVER_FIRE_RESISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> SPELLWEAVER_FLY;
    public static final ForgeConfigSpec.ConfigValue<Integer> SPELLWEAVER_HEALTH_BOOST;
    public static final ForgeConfigSpec.ConfigValue<Integer> SPELLWEAVER_JUMP;
    public static final ForgeConfigSpec.ConfigValue<Integer> SPELLWEAVER_KINETIC_RESISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> SPELLWEAVER_MANA_BOOST;
    public static final ForgeConfigSpec.ConfigValue<Integer> SPELLWEAVER_MANA_REGEN;
    public static final ForgeConfigSpec.ConfigValue<Integer> SPELLWEAVER_METEOR_JUMP;
    public static final ForgeConfigSpec.ConfigValue<Integer> SPELLWEAVER_MIST_FORM;
    public static final ForgeConfigSpec.ConfigValue<Integer> SPELLWEAVER_MOVEMENT_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Integer> SPELLWEAVER_NIGHT_VISION;
    public static final ForgeConfigSpec.ConfigValue<Integer> SPELLWEAVER_PROJECTILE_REFLECTION;
    public static final ForgeConfigSpec.ConfigValue<Integer> SPELLWEAVER_REGENERATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> SPELLWEAVER_SATURATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> SPELLWEAVER_WATER_BREATHING;
    public static final ForgeConfigSpec.ConfigValue<Integer> SPELLWEAVER_WELLSPRING_SIGHT;

    public static final ForgeConfigSpec.ConfigValue<Integer> DRUID_DAMAGE_BOOST;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRUID_DAMAGE_RESISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRUID_DOLPHINS_GRACE;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRUID_EDLRIN_SIGHT;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRUID_ELYTRA;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRUID_EXPLOSION_RESISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRUID_FIRE_RESISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRUID_FLY;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRUID_HEALTH_BOOST;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRUID_JUMP;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRUID_KINETIC_RESISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRUID_MANA_BOOST;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRUID_MANA_REGEN;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRUID_METEOR_JUMP;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRUID_MIST_FORM;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRUID_MOVEMENT_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRUID_NIGHT_VISION;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRUID_PROJECTILE_REFLECTION;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRUID_REGENERATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRUID_SATURATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRUID_WATER_BREATHING;
    public static final ForgeConfigSpec.ConfigValue<Integer> DRUID_WELLSPRING_SIGHT;

    public static final ForgeConfigSpec.ConfigValue<Integer> INFERNAL_DAMAGE_BOOST;
    public static final ForgeConfigSpec.ConfigValue<Integer> INFERNAL_DAMAGE_RESISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> INFERNAL_DOLPHINS_GRACE;
    public static final ForgeConfigSpec.ConfigValue<Integer> INFERNAL_EDLRIN_SIGHT;
    public static final ForgeConfigSpec.ConfigValue<Integer> INFERNAL_ELYTRA;
    public static final ForgeConfigSpec.ConfigValue<Integer> INFERNAL_EXPLOSION_RESISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> INFERNAL_FIRE_RESISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> INFERNAL_FLY;
    public static final ForgeConfigSpec.ConfigValue<Integer> INFERNAL_HEALTH_BOOST;
    public static final ForgeConfigSpec.ConfigValue<Integer> INFERNAL_JUMP;
    public static final ForgeConfigSpec.ConfigValue<Integer> INFERNAL_KINETIC_RESISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> INFERNAL_MANA_BOOST;
    public static final ForgeConfigSpec.ConfigValue<Integer> INFERNAL_MANA_REGEN;
    public static final ForgeConfigSpec.ConfigValue<Integer> INFERNAL_METEOR_JUMP;
    public static final ForgeConfigSpec.ConfigValue<Integer> INFERNAL_MIST_FORM;
    public static final ForgeConfigSpec.ConfigValue<Integer> INFERNAL_MOVEMENT_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Integer> INFERNAL_NIGHT_VISION;
    public static final ForgeConfigSpec.ConfigValue<Integer> INFERNAL_PROJECTILE_REFLECTION;
    public static final ForgeConfigSpec.ConfigValue<Integer> INFERNAL_REGENERATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> INFERNAL_SATURATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> INFERNAL_WATER_BREATHING;
    public static final ForgeConfigSpec.ConfigValue<Integer> INFERNAL_WELLSPRING_SIGHT;

    public static final ForgeConfigSpec.ConfigValue<Integer> WITHERBONE_DAMAGE_BOOST;
    public static final ForgeConfigSpec.ConfigValue<Integer> WITHERBONE_DAMAGE_RESISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> WITHERBONE_DOLPHINS_GRACE;
    public static final ForgeConfigSpec.ConfigValue<Integer> WITHERBONE_EDLRIN_SIGHT;
    public static final ForgeConfigSpec.ConfigValue<Integer> WITHERBONE_ELYTRA;
    public static final ForgeConfigSpec.ConfigValue<Integer> WITHERBONE_EXPLOSION_RESISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> WITHERBONE_FIRE_RESISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> WITHERBONE_FLY;
    public static final ForgeConfigSpec.ConfigValue<Integer> WITHERBONE_HEALTH_BOOST;
    public static final ForgeConfigSpec.ConfigValue<Integer> WITHERBONE_JUMP;
    public static final ForgeConfigSpec.ConfigValue<Integer> WITHERBONE_KINETIC_RESISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> WITHERBONE_MANA_BOOST;
    public static final ForgeConfigSpec.ConfigValue<Integer> WITHERBONE_MANA_REGEN;
    public static final ForgeConfigSpec.ConfigValue<Integer> WITHERBONE_METEOR_JUMP;
    public static final ForgeConfigSpec.ConfigValue<Integer> WITHERBONE_MIST_FORM;
    public static final ForgeConfigSpec.ConfigValue<Integer> WITHERBONE_MOVEMENT_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Integer> WITHERBONE_NIGHT_VISION;
    public static final ForgeConfigSpec.ConfigValue<Integer> WITHERBONE_PROJECTILE_REFLECTION;
    public static final ForgeConfigSpec.ConfigValue<Integer> WITHERBONE_REGENERATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> WITHERBONE_SATURATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> WITHERBONE_WATER_BREATHING;
    public static final ForgeConfigSpec.ConfigValue<Integer> WITHERBONE_WELLSPRING_SIGHT;



    static {
        BUILDER.push("Initial upgrades through the ritual of the Dragon Mage Armor");
        BUILDER.comment("This is broken down into each faction armor, each with all upgrades.").comment(" The number represents the level of the upgrade. If the entry is 0, the upgrade will not be added");

        BUILDER.push("spellweaver_robes");
        SPELLWEAVER_DAMAGE_BOOST = BUILDER.defineInRange("sw_damage_boost", 0, 0, 4);
        SPELLWEAVER_DAMAGE_RESISTANCE = BUILDER.defineInRange("sw_damage_resistance", 0, 0, 3);
        SPELLWEAVER_DOLPHINS_GRACE = BUILDER.defineInRange("sw_dolphins_grace", 0, 0, 2);
        SPELLWEAVER_EDLRIN_SIGHT = BUILDER.defineInRange("sw_eldrin_sight", 0, 0, 1);
        SPELLWEAVER_ELYTRA = BUILDER.defineInRange("sw_elytra", 0, 0, 2);
        SPELLWEAVER_EXPLOSION_RESISTANCE = BUILDER.defineInRange("sw_explosion_resistance", 0, 0, 1);
        SPELLWEAVER_FIRE_RESISTANCE = BUILDER.defineInRange("sw_fire_resistance", 0, 0, 2);
        SPELLWEAVER_FLY = BUILDER.defineInRange("sw_fly", 1, 0, 2);
        SPELLWEAVER_HEALTH_BOOST = BUILDER.defineInRange("sw_health_boost", 0, 0, 5);
        SPELLWEAVER_JUMP = BUILDER.defineInRange("sw_jump", 0, 0, 3);
        SPELLWEAVER_KINETIC_RESISTANCE = BUILDER.defineInRange("sw_kinetic_resistance", 0, 0, 1);
        SPELLWEAVER_MANA_BOOST = BUILDER.defineInRange("sw_mana_boost", 5, 0, 5);
        SPELLWEAVER_MANA_REGEN = BUILDER.defineInRange("sw_mana_regen", 5, 0, 5);
        SPELLWEAVER_METEOR_JUMP = BUILDER.defineInRange("sw_meteor_jump", 0, 0, 1);
        SPELLWEAVER_MIST_FORM = BUILDER.defineInRange("sw_mist_form", 0, 0, 1);
        SPELLWEAVER_MOVEMENT_SPEED = BUILDER.defineInRange("sw_movement_speed", 0, 0, 3);
        SPELLWEAVER_NIGHT_VISION = BUILDER.defineInRange("sw_night_vision", 0, 0, 1);
        SPELLWEAVER_PROJECTILE_REFLECTION = BUILDER.defineInRange("sw_projectile_reflection", 0, 0, 3);
        SPELLWEAVER_REGENERATION = BUILDER.defineInRange("sw_regeneration", 0, 0, 1);
        SPELLWEAVER_SATURATION = BUILDER.defineInRange("sw_saturation", 0, 0, 1);
        SPELLWEAVER_WATER_BREATHING = BUILDER.defineInRange("sw_water_breathing", 0, 0, 2);
        SPELLWEAVER_WELLSPRING_SIGHT = BUILDER.defineInRange("sw_wellspring_sight", 0, 0, 1);
        BUILDER.pop();

        BUILDER.push("druid_robes");
        DRUID_DAMAGE_BOOST = BUILDER.defineInRange("dr_damage_boost", 0, 0, 4);
        DRUID_DAMAGE_RESISTANCE = BUILDER.defineInRange("dr_damage_resistance", 0, 0, 3);
        DRUID_DOLPHINS_GRACE = BUILDER.defineInRange("dr_dolphins_grace", 0, 0, 2);
        DRUID_EDLRIN_SIGHT = BUILDER.defineInRange("dr_eldrin_sight", 0, 0, 1);
        DRUID_ELYTRA = BUILDER.defineInRange("dr_elytra", 2, 0, 2);
        DRUID_EXPLOSION_RESISTANCE = BUILDER.defineInRange("dr_explosion_resistance", 0, 0, 1);
        DRUID_FIRE_RESISTANCE = BUILDER.defineInRange("dr_fire_resistance", 0, 0, 2);
        DRUID_FLY = BUILDER.defineInRange("dr_fly", 2, 0, 2);
        DRUID_HEALTH_BOOST = BUILDER.defineInRange("dr_health_boost", 1, 0, 5);
        DRUID_JUMP = BUILDER.defineInRange("dr_jump", 0, 0, 3);
        DRUID_KINETIC_RESISTANCE = BUILDER.defineInRange("dr_kinetic_resistance", 1, 0, 1);
        DRUID_MANA_BOOST = BUILDER.defineInRange("dr_mana_boost", 0, 0, 5);
        DRUID_MANA_REGEN = BUILDER.defineInRange("dr_mana_regen", 0, 0, 5);
        DRUID_METEOR_JUMP = BUILDER.defineInRange("dr_meteor_jump", 0, 0, 1);
        DRUID_MIST_FORM = BUILDER.defineInRange("dr_mist_form", 0, 0, 1);
        DRUID_MOVEMENT_SPEED = BUILDER.defineInRange("dr_movement_speed", 0, 0, 3);
        DRUID_NIGHT_VISION = BUILDER.defineInRange("dr_night_vision", 0, 0, 1);
        DRUID_PROJECTILE_REFLECTION = BUILDER.defineInRange("dr_projectile_reflection", 0, 0, 3);
        DRUID_REGENERATION = BUILDER.defineInRange("dr_regeneration", 0, 0, 1);
        DRUID_SATURATION = BUILDER.defineInRange("dr_saturation", 0, 0, 1);
        DRUID_WATER_BREATHING = BUILDER.defineInRange("dr_water_breathing", 0, 0, 2);
        DRUID_WELLSPRING_SIGHT = BUILDER.defineInRange("dr_wellspring_sight", 0, 0, 1);
        BUILDER.pop();

        BUILDER.push("infernal_armor");
        INFERNAL_DAMAGE_BOOST = BUILDER.defineInRange("if_damage_boost", 0, 0, 4);
        INFERNAL_DAMAGE_RESISTANCE = BUILDER.defineInRange("if_damage_resistance", 0, 0, 3);
        INFERNAL_DOLPHINS_GRACE = BUILDER.defineInRange("if_dolphins_grace", 0, 0, 2);
        INFERNAL_EDLRIN_SIGHT = BUILDER.defineInRange("if_eldrin_sight", 0, 0, 1);
        INFERNAL_ELYTRA = BUILDER.defineInRange("if_elytra", 0, 0, 2);
        INFERNAL_EXPLOSION_RESISTANCE = BUILDER.defineInRange("if_explosion_resistance", 1, 0, 1);
        INFERNAL_FIRE_RESISTANCE = BUILDER.defineInRange("if_fire_resistance", 2, 0, 2);
        INFERNAL_FLY = BUILDER.defineInRange("if_fly", 0, 0, 2);
        INFERNAL_HEALTH_BOOST = BUILDER.defineInRange("if_health_boost", 0, 0, 5);
        INFERNAL_JUMP = BUILDER.defineInRange("if_jump", 3, 0, 3);
        INFERNAL_KINETIC_RESISTANCE = BUILDER.defineInRange("if_kinetic_resistance", 0, 0, 1);
        INFERNAL_MANA_BOOST = BUILDER.defineInRange("if_mana_boost", 0, 0, 5);
        INFERNAL_MANA_REGEN = BUILDER.defineInRange("if_mana_regen", 0, 0, 5);
        INFERNAL_METEOR_JUMP = BUILDER.defineInRange("if_meteor_jump", 1, 0, 1);
        INFERNAL_MIST_FORM = BUILDER.defineInRange("if_mist_form", 0, 0, 1);
        INFERNAL_MOVEMENT_SPEED = BUILDER.defineInRange("if_movement_speed", 3, 0, 3);
        INFERNAL_NIGHT_VISION = BUILDER.defineInRange("if_night_vision", 0, 0, 1);
        INFERNAL_PROJECTILE_REFLECTION = BUILDER.defineInRange("if_projectile_reflection", 0, 0, 3);
        INFERNAL_REGENERATION = BUILDER.defineInRange("if_regeneration", 0, 0, 1);
        INFERNAL_SATURATION = BUILDER.defineInRange("if_saturation", 0, 0, 1);
        INFERNAL_WATER_BREATHING = BUILDER.defineInRange("if_water_breathing", 0, 0, 2);
        INFERNAL_WELLSPRING_SIGHT = BUILDER.defineInRange("if_wellspring_sight", 0, 0, 1);
        BUILDER.pop();

        BUILDER.push("witherbone_armor");
        WITHERBONE_DAMAGE_BOOST = BUILDER.defineInRange("wi_damage_boost", 0, 0, 4);
        WITHERBONE_DAMAGE_RESISTANCE = BUILDER.defineInRange("wi_damage_resistance", 1, 0, 3);
        WITHERBONE_DOLPHINS_GRACE = BUILDER.defineInRange("wi_dolphins_grace", 0, 0, 2);
        WITHERBONE_EDLRIN_SIGHT = BUILDER.defineInRange("wi_eldrin_sight", 0, 0, 1);
        WITHERBONE_ELYTRA = BUILDER.defineInRange("wi_elytra", 0, 0, 2);
        WITHERBONE_EXPLOSION_RESISTANCE = BUILDER.defineInRange("wi_explosion_resistance", 0, 0, 1);
        WITHERBONE_FIRE_RESISTANCE = BUILDER.defineInRange("wi_fire_resistance", 0, 0, 2);
        WITHERBONE_FLY = BUILDER.defineInRange("wi_fly", 0, 0, 2);
        WITHERBONE_HEALTH_BOOST = BUILDER.defineInRange("wi_health_boost", 0, 0, 5);
        WITHERBONE_JUMP = BUILDER.defineInRange("wi_jump", 0, 0, 3);
        WITHERBONE_KINETIC_RESISTANCE = BUILDER.defineInRange("wi_kinetic_resistance", 0, 0, 1);
        WITHERBONE_MANA_BOOST = BUILDER.defineInRange("wi_mana_boost", 0, 0, 5);
        WITHERBONE_MANA_REGEN = BUILDER.defineInRange("wi_mana_regen", 2, 0, 5);
        WITHERBONE_METEOR_JUMP = BUILDER.defineInRange("wi_meteor_jump", 0, 0, 1);
        WITHERBONE_MIST_FORM = BUILDER.defineInRange("wi_mist_form", 1, 0, 1);
        WITHERBONE_MOVEMENT_SPEED = BUILDER.defineInRange("wi_movement_speed", 0, 0, 3);
        WITHERBONE_NIGHT_VISION = BUILDER.defineInRange("wi_night_vision", 0, 0, 1);
        WITHERBONE_PROJECTILE_REFLECTION = BUILDER.defineInRange("wi_projectile_reflection", 0, 0, 3);
        WITHERBONE_REGENERATION = BUILDER.defineInRange("wi_regeneration", 0, 0, 1);
        WITHERBONE_SATURATION = BUILDER.defineInRange("wi_saturation", 0, 0, 1);
        WITHERBONE_WATER_BREATHING = BUILDER.defineInRange("wi_water_breathing", 2, 0, 2);
        WITHERBONE_WELLSPRING_SIGHT = BUILDER.defineInRange("wi_wellspring_sight", 0, 0, 1);
        BUILDER.pop();

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static HashMap getCouncilInitEffects(){
        HashMap<ArmorUpgrade, Integer> ret = new HashMap<>();

        if(SPELLWEAVER_DAMAGE_BOOST.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("damage_boost"), SPELLWEAVER_DAMAGE_BOOST.get());
        }
        if(SPELLWEAVER_DAMAGE_RESISTANCE.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("damage_resistance"), SPELLWEAVER_DAMAGE_RESISTANCE.get());
        }
        if(SPELLWEAVER_DOLPHINS_GRACE.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("dolphins_grace"), SPELLWEAVER_DOLPHINS_GRACE.get());
        }
        if(SPELLWEAVER_EDLRIN_SIGHT.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("eldrin_sight"), SPELLWEAVER_EDLRIN_SIGHT.get());
        }
        if(SPELLWEAVER_ELYTRA.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("elytra"), SPELLWEAVER_ELYTRA.get());
        }

        if(SPELLWEAVER_EXPLOSION_RESISTANCE.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("explosion_resistance"), SPELLWEAVER_EXPLOSION_RESISTANCE.get());
        }
        if(SPELLWEAVER_FIRE_RESISTANCE.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("fire_resistance"), SPELLWEAVER_FIRE_RESISTANCE.get());
        }
        if(SPELLWEAVER_FLY.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("fly"), SPELLWEAVER_FLY.get());
        }
        if(SPELLWEAVER_HEALTH_BOOST.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("health_boost"), SPELLWEAVER_HEALTH_BOOST.get());
        }
        if(SPELLWEAVER_JUMP.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("jump"), SPELLWEAVER_JUMP.get());
        }

        if(SPELLWEAVER_KINETIC_RESISTANCE.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("kinetic_resistance"), SPELLWEAVER_KINETIC_RESISTANCE.get());
        }
        if(SPELLWEAVER_MANA_BOOST.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("mana_boost"), SPELLWEAVER_MANA_BOOST.get());
        }
        if(SPELLWEAVER_MANA_REGEN.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("mana_regen"), SPELLWEAVER_MANA_REGEN.get());
        }
        if(SPELLWEAVER_METEOR_JUMP.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("meteor_jump"), SPELLWEAVER_METEOR_JUMP.get());
        }
        if(SPELLWEAVER_MIST_FORM.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("mist_form"), SPELLWEAVER_MIST_FORM.get());
        }
        if(SPELLWEAVER_MOVEMENT_SPEED.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("movement_speed"), SPELLWEAVER_MOVEMENT_SPEED.get());
        }

        if(SPELLWEAVER_NIGHT_VISION.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("night_vision"), SPELLWEAVER_NIGHT_VISION.get());
        }
        if(SPELLWEAVER_PROJECTILE_REFLECTION.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("projectile_reflection"), SPELLWEAVER_PROJECTILE_REFLECTION.get());
        }
        if(SPELLWEAVER_REGENERATION.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("regeneration"), SPELLWEAVER_REGENERATION.get());
        }
        if(SPELLWEAVER_SATURATION.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("saturation"), SPELLWEAVER_SATURATION.get());
        }
        if(SPELLWEAVER_WATER_BREATHING.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("water_breathing"), SPELLWEAVER_WATER_BREATHING.get());
        }
        if(SPELLWEAVER_WELLSPRING_SIGHT.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("wellspring_sight"), SPELLWEAVER_WELLSPRING_SIGHT.get());
        }

        return ret;
    }

    public static HashMap getFeyInitEffects(){
        HashMap<ArmorUpgrade, Integer> ret = new HashMap<>();

        if(DRUID_DAMAGE_BOOST.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("damage_boost"), DRUID_DAMAGE_BOOST.get());
        }
        if(DRUID_DAMAGE_RESISTANCE.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("damage_resistance"), DRUID_DAMAGE_RESISTANCE.get());
        }
        if(DRUID_DOLPHINS_GRACE.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("dolphins_grace"), DRUID_DOLPHINS_GRACE.get());
        }
        if(DRUID_EDLRIN_SIGHT.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("eldrin_sight"), DRUID_EDLRIN_SIGHT.get());
        }
        if(DRUID_ELYTRA.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("elytra"), DRUID_ELYTRA.get());
        }

        if(DRUID_EXPLOSION_RESISTANCE.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("explosion_resistance"), DRUID_EXPLOSION_RESISTANCE.get());
        }
        if(DRUID_FIRE_RESISTANCE.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("fire_resistance"), DRUID_FIRE_RESISTANCE.get());
        }
        if(DRUID_FLY.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("fly"), DRUID_FLY.get());
        }
        if(DRUID_HEALTH_BOOST.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("health_boost"), DRUID_HEALTH_BOOST.get());
        }
        if(DRUID_JUMP.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("jump"), DRUID_JUMP.get());
        }

        if(DRUID_KINETIC_RESISTANCE.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("kinetic_resistance"), DRUID_KINETIC_RESISTANCE.get());
        }
        if(DRUID_MANA_BOOST.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("mana_boost"), DRUID_MANA_BOOST.get());
        }
        if(DRUID_MANA_REGEN.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("mana_regen"), DRUID_MANA_REGEN.get());
        }
        if(DRUID_METEOR_JUMP.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("meteor_jump"), DRUID_METEOR_JUMP.get());
        }
        if(DRUID_MIST_FORM.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("mist_form"), DRUID_MIST_FORM.get());
        }
        if(DRUID_MOVEMENT_SPEED.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("movement_speed"), DRUID_MOVEMENT_SPEED.get());
        }

        if(DRUID_NIGHT_VISION.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("night_vision"), DRUID_NIGHT_VISION.get());
        }
        if(DRUID_PROJECTILE_REFLECTION.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("projectile_reflection"), DRUID_PROJECTILE_REFLECTION.get());
        }
        if(DRUID_REGENERATION.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("regeneration"), DRUID_REGENERATION.get());
        }
        if(DRUID_SATURATION.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("saturation"), DRUID_SATURATION.get());
        }
        if(DRUID_WATER_BREATHING.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("water_breathing"), DRUID_WATER_BREATHING.get());
        }
        if(DRUID_WELLSPRING_SIGHT.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("wellspring_sight"), DRUID_WELLSPRING_SIGHT.get());
        }

        return ret;
    }

    public static HashMap getDemonInitEffects(){
        HashMap<ArmorUpgrade, Integer> ret = new HashMap<>();

        if(INFERNAL_DAMAGE_BOOST.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("damage_boost"), INFERNAL_DAMAGE_BOOST.get());
        }
        if(INFERNAL_DAMAGE_RESISTANCE.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("damage_resistance"), INFERNAL_DAMAGE_RESISTANCE.get());
        }
        if(INFERNAL_DOLPHINS_GRACE.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("dolphins_grace"), INFERNAL_DOLPHINS_GRACE.get());
        }
        if(INFERNAL_EDLRIN_SIGHT.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("eldrin_sight"), INFERNAL_EDLRIN_SIGHT.get());
        }
        if(INFERNAL_ELYTRA.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("elytra"), INFERNAL_ELYTRA.get());
        }

        if(INFERNAL_EXPLOSION_RESISTANCE.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("explosion_resistance"), INFERNAL_EXPLOSION_RESISTANCE.get());
        }
        if(INFERNAL_FIRE_RESISTANCE.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("fire_resistance"), INFERNAL_FIRE_RESISTANCE.get());
        }
        if(INFERNAL_FLY.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("fly"), INFERNAL_FLY.get());
        }
        if(INFERNAL_HEALTH_BOOST.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("health_boost"), INFERNAL_HEALTH_BOOST.get());
        }
        if(INFERNAL_JUMP.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("jump"), INFERNAL_JUMP.get());
        }

        if(INFERNAL_KINETIC_RESISTANCE.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("kinetic_resistance"), INFERNAL_KINETIC_RESISTANCE.get());
        }
        if(INFERNAL_MANA_BOOST.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("mana_boost"), INFERNAL_MANA_BOOST.get());
        }
        if(INFERNAL_MANA_REGEN.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("mana_regen"), INFERNAL_MANA_REGEN.get());
        }
        if(INFERNAL_METEOR_JUMP.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("meteor_jump"), INFERNAL_METEOR_JUMP.get());
        }
        if(INFERNAL_MIST_FORM.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("mist_form"), INFERNAL_MIST_FORM.get());
        }
        if(INFERNAL_MOVEMENT_SPEED.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("movement_speed"), INFERNAL_MOVEMENT_SPEED.get());
        }

        if(INFERNAL_NIGHT_VISION.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("night_vision"), INFERNAL_NIGHT_VISION.get());
        }
        if(INFERNAL_PROJECTILE_REFLECTION.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("projectile_reflection"), INFERNAL_PROJECTILE_REFLECTION.get());
        }
        if(INFERNAL_REGENERATION.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("regeneration"), INFERNAL_REGENERATION.get());
        }
        if(INFERNAL_SATURATION.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("saturation"), INFERNAL_SATURATION.get());
        }
        if(INFERNAL_WATER_BREATHING.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("water_breathing"), INFERNAL_WATER_BREATHING.get());
        }
        if(INFERNAL_WELLSPRING_SIGHT.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("wellspring_sight"), INFERNAL_WELLSPRING_SIGHT.get());
        }

        return ret;
    }

    public static HashMap getBoneInitEffects(){
        HashMap<ArmorUpgrade, Integer> ret = new HashMap<>();

        if(WITHERBONE_DAMAGE_BOOST.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("damage_boost"), WITHERBONE_DAMAGE_BOOST.get());
        }
        if(WITHERBONE_DAMAGE_RESISTANCE.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("damage_resistance"), WITHERBONE_DAMAGE_RESISTANCE.get());
        }
        if(WITHERBONE_DOLPHINS_GRACE.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("dolphins_grace"), WITHERBONE_DOLPHINS_GRACE.get());
        }
        if(WITHERBONE_EDLRIN_SIGHT.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("eldrin_sight"), WITHERBONE_EDLRIN_SIGHT.get());
        }
        if(WITHERBONE_ELYTRA.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("elytra"), WITHERBONE_ELYTRA.get());
        }

        if(WITHERBONE_EXPLOSION_RESISTANCE.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("explosion_resistance"), WITHERBONE_EXPLOSION_RESISTANCE.get());
        }
        if(WITHERBONE_FIRE_RESISTANCE.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("fire_resistance"), WITHERBONE_FIRE_RESISTANCE.get());
        }
        if(WITHERBONE_FLY.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("fly"), WITHERBONE_FLY.get());
        }
        if(WITHERBONE_HEALTH_BOOST.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("health_boost"), WITHERBONE_HEALTH_BOOST.get());
        }
        if(WITHERBONE_JUMP.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("jump"), WITHERBONE_JUMP.get());
        }

        if(WITHERBONE_KINETIC_RESISTANCE.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("kinetic_resistance"), WITHERBONE_KINETIC_RESISTANCE.get());
        }
        if(WITHERBONE_MANA_BOOST.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("mana_boost"), WITHERBONE_MANA_BOOST.get());
        }
        if(WITHERBONE_MANA_REGEN.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("mana_regen"), WITHERBONE_MANA_REGEN.get());
        }
        if(WITHERBONE_METEOR_JUMP.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("meteor_jump"), WITHERBONE_METEOR_JUMP.get());
        }
        if(WITHERBONE_MIST_FORM.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("mist_form"), WITHERBONE_MIST_FORM.get());
        }
        if(WITHERBONE_MOVEMENT_SPEED.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("movement_speed"), WITHERBONE_MOVEMENT_SPEED.get());
        }

        if(WITHERBONE_NIGHT_VISION.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("night_vision"), WITHERBONE_NIGHT_VISION.get());
        }
        if(WITHERBONE_PROJECTILE_REFLECTION.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("projectile_reflection"), WITHERBONE_PROJECTILE_REFLECTION.get());
        }
        if(WITHERBONE_REGENERATION.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("regeneration"), WITHERBONE_REGENERATION.get());
        }
        if(WITHERBONE_SATURATION.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("saturation"), WITHERBONE_SATURATION.get());
        }
        if(WITHERBONE_WATER_BREATHING.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("water_breathing"), WITHERBONE_WATER_BREATHING.get());
        }
        if(WITHERBONE_WELLSPRING_SIGHT.get() > 0){
            ret.put(ArmorUpgradeInit.getArmorUpgradeFromString("wellspring_sight"), WITHERBONE_WELLSPRING_SIGHT.get());
        }

        return ret;
    }
}
