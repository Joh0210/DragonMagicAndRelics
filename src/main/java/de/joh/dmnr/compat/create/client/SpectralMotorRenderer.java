package de.joh.dmnr.compat.create.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import de.joh.dmnr.compat.create.blockentity.SpectralMotorBlockEntity;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Draws the rotor of the Spectral Motor if Flywheel does not render it: the same parts, rotation and movement as the
 * {@link SpectralMotorVisual}, full bright like the spectral blocks of M&A.
 * @see SpectralMotorModel
 * @author Joh0210
 */
public class SpectralMotorRenderer extends KineticBlockEntityRenderer<SpectralMotorBlockEntity> {
    public SpectralMotorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(SpectralMotorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (VisualizationManager.supportsVisualization(be.getLevel())) {
            return;
        }

        BlockState state = be.getBlockState();
        Direction.Axis axis = getRotationAxisOf(be);
        Direction facing = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE);
        VertexConsumer vertices = buffer.getBuffer(getRenderType(be, state));

        standardKineticRotationTransform(CachedBuffers.partialFacingVertical(SpectralMotorModel.CORE, state, facing), be, LightTexture.FULL_BRIGHT)
                .renderInto(ms, vertices);

        float renderTime = AnimationTickHolder.getRenderTime(be.getLevel());
        float rotationOffset = getRotationOffsetForPosition(be, be.getBlockPos(), axis);
        for (SpectralMotorModel.Layer layer : SpectralMotorModel.LAYERS) {
            float offset = layer.offset(renderTime);
            SuperByteBuffer part = CachedBuffers.partialFacingVertical(layer.model(), state, facing)
                    .translate(axis.choose(offset, 0, 0), axis.choose(0, offset, 0), axis.choose(0, 0, offset));
            // Same formula as KineticBlockEntityRenderer.getAngleForBe, with the speed of the layer
            float angle = ((renderTime * layer.speed(be) * 3.0F / 10.0F + rotationOffset) % 360) / 180 * (float) Math.PI;
            kineticRotationTransform(part, be, axis, angle, LightTexture.FULL_BRIGHT).renderInto(ms, vertices);
        }
    }
}
