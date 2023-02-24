package de.joh.dragonmagicandrelics.item.client.armor;

import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import de.joh.dragonmagicandrelics.utils.RLoc;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

/**
 * GeckoLib model for the DragonMageArmor.
 * <br> This code is generic.
 * @author Joh021
 */
public class DragonMageArmorModel extends AnimatedGeoModel<DragonMageArmor> {
    @Override
    public ResourceLocation getModelLocation(DragonMageArmor object) {
        return RLoc.create("geo/dragon_mage_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(DragonMageArmor object) {
        return RLoc.create(object.getTextureLocation());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(DragonMageArmor animatable) {
        return RLoc.create("animations/armor_animation.json");
    }
}
