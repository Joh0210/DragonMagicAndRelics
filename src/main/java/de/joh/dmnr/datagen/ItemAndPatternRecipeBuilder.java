package de.joh.dmnr.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.joh.dmnr.DragonMagicAndRelics;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Base of the builders for the recipes of Mana and Artifice that consist of items and manaweaving patterns
 * ({@code com.mna.recipes.ItemAndPatternRecipe}): manaweaving recipes, and later spell components and shapes. They
 * share {@code tier}, {@code requiredFaction}, {@code items} and {@code patterns} and only differ in their output.
 * <p>
 * The format follows M&A's parser:
 * <ul>
 *     <li>{@code items} is a list of IDs. M&A resolves an ID as an item if it is registered as one and as an item tag
 *     otherwise, so tags are written without {@code #}. The order is kept, although the items are matched shapeless.</li>
 *     <li>{@code patterns} is a list of manaweave pattern IDs. They are matched in order.</li>
 * </ul>
 * {@link #build} fails the data run with a list of all problems (see {@link #validate}). No recipe advancements are
 * generated.
 * @param <T> type of the concrete builder, returned by the fluent methods
 * @author Joh0210
 */
public abstract class ItemAndPatternRecipeBuilder<T extends ItemAndPatternRecipeBuilder<T>> {
    public static final int MIN_TIER = 1;
    public static final int MAX_TIER = 5;

    private final ResourceLocation serializerId;
    private final int tier;
    @Nullable
    private ResourceLocation requiredFaction = null;
    private final List<Input> inputs = new ArrayList<>();
    private final List<ResourceLocation> patterns = new ArrayList<>();

    /**
     * @param serializerId ID of the M&A recipe serializer, which is also the {@code type} of the recipe
     * @param tier magic tier the player needs to craft the recipe
     */
    protected ItemAndPatternRecipeBuilder(ResourceLocation serializerId, int tier) {
        this.serializerId = serializerId;
        this.tier = tier;
    }

    /**
     * One entry of {@code items}.
     * @param id ID of the item or the item tag
     * @param isTag whether {@link #id} is an item tag
     */
    public record Input(ResourceLocation id, boolean isTag) {
        @Override
        public String toString() {
            return (isTag ? "#" : "") + id;
        }
    }

    @SuppressWarnings("unchecked")
    private T self() {
        return (T) this;
    }

    /**
     * Only players of this faction can craft the recipe.
     * @param factionId ID of the faction, see {@code com.mna.api.faction.FactionIDs}
     */
    public T requiredFaction(ResourceLocation factionId) {
        this.requiredFaction = factionId;
        return self();
    }

    /**
     * Adds the items to {@code items}, in the given order.
     */
    public T items(ItemLike... items) {
        for (ItemLike item : items) {
            // An unregistered item has the default key minecraft:air, which validate() reports
            inputs.add(new Input(ForgeRegistries.ITEMS.getKey(item.asItem()), false));
        }
        return self();
    }

    /**
     * Adds an item by its ID, for items of other mods without a usable constant.
     */
    public T item(ResourceLocation itemId) {
        inputs.add(new Input(itemId, false));
        return self();
    }

    /**
     * Adds an item tag: any item in the tag matches this entry.
     */
    public T tag(TagKey<Item> tag) {
        inputs.add(new Input(tag.location(), true));
        return self();
    }

    /**
     * Adds the patterns to {@code patterns}, in the given order. The order is relevant: the patterns have to be
     * drawn in this order.
     * @param patterns IDs of the manaweave patterns ({@code mna:manaweave_patterns/<name>})
     */
    public T patterns(ResourceLocation... patterns) {
        this.patterns.addAll(List.of(patterns));
        return self();
    }

    /**
     * @return the maximum number of items the crafting station accepts; M&A ignores the remaining ones
     */
    protected abstract int maxItems();

    /**
     * @return the maximum number of patterns the crafting station accepts; M&A ignores the remaining ones
     */
    protected abstract int maxPatterns();

    /**
     * Writes the output of the recipe ({@code output} and further fields) into the JSON.
     */
    protected abstract void serializeOutput(JsonObject json);

    /**
     * Adds the problems of the output to {@code problems}.
     */
    protected abstract void validateOutput(List<String> problems);

    /**
     * Validates the recipe and creates it.
     * @param id ID of the recipe, it decides the path of the file ({@code data/<namespace>/recipes/<path>.json})
     * @param existingFileHelper used to check that the tags and patterns exist. It knows vanilla, the files of this
     *                           mod and of the mods passed with {@code --existing-mod} in {@code build.gradle}.
     * @throws IllegalStateException if the recipe is invalid, see {@link #validate}
     */
    public Result build(ResourceLocation id, ExistingFileHelper existingFileHelper) {
        RecipeSerializer<?> serializer = ForgeRegistries.RECIPE_SERIALIZERS.getValue(serializerId);
        if (serializer == null) {
            throw new IllegalStateException(DragonMagicAndRelics.MOD_ID + ": recipe serializer " + serializerId + " is not registered");
        }

        List<String> problems = validate(existingFileHelper);
        if (!problems.isEmpty()) {
            throw new IllegalStateException(DragonMagicAndRelics.MOD_ID + ": invalid recipe " + id + ":\n" + String.join("\n", problems));
        }

        return new Result(id, serializer);
    }

    /**
     * Shortcut for {@link #build} that passes the recipe to {@code writer}.
     */
    public void save(Consumer<FinishedRecipe> writer, ResourceLocation id, ExistingFileHelper existingFileHelper) {
        writer.accept(build(id, existingFileHelper));
    }

    /**
     * Checks the recipe. Not checked: whether the required faction exists, and whether the tags contain items.
     * @return the problems, empty if the recipe is valid:
     * <ul>
     *     <li>the tier is outside of {@value MIN_TIER}–{@value MAX_TIER}</li>
     *     <li>{@code items} or {@code patterns} are empty or longer than the crafting station allows</li>
     *     <li>an item is not registered, or a tag has the ID of an item (M&A would resolve it as that item)</li>
     *     <li>a tag or pattern does not exist according to the {@link ExistingFileHelper}</li>
     *     <li>the output is invalid, see {@link #validateOutput}</li>
     * </ul>
     */
    protected List<String> validate(ExistingFileHelper existingFileHelper) {
        List<String> problems = new ArrayList<>();

        if (tier < MIN_TIER || tier > MAX_TIER) {
            problems.add("tier " + tier + " is outside of " + MIN_TIER + "-" + MAX_TIER);
        }

        validateOutput(problems);

        if (inputs.isEmpty()) {
            problems.add("items is empty");
        } else if (inputs.size() > maxItems()) {
            problems.add("items has " + inputs.size() + " entries, the maximum is " + maxItems());
        }

        for (Input input : inputs) {
            if (input.isTag()) {
                if (ForgeRegistries.ITEMS.containsKey(input.id())) {
                    problems.add("tag " + input + " has the ID of an item, M&A would only accept that item");
                } else if (!existingFileHelper.exists(input.id(), PackType.SERVER_DATA, ".json", "tags/items")) {
                    // Tags of this mod are only known if ModItemTagsProvider has generated them before
                    problems.add("tag " + input + " does not exist");
                }
            } else if (!isRegisteredItem(input.id())) {
                problems.add("item " + input + " is not registered");
            }
        }

        if (patterns.isEmpty()) {
            problems.add("patterns is empty");
        } else if (patterns.size() > maxPatterns()) {
            problems.add("patterns has " + patterns.size() + " entries, the maximum is " + maxPatterns());
        }

        for (ResourceLocation pattern : patterns) {
            // Manaweave patterns are recipes of M&A (data/<namespace>/recipes/manaweave_patterns/<name>.json)
            if (!existingFileHelper.exists(pattern, PackType.SERVER_DATA, ".json", "recipes")) {
                problems.add("pattern " + pattern + " does not exist");
            }
        }

        return problems;
    }

    /**
     * @return whether {@code id} is a registered item other than air
     */
    protected static boolean isRegisteredItem(@Nullable ResourceLocation id) {
        return id != null && ForgeRegistries.ITEMS.containsKey(id) && ForgeRegistries.ITEMS.getValue(id) != Items.AIR;
    }

    /**
     * The generated recipe. Gives access to its content, e.g. for checks across recipes.
     */
    public class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final RecipeSerializer<?> serializer;

        private Result(ResourceLocation id, RecipeSerializer<?> serializer) {
            this.id = id;
            this.serializer = serializer;
        }

        public int getTier() {
            return tier;
        }

        public @Nullable ResourceLocation getRequiredFaction() {
            return requiredFaction;
        }

        public List<Input> getInputs() {
            return List.copyOf(inputs);
        }

        public List<ResourceLocation> getPatterns() {
            return List.copyOf(patterns);
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject json) {
            json.addProperty("tier", tier);
            serializeOutput(json);
            if (requiredFaction != null) {
                json.addProperty("requiredFaction", requiredFaction.toString());
            }

            JsonArray items = new JsonArray();
            inputs.forEach(input -> items.add(input.id().toString()));
            json.add("items", items);

            JsonArray patternArray = new JsonArray();
            patterns.forEach(pattern -> patternArray.add(pattern.toString()));
            json.add("patterns", patternArray);
        }

        @Override
        public @NotNull ResourceLocation getId() {
            return id;
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return serializer;
        }

        @Override
        public @Nullable JsonObject serializeAdvancement() {
            return null;
        }

        @Override
        public @Nullable ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
