package de.joh.dmnr.datagen;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import com.mna.api.faction.FactionIDs;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.common.util.RLoc;
import de.joh.dmnr.compat.ae2.AE2Init;
import de.joh.dmnr.compat.create.CreateInit;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.mna.items.ItemInit.*;
import static de.joh.dmnr.datagen.ManaweavingRecipeBuilder.manaweaving;

/**
 * Generates the manaweaving recipes ({@code data/dmnr/recipes/manaweaving}) and the manaweaving recipes of the
 * integrations ({@code data/dmnr/recipes/<mod_id>}), which are wrapped in a {@code forge:conditional} recipe with a
 * {@code forge:mod_loaded} condition.
 * <p>
 * The items of Mana and Artifice are statically imported ({@code com.mna.items.ItemInit}), the items of this mod are
 * referenced through {@link ItemInit}.
 * <p>
 * The recipe IDs must not change: the Codex refers to the recipes by their ID.
 * <br>All other recipe types (Eldrin Altar, components, shapes, rituals, progression, MagiChem) are still maintained by
 * hand in {@code src/main/resources}.
 * @author Joh0210
 */
public class ModRecipeProvider extends RecipeProvider {
    // Manaweave patterns of Mana and Artifice. M&A also accepts the short form "mna:<name>", only the full ID is used.
    private static final ResourceLocation BACKSLASH = pattern("backslash");
    private static final ResourceLocation BOLT = pattern("bolt");
    private static final ResourceLocation CIRCLE = pattern("circle");
    private static final ResourceLocation DIAMOND = pattern("diamond");
    private static final ResourceLocation HOURGLASS = pattern("hourglass");
    private static final ResourceLocation INFINITY = pattern("infinity");
    private static final ResourceLocation INVERTED_SPLIT_TRIANGLE = pattern("inverted_split_triangle");
    private static final ResourceLocation INVERTED_TRIANGLE = pattern("inverted_triangle");
    private static final ResourceLocation KNOT = pattern("knot");
    private static final ResourceLocation KNOT2 = pattern("knot2");
    private static final ResourceLocation KNOT3 = pattern("knot3");
    private static final ResourceLocation KNOT4 = pattern("knot4");
    private static final ResourceLocation SLASH = pattern("slash");
    private static final ResourceLocation SPLIT_TRIANGLE = pattern("split_triangle");
    private static final ResourceLocation SQUARE = pattern("square");
    private static final ResourceLocation STAR = pattern("star");
    private static final ResourceLocation TRIANGLE = pattern("triangle");

    private static final TagKey<Item> MA_FLOWERS = ItemTags.create(new ResourceLocation("mna", "ma_flowers"));

    private final ExistingFileHelper existingFileHelper;

