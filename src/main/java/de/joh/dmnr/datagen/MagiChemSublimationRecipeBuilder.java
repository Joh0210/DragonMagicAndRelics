package de.joh.dmnr.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for the sublimation recipes of MagiChem ({@code magichem:sublimation}): the Alchemical Nexus crafts the
 * {@code object} in stages, each consuming items, materia and experience.
 * <p>
 * MagiChem reads a {@code tag} in {@code components}, but ignores it, so only single items are supported.
 * @author Joh0210
 */
public class MagiChemSublimationRecipeBuilder extends MagiChemRecipeBuilder<MagiChemSublimationRecipeBuilder> {
    private static final ResourceLocation TYPE = new ResourceLocation(MAGICHEM, "sublimation");
    // Limits of MagiChem's own sublimation recipes
    private static final int MAX_WISDOM = 5;
    private static final int MAX_STAGES = 4;
    private static final int MAX_COMPONENTS = 5;
    private static final int MAX_MATERIA = 5;

    private final int tier;
    private int quantity = 1;
    private int wisdom = 0;
    private final List<Stage> stages = new ArrayList<>();

    private MagiChemSublimationRecipeBuilder(ItemLike object, int tier) {
        super(TYPE, object);
        this.tier = tier;
    }

    private record Stage(int experience, List<ResourceLocation> components, List<Materia> materia) {
    }

    public static MagiChemSublimationRecipeBuilder sublimation(ItemLike object, int tier) {
        return new MagiChemSublimationRecipeBuilder(object, tier);
    }

    public MagiChemSublimationRecipeBuilder quantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    /**
     * @param wisdom wisdom the Alchemical Nexus needs to offer the recipe
     */
    public MagiChemSublimationRecipeBuilder wisdom(int wisdom) {
        this.wisdom = wisdom;
        return this;
    }

    /**
     * Starts the next stage, the following items and materia belong to it.
     */
    public MagiChemSublimationRecipeBuilder stage(int experience) {
        stages.add(new Stage(experience, new ArrayList<>(), new ArrayList<>()));
        return this;
    }

    public MagiChemSublimationRecipeBuilder items(ItemLike... items) {
        for (ItemLike item : items) {
            currentStage().components().add(ForgeRegistries.ITEMS.getKey(item.asItem()));
        }
        return this;
    }

    /**
     * For the items of MagiChem, which are not registered during the data run.
     */
    public MagiChemSublimationRecipeBuilder item(ResourceLocation itemId) {
        currentStage().components().add(itemId);
        return this;
    }

    @Override
    protected void addMateria(Materia materia) {
        currentStage().materia().add(materia);
    }

    private Stage currentStage() {
        if (stages.isEmpty()) {
            throw new IllegalStateException("start a stage with stage(experience) before adding components or materia");
        }
        return stages.get(stages.size() - 1);
    }

    @Override
    protected void serialize(JsonObject json) {
        json.addProperty("tier", tier);
        json.addProperty("wisdom", wisdom);
        json.add("object", serializeObject(quantity));

        JsonArray stageArray = new JsonArray();
        for (Stage stage : stages) {
            JsonObject stageJson = new JsonObject();
            stageJson.addProperty("experience", stage.experience());

            JsonArray components = new JsonArray();
            for (ResourceLocation component : stage.components()) {
                JsonObject entry = new JsonObject();
                entry.addProperty("item", component.toString());
                components.add(entry);
            }
            stageJson.add("components", components);
            stageJson.add("materia", serializeMateria(stage.materia()));

            stageArray.add(stageJson);
        }
        json.add("stages", stageArray);
    }

    @Override
    protected void validate(List<String> problems, ExistingFileHelper existingFileHelper) {
        checkItemOutput(problems, "object", getObject(), quantity);
        checkRange(problems, "tier", tier, MIN_TIER, MAX_TIER);
        checkRange(problems, "wisdom", wisdom, 0, MAX_WISDOM);
        checkSize(problems, "stages", stages, MAX_STAGES);

        for (int i = 0; i < stages.size(); i++) {
            Stage stage = stages.get(i);
            String prefix = "stage " + (i + 1);

            if (stage.experience() < 0) {
                problems.add(prefix + ": experience " + stage.experience() + " is negative");
            }
            checkSize(problems, prefix + ": components", stage.components(), MAX_COMPONENTS);
            for (ResourceLocation component : stage.components()) {
                if (!isKnownItem(component)) {
                    problems.add(prefix + ": item " + component + " is not registered");
                }
            }
            validateMateria(problems, prefix + " materia", stage.materia(), MAX_MATERIA);
        }
    }
}
