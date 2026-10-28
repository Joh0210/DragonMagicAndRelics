package de.joh.dmnr.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mna.api.affinity.Affinity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Builder for the Eldrin Altar recipes of Mana and Artifice ({@code mna:eldrin-altar}). The altar uses no patterns, but
 * draws Eldrin power per affinity ({@code power_requirements}). M&A also reads {@code colors}, but never uses them.
 * @author Joh0210
 */
public class EldrinAltarRecipeBuilder extends ItemAndPatternRecipeBuilder<EldrinAltarRecipeBuilder> {
    private static final ResourceLocation TYPE = new ResourceLocation("mna", "eldrin-altar");
    // There are no Eldrin conduits for the other affinities
    private static final Set<Affinity> CORE_AFFINITIES = EnumSet.copyOf(List.of(Affinity.CoreSix()));

    private final Item output;
    private int quantity = 1;
    private final List<PowerRequirement> powerRequirements = new ArrayList<>();

    private EldrinAltarRecipeBuilder(ItemLike output, int tier) {
        super(TYPE, tier, 9, 0);
        this.output = output.asItem();
    }

    private record PowerRequirement(Affinity affinity, int amount) {
    }

    public static EldrinAltarRecipeBuilder eldrinAltar(ItemLike output, int tier) {
        return new EldrinAltarRecipeBuilder(output, tier);
    }

    public EldrinAltarRecipeBuilder quantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    /**
     * The altar consumes {@code amount} Eldrin power of each of the affinities.
     */
    public EldrinAltarRecipeBuilder power(int amount, Affinity... affinities) {
        for (Affinity affinity : affinities) {
            powerRequirements.add(new PowerRequirement(affinity, amount));
        }
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

    @Override
    protected void validate(List<String> problems, ExistingFileHelper existingFileHelper) {
        super.validate(problems, existingFileHelper);

        if (powerRequirements.isEmpty()) {
            problems.add("power_requirements is empty");
        }
        Set<Affinity> listed = EnumSet.noneOf(Affinity.class);
        for (PowerRequirement requirement : powerRequirements) {
            Affinity affinity = requirement.affinity();
            if (!CORE_AFFINITIES.contains(affinity)) {
                problems.add("affinity " + affinity + " is not one of the core affinities " + CORE_AFFINITIES);
            }
            // M&A would only keep the last amount
            if (!listed.add(affinity)) {
                problems.add("affinity " + affinity + " is listed more than once");
            }
            if (requirement.amount() < 1) {
                problems.add("amount " + requirement.amount() + " of affinity " + affinity + " is not positive");
            }
        }
    }
}
