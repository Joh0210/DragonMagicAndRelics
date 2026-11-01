package de.joh.dmnr.common.util;

import de.joh.dmnr.api.spell.ChronomancyProgress;
import de.joh.dmnr.api.spell.component.ConjureFluidComponent;
import de.joh.dmnr.common.spell.component.AccelerateTimeComponent;
import de.joh.dmnr.common.spell.component.MarkComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import de.joh.dmnr.common.item.spellstoring.RingOfCooldownSpellStoringItem;

import java.util.ArrayList;
import java.util.List;
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
     * How many harmful effects are needed to transform the Dragon Totem into the Ring of Power or the Ring of Ruling
     * @see de.joh.dmnr.common.item.DragonTotemItem
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> DRAGON_TOTEM_RING_HARMFUL_EFFECTS;

    /**
     * Percentage by which the Bracelet of Defense reduces all incoming damage while it has Dragon Magic.
     * @see de.joh.dmnr.common.item.DefenseBraceletItem
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> DEFENSE_BRACELET_DM_DAMAGE_REDUCTION;

    /**
     * Percentage by which the Warding Amulet of Mana and Artifice reduces magic damage while it has Dragon Magic, in
     * addition to its own reduction.
     * @see de.joh.dmnr.common.dragonmagic.WardingAmuletDragonMagic
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> WARDING_AMULET_DM_DAMAGE_REDUCTION;

    /**
     * Seconds the Fortifying Amulet of Mana and Artifice is on cooldown after it saved its wearer, while it has Dragon
     * Magic.
     * @see de.joh.dmnr.common.dragonmagic.FortifyingAmuletDragonMagic
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> FORTIFYING_AMULET_DM_COOLDOWN;

    /**
     * Level of the Mana Boost the Arcane Crown of Mana and Artifice keeps on its wearer while it has Dragon Magic.
     * @see de.joh.dmnr.common.dragonmagic.ArcaneCrownDragonMagic
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> ARCANE_CROWN_DM_MANA_BOOST_LEVEL;

    /**
     * Percentage of the souls gained while the Ring of Brittle Bone of Mana and Artifice is active that its wearer gains
     * once more while it has Dragon Magic.
     * @see de.joh.dmnr.common.dragonmagic.RingOfBrittleBoneDragonMagic
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> BRITTLE_BONE_RING_DM_SOUL_BONUS;

    /**
     * Percentage of the spell and melee damage bonus of the Amulet of the Battlemage of Mana and Artifice that it grants
     * once more while it has Dragon Magic.
     * @see de.joh.dmnr.common.dragonmagic.BattlemageAmuletDragonMagic
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> BATTLEMAGE_AMULET_DM_BONUS;

    /**
     * Factor by which the bolts of the Eldritch Orb of Mana and Artifice deal more damage while it has Dragon Magic.
     * @see de.joh.dmnr.common.dragonmagic.EldritchOrbDragonMagic
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> ELDRITCH_ORB_DM_DAMAGE_MULTIPLIER;

    /**
     * Number of additional projectiles on each side of the fan of the Prismatic Amulet of Mana and Artifice while it has
     * Dragon Magic.
     * @see de.joh.dmnr.common.dragonmagic.PrismaticAmuletDragonMagic
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> PRISMATIC_AMULET_DM_EXTRA_PAIRS;

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

    /**
     * If false, Accelerate Time can't be inscribed, isn't part of the silver spell loot and inscribed spells fail.
     * @see AccelerateTimeComponent
     */
    public static final ForgeConfigSpec.ConfigValue<Boolean> ACCELERATE_TIME_ENABLED;

    /**
     * Can Accelerate Time accelerate random ticks (crops, saplings, ...) when Precision is used?
     * @see AccelerateTimeComponent
     */
    public static final ForgeConfigSpec.ConfigValue<Boolean> ACCELERATE_TIME_RANDOM_TICKS;

    /**
     * Seconds of Chrono Exhaustion the caster suffers after casting Accelerate Time.
     * @see AccelerateTimeComponent
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> ACCELERATE_TIME_EXHAUSTION;

    /**
     * Minimum M&A tier from which bending time grants rote progress for Accelerate Time.
     * @see ChronomancyProgress
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> CHRONOMANCY_REQUIRED_TIER;

    /**
     * Rote progress for Accelerate Time granted by a source with the weight 1.0.
     * @see ChronomancyProgress
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> CHRONOMANCY_PROGRESS;

    /**
     * Seconds that have to pass before bending time grants rote progress for Accelerate Time again.
     * @see ChronomancyProgress
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> CHRONOMANCY_COOLDOWN;

    /**
     * Additional or changed sources of rote progress for Accelerate Time ("id=weight").
     * @see ChronomancyProgress
     */
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> CHRONOMANCY_SOURCES;

    /**
     * Radius of the area a Transposer without a Mystic Focus swaps, -1 if it needs a focus.
     * @see de.joh.dmnr.common.block.entity.TransposerBlockEntity
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> TRANSPOSER_RADIUS_NO_FOCUS;

    /**
     * Radius of the area a Transposer with a Minor Mystic Focus swaps.
     * @see de.joh.dmnr.common.block.entity.TransposerBlockEntity
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> TRANSPOSER_RADIUS_MINOR;

    /**
     * Radius of the area a Transposer with a Lesser Mystic Focus swaps.
     * @see de.joh.dmnr.common.block.entity.TransposerBlockEntity
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> TRANSPOSER_RADIUS_LESSER;

    /**
     * Radius of the area a Transposer with a Greater Mystic Focus swaps.
     * @see de.joh.dmnr.common.block.entity.TransposerBlockEntity
     */
    public static final ForgeConfigSpec.ConfigValue<Integer> TRANSPOSER_RADIUS_GREATER;

    /**
     * Does the Transposer also swap the entities in its area?
     * @see TranspositionHelper
     */
    public static final ForgeConfigSpec.ConfigValue<Boolean> TRANSPOSER_MOVE_ENTITIES;

    /**
     * Does a Transposer only swap with Transposers placed by the same player?
     * @see de.joh.dmnr.common.block.entity.TransposerBlockEntity
     */
    public static final ForgeConfigSpec.ConfigValue<Boolean> TRANSPOSER_REQUIRE_SAME_OWNER;

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
        DRAGON_TOTEM_RING_HARMFUL_EFFECTS = BUILDER.comment("The Dragon Totem turns into the Ring of Power when its holder kills a creature with at least this many harmful effects, and into the Ring of Ruling when its holder has at least this many harmful effects at once.")
                .defineInRange("Harmful effects needed for the Dragon Totem rings:", 5, 1, 32);
        DEFENSE_BRACELET_DM_DAMAGE_REDUCTION = BUILDER.comment("Percentage by which the Bracelet of Defense reduces all incoming damage while it has Dragon Magic. Several bracelets do not stack. The tooltip and the Codex show the default value.")
                .defineInRange("Bracelet of Defense Dragon Magic damage reduction (%):", 25, 0, 100);
        WARDING_AMULET_DM_DAMAGE_REDUCTION = BUILDER.comment("Percentage by which the Warding Amulet of Mana and Artifice reduces magic damage while it has Dragon Magic, in addition to its own 20%. Both reductions multiply: 25% result in 40% in total. The tooltip and the Codex show the default value.")
                .defineInRange("Warding Amulet Dragon Magic damage reduction (%):", 25, 0, 100);
        FORTIFYING_AMULET_DM_COOLDOWN = BUILDER.comment("Seconds the Fortifying Amulet of Mana and Artifice is on cooldown after it saved its wearer from death, while it has Dragon Magic (600 without). Every rescue still needs the full charge of the amulet. The tooltip and the Codex show the default value.")
                .defineInRange("Fortifying Amulet Dragon Magic cooldown (seconds):", 60, 0, 600);
        ARCANE_CROWN_DM_MANA_BOOST_LEVEL = BUILDER.comment("Level of the Mana Boost the Arcane Crown of Mana and Artifice keeps on its wearer while it has Dragon Magic (II without). Every level grants 50 maximum mana, so the default IV grants 200 instead of 100. The tooltip and the Codex show the default value.")
                .defineInRange("Arcane Crown Dragon Magic Mana Boost level:", 4, 2, 10);
        BRITTLE_BONE_RING_DM_SOUL_BONUS = BUILDER.comment("Percentage of the souls gained while the Ring of Brittle Bone of Mana and Artifice is active that its wearer gains once more while it has Dragon Magic. Applies to the souls from own kills and from the life drain of the Witherbone Armor. 100% doubles them. The tooltip and the Codex show the default value.")
                .defineInRange("Ring of Brittle Bone Dragon Magic soul bonus (%):", 100, 0, 1000);
        BATTLEMAGE_AMULET_DM_BONUS = BUILDER.comment("Percentage of the bonuses of the Amulet of the Battlemage of Mana and Artifice (30% spell damage with a sword in the other hand, up to 5 melee damage with a spell in the other hand) that it grants once more while it has Dragon Magic. 100% doubles them. The tooltip and the Codex show the default value.")
                .defineInRange("Amulet of the Battlemage Dragon Magic bonus (%):", 100, 0, 1000);
        ELDRITCH_ORB_DM_DAMAGE_MULTIPLIER = BUILDER.comment("Factor by which the bolts of the Eldritch Orb of Mana and Artifice deal more damage while it has Dragon Magic (1 damage without). The tooltip and the Codex show the default value.")
                .defineInRange("Eldritch Orb Dragon Magic damage multiplier:", 3, 1, 20);
        PRISMATIC_AMULET_DM_EXTRA_PAIRS = BUILDER.comment("Number of additional projectiles on each side of the fan of the Prismatic Amulet of Mana and Artifice while it has Dragon Magic. 1 results in five projectiles instead of three, 2 in seven. Every projectile carries the whole spell. The tooltip and the Codex show the default value.")
                .defineInRange("Prismatic Amulet Dragon Magic additional projectiles per side:", 1, 0, 3);
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

        BUILDER.push("Accelerate Time Configs");
        ACCELERATE_TIME_ENABLED = BUILDER.comment("If false: Accelerate Time can no longer be inscribed, is no longer part of the silver spells dropped as Arcane Thesis, and spells that already contain it fail. The attribute ranges and costs can be changed in the spell part configs of Mana and Artifice, e.g. the maximum Magnitude: every level doubles the speed of the block's time (1 = 2x, 2 = 4x, 3 = 8x, 4 = 16x).")
                .define("Is Accelerate Time enabled?", true);
        ACCELERATE_TIME_RANDOM_TICKS = BUILDER.comment("If true: With Precision, Accelerate Time also accelerates random ticks (crops, saplings, ...) by the same factor.")
                .define("Can Accelerate Time accelerate random ticks?", true);
        ACCELERATE_TIME_EXHAUSTION = BUILDER.comment("Seconds of Chrono Exhaustion the caster suffers after casting Accelerate Time. While exhausted, neither Accelerate Time nor Chrono Anchor can be cast. 0 disables it.")
                .defineInRange("Accelerate Time Chrono Exhaustion (seconds):", 60, 0, 3600);
        CHRONOMANCY_REQUIRED_TIER = BUILDER.comment("Accelerate Time is a silver spell that becomes rote by bending time (Sunrise, Nightfall, Chrono Anchor, Ritual of Aurora, ...). This is the minimum tier from which bending time grants rote progress.")
                .defineInRange("Accelerate Time progress required tier:", 5, 1, 5);
        CHRONOMANCY_PROGRESS = BUILDER.comment("Rote progress granted by a source with the weight 1.0. Accelerate Time needs 200 rote progress.")
                .defineInRange("Accelerate Time progress per source:", 20, 0, 10_000);
        CHRONOMANCY_COOLDOWN = BUILDER.comment("Seconds that have to pass before bending time grants rote progress again. Measured in game ticks, so changing the time of day does not shorten it. 0 disables it.")
                .defineInRange("Accelerate Time progress cooldown (seconds):", 1200, 0, 86_400);
        CHRONOMANCY_SOURCES = BUILDER.comment("Additional or changed sources of rote progress, as \"id=weight\". The id is a spell part (counted when it is applied to a target, even if its effect fails), a ritual (counted when it is completed) or a source registered by a mod. A weight of 0 disables a source. Built-in: dmnr:components/sunrise=1.0, dmnr:components/moonrise=1.0, mna:components/chrono_anchor=1.0, fnc:components/postponement=1.0, dmnr:weather_fairy_staff=1.0, mna:rituals/aurora=0.5, mna:rituals/eventide=0.5")
                .defineListAllowEmpty(List.of("Accelerate Time progress sources:"), ArrayList::new, CommonConfig::isValidSource);
        BUILDER.pop();

        BUILDER.push("Transposer Configs");
        TRANSPOSER_RADIUS_NO_FOCUS = BUILDER.comment("Radius of the area a Transposer without a Mystic Focus swaps. The area is a cube right above the Transposer, 2 * radius + 1 blocks wide, long and high (0 = 1x1x1, 1 = 3x3x3). -1: The Transposer needs a Mystic Focus.")
                .defineInRange("Transposer radius without a Mystic Focus:", 0, -1, 8);
        TRANSPOSER_RADIUS_MINOR = BUILDER.comment("Radius of the area a Transposer with a Minor Mystic Focus swaps.")
                .defineInRange("Transposer radius with a Minor Mystic Focus:", 1, 0, 8);
        TRANSPOSER_RADIUS_LESSER = BUILDER.comment("Radius of the area a Transposer with a Lesser Mystic Focus swaps.")
                .defineInRange("Transposer radius with a Lesser Mystic Focus:", 2, 0, 8);
        TRANSPOSER_RADIUS_GREATER = BUILDER.comment("Radius of the area a Transposer with a Greater Mystic Focus swaps.")
                .defineInRange("Transposer radius with a Greater Mystic Focus:", 3, 0, 8);
        TRANSPOSER_MOVE_ENTITIES = BUILDER.comment("If true: The Transposer also swaps the players, creatures and other entities in its area. Entities of the entity type tag dmnr:transposer_blacklist (by default the bosses) stay, like the blocks of the block tag dmnr:transposer_blacklist.")
                .define("Does the Transposer swap entities?", true);
        TRANSPOSER_REQUIRE_SAME_OWNER = BUILDER.comment("If true: A Transposer only swaps with Transposers that were placed by the same player. Transposers that were not placed by a player (e.g. by a command) can't swap then. Independent of this option, nothing is swapped if the owner of either Transposer may not break blocks in either area (claims of protection mods, wards of Mana and Artifice, ...).")
                .define("Does the Transposer need the same owner?", false);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static float getFlySpeedPerLevel(Player player) {
        AttributeInstance speedAttribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        float speed = (speedAttribute != null) ? (float) speedAttribute.getValue() * 10.0f : 1.0f;

        float configMultiplier = FLY_SPEED_PER_LEVEL.get() * 1.5f / 100f;

        return (speed * configMultiplier);
    }

    /**
     * Checks an entry of {@link #CHRONOMANCY_SOURCES}: "namespace:path=weight" with a non-negative weight.
     */
    private static boolean isValidSource(Object entry) {
        if (!(entry instanceof String source)) {
            return false;
        }

        String[] parts = source.split("=");
        if (parts.length != 2 || ResourceLocation.tryParse(parts[0].trim()) == null) {
            return false;
        }

        try {
            return Float.parseFloat(parts[1].trim()) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
