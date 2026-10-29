package de.joh.dmnr.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import de.joh.dmnr.client.block.entity.RiftEmitterRenderer;
import de.joh.dmnr.common.init.BlockInit;
import de.joh.dmnr.common.item.RiftEmitterItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

/**
 * Renderer of the RiftEmitterItem: the block model of a single table together with the vortex and the content of the
 * bottle of the RiftEmitterRenderer.
 * @see RiftEmitterItem
 * @author Joh0210
 */
public class RiftEmitterItemRenderer extends BlockEntityWithoutLevelRenderer {
    public RiftEmitterItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    /**
     * The buffers are requested in the order in which they have to be drawn: the opaque parts, the content of the
     * bottle and the vortex are drawn as soon as the next buffer is requested, the glass is drawn last together with
     * the other fixed buffers.
     */
    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext displayContext, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Minecraft minecraft = Minecraft.getInstance();
        BlockState state = BlockInit.RIFT_EMITTER.get().defaultBlockState();
        BakedModel model = minecraft.getBlockRenderer().getBlockModel(state);
        ModelBlockRenderer renderer = minecraft.getBlockRenderer().getModelRenderer();
        float time = minecraft.level == null ? 0 : RiftEmitterRenderer.animationTime(minecraft.level.getGameTime(), minecraft.getFrameTime());

        renderer.renderModel(poseStack.last(), buffer.getBuffer(RenderType.entityCutoutNoCull(InventoryMenu.BLOCK_ATLAS)),
                state, model, 1, 1, 1, packedLight, packedOverlay, ModelData.EMPTY, RenderType.cutout());
        RiftEmitterRenderer.renderBottleContent(state.getValue(HorizontalDirectionalBlock.FACING), poseStack, buffer);
        RiftEmitterRenderer.renderVortex(time, poseStack, buffer, packedOverlay);
        renderer.renderModel(poseStack.last(), buffer.getBuffer(Sheets.translucentCullBlockSheet()),
                state, model, 1, 1, 1, packedLight, packedOverlay, ModelData.EMPTY, RenderType.translucent());
    }
}
