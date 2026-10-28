package de.joh.dmnr.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base of the builders for the Mana and Artifice recipes that consist of items and manaweaving patterns
 * ({@code com.mna.recipes.ItemAndPatternRecipe}).
 * <p>
 * M&A resolves an entry of {@code items} as an item if one with that ID exists and as an item tag otherwise, so tags are
 * written without {@code #}. A crafting station without patterns ({@code maxPatterns} 0) gets no {@code patterns}.
 * @param <T> type of the concrete builder, returned by the fluent methods
 * @author Joh0210
 */
public abstract class ItemAndPatternRecipeBuilder<T extends ItemAndPatternRecipeBuilder<T>> extends ModRecipeBuilder<T> {
    private final int tier;
    private final int maxItems;
    private final int maxPatterns;
    @Nullable
    private ResourceLocation requiredFaction = null;
    private final List<Input> inputs = new ArrayList<>();
    private final List<ResourceLocation> patterns = new ArrayList<>();

    /**
     * @param maxItems limit of the crafting station, M&A ignores further entries
     * @param maxPatterns limit of the crafting station, M&A ignores further entries
     */
    protected ItemAndPatternRecipeBuilder(ResourceLocation type, int tier, int maxItems, int maxPatterns) {
        super(type);
        this.tier = tier;
        this.maxItems = maxItems;
        this.maxPatterns = maxPatterns;
    }

    private record Input(ResourceLocation id, boolean isTag) {
        @Override
        public String toString() {
            return (isTag ? "#" : "") + id;
        }
    }

    /**
     * @param factionId see {@code com.mna.api.faction.FactionIDs}
     */
    public T requiredFaction(ResourceLocation factionId) {
        this.requiredFaction = factionId;
        return self();
    }

    public T items(ItemLike... items) {
        for (ItemLike item : items) {
            inputs.add(new Input(ForgeRegistries.ITEMS.getKey(item.asItem()), false));
        }
        return self();
    }

    public T item(ItemLike item, int count) {
        return items(Collections.nCopies(count, item).toArray(ItemLike[]::new));
    }

    /**
     * For items of mods whose registry constants are not on the compile classpath.
     */
    public T item(ResourceLocation itemId) {
        inputs.add(new Input(itemId, false));
        return self();
    }

    public T tag(TagKey<Item> tag) {
        inputs.add(new Input(tag.location(), true));
        return self();
    }

    /**
     * @param patterns {@code mna:manaweave_patterns/<name>}, drawn in this order
     */
    public T patterns(ResourceLocation... patterns) {
        this.patterns.addAll(List.of(patterns));
        return self();
    }

    /**
     * Writes {@code output} and the fields that belong to it.
     */
    protected abstract void serializeOutput(JsonObject json);

    protected abstract void validateOutput(List<String> problems);

    /**
     * Writes the fields that follow {@code items} and {@code patterns}.
     */
    protected void serializeAdditionalData(JsonObject json) {
    }

    @Override
    protected void serialize(JsonObject json) {
        json.addProperty("tier", tier);
        serializeOutput(json);
        if (requiredFaction != null) {
            json.addProperty("requiredFaction", requiredFaction.toString());
        }

        JsonArray items = new JsonArray();
        inputs.forEach(input -> items.add(input.id().toString()));
        json.add("items", items);

        if (maxPatterns > 0) {
            JsonArray patternArray = new JsonArray();
            patterns.forEach(pattern -> patternArray.add(pattern.toString()));
            json.add("patterns", patternArray);
        }

        serializeAdditionalData(json);
    }

    /**
     * Not checked: whether the faction exists and whether the tags contain items.
     */
    @Override
    protected void validate(List<String> problems, ExistingFileHelper existingFileHelper) {
        if (!ForgeRegistries.RECIPE_SERIALIZERS.containsKey(type())) {
            problems.add("recipe serializer " + type() + " is not registered");
        }
        checkRange(problems, "tier", tier, MIN_TIER, MAX_TIER);
        validateOutput(problems);

        checkSize(problems, "items", inputs, maxItems);
        for (Input input : inputs) {
            if (!input.isTag()) {
                if (!isRegisteredItem(input.id())) {
                    problems.add("item " + input + " is not registered");
                }
            } else if (ForgeRegistries.ITEMS.containsKey(input.id())) {
                problems.add("tag " + input + " has the ID of an item, M&A would only accept that item");
            } else if (!existingFileHelper.exists(input.id(), PackType.SERVER_DATA, ".json", "tags/items")) {
                problems.add("tag " + input + " does not exist");
            }
        }

        if (maxPatterns == 0) {
            if (!patterns.isEmpty()) {
                problems.add("patterns has " + patterns.size() + " entries, but the crafting station does not use patterns");
            }
        } else {
            checkSize(problems, "patterns", patterns, maxPatterns);
        }
        for (ResourceLocation pattern : patterns) {
            // Manaweave patterns are recipes of M&A
            if (!existingFileHelper.exists(pattern, PackType.SERVER_DATA, ".json", "recipes")) {
                problems.add("pattern " + pattern + " does not exist");
            }
        }
    }
}
