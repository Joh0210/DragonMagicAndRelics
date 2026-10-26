package de.joh.dmnr.datagen;

import com.mna.api.items.IRelic;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.common.item.CurioSlot;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Generates the item tags ({@code data/<namespace>/tags/items}).
 * <p>
 * The tags of the Curios slots and {@code mna:relics} are derived from the items: the slot of a curio is set at its
 * registration ({@link ItemInit#getCurioSlots()}), relics are the items that implement {@link IRelic}.
 * @author Joh0210
 */
public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                               CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, DragonMagicAndRelics.MOD_ID, existingFileHelper);
    }

    /**
     * The item tag through which the loot table of an integration block drops the block, see {@link ModBlockLootProvider}.
     * It has the same ID as the block and optionally contains the block item, which has that ID as well.
     */
    public static TagKey<Item> selfDropTag(RegistryObject<? extends Block> block) {
        return ItemTags.create(block.getId());
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        Map<RegistryObject<Item>, CurioSlot> curioSlots = ItemInit.getCurioSlots();
        validateCurioSlots(curioSlots);
        curioSlots.forEach((item, slot) -> tag(slot.getTag()).add(item.get()));

        tag(ItemTags.create(new ResourceLocation("mna", "relics")))
                .add(modItems().stream().filter(item -> item instanceof IRelic).toArray(Item[]::new));

        for (RegistryObject<? extends Block> block : DataGenerators.integrationBlocks()) {
            tag(selfDropTag(block)).addOptional(block.getId());
        }
    }

    /**
     * Curios only allows an item in a slot whose tag contains it, so a curio without a slot cannot be equipped.
     * <br>Only {@link ICurioItem}s can be recognized as curios. Curios whose effect is handled by events (e.g. the
     * Ring of Spell Storing) do not implement it and are not checked.
     * @throws IllegalStateException if an {@link ICurioItem} of this mod has no slot
     */
    private static void validateCurioSlots(Map<RegistryObject<Item>, CurioSlot> curioSlots) {
        List<Item> withSlot = curioSlots.keySet().stream().map(RegistryObject::get).toList();
        List<String> problems = new ArrayList<>();

        for (Item item : modItems()) {
            if (item instanceof ICurioItem && !withSlot.contains(item)) {
                problems.add(ForgeRegistries.ITEMS.getKey(item) + " is an ICurioItem without a Curios slot, register it with ItemInit.registerCurio");
            }
        }

        if (!problems.isEmpty()) {
            throw new IllegalStateException(DragonMagicAndRelics.MOD_ID + ": curios without a slot:\n" + String.join("\n", problems));
        }
    }

    /**
     * @return all registered items of this mod, including those of the integrations
     */
    private static List<Item> modItems() {
        return DataGenerators.itemEntries().map(RegistryObject::get).toList();
    }
}
