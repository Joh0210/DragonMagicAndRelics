package de.joh.dmnr.block.entity.client;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.block.entity.RiftEmitterEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RiftEmitterModel extends AnimatedGeoModel<RiftEmitterEntity> {
        @Override
        public ResourceLocation getModelLocation(RiftEmitterEntity object) {
                return new ResourceLocation(DragonMagicAndRelics.MOD_ID, "geo/rift_emitter.geo.json");
        }

        @Override
        public ResourceLocation getTextureLocation(RiftEmitterEntity object) {
                return new ResourceLocation(DragonMagicAndRelics.MOD_ID, "textures/block/rift_emitter.png");
        }

        @Override
        public ResourceLocation getAnimationFileLocation(RiftEmitterEntity animatable) {
                return new ResourceLocation(DragonMagicAndRelics.MOD_ID, "animations/rift_emitter.animation.json");
        }
}
