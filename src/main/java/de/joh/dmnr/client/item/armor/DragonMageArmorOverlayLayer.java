package de.joh.dmnr.client.item.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.joh.dmnr.common.item.DragonMageArmorItem;
import de.joh.dmnr.common.util.RLoc;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

/**
 * Renders the plates of the Dragon Mage Armor, tinted with the secondary color.
 * @see DragonMageArmorItem#getSecondaryColor(ItemStack)
 * @author Joh0210
 */
public class DragonMageArmorOverlayLayer extends GeoRenderLayer<DragonMageArmorItem> {
    private static final ResourceLocation OVERLAY = RLoc.create("textures/models/armor/dragon_mage_armor_texture_overlay.png");

    public DragonMageArmorOverlayLayer(GeoRenderer<DragonMageArmorItem> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, DragonMageArmorItem animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType overlayRenderType = RenderType.armorCutoutNoCull(OVERLAY);
        ItemStack stack = getRenderer() instanceof GeoArmorRenderer<?> armorRenderer ? armorRenderer.getCurrentStack() : null;
        int color = stack != null ? DragonMageArmorItem.getSecondaryColor(stack) : DragonMageArmorItem.DEFAULT_SECONDARY_COLOR;

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
                FastColor.ARGB32.red(color) / 255.0f,
                FastColor.ARGB32.green(color) / 255.0f,
                FastColor.ARGB32.blue(color) / 255.0f,
                1.0f  // Alpha
        );
    }
}
