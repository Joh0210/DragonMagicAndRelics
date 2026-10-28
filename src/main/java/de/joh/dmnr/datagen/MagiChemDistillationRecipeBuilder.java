package de.joh.dmnr.datagen;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Builder for the distillation recipes of MagiChem ({@code magichem:distillation_fabrication}): the Distillery breaks the
 * {@code object} down into the materia of {@code components}. The same recipe also allows fabricating the object from
 * them, unless the wisdom is {@value #DISTILL_ONLY}, the default here.
 * @author Joh0210
 */
public class MagiChemDistillationRecipeBuilder extends MagiChemRecipeBuilder<MagiChemDistillationRecipeBuilder> {
    private static final ResourceLocation TYPE = new ResourceLocation(MAGICHEM, "distillation_fabrication");
    private static final int DISTILL_ONLY = 6;
    // No recipe of MagiChem itself has more
    private static final int MAX_COMPONENTS = 5;

    private int wisdom = DISTILL_ONLY;
    private Set<Category> categories = EnumSet.of(Category.CRAFTABLE);
    private int batchSize = 1;
    private float outputRate = 1.0f;
    private final List<Materia> components = new ArrayList<>();

    private MagiChemDistillationRecipeBuilder(ItemLike object) {
        super(TYPE, object);
    }

    /**
     * Where the object comes from, written as the bitmask {@code categories} ({@code DistillationFabricationRecipe.FLAG_*}).
     */
    public enum Category {
        CRAFTABLE(1),
        GATHERABLE(2),
        FARMABLE(4),
        RENEWABLE(8),
        TROPHY(16),
        RARE(32);

        private final int flag;

        Category(int flag) {
            this.flag = flag;
        }
    }

    public static MagiChemDistillationRecipeBuilder distillation(ItemLike object) {
        return new MagiChemDistillationRecipeBuilder(object);
    }

    /**
     * @param wisdom 0–5 allows fabrication, {@value #DISTILL_ONLY} only distillation
     */
    public MagiChemDistillationRecipeBuilder wisdom(int wisdom) {
        this.wisdom = wisdom;
        return this;
    }

    public MagiChemDistillationRecipeBuilder categories(Category... categories) {
        this.categories = categories.length == 0 ? EnumSet.noneOf(Category.class) : EnumSet.copyOf(List.of(categories));
        return this;
    }

    public MagiChemDistillationRecipeBuilder batchSize(int batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    /**
     * @param outputRate share of the materia that is produced
     */
    public MagiChemDistillationRecipeBuilder outputRate(float outputRate) {
        this.outputRate = outputRate;
        return this;
    }

    @Override
    protected void addMateria(Materia materia) {
        components.add(materia);
    }

    @Override
    protected void serialize(JsonObject json) {
        json.addProperty("wisdom", wisdom);
        json.addProperty("categories", categories.stream().mapToInt(category -> category.flag).sum());
        json.addProperty("batch_size", batchSize);
        json.addProperty("output_rate", outputRate);
        json.add("object", serializeObject(null));
        json.add("components", serializeMateria(components));
    }

    @Override
    protected void validate(List<String> problems, ExistingFileHelper existingFileHelper) {
        if (!isRegisteredItem(ForgeRegistries.ITEMS.getKey(getObject()))) {
            problems.add("object " + ForgeRegistries.ITEMS.getKey(getObject()) + " is not registered");
        }
        checkRange(problems, "wisdom", wisdom, 0, DISTILL_ONLY);
        if (categories.isEmpty()) {
            problems.add("categories is empty");
        }
        // MagiChem reads a byte
        checkRange(problems, "batch_size", batchSize, 1, Byte.MAX_VALUE);
        if (!(outputRate > 0 && outputRate <= 1)) {
            problems.add("output_rate " + outputRate + " is outside of 0 (exclusive) and 1");
        }
        validateMateria(problems, "components", components, MAX_COMPONENTS);
    }
}
