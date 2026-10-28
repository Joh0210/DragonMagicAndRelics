package de.joh.dmnr.datagen;

import de.joh.dmnr.common.init.BlockInit;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Generates the block loot tables. Every block without {@code noLootTable()} needs one, otherwise the data run fails.
 * <p>
 * Loot tables have no conditions in this Forge version, so the blocks of the integrations drop themselves through
 * their {@link ModItemTagsProvider#selfDropTag self drop tag} instead of their item, which may not exist.
 * @author Joh0210
 */
public class ModBlockLootProvider extends BlockLootSubProvider {
    public ModBlockLootProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(BlockInit.RIFT_EMITTER.get());

        for (RegistryObject<? extends Block> block : DataGenerators.integrationBlocks()) {
            add(block.get(), LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(TagEntry.tagContents(ModItemTagsProvider.selfDropTag(block)))
                    .when(ExplosionCondition.survivesExplosion())));
        }
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return () -> DataGenerators.blockEntries().map(RegistryObject::get).iterator();
    }
}
