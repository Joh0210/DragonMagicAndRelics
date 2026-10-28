package de.joh.dmnr.datagen;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.init.BlockInit;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.compat.ModCompat;
import de.joh.dmnr.compat.ae2.AE2Init;
import de.joh.dmnr.compat.create.CreateInit;
import de.joh.tnl.api.datagen.DataGenValidation;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
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
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * @author Joh0210
 */
@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        // The providers run in this order, and the ExistingFileHelper only knows the files of the earlier ones.
        // The checks run first, before any file is generated.
        generator.addProvider(true, new ValidationProvider());
        generator.addProvider(event.includeClient(), new ModBlockStateProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, existingFileHelper));

        ModBlockTagsProvider blockTags = generator.addProvider(event.includeServer(), new ModBlockTagsProvider(output, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new ModItemTagsProvider(output, lookupProvider, blockTags.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeServer(), new ModDamageTypeTagsProvider(output, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new LootTableProvider(output, Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(ModBlockLootProvider::new, LootContextParamSets.BLOCK))));
        generator.addProvider(event.includeServer(), new ModRecipeProvider(output, existingFileHelper));
    }

    static List<DeferredRegister<Item>> itemRegisters() {
        return List.of(ItemInit.ITEMS, AE2Init.ITEMS, CreateInit.ITEMS);
    }

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
     * They only exist with their optional mod, so the generated data must reference them optionally.
     */
    static List<RegistryObject<? extends Block>> placeableIntegrationBlocks() {
        return List.of(AE2Init.ALTAR_INTERFACE, CreateInit.ARCANE_ENGINE, CreateInit.BRIMSTONE_ENGINE);
    }

    /**
     * Forge fires the GatherDataEvent for every loaded mod, also when another mod's data is generated (e.g. an addon
     * with DM&R in its development runs), and the event does not tell whose data is generated. Only the providers of
     * the generated mods run, so the checks are a provider instead of being part of the event handler.
     */
    private static class ValidationProvider implements DataProvider {
        @Override
        public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
            for (ModCompat compat : ModCompat.values()) {
                if (!compat.isLoaded()) {
                    throw new IllegalStateException(DragonMagicAndRelics.MOD_ID + ": data generation needs all optional mods with code integration, missing: " + compat.name());
                }
            }
            DataGenValidation.validateRegisters(DragonMagicAndRelics.MOD_ID, "DataGenerators.itemRegisters()",
                    ForgeRegistries.ITEMS.getKeys(), itemEntries());
            DataGenValidation.validateRegisters(DragonMagicAndRelics.MOD_ID, "DataGenerators.blockRegisters()",
                    ForgeRegistries.BLOCKS.getKeys(), blockEntries());
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public @NotNull String getName() {
            return "Validation: " + DragonMagicAndRelics.MOD_ID;
        }
    }
}
