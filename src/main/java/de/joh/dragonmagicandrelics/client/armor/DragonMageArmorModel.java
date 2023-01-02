package de.joh.dragonmagicandrelics.client.armor;

import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import de.joh.dragonmagicandrelics.utils.RLoc;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DragonMageArmorModel extends AnimatedGeoModel<DragonMageArmor> {
    @Override
    public ResourceLocation getModelLocation(DragonMageArmor object) {
        return RLoc.create("geo/dragon_mage_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(DragonMageArmor object) {
        return RLoc.create("textures/models/armor/dragon_mage_armor_texture.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(DragonMageArmor animatable) {
        return RLoc.create("animations/armor_animation.json");
    }
}
