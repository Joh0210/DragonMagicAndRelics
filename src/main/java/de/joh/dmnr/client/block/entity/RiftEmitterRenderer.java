package de.joh.dmnr.client.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.joh.dmnr.common.block.entity.RiftEmitterBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

/**
 * GeckoLib Geo Renderer for the Rift Emitter
 * @see RiftEmitterBlockEntity
 * @author Joh0210
 */
public class RiftEmitterRenderer extends GeoBlockRenderer<RiftEmitterBlockEntity> {
    public RiftEmitterRenderer(BlockEntityRendererProvider.Context context) {
        super(new RiftEmitterModel());
    }
}