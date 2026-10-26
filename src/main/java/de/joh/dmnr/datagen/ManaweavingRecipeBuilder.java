package de.joh.dmnr.datagen;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

/**
 * Builder for the manaweaving recipes of Mana and Artifice ({@code mna:manaweaving-recipe}), see
 * {@link ItemAndPatternRecipeBuilder} for the shared fields.
 * <p>
 * {@code quantity} is always written, although M&A uses 1 if it is missing.
 * @author Joh0210
 */
public class ManaweavingRecipeBuilder extends ItemAndPatternRecipeBuilder<ManaweavingRecipeBuilder> {
    public static final ResourceLocation SERIALIZER = new ResourceLocation("mna", "manaweaving-recipe");

    private final Item output;
    private int quantity = 1;

    private ManaweavingRecipeBuilder(ItemLike output, int tier) {
        super(SERIALIZER, tier);
        this.output = output.asItem();
    }

    /**
     * @param output the crafted item
     * @param tier magic tier the player needs to craft the recipe
     */
    public static ManaweavingRecipeBuilder manaweaving(ItemLike output, int tier) {
        return new ManaweavingRecipeBuilder(output, tier);
    }

    /**
     * @param quantity number of crafted items, 1 by default
     */
    public ManaweavingRecipeBuilder quantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Item getOutput() {
        return output;
    }

    @Override
    protected int maxItems() {
        return 9;
    }

    @Override
    protected int maxPatterns() {
        return 6;
    }

    @Override
    protected void serializeOutput(JsonObject json) {
        json.addProperty("output", ForgeRegistries.ITEMS.getKey(output).toString());
        json.addProperty("quantity", quantity);
    }

    @Override
    protected void validateOutput(List<String> problems) {
        ResourceLocation outputId = ForgeRegistries.ITEMS.getKey(output);
        if (!isRegisteredItem(outputId)) {
            problems.add("output " + outputId + " is not registered");
        }

        int maxStackSize = output.getDefaultInstance().getMaxStackSize();
        if (quantity < 1 || quantity > maxStackSize) {
            problems.add("quantity " + quantity + " is outside of 1-" + maxStackSize);
        }
    }
}
