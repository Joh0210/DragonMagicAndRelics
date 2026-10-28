package de.joh.dmnr.datagen;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.common.item.DragonMageArmorItem;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.CompositeModelBuilder;
import net.minecraftforge.client.model.generators.loaders.ObjModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

/**
 * Generates the item models. Items without a special model get one by their type: block items use the model of their
 * block, all others a flat model with the texture {@code item/<item_id>}.
 * @author Joh0210
 */
public class ModItemModelProvider extends ItemModelProvider {
    private static final ModelFile GENERATED = new ModelFile.UncheckedModelFile("item/generated");
    private static final ModelFile HANDHELD = new ModelFile.UncheckedModelFile("item/handheld");

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, DragonMagicAndRelics.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        handheldItem(ItemInit.WEATHER_FAIRY_STAFF);
        fireResistanceBracelet();

        DataGenerators.itemEntries()
                .filter(item -> !generatedModels.containsKey(item.getId().withPrefix(ITEM_FOLDER + "/")))
                .forEach(item -> defaultItem(item.get()));
    }

    private void defaultItem(Item item) {
        ResourceLocation id = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));

        if (item instanceof BlockItem blockItem) {
            ResourceLocation blockId = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(blockItem.getBlock()));
            withExistingParent(id.getPath(), blockId.withPrefix(BLOCK_FOLDER + "/"));
        } else if (item instanceof DragonMageArmorItem) {
            // Dyeable: layer0 is tinted with the color of the armor, layer1 stays as it is
            getBuilder(id.getPath())
                    .parent(GENERATED)
                    .texture("layer0", id.withPath(path -> ITEM_FOLDER + "/" + path + "_base"))
                    .texture("layer1", id.withPath(path -> ITEM_FOLDER + "/" + path + "_overlay"));
        } else {
            basicItem(id);
        }
    }

    private void handheldItem(RegistryObject<Item> item) {
        ResourceLocation id = item.getId();
        getBuilder(id.getPath())
                .parent(HANDHELD)
                .texture("layer0", id.withPrefix(ITEM_FOLDER + "/"));
    }

    private void fireResistanceBracelet() {
        getBuilder(ItemInit.FIRE_RESISTANCE_BRACELET.getId().getPath())
                .parent(new ModelFile.UncheckedModelFile("forge:item/default"))
                .customLoader(CompositeModelBuilder::begin)
                .child("main", nested()
                        .customLoader(ObjModelBuilder::begin)
                        .modelLocation(modLoc("models/item/obj/fire_resistance_bracelet.obj"))
                        .automaticCulling(true)
                        .shadeQuads(true)
                        .flipV(true)
                        .emissiveAmbient(false)
                        .end())
                .end()
                .transforms()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).scale(1).rotation(90, 180, 0).translation(0, 0, 7.5F).end()
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).scale(1).rotation(90, 180, 0).translation(0, 0, 7.5F).end()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).scale(0.75F).rotation(15, -90, 0).translation(0, 8, 2.5F).end()
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).scale(0.75F).rotation(15, -90, 0).translation(0, 8, 2.5F).end()
                .transform(ItemDisplayContext.GROUND).scale(0.6F).translation(0, 4, 0).end()
                .transform(ItemDisplayContext.GUI).scale(2).rotation(30, 30, 15).translation(-3.5F, 13, 0).end()
                .transform(ItemDisplayContext.FIXED).scale(2).rotation(-90, 180, 0).translation(0, 0, -20).end();
    }
}
