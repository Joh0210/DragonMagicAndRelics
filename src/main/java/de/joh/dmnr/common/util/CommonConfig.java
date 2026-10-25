package de.joh.dmnr.common.util;

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
     * projectile reflection upgrade:
     * How many ticks it takes to regenerate a Charge
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> PROJECTILE_REFLECTION_TICKS_PER_CHARGE;

    /**
     * Rotation speed of the Arcane Engine in RPM (Create integration).
     * @see de.joh.dmnr.compat.create.block.EngineBlock
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> ARCANE_ENGINE_SPEED;

    /**
     * Stress capacity of the Arcane Engine in SU (Create integration). Independent of the speed.
     * @see de.joh.dmnr.compat.create.block.EngineBlock
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> ARCANE_ENGINE_STRESS;

    /**
     * Rotation speed of the Brimstone Engine in RPM (Create integration).
     * @see de.joh.dmnr.compat.create.block.EngineBlock
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> BRIMSTONE_ENGINE_SPEED;

    /**
     * Stress capacity of the Brimstone Engine in SU (Create integration). Independent of the speed.
     * @see de.joh.dmnr.compat.create.block.EngineBlock
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> BRIMSTONE_ENGINE_STRESS;

    static {
        BUILDER.push("General Configs");

        CAN_CONJURE_FLUID_IGNORE_VAPORIZE = BUILDER.comment("If true: Allow Conjure Water (etc.) to place water in the Nether (etc.) when magnitude has been increased.")
                .define("Can conjure fluid ignore vaporize?", true);
        MARK_SUPPORT_PLAYERCHARM = BUILDER.define("If true: The Mark Component supports PlayerCharms", true);
        RECALL_SUPPORT_PLAYERCHARM = BUILDER.define("If true: The Alternative Recall Component supports PlayerCharms", true);
        RECALL_UNLIMITED_RANGE = BUILDER.define("If true: The range of the Alternative Recall Component is unlimited when the range attribute is at maximum level? ", true);
        SPELL_STORING_COOLDOWN_FACTOR = BUILDER.define("This number indicates the factor by which the cooldown is increased when casting the spell via the Bracelet of Spell Storing - Cooldown:", 10);
        FLY_SPEED_PER_LEVEL = BUILDER.comment("The airspeed is calculated with this value v. (Airspeed = v * upgrade level / 100). The default Creaktiv flight speed is 0.5").defineInRange("Flight Speed per Level:", 2, 0, 10);
        PROJECTILE_REFLECTION_TICKS_PER_CHARGE = BUILDER.defineInRange("How many ticks it takes to regenerate a Charge:", 400, 20, 4000);
        BUILDER.pop();

        BUILDER.push("Create Integration Configs");
        ARCANE_ENGINE_SPEED = BUILDER.comment("Only used if Create is installed. Rotation speed of the Arcane Engine in RPM. Engines that are already placed adopt a changed value when their chunk is loaded.")
                .defineInRange("Arcane Engine Speed (RPM):", 16, 1, 256);
        ARCANE_ENGINE_STRESS = BUILDER.comment("Only used if Create is installed. Stress capacity of the Arcane Engine in SU, the same at every speed. The default is the capacity of a Large Water Wheel (4 RPM * 128 SU).")
                .defineInRange("Arcane Engine Stress Capacity (SU):", 512, 0, 1_000_000);
        BRIMSTONE_ENGINE_SPEED = BUILDER.comment("Only used if Create is installed. Rotation speed of the Brimstone Engine in RPM.")
                .defineInRange("Brimstone Engine Speed (RPM):", 128, 1, 256);
        BRIMSTONE_ENGINE_STRESS = BUILDER.comment("Only used if Create is installed. Stress capacity of the Brimstone Engine in SU, the same at every speed.")
                .defineInRange("Brimstone Engine Stress Capacity (SU):", 8192, 0, 1_000_000);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static float getFlySpeedPerLevel(Player player) {
        AttributeInstance speedAttribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        float speed = (speedAttribute != null) ? (float) speedAttribute.getValue() * 10.0f : 1.0f;

        float configMultiplier = FLY_SPEED_PER_LEVEL.get() * 1.5f / 100f;

        return (speed * configMultiplier);
    }
}
