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
import net.minecraftforge.client.model.generators.loaders.CompositeModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

/**
 * Every block gets a single variant with the model of the same name: no block is rendered by its model (GeckoLib,
 * block entity renderers, Flywheel), so the model only provides the particle texture.
 * @author Joh0210
 */
public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, DragonMagicAndRelics.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        riftEmitterModel();
        cubeAll(AE2Init.ALTAR_INTERFACE.get());
        engineModel(CreateInit.ARCANE_ENGINE, new ResourceLocation("mna", "block/artifice/decoration/transmuted_silver_block"));
        engineModel(CreateInit.BRIMSTONE_ENGINE, mcLoc("block/gilded_blackstone"));
        models().getBuilder(CreateInit.SPECTRAL_MOTOR.getId().getPath())
                .texture("particle", new ResourceLocation("mna", "block/material/spectral/conjures"));

        DataGenerators.blockEntries()
                .forEach(block -> simpleBlock(block.get(), models().getExistingFile(block.getId())));
    }

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

    // EngineModel (Create integration) renders the hand-made children separately
    private void engineModel(RegistryObject<? extends Block> engine, ResourceLocation particle) {
        String folder = "block/" + engine.getId().getPath();
        models().getBuilder(engine.getId().getPath())
                .parent(models().getExistingFile(mcLoc("block/block")))
                .texture("particle", particle)
                .customLoader(CompositeModelBuilder::begin)
                .child("rotor", models().nested().parent(models().getExistingFile(modLoc(folder + "/rotor"))))
                .child("crystal", models().nested().parent(models().getExistingFile(modLoc(folder + "/crystal"))))
                .end();
    }
}
