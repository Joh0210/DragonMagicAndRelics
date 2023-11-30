package de.joh.dmnr.client.item.armor;

import com.mna.factions.Factions;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.joh.dmnr.common.armorupgrade.FlyArmorUpgrade;
import de.joh.dmnr.common.init.EffectInit;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.common.item.AngelRingItem;
import de.joh.dmnr.api.item.DragonMageArmorItem;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;

/**
 * This class adds visible wings to the player when wearing the Dragon Mage armor and has the Elytra upgrade installed.
 * @see FlyArmorUpgrade
 * @see DragonMageArmorItem
 */
public class WingRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final ElytraModel<T> modelElytra;

    public WingRenderLayer(RenderLayerParent<T, M> rendererIn, EntityModelSet p_174494_) {
        super(rendererIn);
        this.modelElytra = new ElytraModel<>(p_174494_.bakeLayer(ModelLayers.ELYTRA));
    }

    public void render(@NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack chest = entity.getItemBySlot(EquipmentSlot.CHEST);
        if (!chest.isEmpty() && chest.getItem() instanceof DragonMageArmorItem dmArmor && entity.hasEffect(EffectInit.ELYTRA.get())) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.0D, 0.0D, 0.125D);
            this.getParentModel().copyPropertiesTo(this.modelElytra);
            this.modelElytra.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, this.modelElytra.renderType(dmArmor.getWingTextureLocation()), false, dmArmor.isFoil(chest));
            this.modelElytra.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.popPose();
        }
        else if (CuriosApi.getCuriosHelper().findFirstCurio(entity, ItemInit.ANGEL_RING.get()).isPresent()) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.0D, 0.0D, 0.125D);
            this.getParentModel().copyPropertiesTo(this.modelElytra);
            this.modelElytra.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, this.modelElytra.renderType(AngelRingItem.getWingTextureLocation(Factions.FEY)), false, false);
            this.modelElytra.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.popPose();
        }
        else if (CuriosApi.getCuriosHelper().findFirstCurio(entity, ItemInit.FALLEN_ANGEL_RING.get()).isPresent()) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.0D, 0.0D, 0.125D);
            this.getParentModel().copyPropertiesTo(this.modelElytra);
            this.modelElytra.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, this.modelElytra.renderType(AngelRingItem.getWingTextureLocation(Factions.UNDEAD)), false, false);
            this.modelElytra.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.popPose();
        }
    }
}
