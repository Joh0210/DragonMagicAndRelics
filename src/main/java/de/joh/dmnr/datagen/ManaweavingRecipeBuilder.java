package de.joh.dmnr.datagen;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

/**
 * Builder for the manaweaving recipes of Mana and Artifice ({@code mna:manaweaving-recipe}).
 * @author Joh0210
 */
public class ManaweavingRecipeBuilder extends ItemAndPatternRecipeBuilder<ManaweavingRecipeBuilder> {
    private static final ResourceLocation TYPE = new ResourceLocation("mna", "manaweaving-recipe");

    private final Item output;
    private int quantity = 1;

    private ManaweavingRecipeBuilder(ItemLike output, int tier) {
        super(TYPE, tier, 9, 6);
        this.output = output.asItem();
    }

    public static ManaweavingRecipeBuilder manaweaving(ItemLike output, int tier) {
        return new ManaweavingRecipeBuilder(output, tier);
    }

    public ManaweavingRecipeBuilder quantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    @Override
    protected void serializeOutput(JsonObject json) {
        json.addProperty("output", ForgeRegistries.ITEMS.getKey(output).toString());
        json.addProperty("quantity", quantity);
    }

    @Override
    protected void validateOutput(List<String> problems) {
        checkItemOutput(problems, "output", output, quantity);
    }
}