    public ModRecipeProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output);
        this.existingFileHelper = existingFileHelper;
    }

    private static ResourceLocation pattern(String name) {
        return new ResourceLocation("mna", "manaweave_patterns/" + name);
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
        ae2(writer);
        create(writer);
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
                .items(PURIFIED_VINTEUM_DUST.get(), PURIFIED_VINTEUM_DUST.get(), PURIFIED_VINTEUM_DUST.get(),
                        PURIFIED_VINTEUM_DUST.get())
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
                .items(BRIMSTONE_CHARM.get(), Items.COAL, Items.COAL, Items.COAL, Items.COAL, Items.COAL, Items.COAL,
                        Items.COAL, Items.COAL)
                .patterns(DIAMOND, DIAMOND, DIAMOND));
    }

    private void rings(Consumer<FinishedRecipe> writer) {
        save(writer, "ocelot_ring_minor", manaweaving(ItemInit.OCELOT_RING_MINOR.get(), 2)
                .items(MUNDANE_RING.get(), STONE_RUNE_GREEN.get(), Items.SUGAR, Items.JUNGLE_SAPLING)
                .patterns(CIRCLE, DIAMOND, CIRCLE));

        save(writer, "ocelot_ring", manaweaving(ItemInit.OCELOT_RING.get(), 2)
                .items(ItemInit.OCELOT_RING_MINOR.get(), Items.GOLD_BLOCK, Items.RABBIT_FOOT, Items.GOLD_BLOCK,
                        Items.GLOWSTONE_DUST, Items.GOLD_BLOCK, MOTE_AIR.get(), Items.GOLD_BLOCK, Items.FERMENTED_SPIDER_EYE)
                .patterns(CIRCLE, DIAMOND, CIRCLE));

        save(writer, "reach_ring_minor", manaweaving(ItemInit.REACH_RING_MINOR.get(), 2)
                .items(Items.ENDER_PEARL, Items.EMERALD, Items.GOLD_BLOCK, STONE_RUNE_PURPLE.get(), MUNDANE_RING.get(),
                        MOTE_ENDER.get())
                .patterns(SQUARE, CIRCLE));

        save(writer, "reach_ring", manaweaving(ItemInit.REACH_RING.get(), 4)
                .items(Items.ENDER_EYE, Items.ENDER_EYE, RESONATING_LUMP.get(), RESONATING_LUMP.get(),
                        GREATER_MOTE_ENDER.get(), ItemInit.REACH_RING_MINOR.get())
                .patterns(HOURGLASS, SPLIT_TRIANGLE, INVERTED_TRIANGLE));

        save(writer, "ring_of_spell_storing", manaweaving(ItemInit.RING_OF_SPELL_STORING.get(), 3)
                .items(MUNDANE_BRACELET.get(), MOTE_ARCANE.get(), RUNE_ARCANE.get(), CHIMERITE_GEM.get(),
                        CHIMERITE_GEM.get(), CHIMERITE_GEM.get(), CHIMERITE_GEM.get(), CHIMERITE_GEM.get(),
                        CHIMERITE_GEM.get())
                .patterns(CIRCLE, SQUARE, DIAMOND, SLASH, SQUARE, BACKSLASH));

        save(writer, "ring_of_spell_storing_cooldown", manaweaving(ItemInit.RING_OF_SPELL_STORING_COOLDOWN.get(), 3)
                .items(ItemInit.RING_OF_SPELL_STORING.get(), Items.CLOCK, Items.CLOCK, Items.CLOCK, Items.CLOCK)
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

        save(writer, "belt_of_life_minor", manaweaving(ItemInit.BELT_OF_LIFE_MINOR.get(), 2)
                .items(Items.GHAST_TEAR, INFUSED_SILK.get(), HEALING_POULTICE.get(), Items.GOLDEN_APPLE,
                        STONE_RUNE_RED.get(), BELT_BUCKLE.get())
                .patterns(SQUARE, DIAMOND));
    }

    private void charms(Consumer<FinishedRecipe> writer) {
        save(writer, "revenge_charm_dmg", manaweaving(ItemInit.REVENGE_CHARM_DMG.get(), 3)
                .items(CHARM_PATTERN.get(), MANA_CRYSTAL_FRAGMENT.get(), Items.DIAMOND_SWORD, MOTE_EARTH.get())
                .patterns(BOLT, DIAMOND));

        save(writer, "revenge_charm_fire", manaweaving(ItemInit.REVENGE_CHARM_FIRE.get(), 3)
                .requiredFaction(FactionIDs.DEMONS)
                .items(CHARM_PATTERN.get(), MANA_CRYSTAL_FRAGMENT.get(), Items.IRON_SWORD, Items.FLINT_AND_STEEL)
                .patterns(BOLT, KNOT));

        save(writer, "revenge_charm_reflect", manaweaving(ItemInit.REVENGE_CHARM_REFLECT.get(), 3)
                .items(CHARM_PATTERN.get(), MANA_CRYSTAL_FRAGMENT.get(), Items.DIAMOND_SWORD, MOTE_ARCANE.get())
                .patterns(BOLT, KNOT));

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
                .items(Items.GOLDEN_CHESTPLATE, INFUSED_THREAD.get(), RUNE_ARCANE.get(), Items.AMETHYST_SHARD,
                        Items.AMETHYST_SHARD, Items.AMETHYST_SHARD, Items.AMETHYST_SHARD, Items.AMETHYST_SHARD,
                        MOTE_ARCANE.get())
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

    private void ae2(Consumer<FinishedRecipe> writer) {
        saveConditional(writer, "ae2", "altar_me_interface", manaweaving(AE2Init.ALTAR_INTERFACE_ITEM.get(), 3)
                .items(AEBlocks.INTERFACE, RUNE_MARKING.get(), AEItems.ENGINEERING_PROCESSOR, AEItems.FLUIX_CRYSTAL,
                        PURIFIED_VINTEUM_DUST.get(), CHIMERITE_GEM.get())
                .patterns(SQUARE, HOURGLASS, CIRCLE));
    }

    private void create(Consumer<FinishedRecipe> writer) {
        // The items of Create are referenced by ID: its registry constants need Registrate, which is not on the compile
        // classpath
        saveConditional(writer, "create", "arcane_engine", manaweaving(CreateInit.ARCANE_ENGINE_ITEM.get(), 2)
                .item(createItem("andesite_casing"))
                .item(createItem("shaft"))
                .item(createItem("cogwheel"))
                .items(VINTEUM_DUST.get(), MANA_CRYSTAL_FRAGMENT.get())
                .patterns(CIRCLE, CIRCLE));

        saveConditional(writer, "create", "brimstone_engine", manaweaving(CreateInit.BRIMSTONE_ENGINE_ITEM.get(), 4)
                .items(CreateInit.ARCANE_ENGINE_ITEM.get(), BRIMSTONE_CHARM.get())
                .item(createItem("blaze_burner"))
                .patterns(TRIANGLE, INFINITY, TRIANGLE));
    }

    private static ResourceLocation createItem(String path) {
        return new ResourceLocation("create", path);
    }

    /**
     * Saves a manaweaving recipe as {@code dmnr:manaweaving/<name>}.
     */
    private void save(Consumer<FinishedRecipe> writer, String name, ManaweavingRecipeBuilder recipe) {
        recipe.save(writer, RLoc.create("manaweaving/" + name), existingFileHelper);
    }

    /**
     * Saves a recipe of an integration as {@code dmnr:<mod_id>/<name>}. It is only loaded if the mod is installed.
     */
    private void saveConditional(Consumer<FinishedRecipe> writer, String modId, String name, ManaweavingRecipeBuilder recipe) {
        ResourceLocation id = RLoc.create(modId + "/" + name);
        ConditionalRecipe.builder()
                .addCondition(new ModLoadedCondition(modId))
                .addRecipe(recipe.build(id, existingFileHelper))
                .build(writer, id);
    }
}
