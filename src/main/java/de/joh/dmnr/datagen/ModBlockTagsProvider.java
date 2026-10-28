package de.joh.dmnr.datagen;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.api.util.ModTags;
import de.joh.dmnr.common.init.BlockInit;
import de.joh.dmnr.compat.create.CreateInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * @author Joh0210
 */
public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, DragonMagicAndRelics.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        TagAppender<Block> pickaxe = tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BlockInit.RIFT_EMITTER.get());
        for (RegistryObject<? extends Block> block : DataGenerators.placeableIntegrationBlocks()) {
            pickaxe.addOptional(block.getId());
        }

        tag(BlockTags.NEEDS_IRON_TOOL).add(BlockInit.RIFT_EMITTER.get());

        tag(BlockTags.create(ModTags.Blocks.MNA_FLOWERS)).add(
                com.mna.blocks.BlockInit.AUM.get(),
                com.mna.blocks.BlockInit.CERUBLOSSOM.get(),
                com.mna.blocks.BlockInit.TARMA_ROOT.get(),
                com.mna.blocks.BlockInit.DESERT_NOVA.get(),
                com.mna.blocks.BlockInit.WAKEBLOOM.get());

        tag(BlockTags.create(ModTags.Blocks.MUTANDIS_PLANTS))
                .addTag(BlockTags.FLOWERS)
                .addTag(BlockTags.SAPLINGS)
                .add(Blocks.CACTUS, Blocks.SUGAR_CANE, Blocks.BAMBOO, Blocks.VINE, Blocks.GLOW_LICHEN, Blocks.LILY_PAD,
                        Blocks.BIG_DRIPLEAF, Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM);

        tag(BlockTags.create(new ResourceLocation("create", "non_movable")))
                .addOptional(CreateInit.SPECTRAL_MOTOR.getId())
                .addOptional(CreateInit.ARCANE_ENGINE.getId())
                .addOptional(CreateInit.BRIMSTONE_ENGINE.getId());
    }
}
