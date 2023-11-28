package de.joh.dmnr.item.client.armor;

import de.joh.dmnr.item.items.dragonmagearmor.DragonMageArmor;
import de.joh.dmnr.utils.RLoc;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

/**
 * GeckoLib model for the DragonMageArmor.
 * <br> This code is generic.
 * @author Joh021
 */
public class DragonMageArmorModel extends AnimatedGeoModel<DragonMageArmor> {
    @Override
    public ResourceLocation getModelResource(DragonMageArmor object) {
        return RLoc.create("geo/dragon_mage_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DragonMageArmor object) {
        return object.getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationResource(DragonMageArmor animatable) {
        return RLoc.create("animations/armor_animation.json");
    }
}
