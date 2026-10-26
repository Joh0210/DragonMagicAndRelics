package de.joh.dmnr.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mna.api.affinity.Affinity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Builder for the Eldrin Altar recipes of Mana and Artifice ({@code mna:eldrin-altar}), see
 * {@link ItemAndPatternRecipeBuilder} for the shared fields. The Eldrin Altar does not use patterns.
 * <p>
 * Additional field {@code power_requirements}: the Eldrin power per affinity that the altar draws from the Eldrin
 * conduits and capacitors around it. M&A reads the amount as a float, only whole numbers are written.
 * <p>
 * {@code quantity} is always written, although M&A uses 1 if it is missing. M&A also reads {@code colors}, but does
 * not use them anywhere, so they are not supported.
 * @author Joh0210
 */
public class EldrinAltarRecipeBuilder extends ItemAndPatternRecipeBuilder<EldrinAltarRecipeBuilder> {
    public static final ResourceLocation SERIALIZER = new ResourceLocation("mna", "eldrin-altar");

    private final Item output;
    private int quantity = 1;
    private final List<PowerRequirement> powerRequirements = new ArrayList<>();

    private EldrinAltarRecipeBuilder(ItemLike output, int tier) {
        super(SERIALIZER, tier);
        this.output = output.asItem();
    }

    /**
     * One entry of {@code power_requirements}.
     * @param affinity affinity of the Eldrin power
     * @param amount amount of Eldrin power the altar consumes
     */
    public record PowerRequirement(Affinity affinity, int amount) {
    }

    /**
     * @param output the crafted item
     * @param tier magic tier the player needs to craft the recipe
     */
    public static EldrinAltarRecipeBuilder eldrinAltar(ItemLike output, int tier) {
        return new EldrinAltarRecipeBuilder(output, tier);
    }

    /**
     * @param quantity number of crafted items, 1 by default
     */
    public EldrinAltarRecipeBuilder quantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    /**
     * The altar consumes {@code amount} Eldrin power of each of the affinities, in the given order.
     * @param affinities affinities of the Eldrin power, only the six core affinities ({@link Affinity#CoreSix()})
     */
    public EldrinAltarRecipeBuilder power(int amount, Affinity... affinities) {
        for (Affinity affinity : affinities) {
            powerRequirements.add(new PowerRequirement(affinity, amount));
        }
        return this;
    }

    public Item getOutput() {
        return output;
    }

    public List<PowerRequirement> getPowerRequirements() {
        return List.copyOf(powerRequirements);
    }

    @Override
    protected int maxItems() {
        return 9;
    }

    @Override
    protected int maxPatterns() {
        return 0;
    }

    @Override
    protected void serializeOutput(JsonObject json) {
        serializeItemOutput(json, output, quantity);
    }

    @Override
    protected void validateOutput(List<String> problems) {
        validateItemOutput(problems, output, quantity);
    }

    @Override
    protected void serializeAdditionalData(JsonObject json) {
        JsonArray requirements = new JsonArray();
        for (PowerRequirement requirement : powerRequirements) {
            JsonObject entry = new JsonObject();
            entry.addProperty("affinity", requirement.affinity().name());
            entry.addProperty("amount", requirement.amount());
            requirements.add(entry);
        }
        json.add("power_requirements", requirements);
    }

    /**
     * Additionally to {@link ItemAndPatternRecipeBuilder#validate}:
     * <ul>
     *     <li>{@code power_requirements} is empty: the altar would craft the item without consuming power</li>
     *     <li>an affinity is not one of the six core affinities: there are no Eldrin conduits for the others, so the
     *     recipe could never be crafted</li>
     *     <li>an affinity is listed more than once: M&A would only keep the last amount</li>
     *     <li>an amount is not positive</li>
     * </ul>
     */
    @Override
    protected List<String> validate(ExistingFileHelper existingFileHelper) {
        List<String> problems = super.validate(existingFileHelper);

        if (powerRequirements.isEmpty()) {
            problems.add("power_requirements is empty");
        }

        Set<Affinity> coreAffinities = EnumSet.copyOf(List.of(Affinity.CoreSix()));
        Set<Affinity> listed = EnumSet.noneOf(Affinity.class);
        for (PowerRequirement requirement : powerRequirements) {
            Affinity affinity = requirement.affinity();
            if (!coreAffinities.contains(affinity)) {
                problems.add("affinity " + affinity + " is not one of the core affinities " + coreAffinities);
            }
            if (!listed.add(affinity)) {
                problems.add("affinity " + affinity + " is listed more than once");
            }
            if (requirement.amount() < 1) {
                problems.add("amount " + requirement.amount() + " of affinity " + affinity + " is not positive");
            }
        }

        return problems;
    }
}
