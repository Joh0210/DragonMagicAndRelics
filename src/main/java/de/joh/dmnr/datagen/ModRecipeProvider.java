package de.joh.dmnr.datagen;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import com.mna.api.affinity.Affinity;
import com.mna.api.faction.FactionIDs;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.common.init.SpellInit;
import de.joh.dmnr.compat.ae2.AE2Init;
import de.joh.dmnr.compat.create.CreateInit;
import de.joh.tnl.api.datagen.MagiChemDistillationRecipeBuilder.Category;
import de.joh.tnl.api.datagen.MnaRecipeProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.mna.items.ItemInit.*;
import static de.joh.tnl.api.datagen.EldrinAltarRecipeBuilder.eldrinAltar;
import static de.joh.tnl.api.datagen.MagiChemDistillationRecipeBuilder.distillation;
import static de.joh.tnl.api.datagen.MagiChemSublimationRecipeBuilder.sublimation;
import static de.joh.tnl.api.datagen.ManaweavePatterns.*;
import static de.joh.tnl.api.datagen.ManaweavingRecipeBuilder.manaweaving;
import static de.joh.tnl.api.datagen.SpellPartRecipeBuilder.component;
import static de.joh.tnl.api.datagen.SpellPartRecipeBuilder.shape;

/**
 * The recipes are grouped by the item they craft instead of by crafting station, so the upgrade stages of an item
 * stay together.
 * @author Joh0210
 */
public class ModRecipeProvider extends MnaRecipeProvider {
    private static final String AE2 = "ae2";
    private static final String CREATE = "create";

    private static final TagKey<Item> MA_FLOWERS = ItemTags.create(new ResourceLocation("mna", "ma_flowers"));

