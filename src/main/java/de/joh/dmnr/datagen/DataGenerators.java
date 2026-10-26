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
 * Registers the data generators of this mod ({@code ./gradlew runData}). The output is written to
 * {@code src/generated/resources} and must not be edited by hand.
 * <p>
 * Generated: blockstates, block models ({@code models/block/<block_id>.json}), item models, tags, block loot tables
 * and the manaweaving and Eldrin Altar recipes ({@link ModRecipeProvider}). Everything else is still maintained by hand in
 * {@code src/main/resources}, including the models in the subfolders of {@code models/block} and all other recipe
 * types.
 * <p>
 * The providers that handle every item or block of this mod get them from {@link #itemRegisters()} and
 * {@link #blockRegisters()}. A new register (e.g. of a new integration) has to be added there, otherwise the data run
 * fails.
 * @author Joh0210
 */
@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        // The content of the integrations is part of the generated data, so it has to be registered
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

        // The block models have to be generated before the item models, which use them as parents
        generator.addProvider(event.includeClient(), new ModBlockStateProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, existingFileHelper));

        ModBlockTagsProvider blockTags = generator.addProvider(event.includeServer(), new ModBlockTagsProvider(output, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new ModItemTagsProvider(output, lookupProvider, blockTags.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeServer(), new ModDamageTypeTagsProvider(output, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new LootTableProvider(output, Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(ModBlockLootProvider::new, LootContextParamSets.BLOCK))));
        // After the item tags: the recipe builders check that the tags they use exist, and the existing file helper
        // only knows the generated item tags of this mod once ModItemTagsProvider has run
        generator.addProvider(event.includeServer(), new ModRecipeProvider(output, existingFileHelper));
    }

    /**
     * @return the registers of all items of this mod, including those of the integrations
     */
    static List<DeferredRegister<Item>> itemRegisters() {
        return List.of(ItemInit.ITEMS, AE2Init.ITEMS, CreateInit.ITEMS);
    }

    /**
     * @return the registers of all blocks of this mod, including those of the integrations
     */
    static List<DeferredRegister<Block>> blockRegisters() {
        return List.of(BlockInit.BLOCKS, AE2Init.BLOCKS, CreateInit.BLOCKS);
    }

    /**
     * @return all items of this mod, in the order of {@link #itemRegisters()}
     */
    static Stream<RegistryObject<Item>> itemEntries() {
        return itemRegisters().stream().flatMap(register -> register.getEntries().stream());
    }

    /**
     * @return all blocks of this mod, in the order of {@link #blockRegisters()}
     */
    static Stream<RegistryObject<Block>> blockEntries() {
        return blockRegisters().stream().flatMap(register -> register.getEntries().stream());
    }

    /**
     * The blocks of the integrations that can be placed and mined. They only exist if the optional mod is installed,
     * so the generated data must reference them optionally.
     */
    static List<RegistryObject<? extends Block>> integrationBlocks() {
        return List.of(AE2Init.ALTAR_INTERFACE, CreateInit.ARCANE_ENGINE, CreateInit.BRIMSTONE_ENGINE);
    }

    /**
     * Ensures that the registers list every registered entry of this mod, so that no provider misses one.
     * @param registersMethod name of the method that lists the registers, for the error message
     * @param registered IDs of all entries of the game registry
     * @throws IllegalStateException if an entry of this mod is not in one of the registers
     */
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
