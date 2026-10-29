package de.joh.dmnr.datagen;

import com.mna.api.blocks.WizardLabBlock;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.init.BlockInit;
import de.joh.dmnr.compat.ae2.AE2Init;
import de.joh.dmnr.compat.create.CreateInit;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.client.model.generators.loaders.CompositeModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

/**
 * Every block gets a single variant with the model of the same name, except the Rift Emitter: it is a table of the
 * M&A wizard lab and changes its model with the neighbouring tables.
 * <br> Most blocks are not rendered by their model (block entity renderers, Flywheel), so the model only provides
 * the particle texture.
 * @author Joh0210
 */
public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, DragonMagicAndRelics.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        riftEmitter();
        cubeAll(AE2Init.ALTAR_INTERFACE.get());
        engineModel(CreateInit.ARCANE_ENGINE, new ResourceLocation("mna", "block/artifice/decoration/transmuted_silver_block"));
        engineModel(CreateInit.BRIMSTONE_ENGINE, mcLoc("block/gilded_blackstone"));
        models().getBuilder(CreateInit.SPECTRAL_MOTOR.getId().getPath())
                .texture("particle", new ResourceLocation("mna", "block/material/spectral/conjures"));

        DataGenerators.blockEntries()
                .filter(block -> !registeredBlocks.containsKey(block.get()))
                .forEach(block -> simpleBlock(block.get(), models().getExistingFile(block.getId())));
    }

    /**
     * The model of the block holds the decoration (bowl, cloth, bottle); the table below it is one of the desks of
     * M&A, like for the Altar of Arcana: on its own, connected to the left, to the right or to both sides.
     * The vortex and the content of the bottle are rendered by the RiftEmitterRenderer.
     */
    private void riftEmitter() {
        String folder = "block/" + BlockInit.RIFT_EMITTER.getId().getPath();
        ModelFile decoration = models().getBuilder(BlockInit.RIFT_EMITTER.getId().getPath())
                .parent(models().getExistingFile(mcLoc("block/block")))
                .texture("particle", new ResourceLocation("mna", "block/artifice/wizard_lab/inscription_table"))
                .customLoader(CompositeModelBuilder::begin)
                .child("deco", models().nested().parent(models().getExistingFile(modLoc(folder + "/deco"))))
                .child("bottle", models().nested().parent(models().getExistingFile(modLoc(folder + "/bottle"))))
                .end();

        MultiPartBlockStateBuilder builder = getMultipartBuilder(BlockInit.RIFT_EMITTER.get());
        for (Direction facing : HorizontalDirectionalBlock.FACING.getPossibleValues()) {
            int rotation = ((int) facing.toYRot() + 180) % 360;
            builder.part().modelFile(decoration).rotationY(rotation).addModel()
                    .condition(HorizontalDirectionalBlock.FACING, facing).end();

            for (boolean left : new boolean[]{false, true}) {
                for (boolean right : new boolean[]{false, true}) {
                    String table = left ? (right ? "table_center" : "table_left") : (right ? "table_right" : "table_single");
                    builder.part().modelFile(models().getExistingFile(modLoc(folder + "/" + table))).rotationY(rotation).addModel()
                            .condition(HorizontalDirectionalBlock.FACING, facing)
                            .condition(WizardLabBlock.LEFT, left)
                            .condition(WizardLabBlock.RIGHT, right)
                            .end();
                }
            }
        }
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