    public ModRecipeProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, DragonMagicAndRelics.MOD_ID, existingFileHelper);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> writer) {
        materials(writer);
        rings(writer);
        bracelets(writer);
        amuletsAndBelts(writer);
        charms(writer);
        headgearAndArmor(writer);
        blocks(writer);
        components(writer);
        shapes(writer);
        ae2(writer);
        create(writer);
        magichem(writer);
    }

    private void materials(Consumer<FinishedRecipe> writer) {
        save(writer, "mutandis", manaweaving(ItemInit.MUTANDIS.get(), 1)
                .quantity(16)
                .items(VINTEUM_DUST.get(), Items.EGG, Items.NETHER_WART, Items.BONE_MEAL)
                .tag(MA_FLOWERS)
                .patterns(SQUARE, TRIANGLE, SQUARE));

        save(writer, "purified_mutandis", manaweaving(ItemInit.PURIFIED_MUTANDIS.get(), 3)
                .quantity(16)
                .requiredFaction(FactionIDs.FEY)
                .items(ItemInit.MUTANDIS.get(), Items.EGG, Items.NETHER_WART, Items.BONE_MEAL)
                .tag(MA_FLOWERS)
                .item(PURIFIED_VINTEUM_DUST.get(), 4)
                .patterns(DIAMOND, SLASH, BACKSLASH));

        save(writer, "mana_cake", manaweaving(ItemInit.MANA_CAKE.get(), 3)
                .items(Items.CAKE, Items.COOKIE, Items.SUGAR, Items.PUMPKIN_PIE)
                .tag(MA_FLOWERS)
                .items(VINTEUM_DUST.get())
                .patterns(TRIANGLE, CIRCLE, TRIANGLE));

        save(writer, "potion_of_infinity", manaweaving(ItemInit.POTION_OF_INFINITY.get(), 3)
                .items(Items.NETHER_WART, Items.BREWING_STAND, Items.NETHER_WART, Items.GHAST_TEAR, ARCANIST_INK.get(),
                        Items.GHAST_TEAR, MOTE_WATER.get(), Items.NETHERITE_INGOT, MOTE_ARCANE.get())
                .patterns(BOLT, SPLIT_TRIANGLE, BOLT));

        save(writer, "brimstone_chalk", manaweaving(ItemInit.BRIMSTONE_CHALK.get(), 4)
                .items(BRIMSTONE_CHARM.get(), WIZARD_CHALK.get())
                .patterns(TRIANGLE, CIRCLE, TRIANGLE));

        save(writer, "brimstone_coal", manaweaving(ItemInit.BRIMSTONE_COAL.get(), 4)
                .items(BRIMSTONE_CHARM.get())
                .item(Items.COAL, 8)
                .patterns(DIAMOND, DIAMOND, DIAMOND));

        save(writer, "dragon_totem", eldrinAltar(ItemInit.DRAGON_TOTEM.get(), 5)
                .items(ItemInit.DRAGON_TOTEM_EMPTY.get(), GREATER_MOTE_WATER.get(), GREATER_MOTE_EARTH.get(),
                        GREATER_MOTE_FIRE.get(), GREATER_MOTE_AIR.get())
                .power(3000, Affinity.ENDER, Affinity.ARCANE));
    }

    private void rings(Consumer<FinishedRecipe> writer) {
        save(writer, "ocelot_ring_minor", manaweaving(ItemInit.OCELOT_RING_MINOR.get(), 2)
                .items(MUNDANE_RING.get(), STONE_RUNE_GREEN.get(), Items.SUGAR, Items.JUNGLE_SAPLING)
                .patterns(CIRCLE, DIAMOND, CIRCLE));

        save(writer, "ocelot_ring", manaweaving(ItemInit.OCELOT_RING.get(), 2)
                .items(ItemInit.OCELOT_RING_MINOR.get(), Items.GOLD_BLOCK, Items.RABBIT_FOOT, Items.GOLD_BLOCK,
                        Items.GLOWSTONE_DUST, Items.GOLD_BLOCK, MOTE_AIR.get(), Items.GOLD_BLOCK, Items.FERMENTED_SPIDER_EYE)
                .patterns(CIRCLE, DIAMOND, CIRCLE));

        save(writer, "ocelot_ring_greater", eldrinAltar(ItemInit.OCELOT_RING_GREATER.get(), 5)
                .items(ItemInit.OCELOT_RING.get(), Items.NETHERITE_INGOT, GREATER_MOTE_AIR.get(), Items.GOLD_INGOT)
                .power(500, Affinity.WIND));

        save(writer, "reach_ring_minor", manaweaving(ItemInit.REACH_RING_MINOR.get(), 2)
                .items(Items.ENDER_PEARL, Items.EMERALD, Items.GOLD_BLOCK, STONE_RUNE_PURPLE.get(), MUNDANE_RING.get(),
                        MOTE_ENDER.get())
                .patterns(SQUARE, CIRCLE));

        save(writer, "reach_ring", manaweaving(ItemInit.REACH_RING.get(), 4)
                .items(Items.ENDER_EYE, Items.ENDER_EYE, RESONATING_LUMP.get(), RESONATING_LUMP.get(),
                        GREATER_MOTE_ENDER.get(), ItemInit.REACH_RING_MINOR.get())
                .patterns(HOURGLASS, SPLIT_TRIANGLE, INVERTED_TRIANGLE));

        save(writer, "reach_ring_greater", eldrinAltar(ItemInit.REACH_RING_GREATER.get(), 5)
                .items(ItemInit.REACH_RING.get(), ItemInit.DRAGON_CORE.get())
                .power(2500, Affinity.ENDER));

        save(writer, "projectile_reflection_ring", eldrinAltar(ItemInit.PROJECTILE_REFLECTION_RING.get(), 4)
                .items(MUNDANE_RING.get(), com.mna.blocks.BlockInit.TARMA_ROOT.get(),
                        com.mna.blocks.BlockInit.DESERT_NOVA.get(), RUNE_DEFENSE.get(), GREATER_MOTE_AIR.get(),
                        GREATER_MOTE_ARCANE.get(), Items.GHAST_TEAR, Items.NETHERITE_INGOT, Items.PHANTOM_MEMBRANE)
                .power(700, Affinity.WIND, Affinity.ARCANE));

        save(writer, "ring_of_spell_storing", manaweaving(ItemInit.RING_OF_SPELL_STORING.get(), 3)
                .items(MUNDANE_BRACELET.get(), MOTE_ARCANE.get(), RUNE_ARCANE.get())
                .item(CHIMERITE_GEM.get(), 6)
                .patterns(CIRCLE, SQUARE, DIAMOND, SLASH, SQUARE, BACKSLASH));

        save(writer, "ring_of_spell_storing_cooldown", manaweaving(ItemInit.RING_OF_SPELL_STORING_COOLDOWN.get(), 3)
                .items(ItemInit.RING_OF_SPELL_STORING.get())
                .item(Items.CLOCK, 4)
                .patterns(DIAMOND, DIAMOND, DIAMOND));

        save(writer, "angel_ring", manaweaving(ItemInit.ANGEL_RING.get(), 5)
                .requiredFaction(FactionIDs.FEY)
                .items(Items.NETHER_STAR, Items.GOLD_BLOCK, Items.ELYTRA, Items.GOLD_BLOCK, MUNDANE_RING.get(),
                        GREATER_MOTE_AIR.get(), Items.GOLD_BLOCK, SPECTRAL_ELYTRA.get(), Items.GOLD_BLOCK)
                .patterns(INFINITY, SPLIT_TRIANGLE, HOURGLASS, INVERTED_SPLIT_TRIANGLE, INFINITY));

        save(writer, "devil_ring", manaweaving(ItemInit.DEVIL_RING.get(), 5)
                .requiredFaction(FactionIDs.DEMONS)
                .items(Items.NETHER_STAR, Items.NETHERITE_INGOT, HELLFIRE_TRIDENT.get(), Items.NETHERITE_INGOT,
                        MUNDANE_RING.get(), GREATER_MOTE_FIRE.get(), Items.NETHERITE_INGOT, LIVING_FLAME.get(),
                        Items.NETHERITE_INGOT)
                .patterns(INFINITY, SPLIT_TRIANGLE, STAR, INFINITY));

        save(writer, "fallen_angel_ring", manaweaving(ItemInit.FALLEN_ANGEL_RING.get(), 5)
                .requiredFaction(FactionIDs.UNDEAD)
                .items(Items.NETHER_STAR, Items.NETHERITE_INGOT, Items.ELYTRA, Items.NETHERITE_INGOT, MUNDANE_RING.get(),
                        GREATER_MOTE_ENDER.get(), Items.NETHERITE_INGOT, WITHERBONE.get(), Items.NETHERITE_INGOT)
                .patterns(STAR, INFINITY, SQUARE, DIAMOND, HOURGLASS, HOURGLASS));

        save(writer, "ring_of_power", manaweaving(ItemInit.RING_OF_POWER.get(), 5)
                .items(MUNDANE_RING.get(), ItemInit.DRAGON_CORE.get(), ItemInit.DRAGON_CORE.get(), Items.NETHERITE_BLOCK,
                        Items.SMOOTH_QUARTZ, WITHERBONE.get(), Items.BONE, com.mna.blocks.BlockInit.CHIMERITE_BLOCK.get(),
                        RUNE_DEFENSE.get())
                .patterns(INFINITY, SPLIT_TRIANGLE, HOURGLASS, INVERTED_SPLIT_TRIANGLE, INFINITY));

        save(writer, "ring_of_ruling", manaweaving(ItemInit.RING_OF_RULING.get(), 5)
                .items(MUNDANE_RING.get(), ItemInit.DRAGON_CORE.get(), ItemInit.DRAGON_CORE.get(), Items.NETHERITE_BLOCK,
                        Items.SMOOTH_QUARTZ, Items.GHAST_TEAR, Items.NETHERITE_INGOT, GREATER_MOTE_ENDER.get(),
                        GREATER_MOTE_AIR.get())
                .patterns(INFINITY, SPLIT_TRIANGLE, HOURGLASS, INVERTED_SPLIT_TRIANGLE, INFINITY));
    }

    private void bracelets(Consumer<FinishedRecipe> writer) {
        save(writer, "bracelet_of_water", manaweaving(ItemInit.BRACELET_OF_WATER.get(), 2)
                .tag(ItemTags.FISHES)
                .tag(ItemTags.FISHES)
                .items(Items.PACKED_ICE, Items.PACKED_ICE, MUNDANE_BRACELET.get(), MOTE_WATER.get())
                .patterns(TRIANGLE, INVERTED_TRIANGLE));

        save(writer, "bracelet_of_water_greater", manaweaving(ItemInit.BRACELET_OF_WATER_GREATER.get(), 4)
                .items(Items.FIRE_CORAL, Items.HORN_CORAL, Items.TUBE_CORAL, Items.BUBBLE_CORAL, Items.BRAIN_CORAL,
                        GREATER_MOTE_WATER.get(), ItemInit.BRACELET_OF_WATER.get())
                .patterns(KNOT, KNOT2, KNOT3, KNOT4));

        save(writer, "defense_bracelet_minor", manaweaving(ItemInit.DEFENSE_BRACELET_MINOR.get(), 2)
                .items(MUNDANE_BRACELET.get(), RUNE_DEFENSE.get(), Items.IRON_CHESTPLATE, Items.SHIELD, MOTE_EARTH.get())
                .patterns(DIAMOND, DIAMOND, DIAMOND, DIAMOND, DIAMOND, DIAMOND));

        save(writer, "defense_bracelet", manaweaving(ItemInit.DEFENSE_BRACELET.get(), 4)
                .items(ItemInit.DEFENSE_BRACELET_MINOR.get(), RUNE_DEFENSE.get(), Items.DIAMOND_CHESTPLATE,
                        Items.AMETHYST_SHARD, GREATER_MOTE_EARTH.get())
                .patterns(BOLT, STAR, BOLT));

        save(writer, "defense_bracelet_greater", eldrinAltar(ItemInit.DEFENSE_BRACELET_GREATER.get(), 5)
                .items(ItemInit.DEFENSE_BRACELET.get(), Items.OBSIDIAN, RUNE_DEFENSE.get(), Items.OBSIDIAN,
                        Items.NETHERITE_CHESTPLATE, Items.OBSIDIAN, RUNE_DEFENSE.get(), Items.OBSIDIAN, Items.NETHER_STAR)
                .power(2750, Affinity.EARTH));

        save(writer, "fire_resistance_bracelet", manaweaving(ItemInit.FIRE_RESISTANCE_BRACELET.get(), 4)
                .requiredFaction(FactionIDs.DEMONS)
                .items(BRIMSTONE_CHARM.get(), EMBERGLOW_BRACELET.get())
                .patterns(BOLT));

        save(writer, "bracelet_of_friendship", manaweaving(ItemInit.BRACELET_OF_FRIENDSHIP.get(), 3)
                .items(MUNDANE_BRACELET.get(), Items.GOLD_INGOT, Items.GLISTERING_MELON_SLICE, Items.GOLDEN_CARROT,
                        Items.GOLDEN_APPLE, Items.AMETHYST_SHARD, Items.AMETHYST_SHARD, PLAYER_CHARM.get(),
                        RUNE_DEFENSE.get())
                .patterns(TRIANGLE, SLASH, BACKSLASH, TRIANGLE));
    }

    private void amuletsAndBelts(Consumer<FinishedRecipe> writer) {
        save(writer, "collectors_amulet", manaweaving(ItemInit.COLLECTORS_AMULET.get(), 3)
                .items(MUNDANE_AMULET.get(), MUNDANE_RING.get(), MUNDANE_RING.get(), MUNDANE_RING.get(), Items.NETHER_STAR)
                .patterns(BOLT, INVERTED_TRIANGLE, DIAMOND));

        save(writer, "curse_protection_amulet", manaweaving(ItemInit.CURSE_PROTECTION_AMULET.get(), 5)
                .items(WITHERGUARD_AMULET.get(), Items.DIAMOND_BLOCK, Items.SOUL_SAND, Items.SOUL_SAND, Items.IRON_INGOT,
                        Items.MILK_BUCKET, ItemInit.DRAGON_CORE.get(), Items.TOTEM_OF_UNDYING, LIVING_FLAME.get())
                .patterns(INVERTED_SPLIT_TRIANGLE, SPLIT_TRIANGLE, INFINITY, HOURGLASS, INFINITY));

        save(writer, "regeneration_amulet", eldrinAltar(ItemInit.REGENERATION_AMULET.get(), 4)
                .items(MUNDANE_AMULET.get(), Items.ENCHANTED_GOLDEN_APPLE, STONE_RUNE_RED.get(), Items.GHAST_TEAR,
                        STONE_RUNE_RED.get(), Items.NETHER_STAR, STONE_RUNE_RED.get(), Items.GHAST_TEAR,
                        STONE_RUNE_RED.get())
                .power(1200, ALL_AFFINITIES));

        save(writer, "faction_amulet", eldrinAltar(ItemInit.FACTION_AMULET.get(), 5)
                .items(MUNDANE_AMULET.get(), Items.NETHER_STAR, MARK_OF_THE_COUNCIL.get(), Items.GOLD_BLOCK,
                        MARK_OF_THE_FEY.get(), Items.GHAST_TEAR, MARK_OF_THE_NETHER.get(), Items.GOLD_BLOCK,
                        MARK_OF_THE_UNDEAD.get())
                .power(1000, ALL_AFFINITIES));

        save(writer, "amulet_of_hellfire", eldrinAltar(ItemInit.AMULET_OF_HELLFIRE.get(), 5)
                .requiredFaction(FactionIDs.DEMONS)
                .items(EMBERGLOW_BRACELET.get(), Items.NETHERITE_INGOT, Items.NETHER_STAR, Items.NETHERITE_INGOT,
                        GREATER_MOTE_FIRE.get(), Items.NETHERITE_INGOT, LIVING_FLAME.get(), Items.NETHERITE_INGOT,
                        RUNE_FIRE.get())
                .power(800, Affinity.FIRE)
                .power(400, Affinity.ARCANE));

        save(writer, "belt_of_life_minor", manaweaving(ItemInit.BELT_OF_LIFE_MINOR.get(), 2)
                .items(Items.GHAST_TEAR, INFUSED_SILK.get(), HEALING_POULTICE.get(), Items.GOLDEN_APPLE,
                        STONE_RUNE_RED.get(), BELT_BUCKLE.get())
                .patterns(SQUARE, DIAMOND));

        save(writer, "belt_of_life", eldrinAltar(ItemInit.BELT_OF_LIFE.get(), 4)
                .items(ItemInit.BELT_OF_LIFE_MINOR.get(), Items.DIAMOND_BLOCK, STONE_RUNE_GREEN.get(),
                        STONE_RUNE_LIME.get(), com.mna.blocks.BlockInit.CERUBLOSSOM.get(), VINTEUM_DUST.get(),
                        VINTEUM_DUST.get(), Items.EMERALD_BLOCK, Items.REDSTONE_BLOCK)
                .power(400, Affinity.EARTH, Affinity.ARCANE));

        save(writer, "belt_of_life_greater", eldrinAltar(ItemInit.BELT_OF_LIFE_GREATER.get(), 5)
                .items(ItemInit.BELT_OF_LIFE.get(), STONE_RUNE_RED.get(), Items.NETHERITE_BLOCK, Items.NETHER_STAR)
                .tag(ItemTags.WOOL)
                .power(800, ALL_AFFINITIES));

        save(writer, "glass_cannon_belt", eldrinAltar(ItemInit.GLASS_CANNON_BELT.get(), 4)
                .items(BELT_BUCKLE.get(), RITUAL_FOCUS_GREATER.get(), Items.GLASS, Items.GHAST_TEAR, Items.GLASS,
                        Items.NETHERITE_SWORD, Items.GLASS, Items.GHAST_TEAR, Items.GLASS)
                .power(400, Affinity.FIRE));

        save(writer, "sturdy_belt", eldrinAltar(ItemInit.STURDY_BELT.get(), 4)
                .items(BELT_BUCKLE.get(), Items.OBSIDIAN)
                .tag(ItemTags.WOOL)
                .items(Items.NETHERITE_INGOT)
                .tag(ItemTags.WOOL)
                .items(Items.SHIELD)
                .tag(ItemTags.WOOL)
                .items(Items.NETHERITE_INGOT)
                .tag(ItemTags.WOOL)
                .power(400, Affinity.EARTH));
    }

    private void charms(Consumer<FinishedRecipe> writer) {
        save(writer, "revenge_charm_dmg", manaweaving(ItemInit.REVENGE_CHARM_DMG.get(), 3)
                .items(CHARM_PATTERN.get(), MANA_CRYSTAL_FRAGMENT.get(), Items.DIAMOND_SWORD, MOTE_EARTH.get())
                .patterns(BOLT, DIAMOND));

        save(writer, "revenge_charm_dmg_major", eldrinAltar(ItemInit.REVENGE_CHARM_DMG_MAJOR.get(), 5)
                .items(ItemInit.REVENGE_CHARM_DMG.get(), Items.NETHERITE_INGOT, Items.NETHERITE_INGOT)
                .power(1000, Affinity.WATER, Affinity.FIRE, Affinity.WIND, Affinity.ARCANE, Affinity.ENDER));

        save(writer, "revenge_charm_fire", manaweaving(ItemInit.REVENGE_CHARM_FIRE.get(), 3)
                .requiredFaction(FactionIDs.DEMONS)
                .items(CHARM_PATTERN.get(), MANA_CRYSTAL_FRAGMENT.get(), Items.IRON_SWORD, Items.FLINT_AND_STEEL)
                .patterns(BOLT, KNOT));

        save(writer, "revenge_charm_fire_major", eldrinAltar(ItemInit.REVENGE_CHARM_FIRE_MAJOR.get(), 5)
                .items(ItemInit.REVENGE_CHARM_FIRE.get(), Items.NETHERITE_INGOT, Items.NETHERITE_INGOT)
                .power(1000, Affinity.WATER, Affinity.FIRE, Affinity.WIND, Affinity.ARCANE, Affinity.ENDER));

        save(writer, "revenge_charm_reflect", manaweaving(ItemInit.REVENGE_CHARM_REFLECT.get(), 3)
                .items(CHARM_PATTERN.get(), MANA_CRYSTAL_FRAGMENT.get(), Items.DIAMOND_SWORD, MOTE_ARCANE.get())
                .patterns(BOLT, KNOT));

        save(writer, "revenge_charm_reflect_major", eldrinAltar(ItemInit.REVENGE_CHARM_REFLECT_MAJOR.get(), 5)
                .items(ItemInit.REVENGE_CHARM_REFLECT.get(), Items.NETHERITE_INGOT, Items.NETHERITE_INGOT)
                .power(1000, Affinity.WATER, Affinity.FIRE, Affinity.WIND, Affinity.ARCANE, Affinity.ENDER));

        save(writer, "voidfeather_charm", manaweaving(ItemInit.VOIDFEATHER_CHARM.get(), 2)
                .items(Items.BLAZE_ROD, Items.ENDER_PEARL, Items.FEATHER, INFUSED_SILK.get(), Items.BLACK_DYE,
                        PURIFIED_VINTEUM_DUST.get())
                .patterns(TRIANGLE, SQUARE, TRIANGLE));
    }

    private void headgearAndArmor(Consumer<FinishedRecipe> writer) {
        save(writer, "night_goggles", manaweaving(ItemInit.NIGHT_GOGGLES.get(), 2)
                .items(Items.AMETHYST_SHARD, RITUAL_FOCUS_LESSER.get(), Items.AMETHYST_SHARD, Items.LEATHER,
                        Items.SOUL_TORCH, Items.LEATHER)
                .patterns(CIRCLE, DIAMOND, CIRCLE));

        save(writer, "disappearing_tiara", manaweaving(ItemInit.DISAPPEARING_TIARA.get(), 3)
                .requiredFaction(FactionIDs.FEY)
                .items(Items.GHAST_TEAR, Items.IRON_HELMET, Items.CARROT, Items.SPIDER_EYE)
                .tag(Tags.Items.GLASS)
                .tag(Tags.Items.GLASS)
                .items(MOTE_ARCANE.get(), MOTE_AIR.get())
                .patterns(KNOT, KNOT2, KNOT3, KNOT4));

        save(writer, "hydra_crown", manaweaving(ItemInit.HYDRA_CROWN.get(), 3)
                .requiredFaction(FactionIDs.UNDEAD)
                .items(Items.GHAST_TEAR, com.mna.blocks.BlockInit.TARMA_ROOT.get(), Items.PURPLE_CANDLE,
                        STONE_RUNE_PURPLE.get(), Items.GOLDEN_HELMET)
                .patterns(SLASH, KNOT, SPLIT_TRIANGLE, KNOT4, BACKSLASH));

        save(writer, "rune_helmet", manaweaving(ItemInit.RUNE_HELMET.get(), 2)
                .items(Items.GOLDEN_HELMET, INFUSED_THREAD.get(), RUNE_ARCANE.get(), Items.AMETHYST_SHARD)
                .patterns(TRIANGLE, DIAMOND));

        save(writer, "rune_chestplate", manaweaving(ItemInit.RUNE_CHESTPLATE.get(), 2)
                .items(Items.GOLDEN_CHESTPLATE, INFUSED_THREAD.get(), RUNE_ARCANE.get())
                .item(Items.AMETHYST_SHARD, 5)
                .items(MOTE_ARCANE.get())
                .patterns(SQUARE, DIAMOND));

        save(writer, "rune_legging", manaweaving(ItemInit.RUNE_LEGGING.get(), 2)
                .items(Items.GOLDEN_LEGGINGS, INFUSED_THREAD.get(), RUNE_ARCANE.get(), Items.AMETHYST_SHARD)
                .patterns(INVERTED_TRIANGLE, DIAMOND));

        save(writer, "rune_boots", manaweaving(ItemInit.RUNE_BOOTS.get(), 2)
                .items(Items.GOLDEN_BOOTS, INFUSED_THREAD.get(), RUNE_ARCANE.get(), Items.AMETHYST_SHARD)
                .patterns(SQUARE, SQUARE, DIAMOND));
    }

    private void blocks(Consumer<FinishedRecipe> writer) {
        save(writer, "rift_emitter", manaweaving(ItemInit.RIFT_EMITTER_ITEM.get(), 3)
                .items(Items.ENDER_PEARL, Items.ENDER_PEARL, Items.ENDER_CHEST, Items.OBSIDIAN, Items.OBSIDIAN,
                        Items.OBSIDIAN, Items.GOLD_BLOCK, MOTE_ENDER.get(), ARCANE_ASH.get())
                .patterns(SQUARE, DIAMOND, SQUARE));
    }

    private void components(Consumer<FinishedRecipe> writer) {
        save(writer, component(SpellInit.ALTERNATIVERECALL, 3)
                .items(VINTEUM_DUST.get(), VINTEUM_DUST.get(), RUNE_MARKING.get(), MOTE_ENDER.get())
                .patterns(INVERTED_TRIANGLE, SQUARE));

        save(writer, component(SpellInit.CONJURE_LAVA, 5)
                .items(FLUID_JUG_INFINITE_LAVA.get(), GREATER_MOTE_FIRE.get(), STONE_RUNE_RED.get(),
                        STONE_RUNE_ORANGE.get(), Items.BLAZE_ROD, Items.BLAZE_ROD)
                .patterns(TRIANGLE, INVERTED_TRIANGLE, DIAMOND, INVERTED_TRIANGLE));

        save(writer, component(SpellInit.CONJURE_WATER, 3)
                .items(FLUID_JUG_INFINITE_WATER.get(), MOTE_WATER.get(), MOTE_WATER.get(), STONE_RUNE_BLUE.get(),
                        STONE_RUNE_LIGHT_BLUE.get(), STONE_RUNE_CYAN.get())
                .patterns(INVERTED_TRIANGLE, KNOT4, CIRCLE, KNOT, INVERTED_TRIANGLE));

        save(writer, component(SpellInit.FORCE_DAMAGE, 3)
                .items(STONE_RUNE_WHITE.get(), RUNE_DEFENSE.get(), Items.ANVIL, Items.ENDER_PEARL,
                        Items.PHANTOM_MEMBRANE)
                .patterns(TRIANGLE, KNOT, KNOT3, CIRCLE));

        save(writer, component(SpellInit.HELLFIRE, 5)
                .items(LIVING_FLAME.get(), GREATER_MOTE_FIRE.get(), LIVING_FLAME.get())
                .patterns(SLASH, KNOT2, KNOT));

        save(writer, component(SpellInit.MARK, 3)
                .items(BOOK_MARKS.get(), RUNE_MARKING.get(), RUNE_ENDER.get(), Items.COMPASS)
                .patterns(SLASH, KNOT2, KNOT));

        save(writer, component(SpellInit.MOONRISE, 5)
                .items(GREATER_MOTE_ENDER.get(), com.mna.blocks.BlockInit.CERUBLOSSOM.get(), RITUAL_FOCUS_GREATER.get(),
                        Items.CLOCK, Items.ENDER_PEARL, Items.NETHER_STAR)
                .patterns(STAR, INFINITY, HOURGLASS));

        save(writer, component(SpellInit.SATURATE, 4)
                .items(ItemInit.MANA_CAKE.get(), Items.CAKE, Items.BREAD, Items.COOKIE, Items.COOKED_SALMON,
                        Items.COOKED_BEEF, Items.COOKED_CHICKEN, Items.COOKED_RABBIT)
                .patterns(CIRCLE, SQUARE, CIRCLE));

        save(writer, component(SpellInit.SORCERERS_PRIDE, 5)
                .items(GREATER_MOTE_ENDER.get(), RITUAL_FOCUS_GREATER.get(), Items.DRAGON_HEAD,
                        ItemInit.DRAGON_CORE.get(), Items.NETHER_STAR)
                .patterns(INFINITY, STAR, HOURGLASS));

        save(writer, component(SpellInit.SUNRISE, 5)
                .items(GREATER_MOTE_ARCANE.get(), com.mna.blocks.BlockInit.DESERT_NOVA.get(), RITUAL_FOCUS_GREATER.get(),
                        Items.CLOCK, Items.ENDER_PEARL, Items.NETHER_STAR)
                .patterns(STAR, INFINITY, HOURGLASS));
    }

    private void shapes(Consumer<FinishedRecipe> writer) {
        save(writer, shape(SpellInit.ATMARK, 3)
                .items(BOOK_MARKS.get(), RUNE_MARKING.get(), RUNE_ENDER.get(), MOTE_ENDER.get())
                .patterns(KNOT, KNOT2, KNOT3, KNOT4));

        save(writer, shape(SpellInit.CURSE, 5)
                .items(PLAYER_CHARM.get(), RUNIC_SILK.get(), Items.NETHER_STAR, RUNE_ENDER.get(), MOTE_ENDER.get())
                .patterns(INFINITY, SPLIT_TRIANGLE, HOURGLASS, INVERTED_SPLIT_TRIANGLE, INFINITY));

        save(writer, shape(SpellInit.FRIENDS, 3)
                .items(PLAYER_CHARM.get(), Items.GLISTERING_MELON_SLICE, Items.GOLDEN_CARROT, Items.GOLDEN_APPLE,
                        Items.AMETHYST_SHARD, Items.TNT)
                .patterns(KNOT, SQUARE, TRIANGLE));

        save(writer, shape(SpellInit.TRUE_TOUCH, 2)
                .items(com.mna.blocks.BlockInit.AUM.get(), Items.ARMOR_STAND, VINTEUM_DUST.get(), INFUSED_SILK.get(),
                        Items.FEATHER, Items.CLAY)
                .patterns(CIRCLE, TRIANGLE, SQUARE, CIRCLE));
    }

    private void ae2(Consumer<FinishedRecipe> writer) {
        saveIntegration(writer, AE2, "altar_me_interface", manaweaving(AE2Init.ALTAR_INTERFACE_ITEM.get(), 3)
                .items(AEBlocks.INTERFACE, RUNE_MARKING.get(), AEItems.ENGINEERING_PROCESSOR, AEItems.FLUIX_CRYSTAL,
                        PURIFIED_VINTEUM_DUST.get(), CHIMERITE_GEM.get())
                .patterns(SQUARE, HOURGLASS, CIRCLE));
    }

    // Create's registry constants need Registrate, which is not on the compile classpath
    private void create(Consumer<FinishedRecipe> writer) {
        saveIntegration(writer, CREATE, "arcane_engine", manaweaving(CreateInit.ARCANE_ENGINE_ITEM.get(), 2)
                .item(createItem("andesite_casing"))
                .item(createItem("shaft"))
                .item(createItem("cogwheel"))
                .items(VINTEUM_DUST.get(), MANA_CRYSTAL_FRAGMENT.get())
                .patterns(CIRCLE, CIRCLE));

        saveIntegration(writer, CREATE, "brimstone_engine", manaweaving(CreateInit.BRIMSTONE_ENGINE_ITEM.get(), 4)
                .items(CreateInit.ARCANE_ENGINE_ITEM.get(), BRIMSTONE_CHARM.get())
                .item(createItem("blaze_burner"))
                .patterns(TRIANGLE, INFINITY, TRIANGLE));

        save(writer, component(CreateInit.SPECTRAL_MOTOR_COMPONENT, 3)
                .whenModLoaded(CREATE)
                .item(createItem("precision_mechanism"))
                .item(createItem("cogwheel"))
                .item(createItem("shaft"))
                .items(MOTE_AIR.get(), PURIFIED_VINTEUM_DUST.get())
                .patterns(CIRCLE, INFINITY, CIRCLE));
    }

    private static ResourceLocation createItem(String path) {
        return new ResourceLocation(CREATE, path);
    }

    private void magichem(Consumer<FinishedRecipe> writer) {
        save(writer, distillation(ItemInit.MUTANDIS.get())
                .outputRate(0.3f)
                .admixture("change", 4).admixture("plant", 2).admixture("odors", 1));

        save(writer, distillation(ItemInit.PURIFIED_MUTANDIS.get())
                .outputRate(0.4f)
                .essentia("arcane", 1).admixture("change", 4).admixture("plant", 2).admixture("fey", 1));

        save(writer, distillation(ItemInit.MANA_CAKE.get())
                .admixture("permanence", 12).admixture("delight", 8).admixture("sorcery", 7).admixture("lies", 6)
                .essentia("nourishing", 11));

        save(writer, distillation(ItemInit.POTION_OF_INFINITY.get())
                .admixture("witchcraft", 43).admixture("sorcery", 33).admixture("potential", 13)
                .essentia("citrinitas", 3));

        save(writer, distillation(ItemInit.BRIMSTONE_CHALK.get())
                .essentia("fire", 48).admixture("permanence", 25).admixture("sorcery", 11).admixture("instrument", 7)
                .essentia("citrinitas", 2));

        save(writer, distillation(ItemInit.BRIMSTONE_COAL.get())
                .essentia("fire", 128).admixture("energy", 25).admixture("permanence", 25).essentia("citrinitas", 2));

        save(writer, distillation(ItemInit.DRAGON_CORE.get())
                .categories(Category.TROPHY, Category.RARE)
                .admixture("sorcery", 64).essentia("citrinitas", 16).admixture("legend", 4).essentia("precious", 111)
                .essentia("rubedo", 64));

        save(writer, distillation(ItemInit.REACH_RING_MINOR.get())
                .admixture("alien", 42).admixture("travel", 27).admixture("instrument", 9).essentia("albedo", 5));

        save(writer, distillation(ItemInit.REACH_RING.get())
                .admixture("alien", 120).admixture("travel", 49).admixture("instrument", 19).essentia("citrinitas", 6));

        save(writer, distillation(ItemInit.REACH_RING_GREATER.get())
                .admixture("alien", 120).admixture("travel", 120).admixture("instrument", 50)
                .essentia("citrinitas", 16).essentia("rubedo", 48));

        save(writer, distillation(ItemInit.ANGEL_RING.get())
                .essentia("air", 86).admixture("flight", 86).admixture("fey", 38).admixture("instrument", 2)
                .essentia("rubedo", 12));

        save(writer, distillation(ItemInit.DEVIL_RING.get())
                .essentia("fire", 243).admixture("demon", 64).admixture("sorcery", 38).admixture("instrument", 2)
                .essentia("rubedo", 12));

        save(writer, distillation(ItemInit.FALLEN_ANGEL_RING.get())
                .admixture("potential", 14).admixture("exanimate", 38).admixture("instrument", 2)
                .essentia("fleshy", 86).essentia("rubedo", 12));

        save(writer, distillation(ItemInit.BATTLE_MAGE_RING.get())
                .categories(Category.RARE)
                .admixture("violence", 123).admixture("sorcery", 58).admixture("instrument", 20).admixture("legend", 6)
                .essentia("citrinitas", 16));

        save(writer, distillation(ItemInit.BRACELET_OF_WATER.get())
                .essentia("water", 27).admixture("travel", 42).admixture("instrument", 2).essentia("albedo", 5));

        save(writer, distillation(ItemInit.BRACELET_OF_WATER_GREATER.get())
                .essentia("water", 120).admixture("travel", 49).admixture("instrument", 4).essentia("citrinitas", 6));

        save(writer, distillation(ItemInit.DEFENSE_BRACELET_MINOR.get())
                .admixture("metal", 48).admixture("protection", 40).admixture("instrument", 9));

        save(writer, distillation(ItemInit.DEFENSE_BRACELET.get())
                .essentia("precious", 64).admixture("crystal", 56).admixture("protection", 56)
                .admixture("instrument", 13).essentia("nigredo", 16));

        save(writer, distillation(ItemInit.DEFENSE_BRACELET_GREATER.get())
                .admixture("protection", 72).admixture("metal", 32).admixture("instrument", 21)
                .essentia("precious", 104).essentia("albedo", 10));

        save(writer, distillation(ItemInit.BRACELET_OF_FRIENDSHIP.get())
                .admixture("protection", 36).admixture("thought", 7).admixture("history", 6).admixture("instrument", 2)
                .essentia("precious", 11));

        save(writer, distillation(ItemInit.COLLECTORS_AMULET.get())
                .admixture("potential", 38).admixture("sorcery", 28).essentia("citrinitas", 7));

        save(writer, distillation(ItemInit.CURSE_PROTECTION_AMULET.get())
                .admixture("protection", 128).admixture("curse", 21).admixture("luck", 21).admixture("witchcraft", 7)
                .essentia("citrinitas", 12));

        save(writer, distillation(ItemInit.REGENERATION_AMULET.get())
                .admixture("healing", 188).admixture("instrument", 13).essentia("precious", 109)
                .essentia("citrinitas", 6));

        save(writer, distillation(ItemInit.FACTION_AMULET.get())
                .admixture("protection", 38).admixture("demon", 16).admixture("exanimate", 16).admixture("fey", 16)
                .admixture("wizard", 16));

        save(writer, distillation(ItemInit.AMULET_OF_HELLFIRE.get())
                .essentia("fire", 243).admixture("demon", 64).admixture("sorcery", 38).admixture("instrument", 2)
                .essentia("rubedo", 12));

        save(writer, distillation(ItemInit.BELT_OF_LIFE_MINOR.get())
                .admixture("blood", 9).admixture("healing", 9).admixture("instrument", 9).admixture("protection", 9)
                .essentia("albedo", 5));

        save(writer, distillation(ItemInit.BELT_OF_LIFE.get())
                .admixture("blood", 21).admixture("healing", 21).admixture("protection", 21)
                .admixture("instrument", 13).essentia("precious", 99));

        save(writer, distillation(ItemInit.BELT_OF_LIFE_GREATER.get())
                .admixture("blood", 64).admixture("healing", 64).admixture("protection", 64)
                .admixture("instrument", 13).essentia("citrinitas", 6));

        save(writer, distillation(ItemInit.GLASS_CANNON_BELT.get())
                .admixture("violence", 128).admixture("change", 12).admixture("instrument", 2)
                .essentia("citrinitas", 11));

        save(writer, distillation(ItemInit.STURDY_BELT.get())
                .admixture("protection", 128).admixture("change", 12).admixture("instrument", 2)
                .essentia("citrinitas", 11));

        save(writer, distillation(ItemInit.REVENGE_CHARM_DMG.get())
                .essentia("earth", 8).admixture("violence", 8).admixture("instrument", 3).essentia("wrought", 5));

        save(writer, distillation(ItemInit.REVENGE_CHARM_DMG_MAJOR.get())
                .essentia("earth", 16).admixture("violence", 16).admixture("instrument", 8).essentia("albedo", 5));

        save(writer, distillation(ItemInit.REVENGE_CHARM_FIRE.get())
                .essentia("fire", 8).admixture("violence", 8).admixture("instrument", 3).essentia("wrought", 5));

        save(writer, distillation(ItemInit.REVENGE_CHARM_FIRE_MAJOR.get())
                .essentia("fire", 16).admixture("violence", 16).admixture("instrument", 8).essentia("albedo", 5));

        save(writer, distillation(ItemInit.REVENGE_CHARM_REFLECT.get())
                .essentia("arcane", 8).admixture("violence", 8).admixture("instrument", 3).essentia("wrought", 5));

        save(writer, distillation(ItemInit.REVENGE_CHARM_REFLECT_MAJOR.get())
                .essentia("arcane", 16).admixture("violence", 16).admixture("instrument", 8).essentia("albedo", 5));

        save(writer, distillation(ItemInit.VOIDFEATHER_CHARM.get())
                .admixture("protection", 14).admixture("alien", 12).admixture("travel", 8).admixture("instrument", 3)
                .essentia("wrought", 4));

        save(writer, sublimation(ItemInit.VOIDFEATHER_CHARM.get(), 2)
                .stage(8)
                .items(ItemInit.VOIDFEATHER_CHARM.get())
                .item(magichemItem("glass_orb"))
                .items(Items.FEATHER, PURIFIED_VINTEUM_DUST.get())
                .admixture("travel", 20).admixture("alien", 5).admixture("instrument", 2));

        save(writer, distillation(ItemInit.NIGHT_GOGGLES.get())
                .admixture("light", 48).admixture("crystal", 14).admixture("color", 9).admixture("instrument", 6)
                .essentia("albedo", 5));

        save(writer, distillation(ItemInit.DISAPPEARING_TIARA.get())
                .essentia("air", 36).essentia("arcane", 36).admixture("fey", 20).admixture("instrument", 2)
                .essentia("nigredo", 6));

        save(writer, distillation(ItemInit.HYDRA_CROWN.get())
                .admixture("exanimate", 16).admixture("violence", 15).admixture("blood", 14)
                .admixture("witchcraft", 11).admixture("instrument", 4));

        save(writer, distillation(ItemInit.WEATHER_FAIRY_STAFF.get())
                .categories(Category.RARE)
                .admixture("storm", 52).admixture("change", 41).admixture("instrument", 20).admixture("legend", 6)
                .essentia("citrinitas", 16));

        save(writer, distillation(ItemInit.THE_CLICKERS_COOKIE.get())
                .categories(Category.RARE)
                .admixture("delight", 119).admixture("alien", 45).admixture("legend", 6).essentia("nourishing", 184)
                .essentia("citrinitas", 16));

        save(writer, distillation(ItemInit.RIFT_EMITTER_ITEM.get())
                .essentia("ender", 13).admixture("travel", 36).admixture("vessel", 28).admixture("alien", 8));
    }
}
