package de.joh.dmnr.datagen;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.init.BlockInit;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.compat.ModCompat;
import de.joh.dmnr.compat.ae2.AE2Init;
import de.joh.dmnr.compat.create.CreateInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Registers the data generators ({@code ./gradlew runData}), whose output in {@code src/generated/resources} must not be
 * edited by hand.
 * @author Joh0210
 */
@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        // The content of the integrations is part of the generated data
        for (ModCompat compat : ModCompat.values()) {
            if (!compat.isLoaded()) {
                throw new IllegalStateException(DragonMagicAndRelics.MOD_ID + ": data generation needs all optional mods with code integration, missing: " + compat.name());
            }
        }
        validateRegisters("itemRegisters()", ForgeRegistries.ITEMS.getKeys(), itemEntries());
        validateRegisters("blockRegisters()", ForgeRegistries.BLOCKS.getKeys(), blockEntries());

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        // Providers run in this order, and the existing file helper only knows generated files that came before:
        // block models before the item models using them as parents, item tags before the recipes using them
        generator.addProvider(event.includeClient(), new ModBlockStateProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, existingFileHelper));

        ModBlockTagsProvider blockTags = generator.addProvider(event.includeServer(), new ModBlockTagsProvider(output, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new ModItemTagsProvider(output, lookupProvider, blockTags.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeServer(), new ModDamageTypeTagsProvider(output, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new LootTableProvider(output, Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(ModBlockLootProvider::new, LootContextParamSets.BLOCK))));
        generator.addProvider(event.includeServer(), new ModRecipeProvider(output, existingFileHelper));
    }

    /**
     * Every register of this mod, including those of the integrations. A missing one fails the data run.
     */
    static List<DeferredRegister<Item>> itemRegisters() {
        return List.of(ItemInit.ITEMS, AE2Init.ITEMS, CreateInit.ITEMS);
    }

    /**
     * @see #itemRegisters()
     */
    static List<DeferredRegister<Block>> blockRegisters() {
        return List.of(BlockInit.BLOCKS, AE2Init.BLOCKS, CreateInit.BLOCKS);
    }

    static Stream<RegistryObject<Item>> itemEntries() {
        return itemRegisters().stream().flatMap(register -> register.getEntries().stream());
    }

    static Stream<RegistryObject<Block>> blockEntries() {
        return blockRegisters().stream().flatMap(register -> register.getEntries().stream());
    }

    /**
     * Placeable blocks of the integrations. They only exist with the optional mod, so the generated data references
     * them optionally.
     */
    static List<RegistryObject<? extends Block>> integrationBlocks() {
        return List.of(AE2Init.ALTAR_INTERFACE, CreateInit.ARCANE_ENGINE, CreateInit.BRIMSTONE_ENGINE);
    }

    private static void validateRegisters(String registersMethod, Set<ResourceLocation> registered, Stream<? extends RegistryObject<?>> listed) {
        Set<ResourceLocation> listedIds = listed.map(RegistryObject::getId).collect(Collectors.toSet());
        List<ResourceLocation> missing = registered.stream()
                .filter(id -> id.getNamespace().equals(DragonMagicAndRelics.MOD_ID) && !listedIds.contains(id))
                .sorted()
                .toList();

        if (!missing.isEmpty()) {
            throw new IllegalStateException(DragonMagicAndRelics.MOD_ID + ": registered, but not in a register of DataGenerators."
                    + registersMethod + ": " + missing);
        }
    }
}
