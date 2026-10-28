package de.joh.dmnr.datagen;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.tnl.api.datagen.MnaItemTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Joh0210
 */
public class ModItemTagsProvider extends MnaItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                               CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, DragonMagicAndRelics.MOD_ID, existingFileHelper);
    }

    public static TagKey<Item> selfDropTag(RegistryObject<? extends Block> block) {
        return ItemTags.create(block.getId());
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        List<Item> modItems = DataGenerators.itemEntries().map(RegistryObject::get).toList();
        addCurioSlotTags(ItemInit.getCurioSlots(), modItems);
        addRelicTag(modItems);

        for (RegistryObject<? extends Block> block : DataGenerators.placeableIntegrationBlocks()) {
            tag(selfDropTag(block)).addOptional(block.getId());
        }
    }
}
