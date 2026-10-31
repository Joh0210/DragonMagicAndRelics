package de.joh.dmnr.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.joh.dmnr.common.item.IWingItem;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

/**
 * Renders the wings of the visible {@link IWingItem} of an entity, with the model of the vanilla Elytra.
 * Each layer of the wings is drawn tinted with its color.
 * <br> The layer is added to every player renderer (default and slim skin) and to the armor stand renderer whenever
 * the renderers are built, so the wings are visible on all players, not only on the local one.
 * <br> No wings are rendered while a vanilla Elytra is worn, as both would overlap.
 * @see IWingItem#getVisibleWingItem(LivingEntity)
 * @see de.joh.dmnr.client.event.ClientModHandler#addLayers
 * @author Joh0210
 */
public class WingRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final ElytraModel<T> wingModel;

    public WingRenderLayer(RenderLayerParent<T, M> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.wingModel = new ElytraModel<>(modelSet.bakeLayer(ModelLayers.ELYTRA));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, @NotNull T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
            return;
        }

        ItemStack stack = IWingItem.getVisibleWingItem(entity);
        if (!(stack.getItem() instanceof IWingItem wingItem)) {
            return;
        }

        // Same placement as the vanilla Elytra layer
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 0.125F);
        this.getParentModel().copyPropertiesTo(this.wingModel);
        this.wingModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        for (IWingItem.WingLayer layer : wingItem.getWingLayers(stack)) {
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.armorCutoutNoCull(layer.texture()));
            this.wingModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY,
                    FastColor.ARGB32.red(layer.color()) / 255.0F,
                    FastColor.ARGB32.green(layer.color()) / 255.0F,
                    FastColor.ARGB32.blue(layer.color()) / 255.0F,
                    1.0F);
        }
        poseStack.popPose();
    }
}
