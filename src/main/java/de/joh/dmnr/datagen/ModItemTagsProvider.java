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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Generates the item tags. The Curios slot tags come from {@link ItemInit#getCurioSlots()}, {@code mna:relics} contains
 * every item that implements {@link IRelic}.
 * @author Joh0210
 */
public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                               CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, DragonMagicAndRelics.MOD_ID, existingFileHelper);
    }

    /**
     * Optionally contains the block item of an integration block, see {@link ModBlockLootProvider}.
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
     * A curio without a slot tag cannot be equipped. Curios that do not implement {@link ICurioItem} (effects handled by
     * events) cannot be recognized.
     */
    private static void validateCurioSlots(Map<RegistryObject<Item>, CurioSlot> curioSlots) {
        Set<Item> withSlot = curioSlots.keySet().stream().map(RegistryObject::get).collect(Collectors.toSet());
        List<String> problems = modItems().stream()
                .filter(item -> item instanceof ICurioItem && !withSlot.contains(item))
                .map(item -> ForgeRegistries.ITEMS.getKey(item) + " is an ICurioItem without a Curios slot, register it with ItemInit.registerCurio")
                .toList();

        if (!problems.isEmpty()) {
            throw new IllegalStateException(DragonMagicAndRelics.MOD_ID + ": curios without a slot:\n" + String.join("\n", problems));
        }
    }

    private static List<Item> modItems() {
        return DataGenerators.itemEntries().map(RegistryObject::get).toList();
    }
}
