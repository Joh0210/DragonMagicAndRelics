package de.joh.dmnr.client.item.armor;

import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.core.object.Color;
import de.joh.dmnr.common.item.DragonMageArmorItem;
import net.minecraft.world.item.ItemStack;

/**
 * GeckoLib renderer for DragonMageArmor.
 * <br> The base texture is tinted with the primary color, the overlay layer with the secondary color.
 * @see DragonMageArmorOverlayLayer
 * @author Joh0210
 */
public class DragonMageArmorRenderer extends GeoArmorRenderer<DragonMageArmorItem> {

    public DragonMageArmorRenderer() {
        super(new DragonMageArmorModel());

        addRenderLayer(new DragonMageArmorOverlayLayer(this));
    }

    @Override
    public Color getRenderColor(DragonMageArmorItem animatable, float partialTick, int packedLight) {
        ItemStack stack = this.currentStack;
        if (stack != null && stack.getItem() instanceof DragonMageArmorItem) {
            return Color.ofOpaque(DragonMageArmorItem.getPrimaryColor(stack));
        }
        return Color.WHITE; // Fallback
    }

}
