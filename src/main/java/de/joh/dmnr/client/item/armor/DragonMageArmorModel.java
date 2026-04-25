package de.joh.dmnr.client.item.armor;

import de.joh.dmnr.common.item.DragonMageArmorItem;
import de.joh.dmnr.common.util.RLoc;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

/**
 * GeckoLib model for the DragonMageArmor.
 * <br> This code is generic.
 * @author Joh021
 */
public class DragonMageArmorModel extends GeoModel<DragonMageArmorItem> {
    @Override
    public ResourceLocation getModelResource(DragonMageArmorItem object) {
        return RLoc.create("geo/dragon_mage_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DragonMageArmorItem object) {
        return RLoc.create("textures/models/armor/dragon_mage_armor_texture_base.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DragonMageArmorItem animatable) {
        return RLoc.create("animations/armor_animation.json");
    }
}
