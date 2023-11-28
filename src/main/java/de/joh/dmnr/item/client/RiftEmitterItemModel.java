package de.joh.dmnr.item.client;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.item.items.RiftEmitterItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

/**
 * GeckoLib model for the RiftEmitterItem.
 * <br> This code is generic.
 * @author Joh021
 */
public class RiftEmitterItemModel extends AnimatedGeoModel<RiftEmitterItem> {
        @Override
        public ResourceLocation getModelResource(RiftEmitterItem object) {
                return new ResourceLocation(DragonMagicAndRelics.MOD_ID, "geo/rift_emitter.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(RiftEmitterItem object) {
                return new ResourceLocation(DragonMagicAndRelics.MOD_ID, "textures/block/rift_emitter.png");
        }

        @Override
        public ResourceLocation getAnimationResource(RiftEmitterItem animatable) {
                return new ResourceLocation(DragonMagicAndRelics.MOD_ID, "animations/rift_emitter.animation.json");
        }
}
