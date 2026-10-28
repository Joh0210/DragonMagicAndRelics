package de.joh.dmnr.datagen;

import com.google.gson.JsonObject;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.parts.SpellEffect;
import de.joh.dmnr.DragonMagicAndRelics;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.function.Consumer;

/**
 * Builder for the recipes of the spell components ({@code mna:component}) and shapes ({@code mna:shape}) of Mana and
 * Artifice. A spell part is not an item, so {@code quantity} is not written.
 * <p>
 * The recipe ID is always the registry ID of the spell part: M&A takes the tier of a spell part from the recipe with
 * that ID, and the Codex finds the recipe by it.
 * @author Joh0210
 */
public class SpellPartRecipeBuilder extends ItemAndPatternRecipeBuilder<SpellPartRecipeBuilder> {
    private static final ResourceLocation COMPONENT_TYPE = new ResourceLocation("mna", "component");
    private static final ResourceLocation SHAPE_TYPE = new ResourceLocation("mna", "shape");

    private final ISpellComponent output;

    private SpellPartRecipeBuilder(ResourceLocation type, ISpellComponent output, int tier) {
        super(type, tier, 9, 9);
        this.output = output;
    }

    public static SpellPartRecipeBuilder component(SpellEffect output, int tier) {
        return new SpellPartRecipeBuilder(COMPONENT_TYPE, output, tier);
    }

    public static SpellPartRecipeBuilder shape(Shape output, int tier) {
        return new SpellPartRecipeBuilder(SHAPE_TYPE, output, tier);
    }

    private ResourceLocation outputId() {
        ResourceLocation id = output.getRegistryName();
        if (id == null) {
            throw new IllegalStateException(DragonMagicAndRelics.MOD_ID + ": spell part " + output.getClass().getName() + " is not registered");
        }
        return id;
    }

    public void save(Consumer<FinishedRecipe> writer, ExistingFileHelper existingFileHelper) {
        super.save(writer, outputId(), existingFileHelper);
    }

    /**
     * @throws IllegalStateException if {@code id} is not the registry ID of the spell part
     */
    @Override
    public void save(Consumer<FinishedRecipe> writer, ResourceLocation id, ExistingFileHelper existingFileHelper) {
        if (!id.equals(outputId())) {
            throw new IllegalStateException(DragonMagicAndRelics.MOD_ID + ": recipe " + id + " must have the ID of its spell part " + outputId());
        }
        super.save(writer, id, existingFileHelper);
    }

    @Override
    protected void serializeOutput(JsonObject json) {
        json.addProperty("output", outputId().toString());
    }

    @Override
    protected void validateOutput(List<String> problems) {
    }
}
