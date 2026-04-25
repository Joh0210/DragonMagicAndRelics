package de.joh.dmnr.client.item.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.joh.dmnr.common.item.dragonmagearmor.DragonMageArmorItem;
import de.joh.dmnr.common.util.RLoc;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class DragonMageArmorOverlayLayer extends GeoRenderLayer<DragonMageArmorItem> {
    private static final ResourceLocation OVERLAY = RLoc.create("textures/models/armor/dragon_mage_armor_texture_overlay.png");

    public DragonMageArmorOverlayLayer(GeoRenderer<DragonMageArmorItem> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, DragonMageArmorItem animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType overlayRenderType = RenderType.armorCutoutNoCull(OVERLAY);

        getRenderer().reRender(
                bakedModel,
                poseStack,
                bufferSource,
                animatable,
                overlayRenderType,
                bufferSource.getBuffer(overlayRenderType),
                partialTick,
                packedLight,
                packedOverlay,
                1.0f, // Red
                1.0f, // Green
                1.0f, // Blue
                1.0f  // Alpha
        );
    }
}
