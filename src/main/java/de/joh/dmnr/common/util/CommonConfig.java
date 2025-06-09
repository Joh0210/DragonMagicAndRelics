package de.joh.dmnr.common.util;

import de.joh.dmnr.common.armorupgrade.FlyArmorUpgrade;
import de.joh.dmnr.common.armorupgrade.MeteorJumpArmorUpgrade;
import de.joh.dmnr.common.armorupgrade.SaturationArmorUpgrade;
import de.joh.dmnr.common.armorupgrade.SpeedArmorUpgrade;
import de.joh.dmnr.api.spell.component.ConjureFluidComponent;
import de.joh.dmnr.common.spell.component.MarkComponent;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import de.joh.dmnr.common.item.spellstoring.RingOfCooldownSpellStoringItem;
/**
 * This file creates the entire Common Configs of this mod.
 * @author Joh0210
 */
public class CommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    /**
     * @see ConjureFluidComponent
     */
    public static final ForgeConfigSpec.ConfigValue<Boolean> CAN_CONJURE_FLUID_IGNORE_VAPORIZE;

    /**
     * Can the Mark Component use PlayerCharms?
     * @see MarkComponent
     */
    public static final ForgeConfigSpec.ConfigValue<Boolean> MARK_SUPPORT_PLAYERCHARM;

    /**
     * Can the Alternative Recall Component use PlayerCharms?
     * @see MarkComponent
     */
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECALL_SUPPORT_PLAYERCHARM;


    /**
     * Is the range of the Alternative Recall Component unlimited when at maximum level?
     * @see de.joh.dmnr.common.spell.component.AlternativeRecallComponent
     */
    public static final ForgeConfigSpec.ConfigValue<Boolean> RECALL_UNLIMITED_RANGE;

    /**
     * Belt of the Minotaur Multiplication Factor
     * @see RingOfCooldownSpellStoringItem
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> SPELL_STORING_COOLDOWN_FACTOR;

    /**
     * fly upgrade:
     * This times level = speed when flying. (0.5 is default creative)
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> FLY_SPEED_PER_LEVEL;

    /**
     * fly upgrade:
     * Defines whether it is possible to additionally sprint while flying with the Dragon Mage armor
     * @see FlyArmorUpgrade
     */
    public static final ForgeConfigSpec.ConfigValue<Boolean> FLY_ALLOW_SPRTINTING_WHILE_FLYING;

    /**
     * meteor jump upgrade:
     * Power of the Jump Impact
     * @see MeteorJumpArmorUpgrade
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> METEOR_JUMP_IMPACT;

    /**
     * saturation upgrade:
     * @see SaturationArmorUpgrade
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> SATURATION_MANA_PER_NUTRITION;

    /**
     * waterbreathing upgrade:
     * @see SpeedArmorUpgrade
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> WATERBREATHING_MANA_PRO_OXIGEN_BUBBLE;

    /**
     * damage resistance upgrade:
     * By what percentage is the damage reduced by the damage resistance upgrade (in %).
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> DAMAGE_RESISTANCE_DAMAGE_REDUCTION_PER_LEVEL;

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
        MARK_SUPPORT_PLAYERCHARM = BUILDER.define("If true: The Mark Component supports PlayerCharms", true);
        RECALL_SUPPORT_PLAYERCHARM = BUILDER.define("If true: The Alternative Recall Component supports PlayerCharms", true);
        RECALL_UNLIMITED_RANGE = BUILDER.define("If true: The range of the Alternative Recall Component is unlimited when the range attribute is at maximum level? ", true);
        SPELL_STORING_COOLDOWN_FACTOR = BUILDER.define("This number indicates the factor by which the cooldown is increased when casting the spell via the Bracelet of Spell Storing - Cooldown:", 10);
        BUILDER.pop();


        BUILDER.push("Upgrade Configs");
            BUILDER.push("Damage Resistance upgrade");
            DAMAGE_RESISTANCE_DAMAGE_REDUCTION_PER_LEVEL = BUILDER.defineInRange("By what percentage is the damage reduced by the damage resistance upgrade (in %):", 20, 0, 33);
            BUILDER.pop();



            BUILDER.push("Fire Resistance upgrade");
            FIRE_RESISTANCE_MANA_PER_FIRE_DAMAGE = BUILDER.defineInRange("How much mana does the fire resistance upgrade consume for one tick of fire damage?", 20, 0, 200);
            BUILDER.pop();

            BUILDER.push("Fly upgrade");
            FLY_SPEED_PER_LEVEL = BUILDER.comment("The airspeed is calculated with this value v. (Airspeed = v * upgrade level / 100). The default Creaktiv flight speed is 0.5").defineInRange("Flight Speed per Level:", 2, 0, 10);
            FLY_ALLOW_SPRTINTING_WHILE_FLYING = BUILDER.define("Can the wearer sprint in the air with the Fly Upgrade?", true);
            BUILDER.pop();

            BUILDER.push("Meteor Jump upgrade");
            METEOR_JUMP_IMPACT = BUILDER.defineInRange("Strength of the Impact:", 3, 1, 5);
            BUILDER.pop();

            BUILDER.push("Projectile Reflection");
            PROJECTILE_REFLECTION_TICKS_PER_CHARGE = BUILDER.defineInRange("How many ticks it takes to regenerate a Charge:", 400, 20, 4000);
            BUILDER.pop();

            BUILDER.push("Saturation upgrade");
            SATURATION_MANA_PER_NUTRITION = BUILDER.defineInRange("Mana cost per nutrition:", 15, 0, 200);
            BUILDER.pop();

            BUILDER.push("Waterbreathing upgrade");
            WATERBREATHING_MANA_PRO_OXIGEN_BUBBLE = BUILDER.defineInRange("Mana cost per oxigen bubble:", 20, 0, 100);
            BUILDER.pop();


        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static float getFlySpeedPerLevel(Player player) {
        AttributeInstance speedAttribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        float speed = (speedAttribute != null) ? (float) speedAttribute.getValue() * 10.0f : 1.0f;

        float configMultiplier = FLY_SPEED_PER_LEVEL.get() * 1.5f / 100f;

        return (speed * configMultiplier);
    }

    public static float getDamageResistanceDamageReductionPerLevel(){
        return DAMAGE_RESISTANCE_DAMAGE_REDUCTION_PER_LEVEL.get()/100.0F;
    }
}
