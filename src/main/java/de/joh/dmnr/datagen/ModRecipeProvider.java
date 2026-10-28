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
        save(writer, "mutandis", manaweaving(ItemInit.MUTANDIS.get(), 2)
                .quantity(16)
                .items(VINTEUM_DUST.get(), Items.EGG, Items.NETHER_WART, Items.BONE_MEAL)
                .tag(MA_FLOWERS)
                .patterns(SQUARE, TRIANGLE));

        save(writer, "purified_mutandis", manaweaving(ItemInit.PURIFIED_MUTANDIS.get(), 3)
                .quantity(16)
                .requiredFaction(FactionIDs.FEY)
                .items(ItemInit.MUTANDIS.get(), Items.EGG, Items.NETHER_WART, Items.BONE_MEAL)
                .tag(MA_FLOWERS)
                .item(PURIFIED_VINTEUM_DUST.get(), 2)
                .patterns(DIAMOND, SLASH, BACKSLASH));

        save(writer, "mana_cake", manaweaving(ItemInit.MANA_CAKE.get(), 3)
                .items(Items.CAKE, Items.COOKIE, Items.SUGAR, Items.PUMPKIN_PIE)
                .tag(MA_FLOWERS)
                .items(VINTEUM_DUST.get())
                .patterns(TRIANGLE, CIRCLE, TRIANGLE));

        save(writer, "potion_of_infinity", manaweaving(ItemInit.POTION_OF_INFINITY.get(), 3)
                .items(Items.BREWING_STAND, Items.NETHER_WART, Items.GHAST_TEAR, Items.CLOCK, ARCANIST_INK.get(),
                        MOTE_WATER.get(), MOTE_ARCANE.get())
                .patterns(BOLT, SPLIT_TRIANGLE, BOLT));

        save(writer, "brimstone_chalk", manaweaving(ItemInit.BRIMSTONE_CHALK.get(), 4)
                .items(WIZARD_CHALK.get(), BRIMSTONE_CHARM.get())
                .item(Items.OBSIDIAN, 2)
                .item(PURIFIED_VINTEUM_DUST.get(), 2)
                .patterns(TRIANGLE, STAR, TRIANGLE));

        save(writer, "brimstone_coal", manaweaving(ItemInit.BRIMSTONE_COAL.get(), 4)
                .items(BRIMSTONE_CHARM.get())
                .item(Items.COAL, 8)
                .patterns(DIAMOND, DIAMOND, DIAMOND));

        save(writer, "dragon_totem", eldrinAltar(ItemInit.DRAGON_TOTEM.get(), 5)
                .items(ItemInit.DRAGON_TOTEM_EMPTY.get(), GREATER_MOTE_WATER.get(), GREATER_MOTE_EARTH.get(),
                        GREATER_MOTE_FIRE.get(), GREATER_MOTE_AIR.get(), GREATER_MOTE_ENDER.get(),
                        GREATER_MOTE_ARCANE.get(), Items.DRAGON_BREATH)
                .power(2500, ALL_AFFINITIES));
    }

    private void rings(Consumer<FinishedRecipe> writer) {
        save(writer, "ocelot_ring_minor", manaweaving(ItemInit.OCELOT_RING_MINOR.get(), 2)
                .items(MUNDANE_RING.get(), MOTE_AIR.get(), Items.SUGAR, Items.FEATHER)
                .patterns(CIRCLE, DIAMOND));

        save(writer, "ocelot_ring", manaweaving(ItemInit.OCELOT_RING.get(), 3)
                .items(ItemInit.OCELOT_RING_MINOR.get(), MOTE_AIR.get(), Items.RABBIT_FOOT, Items.GOLD_BLOCK,
                        Items.PHANTOM_MEMBRANE)
                .item(PURIFIED_VINTEUM_DUST.get(), 2)
                .patterns(CIRCLE, BOLT, CIRCLE));

        save(writer, "ocelot_ring_greater", eldrinAltar(ItemInit.OCELOT_RING_GREATER.get(), 5)
                .items(ItemInit.OCELOT_RING.get())
                .item(GREATER_MOTE_AIR.get(), 2)
                .items(GREATER_MOTE_EARTH.get(), Items.RABBIT_FOOT, Items.PHANTOM_MEMBRANE,
                        com.mna.blocks.BlockInit.TARMA_ROOT.get())
                .power(2500, Affinity.WIND)
                .power(1000, Affinity.EARTH));

        save(writer, "reach_ring_minor", manaweaving(ItemInit.REACH_RING_MINOR.get(), 2)
                .items(MUNDANE_RING.get(), MOTE_ENDER.get(), STONE_RUNE_PURPLE.get())
                .item(Items.ENDER_PEARL, 2)
                .patterns(SQUARE, CIRCLE));

        save(writer, "reach_ring", manaweaving(ItemInit.REACH_RING.get(), 4)
                .items(ItemInit.REACH_RING_MINOR.get(), GREATER_MOTE_ENDER.get(), RUNE_ENDER.get())
                .item(Items.ENDER_EYE, 2)
                .item(Items.POPPED_CHORUS_FRUIT, 2)
                .patterns(HOURGLASS, SPLIT_TRIANGLE, INVERTED_TRIANGLE));

        save(writer, "reach_ring_greater", eldrinAltar(ItemInit.REACH_RING_GREATER.get(), 5)
                .items(ItemInit.REACH_RING.get(), Items.DRAGON_BREATH, Items.END_CRYSTAL)
                .item(GREATER_MOTE_ENDER.get(), 2)
                .item(Items.POPPED_CHORUS_FRUIT, 2)
                .power(3000, Affinity.ENDER)
                .power(1000, Affinity.ARCANE));

        save(writer, "projectile_reflection_ring", eldrinAltar(ItemInit.PROJECTILE_REFLECTION_RING.get(), 4)
                .requiredFaction(FactionIDs.COUNCIL)
                .items(MUNDANE_RING.get(), RUNE_DEFENSE.get(), GREATER_MOTE_AIR.get(), GREATER_MOTE_AIR.get(),
                        GREATER_MOTE_ARCANE.get(), Items.PHANTOM_MEMBRANE, com.mna.blocks.BlockInit.TARMA_ROOT.get())
                .power(1500, Affinity.WIND)
                .power(500, Affinity.ARCANE));

        save(writer, "ring_of_spell_storing", manaweaving(ItemInit.RING_OF_SPELL_STORING.get(), 3)
                .items(MUNDANE_BRACELET.get(), CRYSTAL_OF_MEMORIES.get(), RUNE_ARCANE.get(), MOTE_ARCANE.get())
                .item(CHIMERITE_GEM.get(), 2)
                .patterns(CIRCLE, SPLIT_TRIANGLE, DIAMOND));

        save(writer, "ring_of_spell_storing_cooldown", manaweaving(ItemInit.RING_OF_SPELL_STORING_COOLDOWN.get(), 3)
                .items(ItemInit.RING_OF_SPELL_STORING.get())
                .item(Items.CLOCK, 2)
                .item(MANA_CRYSTAL_FRAGMENT.get(), 2)
                .patterns(DIAMOND, CIRCLE, DIAMOND));

        save(writer, "angel_ring", manaweaving(ItemInit.ANGEL_RING.get(), 5)
                .requiredFaction(FactionIDs.FEY)
                .items(MUNDANE_RING.get(), Items.NETHER_STAR, SPECTRAL_ELYTRA.get(), GREATER_MOTE_AIR.get(),
                        GREATER_MOTE_AIR.get(), GREATER_MOTE_ARCANE.get(), Items.GOLD_BLOCK, Items.GOLD_BLOCK,
                        Items.PHANTOM_MEMBRANE)
                .patterns(INFINITY, SPLIT_TRIANGLE, HOURGLASS, INVERTED_SPLIT_TRIANGLE, INFINITY));

        save(writer, "devil_ring", manaweaving(ItemInit.DEVIL_RING.get(), 5)
                .requiredFaction(FactionIDs.DEMONS)
                .items(MUNDANE_RING.get(), Items.NETHER_STAR, HELLFIRE_TRIDENT.get(), GREATER_MOTE_FIRE.get(),
                        GREATER_MOTE_FIRE.get(), LIVING_FLAME.get(), LIVING_FLAME.get(), RUNE_FIRE.get(),
                        Items.BLAZE_ROD)
                .patterns(INFINITY, SPLIT_TRIANGLE, STAR, INFINITY));

        save(writer, "fallen_angel_ring", manaweaving(ItemInit.FALLEN_ANGEL_RING.get(), 5)
                .requiredFaction(FactionIDs.UNDEAD)
                .items(MUNDANE_RING.get(), Items.NETHER_STAR, GREATER_MOTE_ENDER.get(), GREATER_MOTE_ENDER.get(),
                        WITHERBONE.get(), WITHERBONE.get(), Items.PHANTOM_MEMBRANE, Items.PHANTOM_MEMBRANE,
                        Items.WITHER_ROSE)
                .patterns(STAR, INFINITY, SQUARE, DIAMOND, HOURGLASS, HOURGLASS));

        save(writer, "ring_of_power", manaweaving(ItemInit.RING_OF_POWER.get(), 5)
                .items(MUNDANE_RING.get(), Items.DRAGON_HEAD, Items.END_CRYSTAL, Items.END_CRYSTAL,
                        com.mna.blocks.BlockInit.CHIMERITE_BLOCK.get(), GREATER_MOTE_ARCANE.get(),
                        GREATER_MOTE_FIRE.get(), RUNE_ARCANE.get(), Items.GOLD_BLOCK)
                .patterns(INFINITY, SPLIT_TRIANGLE, HOURGLASS, INVERTED_SPLIT_TRIANGLE, INFINITY));

        save(writer, "ring_of_ruling", manaweaving(ItemInit.RING_OF_RULING.get(), 5)
                .items(MUNDANE_RING.get(), Items.DRAGON_BREATH, Items.END_CRYSTAL, Items.END_CRYSTAL,
                        com.mna.blocks.BlockInit.CHIMERITE_BLOCK.get(), GREATER_MOTE_ARCANE.get(),
                        GREATER_MOTE_EARTH.get(), RUNE_DEFENSE.get(), Items.GOLD_BLOCK)
                .patterns(INFINITY, SPLIT_TRIANGLE, HOURGLASS, INVERTED_SPLIT_TRIANGLE, INFINITY));
    }

    private void bracelets(Consumer<FinishedRecipe> writer) {
        save(writer, "bracelet_of_water", manaweaving(ItemInit.BRACELET_OF_WATER.get(), 2)
                .tag(ItemTags.FISHES)
                .tag(ItemTags.FISHES)
                .items(Items.KELP)
                .patterns(TRIANGLE, INVERTED_TRIANGLE));

        save(writer, "bracelet_of_water_greater", manaweaving(ItemInit.BRACELET_OF_WATER_GREATER.get(), 4)
                .items(ItemInit.BRACELET_OF_WATER.get(), GREATER_MOTE_WATER.get(), RUNE_WATER.get())
                .item(Items.SCUTE, 2)
                .items(Items.TUBE_CORAL, Items.BRAIN_CORAL, Items.BUBBLE_CORAL)
                .patterns(KNOT, KNOT2, STAR));

        save(writer, "defense_bracelet_minor", manaweaving(ItemInit.DEFENSE_BRACELET_MINOR.get(), 3)
                .items(MUNDANE_BRACELET.get(), RUNE_DEFENSE.get(), MOTE_EARTH.get(), Items.IRON_CHESTPLATE, Items.SHIELD)
                .patterns(SQUARE, DIAMOND, SQUARE));

        save(writer, "defense_bracelet", manaweaving(ItemInit.DEFENSE_BRACELET.get(), 4)
                .items(ItemInit.DEFENSE_BRACELET_MINOR.get(), GREATER_MOTE_EARTH.get(), RUNE_DEFENSE.get(),
                        Items.DIAMOND_CHESTPLATE)
                .item(Items.OBSIDIAN, 2)
                .patterns(BOLT, STAR, BOLT));

        save(writer, "defense_bracelet_greater", eldrinAltar(ItemInit.DEFENSE_BRACELET_GREATER.get(), 5)
                .items(ItemInit.DEFENSE_BRACELET.get(), Items.NETHERITE_CHESTPLATE, Items.NETHER_STAR)
                .item(RUNE_DEFENSE.get(), 2)
                .item(GREATER_MOTE_EARTH.get(), 2)
                .item(Items.CRYING_OBSIDIAN, 2)
                .power(3000, Affinity.EARTH)
                .power(1000, Affinity.ARCANE));

        save(writer, "fire_resistance_bracelet", manaweaving(ItemInit.FIRE_RESISTANCE_BRACELET.get(), 4)
                .requiredFaction(FactionIDs.DEMONS)
                .items(EMBERGLOW_BRACELET.get(), BRIMSTONE_CHARM.get())
                .item(Items.MAGMA_CREAM, 2)
                .item(Items.OBSIDIAN, 2)
                .patterns(SPLIT_TRIANGLE, STAR, BOLT));

        save(writer, "bracelet_of_friendship", manaweaving(ItemInit.BRACELET_OF_FRIENDSHIP.get(), 3)
                .items(MUNDANE_BRACELET.get(), PLAYER_CHARM.get(), RUNE_DEFENSE.get(), Items.GOLDEN_APPLE,
                        Items.GLISTERING_MELON_SLICE, Items.GOLDEN_CARROT, Items.GOLD_INGOT)
                .patterns(TRIANGLE, SLASH, BACKSLASH));
    }

    private void amuletsAndBelts(Consumer<FinishedRecipe> writer) {
        save(writer, "collectors_amulet", manaweaving(ItemInit.COLLECTORS_AMULET.get(), 3)
                .items(MUNDANE_AMULET.get())
                .item(MUNDANE_RING.get(), 3)
                .item(Items.CHAIN, 2)
                .items(MOTE_ARCANE.get())
                .items(MOTE_ARCANE.get())
                .patterns(BOLT, INVERTED_TRIANGLE, DIAMOND));

        save(writer, "curse_protection_amulet", manaweaving(ItemInit.CURSE_PROTECTION_AMULET.get(), 5)
                .items(WITHERGUARD_AMULET.get(), Items.DRAGON_BREATH, Items.MILK_BUCKET, Items.MILK_BUCKET,
                        GREATER_MOTE_ARCANE.get(), GREATER_MOTE_WATER.get(), RUNE_DEFENSE.get(), Items.GOLDEN_APPLE)
                .patterns(INVERTED_SPLIT_TRIANGLE, SPLIT_TRIANGLE, INFINITY, HOURGLASS, INFINITY));

        save(writer, "regeneration_amulet", eldrinAltar(ItemInit.REGENERATION_AMULET.get(), 4)
                .items(MUNDANE_AMULET.get(), Items.GLISTERING_MELON_SLICE)
                .item(Items.GOLDEN_APPLE, 2)
                .item(Items.GHAST_TEAR, 2)
                .item(STONE_RUNE_RED.get(), 2)
                .power(400, ALL_AFFINITIES));

        save(writer, "faction_amulet", eldrinAltar(ItemInit.FACTION_AMULET.get(), 5)
                .items(MUNDANE_AMULET.get(), MARK_OF_THE_COUNCIL.get(), MARK_OF_THE_FEY.get(), MARK_OF_THE_NETHER.get(),
                        MARK_OF_THE_UNDEAD.get(), GREATER_MOTE_ARCANE.get(), Items.GOLD_BLOCK, Items.GOLD_BLOCK)
                .power(2000, ALL_AFFINITIES));

        save(writer, "amulet_of_hellfire", eldrinAltar(ItemInit.AMULET_OF_HELLFIRE.get(), 5)
                .requiredFaction(FactionIDs.DEMONS)
                .items(MUNDANE_AMULET.get(), RUNE_FIRE.get())
                .item(GREATER_MOTE_FIRE.get(), 2)
                .item(LIVING_FLAME.get(), 2)
                .item(Items.BLAZE_ROD, 2)
                .power(3000, Affinity.FIRE)
                .power(1500, Affinity.ARCANE));

        save(writer, "belt_of_life_minor", manaweaving(ItemInit.BELT_OF_LIFE_MINOR.get(), 2)
                .items(Items.GHAST_TEAR, INFUSED_SILK.get(), HEALING_POULTICE.get(), Items.GOLDEN_APPLE,
                        STONE_RUNE_RED.get(), BELT_BUCKLE.get())
                .patterns(SQUARE, DIAMOND));

        save(writer, "belt_of_life", eldrinAltar(ItemInit.BELT_OF_LIFE.get(), 4)
                .items(ItemInit.BELT_OF_LIFE_MINOR.get(), GREATER_MOTE_EARTH.get(), Items.DIAMOND_BLOCK,
                        Items.EMERALD_BLOCK, STONE_RUNE_RED.get())
                .item(Items.GOLDEN_APPLE, 2)
                .power(400, ALL_AFFINITIES));

        save(writer, "belt_of_life_greater", eldrinAltar(ItemInit.BELT_OF_LIFE_GREATER.get(), 5)
                .items(ItemInit.BELT_OF_LIFE.get(), Items.NETHER_STAR, Items.DIAMOND_BLOCK)
                .item(GREATER_MOTE_EARTH.get(), 2)
                .item(Items.GOLDEN_APPLE, 2)
                .item(STONE_RUNE_RED.get(), 2)
                .power(2000, ALL_AFFINITIES));

        save(writer, "glass_cannon_belt", eldrinAltar(ItemInit.GLASS_CANNON_BELT.get(), 4)
                .items(BELT_BUCKLE.get(), Items.NETHERITE_SWORD, GREATER_MOTE_FIRE.get())
                .tag(Tags.Items.GLASS)
                .tag(Tags.Items.GLASS)
                .item(Items.BLAZE_POWDER, 2)
                .power(2000, Affinity.FIRE));

        save(writer, "sturdy_belt", eldrinAltar(ItemInit.STURDY_BELT.get(), 4)
                .items(BELT_BUCKLE.get(), Items.SHIELD, Items.NETHERITE_INGOT, GREATER_MOTE_EARTH.get())
                .item(Items.OBSIDIAN, 2)
                .tag(ItemTags.WOOL)
                .tag(ItemTags.WOOL)
                .power(2000, Affinity.EARTH));
    }

    private void charms(Consumer<FinishedRecipe> writer) {
        save(writer, "revenge_charm_dmg", manaweaving(ItemInit.REVENGE_CHARM_DMG.get(), 3)
                .items(CHARM_PATTERN.get(), MANA_CRYSTAL_FRAGMENT.get(), MOTE_EARTH.get(), Items.DIAMOND_SWORD,
                        Items.CACTUS)
                .patterns(BOLT, DIAMOND));

        save(writer, "revenge_charm_dmg_major", eldrinAltar(ItemInit.REVENGE_CHARM_DMG_MAJOR.get(), 5)
                .items(ItemInit.REVENGE_CHARM_DMG.get(), GREATER_MOTE_EARTH.get(), GREATER_MOTE_EARTH.get(),
                        GREATER_MOTE_ARCANE.get(), Items.NETHERITE_SWORD)
                .item(Items.CACTUS, 2)
                .power(2500, Affinity.EARTH)
                .power(1000, Affinity.ARCANE));

        save(writer, "revenge_charm_fire", manaweaving(ItemInit.REVENGE_CHARM_FIRE.get(), 3)
                .requiredFaction(FactionIDs.DEMONS)
                .items(CHARM_PATTERN.get(), MANA_CRYSTAL_FRAGMENT.get(), MOTE_FIRE.get(), Items.IRON_SWORD,
                        Items.FLINT_AND_STEEL, Items.BLAZE_POWDER)
                .patterns(BOLT, KNOT));

        save(writer, "revenge_charm_fire_major", eldrinAltar(ItemInit.REVENGE_CHARM_FIRE_MAJOR.get(), 5)
                .requiredFaction(FactionIDs.DEMONS)
                .items(ItemInit.REVENGE_CHARM_FIRE.get(), Items.NETHERITE_SWORD, Items.MAGMA_CREAM)
                .item(GREATER_MOTE_FIRE.get(), 2)
                .item(LIVING_FLAME.get(), 2)
                .power(2500, Affinity.FIRE)
                .power(1000, Affinity.ARCANE));

        save(writer, "revenge_charm_reflect", manaweaving(ItemInit.REVENGE_CHARM_REFLECT.get(), 3)
                .items(CHARM_PATTERN.get(), MANA_CRYSTAL_FRAGMENT.get(), MOTE_ARCANE.get(), Items.DIAMOND_SWORD,
                        Items.SHIELD)
                .patterns(BOLT, KNOT));

        save(writer, "revenge_charm_reflect_major", eldrinAltar(ItemInit.REVENGE_CHARM_REFLECT_MAJOR.get(), 5)
                .items(ItemInit.REVENGE_CHARM_REFLECT.get(), GREATER_MOTE_ARCANE.get(), GREATER_MOTE_ARCANE.get(),
                        GREATER_MOTE_AIR.get(), Items.NETHERITE_SWORD, Items.SHIELD, RUNE_DEFENSE.get())
                .power(3500, Affinity.ARCANE));

        save(writer, "voidfeather_charm", manaweaving(ItemInit.VOIDFEATHER_CHARM.get(), 2)
                .items(Items.ENDER_PEARL, Items.FEATHER, Items.BLACK_DYE,
                        INFUSED_SILK.get())
                .patterns(TRIANGLE, SQUARE));
    }

    private void headgearAndArmor(Consumer<FinishedRecipe> writer) {
        save(writer, "night_goggles", manaweaving(ItemInit.NIGHT_GOGGLES.get(), 2)
                .items(Items.AMETHYST_SHARD, RITUAL_FOCUS_LESSER.get(), Items.AMETHYST_SHARD, Items.LEATHER,
                        Items.GOLDEN_CARROT, Items.LEATHER)
                .patterns(CIRCLE, DIAMOND, CIRCLE));

        save(writer, "disappearing_tiara", manaweaving(ItemInit.DISAPPEARING_TIARA.get(), 3)
                .requiredFaction(FactionIDs.FEY)
                .items(Items.IRON_HELMET, Items.FERMENTED_SPIDER_EYE, Items.GOLDEN_CARROT)
                .tag(Tags.Items.GLASS)
                .tag(Tags.Items.GLASS)
                .items(MOTE_ARCANE.get(), MOTE_AIR.get())
                .patterns(KNOT, KNOT2, KNOT3));

        save(writer, "hydra_crown", manaweaving(ItemInit.HYDRA_CROWN.get(), 3)
                .requiredFaction(FactionIDs.UNDEAD)
                .items(Items.GOLDEN_HELMET, Items.GHAST_TEAR, MOTE_WATER.get(), STONE_RUNE_PURPLE.get(),
                        Items.PURPLE_CANDLE, Items.BONE)
                .patterns(SLASH, SPLIT_TRIANGLE, BACKSLASH));

        save(writer, "rune_helmet", manaweaving(ItemInit.RUNE_HELMET.get(), 2)
                .items(Items.GOLDEN_HELMET, INFUSED_THREAD.get(), RUNE_ARCANE.get(), Items.AMETHYST_SHARD)
                .patterns(TRIANGLE, DIAMOND));

        save(writer, "rune_chestplate", manaweaving(ItemInit.RUNE_CHESTPLATE.get(), 2)
                .items(Items.GOLDEN_CHESTPLATE, INFUSED_THREAD.get(), RUNE_ARCANE.get())
                .item(Items.AMETHYST_SHARD, 3)
                .items(MOTE_ARCANE.get())
                .patterns(SQUARE, DIAMOND));

        save(writer, "rune_legging", manaweaving(ItemInit.RUNE_LEGGING.get(), 2)
                .items(Items.GOLDEN_LEGGINGS, INFUSED_THREAD.get(), RUNE_ARCANE.get(), Items.AMETHYST_SHARD)
                .patterns(INVERTED_TRIANGLE, DIAMOND));

        save(writer, "rune_boots", manaweaving(ItemInit.RUNE_BOOTS.get(), 2)
                .items(Items.GOLDEN_BOOTS, INFUSED_THREAD.get(), RUNE_ARCANE.get(), Items.AMETHYST_SHARD)
                .patterns(CIRCLE, DIAMOND));
    }

    private void blocks(Consumer<FinishedRecipe> writer) {
        save(writer, "rift_emitter", manaweaving(ItemInit.RIFT_EMITTER_ITEM.get(), 3)
                .items(Items.ENDER_CHEST, MOTE_ENDER.get(), RUNE_ENDER.get())
                .item(Items.ENDER_PEARL, 2)
                .item(Items.OBSIDIAN, 2)
                .patterns(SQUARE, DIAMOND, SQUARE));
    }

    private void components(Consumer<FinishedRecipe> writer) {
        save(writer, component(SpellInit.ALTERNATIVERECALL, 3)
                .items(VINTEUM_DUST.get(), VINTEUM_DUST.get(), RUNE_MARKING.get(), MOTE_ENDER.get())
                .patterns(INVERTED_TRIANGLE, SQUARE));

        save(writer, component(SpellInit.CONJURE_LAVA, 5)
                .items(FLUID_JUG_INFINITE_LAVA.get(), GREATER_MOTE_FIRE.get(), STONE_RUNE_RED.get(),
                        STONE_RUNE_ORANGE.get(), Items.BLAZE_ROD)
                .patterns(TRIANGLE, STAR, INVERTED_TRIANGLE));

        save(writer, component(SpellInit.CONJURE_WATER, 3)
                .items(FLUID_JUG_INFINITE_WATER.get(), MOTE_WATER.get(), STONE_RUNE_BLUE.get(),
                        STONE_RUNE_LIGHT_BLUE.get(), STONE_RUNE_CYAN.get())
                .patterns(INVERTED_TRIANGLE, KNOT));

        save(writer, component(SpellInit.FORCE_DAMAGE, 3)
                .items(STONE_RUNE_WHITE.get(), RUNE_DEFENSE.get(), Items.ANVIL, Items.ENDER_PEARL,
                        Items.PHANTOM_MEMBRANE)
                .patterns(TRIANGLE, KNOT, KNOT3, CIRCLE));

        save(writer, component(SpellInit.HELLFIRE, 5)
                .items(LIVING_FLAME.get(), GREATER_MOTE_FIRE.get(), LIVING_FLAME.get(), RUNE_FIRE.get())
                .patterns(SLASH, KNOT2, KNOT));

        save(writer, component(SpellInit.MARK, 3)
                .items(BOOK_MARKS.get(), RUNE_MARKING.get(), RUNE_ENDER.get(), Items.COMPASS)
                .patterns(SLASH, KNOT2, KNOT));

        save(writer, component(SpellInit.MOONRISE, 5)
                .items(GREATER_MOTE_ENDER.get(), com.mna.blocks.BlockInit.CERUBLOSSOM.get(), RITUAL_FOCUS_GREATER.get(),
                        Items.CLOCK, Items.ENDER_PEARL)
                .patterns(STAR, INFINITY, HOURGLASS));

        save(writer, component(SpellInit.SATURATE, 4)
                .items(ItemInit.MANA_CAKE.get(), Items.CAKE, Items.BREAD, Items.COOKIE, Items.COOKED_SALMON,
                        Items.COOKED_BEEF, Items.COOKED_CHICKEN, Items.COOKED_RABBIT)
                .patterns(CIRCLE, SQUARE, CIRCLE));

        save(writer, component(SpellInit.SORCERERS_PRIDE, 5)
                .items(GREATER_MOTE_ENDER.get(), GREATER_MOTE_ARCANE.get(), RITUAL_FOCUS_GREATER.get(),
                        Items.DRAGON_HEAD, Items.END_CRYSTAL)
                .patterns(INFINITY, STAR, HOURGLASS));

        save(writer, component(SpellInit.SUNRISE, 5)
                .items(GREATER_MOTE_ARCANE.get(), com.mna.blocks.BlockInit.DESERT_NOVA.get(), RITUAL_FOCUS_GREATER.get(),
                        Items.CLOCK, Items.ENDER_PEARL)
                .patterns(STAR, INFINITY, HOURGLASS));
    }

    private void shapes(Consumer<FinishedRecipe> writer) {
        save(writer, shape(SpellInit.ATMARK, 3)
                .items(BOOK_MARKS.get(), RUNE_MARKING.get(), RUNE_ENDER.get(), MOTE_ENDER.get())
                .patterns(KNOT, KNOT2, KNOT3, KNOT4));

        save(writer, shape(SpellInit.CURSE, 5)
                .items(PLAYER_CHARM.get(), RUNIC_SILK.get(), Items.NETHER_STAR, RUNE_ENDER.get(),
                        GREATER_MOTE_ENDER.get(), Items.WITHER_ROSE)
                .patterns(INFINITY, SPLIT_TRIANGLE, HOURGLASS, INVERTED_SPLIT_TRIANGLE));

        save(writer, shape(SpellInit.FRIENDS, 3)
                .items(PLAYER_CHARM.get(), Items.GLISTERING_MELON_SLICE, Items.GOLDEN_CARROT, Items.GOLDEN_APPLE,
                        Items.AMETHYST_SHARD, Items.TNT)
                .patterns(KNOT, SQUARE, TRIANGLE));

        save(writer, shape(SpellInit.TRUE_TOUCH, 2)
                .items(com.mna.blocks.BlockInit.AUM.get(), Items.ARMOR_STAND, VINTEUM_DUST.get(), INFUSED_SILK.get(),
                        Items.FEATHER, Items.CLAY)
                .patterns(CIRCLE, SQUARE));
    }

    private void ae2(Consumer<FinishedRecipe> writer) {
        saveIntegration(writer, AE2, "altar_me_interface", manaweaving(AE2Init.ALTAR_INTERFACE_ITEM.get(), 3)
                .items(AEBlocks.INTERFACE, RUNE_MARKING.get(), AEItems.ENGINEERING_PROCESSOR, AEItems.FLUIX_CRYSTAL,
                        PURIFIED_VINTEUM_DUST.get(), CHIMERITE_GEM.get())
                .patterns(SQUARE, BOLT, CIRCLE));
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
                .patterns(TRIANGLE, STAR, TRIANGLE));

        save(writer, component(CreateInit.SPECTRAL_MOTOR_COMPONENT, 3)
                .whenModLoaded(CREATE)
                .item(createItem("precision_mechanism"))
                .item(createItem("cogwheel"))
                .item(createItem("shaft"))
                .items(MOTE_AIR.get(), PURIFIED_VINTEUM_DUST.get())
                .patterns(CIRCLE, SPLIT_TRIANGLE, CIRCLE));
    }

    private static ResourceLocation createItem(String path) {
        return new ResourceLocation(CREATE, path);
    }

    /**
     * Distillation yields follow MagiChem's own yields for the Mana and Artifice curios: far below the materia of the
     * ingredients, one leading materia for what the item does, {@code adornment} for jewelry, {@code instrument} for
     * charms and tools, and the alchemy essentia of the recipe tier (2 nigredo, 3 albedo, 4 citrinitas, 5 rubedo).
     * The total grows with the tier (about 30-40 / 50-90 / 90-140 / 150-250) and with every upgrade stage. Relics
     * without a recipe yield {@code legend} and citrinitas like the Mana and Artifice relics.
     */
    private void magichem(Consumer<FinishedRecipe> writer) {
        save(writer, distillation(ItemInit.MUTANDIS.get())
                .outputRate(0.15f)
                .admixture("plant", 2).essentia("nourishing", 2).admixture("change", 1));

        save(writer, distillation(ItemInit.PURIFIED_MUTANDIS.get())
                .outputRate(0.15f)
                .admixture("plant", 2).admixture("change", 1).admixture("fey", 1));

        save(writer, distillation(ItemInit.MANA_CAKE.get())
                .admixture("delight", 12).admixture("mana", 10).admixture("satiety", 8).admixture("permanence", 6)
                .admixture("lies", 3));

        save(writer, distillation(ItemInit.POTION_OF_INFINITY.get())
                .admixture("witchcraft", 24).admixture("vessel", 16).admixture("sorcery", 12)
                .admixture("permanence", 10).essentia("albedo", 5));

        save(writer, distillation(ItemInit.BRIMSTONE_CHALK.get())
                .essentia("fire", 24).admixture("permanence", 16).admixture("wizard", 4).admixture("instrument", 4)
                .essentia("citrinitas", 4));

        save(writer, distillation(ItemInit.BRIMSTONE_COAL.get())
                .essentia("fire", 64).admixture("energy", 16).admixture("permanence", 16).admixture("hells", 8)
                .essentia("citrinitas", 6));

        save(writer, distillation(ItemInit.OCELOT_RING_MINOR.get())
                .admixture("motion", 12).essentia("air", 8).admixture("creature", 6).admixture("adornment", 3)
                .essentia("nigredo", 4));

        save(writer, distillation(ItemInit.OCELOT_RING.get())
                .admixture("motion", 24).essentia("air", 16).admixture("creature", 10).admixture("adornment", 4)
                .essentia("albedo", 5));

        save(writer, distillation(ItemInit.OCELOT_RING_GREATER.get())
                .admixture("motion", 72).essentia("air", 48).admixture("creature", 24).admixture("adornment", 8)
                .essentia("rubedo", 10));

        save(writer, distillation(ItemInit.REACH_RING_MINOR.get())
                .admixture("alien", 16).admixture("travel", 8).admixture("adornment", 3).essentia("wrought", 2)
                .essentia("nigredo", 4));

        save(writer, distillation(ItemInit.REACH_RING.get())
                .admixture("alien", 48).admixture("travel", 24).admixture("sorcery", 8).admixture("adornment", 5)
                .essentia("citrinitas", 6));

        save(writer, distillation(ItemInit.REACH_RING_GREATER.get())
                .admixture("alien", 96).admixture("travel", 48).admixture("sorcery", 16).admixture("adornment", 8)
                .essentia("rubedo", 10));

        save(writer, distillation(ItemInit.PROJECTILE_REFLECTION_RING.get())
                .admixture("protection", 32).admixture("wizard", 24).admixture("motion", 24)
                .admixture("adornment", 5).essentia("citrinitas", 6));

        save(writer, distillation(ItemInit.RING_OF_SPELL_STORING.get())
                .admixture("memory", 24).admixture("sorcery", 16).admixture("mana", 12).admixture("adornment", 4)
                .essentia("albedo", 5));

        save(writer, distillation(ItemInit.RING_OF_SPELL_STORING_COOLDOWN.get())
                .admixture("memory", 28).admixture("sorcery", 20).essentia("precious", 20).admixture("adornment", 4)
                .essentia("albedo", 6));

        save(writer, distillation(ItemInit.ANGEL_RING.get())
                .admixture("flight", 160).admixture("fey", 40).admixture("firmament", 16).admixture("adornment", 10)
                .essentia("rubedo", 16));

        save(writer, distillation(ItemInit.DEVIL_RING.get())
                .essentia("fire", 120).admixture("demon", 48).admixture("sorcery", 24).admixture("adornment", 10)
                .essentia("rubedo", 16));

        save(writer, distillation(ItemInit.FALLEN_ANGEL_RING.get())
                .admixture("flight", 64).admixture("darkness", 48).admixture("exanimate", 24)
                .admixture("adornment", 10).essentia("rubedo", 16));

        save(writer, distillation(ItemInit.RING_OF_POWER.get())
                .admixture("sorcery", 80).admixture("violence", 40).admixture("monster", 24).admixture("adornment", 10)
                .essentia("rubedo", 16));

        save(writer, distillation(ItemInit.RING_OF_RULING.get())
                .admixture("sorcery", 80).admixture("protection", 40).admixture("breath", 16).admixture("adornment", 10)
                .essentia("rubedo", 16));

        save(writer, distillation(ItemInit.BRACELET_OF_WATER.get())
                .essentia("water", 12).admixture("depths", 2).admixture("adornment", 2).essentia("nigredo", 2));

        save(writer, distillation(ItemInit.BRACELET_OF_WATER_GREATER.get())
                .essentia("water", 48).admixture("depths", 32).admixture("motion", 16).admixture("adornment", 5)
                .essentia("citrinitas", 6));

        save(writer, distillation(ItemInit.DEFENSE_BRACELET_MINOR.get())
                .admixture("protection", 32).admixture("metal", 16).admixture("permanence", 8)
                .admixture("adornment", 4).essentia("albedo", 5));

        save(writer, distillation(ItemInit.DEFENSE_BRACELET.get())
                .admixture("protection", 64).admixture("metal", 24).admixture("permanence", 16)
                .admixture("adornment", 5).essentia("citrinitas", 6));

        save(writer, distillation(ItemInit.DEFENSE_BRACELET_GREATER.get())
                .admixture("protection", 128).admixture("metal", 32).admixture("permanence", 24)
                .admixture("adornment", 8).essentia("rubedo", 12));

        save(writer, distillation(ItemInit.FIRE_RESISTANCE_BRACELET.get())
                .admixture("protection", 32).admixture("demon", 24).admixture("hells", 12).admixture("adornment", 5)
                .essentia("citrinitas", 6));

        save(writer, distillation(ItemInit.BRACELET_OF_FRIENDSHIP.get())
                .admixture("protection", 24).admixture("domestication", 8).admixture("thought", 8)
                .admixture("adornment", 4).essentia("albedo", 5));

        save(writer, distillation(ItemInit.COLLECTORS_AMULET.get())
                .admixture("adornment", 24).admixture("vessel", 12).admixture("metal", 12).admixture("sorcery", 8)
                .essentia("albedo", 5));

        save(writer, distillation(ItemInit.CURSE_PROTECTION_AMULET.get())
                .admixture("protection", 80).admixture("curse", 24).admixture("healing", 24)
                .admixture("adornment", 8).essentia("rubedo", 12));

        save(writer, distillation(ItemInit.REGENERATION_AMULET.get())
                .admixture("healing", 64).admixture("blood", 16).essentia("precious", 16).admixture("adornment", 5)
                .essentia("citrinitas", 6));

        save(writer, distillation(ItemInit.FACTION_AMULET.get())
                .admixture("bargain", 8).admixture("wizard", 8).admixture("fey", 8).admixture("demon", 8)
                .admixture("exanimate", 8));

        save(writer, distillation(ItemInit.AMULET_OF_HELLFIRE.get())
                .essentia("fire", 96).admixture("demon", 40).admixture("destruction", 16).admixture("adornment", 8)
                .essentia("rubedo", 12));

        save(writer, distillation(ItemInit.BELT_OF_LIFE_MINOR.get())
                .admixture("healing", 12).admixture("blood", 8).admixture("protection", 4).admixture("adornment", 3)
                .essentia("nigredo", 4));

        save(writer, distillation(ItemInit.BELT_OF_LIFE.get())
                .admixture("healing", 40).admixture("blood", 24).admixture("protection", 16).admixture("adornment", 5)
                .essentia("citrinitas", 6));

        save(writer, distillation(ItemInit.BELT_OF_LIFE_GREATER.get())
                .admixture("healing", 96).admixture("blood", 48).admixture("protection", 32).admixture("adornment", 8)
                .essentia("rubedo", 12));

        save(writer, distillation(ItemInit.GLASS_CANNON_BELT.get())
                .admixture("violence", 48).admixture("destruction", 24).admixture("crystal", 16)
                .admixture("adornment", 5).essentia("citrinitas", 6));

        save(writer, distillation(ItemInit.STURDY_BELT.get())
                .admixture("protection", 64).admixture("permanence", 24).admixture("stone", 16)
                .admixture("adornment", 5).essentia("citrinitas", 6));

        save(writer, distillation(ItemInit.REVENGE_CHARM_DMG.get())
                .admixture("violence", 16).admixture("trap", 8).admixture("protection", 8).admixture("instrument", 2)
                .essentia("albedo", 5));

        save(writer, distillation(ItemInit.REVENGE_CHARM_DMG_MAJOR.get())
                .admixture("violence", 48).admixture("trap", 24).admixture("protection", 24)
                .admixture("instrument", 4).essentia("rubedo", 10));

        save(writer, distillation(ItemInit.REVENGE_CHARM_FIRE.get())
                .essentia("fire", 16).admixture("demon", 8).admixture("protection", 8).admixture("instrument", 2)
                .essentia("albedo", 5));

        save(writer, distillation(ItemInit.REVENGE_CHARM_FIRE_MAJOR.get())
                .essentia("fire", 48).admixture("demon", 24).admixture("protection", 24).admixture("instrument", 4)
                .essentia("rubedo", 10));

        save(writer, distillation(ItemInit.REVENGE_CHARM_REFLECT.get())
                .essentia("arcane", 16).admixture("violence", 8).admixture("protection", 8)
                .admixture("instrument", 2).essentia("albedo", 5));

        save(writer, distillation(ItemInit.REVENGE_CHARM_REFLECT_MAJOR.get())
                .essentia("arcane", 48).admixture("violence", 24).admixture("protection", 24)
                .admixture("instrument", 4).essentia("rubedo", 10));

        save(writer, distillation(ItemInit.VOIDFEATHER_CHARM.get())
                .admixture("travel", 12).admixture("alien", 8).admixture("protection", 6).admixture("instrument", 2)
                .essentia("nigredo", 4));

        // alternative route from the Enderfeather Charm, like MagiChem's Hellfeather Charm
        save(writer, sublimation(ItemInit.VOIDFEATHER_CHARM.get(), 2)
                .stage(8)
                .items(BED_CHARM.get(), Items.FEATHER)
                .admixture("travel", 13).admixture("alien", 8).admixture("protection", 6));

        save(writer, distillation(ItemInit.NIGHT_GOGGLES.get())
                .admixture("sight", 6).admixture("light", 4).admixture("crystal", 4).admixture("adornment", 2)
                .essentia("nigredo", 4));

        save(writer, distillation(ItemInit.DISAPPEARING_TIARA.get())
                .admixture("lies", 20).admixture("fey", 16).admixture("darkness", 8).admixture("adornment", 4)
                .essentia("albedo", 5));

        save(writer, distillation(ItemInit.HYDRA_CROWN.get())
                .admixture("healing", 16).admixture("violence", 12).admixture("exanimate", 8)
                .admixture("adornment", 6).essentia("albedo", 5));

        save(writer, distillation(ItemInit.RUNE_HELMET.get())
                .essentia("precious", 20).admixture("sorcery", 16).admixture("protection", 12).admixture("adornment", 4)
                .essentia("nigredo", 4));

        save(writer, distillation(ItemInit.RUNE_CHESTPLATE.get())
                .essentia("precious", 32).admixture("sorcery", 32).admixture("protection", 24)
                .admixture("adornment", 10).essentia("nigredo", 4));

        save(writer, distillation(ItemInit.RUNE_LEGGING.get())
                .essentia("precious", 28).admixture("sorcery", 22).admixture("protection", 16).admixture("adornment", 6)
                .essentia("nigredo", 4));

        save(writer, distillation(ItemInit.RUNE_BOOTS.get())
                .essentia("precious", 16).admixture("sorcery", 12).admixture("protection", 9).admixture("adornment", 4)
                .essentia("nigredo", 4));

        save(writer, distillation(ItemInit.RIFT_EMITTER_ITEM.get())
                .admixture("vessel", 32).admixture("alien", 24).admixture("travel", 16).admixture("permanence", 8)
                .essentia("albedo", 5));

        // the Dragon Mage chain consumes a relic, so the item that carries it on yields its legend
        save(writer, distillation(ItemInit.DRAGON_TOTEM_EMPTY.get())
                .categories(Category.RARE)
                .admixture("potential", 16).admixture("monster", 8).admixture("flight", 8).admixture("legend", 6)
                .essentia("rubedo", 8));

        save(writer, distillation(ItemInit.DRAGON_TOTEM.get())
                .categories(Category.RARE)
                .admixture("potential", 32).admixture("realm", 24).admixture("breath", 16).admixture("legend", 6)
                .essentia("rubedo", 16));

        save(writer, distillation(ItemInit.AMULET_OF_DRAGON_POWER.get())
                .categories(Category.TROPHY, Category.RARE)
                .admixture("sorcery", 64).admixture("potential", 32).admixture("adornment", 10).admixture("legend", 6)
                .essentia("rubedo", 16));

        save(writer, distillation(ItemInit.DRAGON_MAGE_HELMET.get())
                .categories(Category.TROPHY, Category.RARE)
                .admixture("protection", 40).admixture("sorcery", 20).essentia("precious", 20)
                .admixture("potential", 10).essentia("rubedo", 10));

        save(writer, distillation(ItemInit.DRAGON_MAGE_CHESTPLATE.get())
                .categories(Category.TROPHY, Category.RARE)
                .admixture("protection", 64).admixture("sorcery", 32).admixture("flight", 32).admixture("potential", 16)
                .essentia("rubedo", 16));

        save(writer, distillation(ItemInit.DRAGON_MAGE_LEGGING.get())
                .categories(Category.TROPHY, Category.RARE)
                .admixture("protection", 56).admixture("sorcery", 28).essentia("precious", 28)
                .admixture("potential", 14).essentia("rubedo", 14));

        save(writer, distillation(ItemInit.DRAGON_MAGE_BOOTS.get())
                .categories(Category.TROPHY, Category.RARE)
                .admixture("protection", 32).admixture("sorcery", 16).essentia("precious", 16).admixture("potential", 8)
                .essentia("rubedo", 8));

        save(writer, distillation(ItemInit.BATTLE_MAGE_RING.get())
                .categories(Category.RARE)
                .admixture("violence", 64).admixture("sorcery", 64).admixture("adornment", 10).admixture("legend", 6)
                .essentia("citrinitas", 16));

        save(writer, distillation(ItemInit.WEATHER_FAIRY_STAFF.get())
                .categories(Category.RARE)
                .admixture("storm", 64).admixture("change", 24).admixture("fey", 16).admixture("legend", 6)
                .essentia("citrinitas", 16));

        save(writer, distillation(ItemInit.THE_CLICKERS_COOKIE.get())
                .categories(Category.RARE)
                .admixture("delight", 64).essentia("nourishing", 48).admixture("luck", 16).admixture("legend", 6)
                .essentia("citrinitas", 16));

        save(writer, distillation(ItemInit.KEY_OF_HOMESTEAD.get())
                .categories(Category.RARE)
                .admixture("travel", 64).admixture("alien", 32).admixture("instrument", 16).admixture("legend", 6)
                .essentia("citrinitas", 16));

        save(writer, distillation(ItemInit.JOURNAL_OF_MYSTERIES.get())
                .categories(Category.RARE)
                .admixture("memory", 64).admixture("thought", 24).admixture("history", 16).admixture("legend", 6)
                .essentia("citrinitas", 16));
    }
}
