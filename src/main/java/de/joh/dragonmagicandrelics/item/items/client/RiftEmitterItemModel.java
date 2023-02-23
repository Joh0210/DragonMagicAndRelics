package de.joh.dragonmagicandrelics.item.items.client;

import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.block.entity.RiftEmitterEntity;
import de.joh.dragonmagicandrelics.item.items.RiftEmitterItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RiftEmitterItemModel extends AnimatedGeoModel<RiftEmitterItem> {
        @Override
        public ResourceLocation getModelLocation(RiftEmitterItem object) {
                return new ResourceLocation(DragonMagicAndRelics.MOD_ID, "geo/rift_emitter.geo.json");
        }

        @Override
        public ResourceLocation getTextureLocation(RiftEmitterItem object) {
                return new ResourceLocation(DragonMagicAndRelics.MOD_ID, "textures/block/rift_emitter.png");
        }

        @Override
        public ResourceLocation getAnimationFileLocation(RiftEmitterItem animatable) {
                return new ResourceLocation(DragonMagicAndRelics.MOD_ID, "animations/rift_emitter.animation.json");
        }
}
