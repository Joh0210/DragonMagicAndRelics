package de.joh.dmnr.client.item.armor;

import de.joh.dmnr.api.item.DragonMageArmorItem;
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
        return object.getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationResource(DragonMageArmorItem animatable) {
        return RLoc.create("animations/armor_animation.json");
    }
}
