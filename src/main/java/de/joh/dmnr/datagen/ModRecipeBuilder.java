package de.joh.dmnr.datagen;

import com.google.gson.JsonObject;
import de.joh.dmnr.DragonMagicAndRelics;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Base of the recipe builders of this mod for recipe types of other mods. {@link #save} collects all problems of a
 * recipe before it fails the data run, and wraps the recipe in {@code forge:conditional} if it has conditions.
 * <p>
 * The {@code type} is written directly instead of being taken from the serializer, because the serializers of mods that
 * are not loaded during the data run (MagiChem) are not registered. No recipe advancements are generated.
 * @param <T> type of the concrete builder, returned by the fluent methods
 * @author Joh0210
 */
public abstract class ModRecipeBuilder<T extends ModRecipeBuilder<T>> {
    public static final int MIN_TIER = 1;
    public static final int MAX_TIER = 5;

    private final ResourceLocation type;
    private final List<ICondition> conditions = new ArrayList<>();

    protected ModRecipeBuilder(ResourceLocation type) {
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }

    protected ResourceLocation type() {
        return type;
    }

    /**
     * The recipe is only loaded if the mod is installed.
     */
    public T whenModLoaded(String modId) {
        conditions.add(new ModLoadedCondition(modId));
        return self();
    }

    protected abstract void validate(List<String> problems, ExistingFileHelper existingFileHelper);

    /**
     * Writes every field except {@code type}.
     */
    protected abstract void serialize(JsonObject json);

    /**
     * @throws IllegalStateException with all problems if the recipe is invalid
     */
    public void save(Consumer<FinishedRecipe> writer, ResourceLocation id, ExistingFileHelper existingFileHelper) {
        List<String> problems = new ArrayList<>();
        validate(problems, existingFileHelper);
        if (!problems.isEmpty()) {
            throw new IllegalStateException(DragonMagicAndRelics.MOD_ID + ": invalid recipe " + id + ":\n" + String.join("\n", problems));
        }

        Result recipe = new Result(id);
        if (conditions.isEmpty()) {
            writer.accept(recipe);
            return;
        }

        ConditionalRecipe.Builder conditional = ConditionalRecipe.builder();
        conditions.forEach(conditional::addCondition);
        conditional.addRecipe(recipe).build(writer, id);
    }

    protected static void checkRange(List<String> problems, String field, int value, int min, int max) {
        if (value < min || value > max) {
            problems.add(field + " " + value + " is outside of " + min + "-" + max);
        }
    }

    protected static void checkSize(List<String> problems, String field, List<?> list, int max) {
        if (list.isEmpty()) {
            problems.add(field + " is empty");
        } else if (list.size() > max) {
            problems.add(field + " has " + list.size() + " entries, the maximum is " + max);
        }
    }

    protected static void checkItemOutput(List<String> problems, String field, Item output, int quantity) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(output);
        if (!isRegisteredItem(id)) {
            problems.add(field + " " + id + " is not registered");
        }
        checkRange(problems, "quantity", quantity, 1, output.getDefaultInstance().getMaxStackSize());
    }

    protected static boolean isRegisteredItem(@Nullable ResourceLocation id) {
        // Unregistered items have the default key minecraft:air
        return id != null && ForgeRegistries.ITEMS.containsKey(id) && ForgeRegistries.ITEMS.getValue(id) != Items.AIR;
    }

    private class Result implements FinishedRecipe {
        private final ResourceLocation id;

        private Result(ResourceLocation id) {
            this.id = id;
        }

        @Override
        public @NotNull JsonObject serializeRecipe() {
            JsonObject json = new JsonObject();
            json.addProperty("type", type.toString());
            serializeRecipeData(json);
            return json;
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject json) {
            serialize(json);
        }

        @Override
        public @NotNull ResourceLocation getId() {
            return id;
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            RecipeSerializer<?> serializer = ForgeRegistries.RECIPE_SERIALIZERS.getValue(type);
            if (serializer == null) {
                throw new IllegalStateException(DragonMagicAndRelics.MOD_ID + ": recipe serializer " + type + " is not registered during the data run");
            }
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
