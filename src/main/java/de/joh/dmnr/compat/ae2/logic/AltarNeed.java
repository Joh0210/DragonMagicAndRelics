package de.joh.dmnr.compat.ae2.logic;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import com.mna.api.recipes.IManaweavePattern;
import com.mna.api.tools.MATags;
import com.mna.blocks.tileentities.wizard_lab.ArcanaAltarTile;
import com.mna.items.ItemInit;
import com.mna.items.manaweaving.ItemManaweaveBottle;
import com.mna.recipes.manaweaving.ManaweavingPatternHelper;
import de.joh.dmnr.DragonMagicAndRelics;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * One thing the Altar of Arcana is currently waiting for.
 * @param options items that all satisfy the requirement (a reagent can be an item tag), in the order of preference
 * @param count how many items in total are required
 * @param ignoreNbt whether items match regardless of their durability and NBT. The altar does that for reagents, so a
 *                  damaged Flint and Steel or an enchanted Crossbow satisfy it, but not for manaweave bottles.
 * @author Joh0210
 */
public record AltarNeed(@NotNull List<AEItemKey> options, int count, boolean ignoreNbt) {
    /**
     * The manaweave bottles still required are only exposed by the altar as a list that is not updated while the
     * bottles are used up, so the map behind it is read directly.
     */
    private static final @Nullable Field REMAINING_MANAWEAVES = findRemainingManaweavesField();

    /**
     * Whether the item is one the altar accepts for this requirement.
     */
    public boolean accepts(@NotNull AEItemKey key) {
        return ignoreNbt ? indexOfItem(key) >= 0 : options.contains(key);
    }

    /**
     * The items of the network that satisfy this requirement, the best choice first: the order of the options, and
     * of those the ones without damage and NBT.
     */
    public @NotNull List<AEItemKey> candidates(@NotNull KeyCounter network) {
        if (!ignoreNbt) {
            return options;
        }

        List<AEItemKey> candidates = new ArrayList<>();
        for (Object2LongMap.Entry<AEKey> entry : network) {
            if (entry.getLongValue() > 0 && entry.getKey() instanceof AEItemKey key && accepts(key)) {
                candidates.add(key);
            }
        }
        candidates.sort(Comparator.comparingInt(this::preference));
        return candidates;
    }

    /**
     * How well the item suits the need, lower is better: the order of the options, and for the same option the plain
     * item before one with damage or NBT.
     */
    public int preference(@NotNull AEItemKey key) {
        int index = indexOfItem(key);
        if (index < 0) {
            index = options.size();
        }
        return index * 2 + (key.hasTag() || key.isDamaged() ? 1 : 0);
    }

    /**
     * @return the index of the first option with the same item, regardless of damage and NBT, or -1
     */
    private int indexOfItem(@NotNull AEItemKey key) {
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).getItem() == key.getItem()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Reads what the altar requires right now. This mirrors the altar itself: it works through its reagents one after
     * the other in the iteration order of its map, so the first entry is the one it is waiting for. The remaining
     * ones are included, so they are on hand as soon as the altar gets to them.
     * <p>
     * Outside of the gathering phases the requirement maps of the altar are not meaningful (they are only cleaned up
     * when the next recipe is cached), so nothing is requested.
     */
    public static @NotNull List<AltarNeed> collect(@NotNull ArcanaAltarTile altar) {
        List<AltarNeed> needs = new ArrayList<>();
        ArcanaAltarTile.States state = altar.getState();

        if (state == ArcanaAltarTile.States.GATHERING_REAGENTS) {
            for (Map.Entry<ResourceLocation, Integer> reagent : altar.getRemainingReagents().entrySet()) {
                if (reagent.getValue() <= 0) {
                    continue;
                }
                List<Item> items = MATags.smartLookupItem(reagent.getKey());
                if (items == null || items.isEmpty()) {
                    continue;
                }
                needs.add(new AltarNeed(items.stream().map(AEItemKey::of).distinct().toList(), reagent.getValue(), true));
            }
        } else if (state == ArcanaAltarTile.States.GATHERING_MANAWEAVES) {
            collectManaweaves(altar, needs);
        }

        return needs;
    }

    @SuppressWarnings("unchecked")
    private static void collectManaweaves(@NotNull ArcanaAltarTile altar, @NotNull List<AltarNeed> needs) {
        Level level = altar.getLevel();
        if (REMAINING_MANAWEAVES == null || level == null) {
            return;
        }

        try {
            Map<ResourceLocation, Integer> remaining = (Map<ResourceLocation, Integer>) REMAINING_MANAWEAVES.get(altar);
            for (Map.Entry<ResourceLocation, Integer> weave : remaining.entrySet()) {
                if (weave.getValue() <= 0) {
                    continue;
                }
                IManaweavePattern pattern = ManaweavingPatternHelper.GetManaweavingRecipe(level, weave.getKey());
                if (pattern == null) {
                    continue;
                }

                ItemStack bottle = new ItemStack(ItemInit.MANAWEAVE_BOTTLE.get());
                ItemManaweaveBottle.setPattern(pattern, bottle);
                needs.add(new AltarNeed(List.of(AEItemKey.of(bottle)), weave.getValue(), false));
            }
        } catch (ReflectiveOperationException | RuntimeException e) {
            DragonMagicAndRelics.LOGGER.warn("{}: could not read the manaweaves required by the Altar of Arcana", DragonMagicAndRelics.MOD_ID, e);
        }
    }

    private static @Nullable Field findRemainingManaweavesField() {
        try {
            Field field = ArcanaAltarTile.class.getDeclaredField("remainingManaweaves");
            field.setAccessible(true);
            return field;
        } catch (ReflectiveOperationException | RuntimeException e) {
            DragonMagicAndRelics.LOGGER.warn("{}: manaweave bottles are not supplied to the Altar of Arcana, its internals changed", DragonMagicAndRelics.MOD_ID, e);
            return null;
        }
    }
}
