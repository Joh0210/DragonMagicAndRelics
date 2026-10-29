package de.joh.dmnr.client.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import de.joh.dmnr.common.block.entity.RiftEmitterBlockEntity;
import de.joh.dmnr.common.util.RLoc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

/**
 * Renders the animated parts of the Rift Emitter that the block model cannot show: the vortex in the bowl and the
 * content of the bottle, which uses the shader of the end portal. Both are also used by the item renderer.
 * @see RiftEmitterBlockEntity
 * @author Joh0210
 */
public class RiftEmitterRenderer implements BlockEntityRenderer<RiftEmitterBlockEntity> {
    /**
     * Content of the bottle, registered as an additional model.
     */
    public static final ResourceLocation BOTTLE_CONTENT = RLoc.create("block/rift_emitter/liquid");
    private static final RenderType VORTEX = RenderType.entityTranslucent(RLoc.create("textures/entity/rift_vortex.png"));
    private static final RandomSource RANDOM = RandomSource.create();

    // floor and inner radius of the bowl, see deco.obj
    private static final float VORTEX_HEIGHT = 16.5F / 16;
    private static final float VORTEX_RADIUS = 3.6F / 16;

    // degrees per tick of the two layers of the vortex
    private static final float VORTEX_SPEED = 4.0F;
    private static final float VORTEX_SPEED_SLOW = 2.5F;
    // both layers have made full turns after this many ticks
    private static final long ANIMATION_CYCLE = 720;

    public RiftEmitterRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(@NotNull RiftEmitterBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Level level = blockEntity.getLevel();
        float time = level == null ? 0 : animationTime(level.getGameTime(), partialTick);

        renderBottleContent(blockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING), poseStack, buffer);
        renderVortex(time, poseStack, buffer, packedOverlay);
    }

    /**
     * Ticks for the animation. The game time is reduced to one animation cycle, so the partial tick is not lost to the
     * precision of a float in old worlds.
     */
    public static float animationTime(long gameTime, float partialTick) {
        return gameTime % ANIMATION_CYCLE + partialTick;
    }

    /**
     * Renders the content of the bottle with the shader of the end portal.
     * @param facing direction of the table, the content is rotated like the block model
     */
    public static void renderBottleContent(Direction facing, PoseStack poseStack, MultiBufferSource buffer) {
        BakedModel model = Minecraft.getInstance().getModelManager().getModel(BOTTLE_CONTENT);
        VertexConsumer consumer = buffer.getBuffer(RenderType.endPortal());

        poseStack.pushPose();
        // same rotation as in the blockstate: north 0°, east 90°, south 180°, west 270°
        poseStack.rotateAround(Axis.YP.rotationDegrees(180 - facing.toYRot()), 0.5F, 0, 0.5F);
        for (BakedQuad quad : model.getQuads(null, null, RANDOM, ModelData.EMPTY, null)) {
            consumer.putBulkData(poseStack.last(), quad, 1, 1, 1, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
        }
        poseStack.popPose();
    }

    /**
     * Renders the vortex in the bowl: two layers of the same texture that turn at different speeds.
     * @param time ticks of the animation, see {@link #animationTime(long, float)}
     */
    public static void renderVortex(float time, PoseStack poseStack, MultiBufferSource buffer, int packedOverlay) {
        VertexConsumer consumer = buffer.getBuffer(VORTEX);
        vortexLayer(consumer, poseStack, VORTEX_HEIGHT + 0.002F, VORTEX_RADIUS, -time * VORTEX_SPEED, 255, packedOverlay);
        vortexLayer(consumer, poseStack, VORTEX_HEIGHT + 0.004F, VORTEX_RADIUS * 0.94F, 40 - time * VORTEX_SPEED_SLOW, 110, packedOverlay);
    }

    private static void vortexLayer(VertexConsumer consumer, PoseStack poseStack, float height, float radius, float angle, int alpha, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5F, height, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(angle % 360));
        PoseStack.Pose pose = poseStack.last();
        vertex(consumer, pose, -radius, -radius, 0, 0, alpha, packedOverlay);
        vertex(consumer, pose, -radius, radius, 0, 1, alpha, packedOverlay);
        vertex(consumer, pose, radius, radius, 1, 1, alpha, packedOverlay);
        vertex(consumer, pose, radius, -radius, 1, 0, alpha, packedOverlay);
        poseStack.popPose();
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float z, float u, float v, int alpha, int packedOverlay) {
        consumer.vertex(pose.pose(), x, 0, z)
                .color(255, 255, 255, alpha)
                .uv(u, v)
                .overlayCoords(packedOverlay)
                .uv2(LightTexture.FULL_BRIGHT)
                .normal(pose.normal(), 0, 1, 0)
                .endVertex();
    }
}
