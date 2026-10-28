package de.joh.dmnr.datagen;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.common.item.DragonMageArmorItem;
import de.joh.tnl.api.datagen.DefaultItemModelProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.CompositeModelBuilder;
import net.minecraftforge.client.model.generators.loaders.ObjModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

/**
 * @author Joh0210
 */
public class ModItemModelProvider extends DefaultItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, DragonMagicAndRelics.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        handheldItem(ItemInit.WEATHER_FAIRY_STAFF);
        fireResistanceBracelet();

        defaultModels(DataGenerators.itemEntries());
    }

    @Override
    protected void defaultItem(Item item) {
        if (item instanceof DragonMageArmorItem) {
            ResourceLocation id = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));
            ResourceLocation texture = id.withPrefix(ITEM_FOLDER + "/");
            getBuilder(id.getPath())
                    .parent(GENERATED)
                    .texture("layer0", texture.withSuffix("_base"))
                    .texture("layer1", texture.withSuffix("_overlay"));
        } else {
            super.defaultItem(item);
        }
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
