package de.joh.dragonmagicandrelics.config;

import net.minecraftforge.common.ForgeConfigSpec;
/**
 * This file creates the entire Common Configs of this mod.
 * @author Joh0210
 */
public class CommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    /**
     * @see de.joh.dragonmagicandrelics.spells.components.IComponentConjureFluid
     */
    public static final ForgeConfigSpec.ConfigValue<Boolean> CAN_CONJURE_FLUID_IGNORE_VAPORIZE;

    /**
     * fly upgrade:
     * This times level = speed when flying. (0.5 is default creative)
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> FLY_SPEED_PER_LEVEL;
    /**
     * fly upgrade:
     * Defines whether it is possible to additionally sprint while flying with the Dragon Mage armor
     * @see de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonarmortick.ArmorUpgradeFly
     */
    public static final ForgeConfigSpec.ConfigValue<Boolean> FLY_ALLOW_SPRTINTING_WHILE_FLYING;
    /**
     * fly upgrade:
     * mana cost while flying * 100
     * @see de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonarmortick.ArmorUpgradeFly
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> FLY_MANA_COST_PER_TICK;

    /**
     * meteor jump upgrade:
     * @see de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonarmortick.ArmorUpgradeMeteorJump
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> METEOR_JUMP_MANA_COST;
    /**
     * meteor jump upgrade:
     * Power of the Jump Impact
     * @see de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonarmortick.ArmorUpgradeMeteorJump
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> METEOR_JUMP_IMPACT;

    /**
     * saturation upgrade:
     * @see de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonarmortick.ArmorUpgradeSaturation
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> SATURATION_MANA_PER_NUTRITION;

    /**
     * speed upgrade:
     * @see de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonarmortick.ArmorUpgradeSpeed
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> SPEED_MANA_PER_TICK_PER_LEVEL;

    /**
     * waterbreathing upgrade:
     * @see de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonarmortick.ArmorUpgradeSpeed
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> WATERBREATHING_MANA_PRO_OXIGEN_BUBBLE;

    /**
     * damage resistance upgrade:
     * By what percentage is the damage reduced by the damage resistance upgrade (in %).
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> DAMAGE_RESISTANCE_DAMAGE_REDUCTION_PER_LEVEL;

    /**
     * elytra upgrade:
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> ELYTRA_MANA_COST_PER_TICK;

    /**
     * fire resistance upgrade:
     * How much mana does the fire resistance upgrade consume for one tick of fire damage?
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> FIRE_RESISTANCE_MANA_PER_FIRE_DAMAGE;

    /**
     * projectile reflection upgrade:
     * How many ticks it takes to regenerate a Charge
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> PROJECTILE_REFLECTION_TICKS_PER_CHARGE;

    static {
        BUILDER.push("General Configs");

        CAN_CONJURE_FLUID_IGNORE_VAPORIZE = BUILDER.comment("If true: Allow Conjure Water (etc.) to place water in the Nether (etc.) when magnitude has been increased.")
                .define("Can conjure fluid ignore vaporize?", true);
        BUILDER.pop();


        BUILDER.push("Upgrade Configs");
            BUILDER.push("Damage Resistance upgrade");
            DAMAGE_RESISTANCE_DAMAGE_REDUCTION_PER_LEVEL = BUILDER.defineInRange("By what percentage is the damage reduced by the damage resistance upgrade (in %):", 20, 0, 33);
            BUILDER.pop();

            BUILDER.push("Elytra upgrade");
            ELYTRA_MANA_COST_PER_TICK = BUILDER.defineInRange("How much mana is consumed every tick when the wearer flies with the Elytra upgrade (The values are divided by 100):", 75, 0, 500);
            BUILDER.pop();

            BUILDER.push("Elytra upgrade");
            FIRE_RESISTANCE_MANA_PER_FIRE_DAMAGE = BUILDER.defineInRange(" How much mana does the fire resistance upgrade consume for one tick of fire damage?:", 20, 0, 200);
            BUILDER.pop();

            BUILDER.push("Fly upgrade");
            FLY_SPEED_PER_LEVEL = BUILDER.comment("The airspeed is calculated with this value v. (Airspeed = v * upgrade level / 100). The default Creaktiv flight speed is 0.5").defineInRange("Flight Speed per Level:", 2, 0, 10);
            FLY_ALLOW_SPRTINTING_WHILE_FLYING = BUILDER.define("Can the wearer sprint in the air with the Fly Upgrade?", true);
            FLY_MANA_COST_PER_TICK = BUILDER.defineInRange("Mana cost per flight tick (The values are divided by 100):", 75, 0, 500);
            BUILDER.pop();

            BUILDER.push("Meteor Jump upgrade");
            METEOR_JUMP_IMPACT = BUILDER.defineInRange("Strength of the Impact:", 3, 1, 5);
            METEOR_JUMP_MANA_COST = BUILDER.defineInRange("Mana cost per meteor jump:", 40, 0, 400);
            BUILDER.pop();

            BUILDER.push("Projectile Reflection");
            PROJECTILE_REFLECTION_TICKS_PER_CHARGE = BUILDER.defineInRange("How many ticks it takes to regenerate a Charge:", 200, 20, 2000);
            BUILDER.pop();

            BUILDER.push("Saturation upgrade");
            SATURATION_MANA_PER_NUTRITION = BUILDER.defineInRange("Mana cost per nutrition:", 15, 0, 200);
            BUILDER.pop();

            BUILDER.push("Speed upgrade");
            SPEED_MANA_PER_TICK_PER_LEVEL = BUILDER.defineInRange("Mana cost per speed tick per level (The values are divided by 100):", 25, 0, 100);
            BUILDER.pop();

            BUILDER.push("Waterbreathing upgrade");
            WATERBREATHING_MANA_PRO_OXIGEN_BUBBLE = BUILDER.defineInRange("Mana cost per oxigen bubble:", 20, 0, 100);
            BUILDER.pop();


        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static float getFlySpeedPerLevel(){
        return FLY_SPEED_PER_LEVEL.get()/100.0F;
    }
    public static float getFlyManaCostPerTick(){
        return FLY_MANA_COST_PER_TICK.get()/100.0F;
    }

    public static float getElytraManaCostPerTick(){
        return ELYTRA_MANA_COST_PER_TICK.get()/100.0F;
    }

    public static float getSpeedManaCostPerTickPerLevel(){
        return SPEED_MANA_PER_TICK_PER_LEVEL.get()/100.0F;
    }

    public static float getDamageResistanceDamageReductionPerLevel(){
        return DAMAGE_RESISTANCE_DAMAGE_REDUCTION_PER_LEVEL.get()/100.0F;
    }
}
