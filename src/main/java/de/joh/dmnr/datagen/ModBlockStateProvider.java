package de.joh.dmnr.datagen;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.init.BlockInit;
import de.joh.dmnr.compat.ae2.AE2Init;
import de.joh.dmnr.compat.create.CreateInit;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.client.model.generators.loaders.CompositeModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

/**
 * Generates the blockstates and the block models {@code models/block/<block_id>.json}. Every block needs an explicit
 * model, otherwise the data run fails; the parts in the subfolders of {@code models/block} are maintained by hand.
 * <p>
 * Every blockstate has a single variant: no block is rendered by its model (GeckoLib, block entity renderers,
 * Flywheel), so the model only provides the particle texture.
 * @author Joh0210
 */
public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, DragonMagicAndRelics.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        riftEmitterModel();
        models().cubeAll(AE2Init.ALTAR_INTERFACE.getId().getPath(), blockTexture(AE2Init.ALTAR_INTERFACE.get()));
        engineModel(CreateInit.ARCANE_ENGINE, new ResourceLocation("mna", "block/artifice/decoration/transmuted_silver_block"));
        engineModel(CreateInit.BRIMSTONE_ENGINE, mcLoc("block/gilded_blackstone"));
        models().getBuilder(CreateInit.SPECTRAL_MOTOR.getId().getPath())
                .texture("particle", new ResourceLocation("mna", "block/material/spectral/conjures"));

        DataGenerators.blockEntries()
                .map(RegistryObject::get)
                .forEach(this::defaultState);
    }

    private void defaultState(Block block) {
        ResourceLocation id = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block));
        simpleBlock(block, models().getExistingFile(id));
    }

    /**
     * Also provides the display transforms of the GeckoLib item.
     */
    private void riftEmitterModel() {
        models().getBuilder(BlockInit.RIFT_EMITTER.getId().getPath())
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                .texture("particle", mcLoc("block/smooth_stone"))
                .transforms()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).scale(0.5F).end()
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).scale(0.5F).end()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).scale(0.3F).end()
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).scale(0.3F).end()
                .transform(ItemDisplayContext.GUI).translation(0, -8, 0).end()
                .transform(ItemDisplayContext.FIXED).translation(0, -8, 0).end()
                .end();
    }

    /**
     * The children are the hand-made parts {@code block/<engine_id>/rotor} and {@code crystal}, which
     * {@link de.joh.dmnr.compat.create.client.EngineModel} renders separately.
     */
    private void engineModel(RegistryObject<? extends Block> engine, ResourceLocation particle) {
        String id = engine.getId().getPath();
        models().getBuilder(id)
                .parent(new ModelFile.UncheckedModelFile(mcLoc(ModelProvider.BLOCK_FOLDER + "/block")))
                .texture("particle", particle)
                .customLoader(CompositeModelBuilder::begin)
                .child("rotor", models().nested().parent(models().getExistingFile(modLoc(ModelProvider.BLOCK_FOLDER + "/" + id + "/rotor"))))
                .child("crystal", models().nested().parent(models().getExistingFile(modLoc(ModelProvider.BLOCK_FOLDER + "/" + id + "/crystal"))))
                .end();
    }
}
