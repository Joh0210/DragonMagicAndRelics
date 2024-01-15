package de.joh.dmnr.client.block.entity;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.block.entity.RiftEmitterBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

/**
 * GeckoLib Geo Model for the Rift Emitter
 * @see RiftEmitterBlockEntity
 * @author Joh0210
 */
public class RiftEmitterModel extends GeoModel<RiftEmitterBlockEntity> {
        @Override
        public ResourceLocation getModelResource(RiftEmitterBlockEntity object) {
                return new ResourceLocation(DragonMagicAndRelics.MOD_ID, "geo/rift_emitter.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(RiftEmitterBlockEntity object) {
                return new ResourceLocation(DragonMagicAndRelics.MOD_ID, "textures/block/rift_emitter.png");
        }

        @Override
        public ResourceLocation getAnimationResource(RiftEmitterBlockEntity animatable) {
                return new ResourceLocation(DragonMagicAndRelics.MOD_ID, "animations/rift_emitter.animation.json");
        }
}
