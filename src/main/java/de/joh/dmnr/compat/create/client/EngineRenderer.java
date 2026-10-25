package de.joh.dmnr.compat.create.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import de.joh.dmnr.compat.create.blockentity.EngineBlockEntity;
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
 * Draws the rotor of the Arcane Engine and the Brimstone Engine if Flywheel does not render it: the same parts,
 * rotation and movement as the {@link EngineVisual}.
 * @see EngineModel
 * @author Joh0210
 */
public class EngineRenderer extends KineticBlockEntityRenderer<EngineBlockEntity> {
    public EngineRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(EngineBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (VisualizationManager.supportsVisualization(be.getLevel())) {
            return;
        }

        BlockState state = be.getBlockState();
        EngineModel.Parts parts = EngineModel.of(state);
        Direction.Axis axis = getRotationAxisOf(be);
        Direction facing = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE);
        VertexConsumer vertices = buffer.getBuffer(getRenderType(be, state));

        standardKineticRotationTransform(CachedBuffers.partialFacingVertical(parts.rotor(), state, facing), be, light)
                .renderInto(ms, vertices);

        float renderTime = AnimationTickHolder.getRenderTime(be.getLevel());
        float offset = EngineModel.crystalOffset(renderTime);
        SuperByteBuffer crystal = CachedBuffers.partialFacingVertical(parts.crystal(), state, facing)
                .translate(axis.choose(offset, 0, 0), axis.choose(0, offset, 0), axis.choose(0, 0, offset));
        // Same formula as KineticBlockEntityRenderer.getAngleForBe, with the speed of the crystal
        float angle = ((renderTime * EngineModel.CRYSTAL_SPEED * 3.0F / 10.0F + getRotationOffsetForPosition(be, be.getBlockPos(), axis)) % 360) / 180 * (float) Math.PI;
        kineticRotationTransform(crystal, be, axis, angle, parts.glowingCrystal() ? LightTexture.FULL_BRIGHT : light).renderInto(ms, vertices);
    }
}
