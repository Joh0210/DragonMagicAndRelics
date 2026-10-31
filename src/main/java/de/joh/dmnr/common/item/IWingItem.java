package de.joh.dmnr.common.item;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.List;

/**
 * Interface for items that give their wearer visible wings, worn as chestplate or as curio.
 * <br> The wings are drawn with the model of the vanilla Elytra.
 * @see de.joh.dmnr.client.item.WingRenderLayer
 * @author Joh0210
 */
public interface IWingItem {
    /**
     * @return the layers of the wings, drawn in this order, or an empty list if this stack has no wings
     */
    @NotNull
    List<WingLayer> getWingLayers(ItemStack stack);

    /**
     * Searches the entity for the wings to render: wings of the chestplate come first, then those of the curios.
     * <br> Curios follow the rules of the Curios render layer:
     * <br> - an item in a cosmetic slot replaces the item of its slot
     * <br> - an item in a slot whose rendering was toggled off in the Curios screen is ignored
     * <br> Curios syncs the items, cosmetic slots and render toggles to every tracking player, so this also works for other players on the client.
     * @return the item that provides the wings, or {@link ItemStack#EMPTY} if the entity has no visible wings
     */
    @NotNull
    static ItemStack getVisibleWingItem(LivingEntity entity) {
        ItemStack chest = entity.getItemBySlot(EquipmentSlot.CHEST);
        if (hasWings(chest)) {
            return chest;
        }

        return CuriosApi.getCuriosInventory(entity).map(curiosInventory -> {
            for (ICurioStacksHandler stacksHandler : curiosInventory.getCurios().values()) {
                NonNullList<Boolean> renderStates = stacksHandler.getRenders();
                for (int i = 0; i < stacksHandler.getSlots(); i++) {
                    ItemStack stack = stacksHandler.getCosmeticStacks().getStackInSlot(i);
                    if (stack.isEmpty() && renderStates.size() > i && renderStates.get(i)) {
                        stack = stacksHandler.getStacks().getStackInSlot(i);
                    }

                    if (hasWings(stack)) {
                        return stack;
                    }
                }
            }
            return ItemStack.EMPTY;
        }).orElse(ItemStack.EMPTY);
    }

    private static boolean hasWings(ItemStack stack) {
        return stack.getItem() instanceof IWingItem wingItem && !wingItem.getWingLayers(stack).isEmpty();
    }

    /**
     * One texture of the wings, either tinted with a color or recolored as a gradient between two colors.
     * @param texture texture with the layout of the vanilla Elytra texture
     * @param color RGB color the texture is multiplied with; for a gradient the color of its brightest pixels
     * @param darkColor null for a tinted layer; for a gradient the RGB color of the darkest pixels of the texture,
     *                  every other pixel gets a color in between by its brightness
     * @see de.joh.dmnr.client.item.WingGradientTextures
     */
    record WingLayer(ResourceLocation texture, int color, @Nullable Integer darkColor) {
        /**
         * A layer that keeps the colors of its texture.
         */
        public WingLayer(ResourceLocation texture) {
            this(texture, 0xFFFFFF);
        }

        /**
         * A layer whose texture is multiplied with the color, meant for gray textures.
         */
        public WingLayer(ResourceLocation texture, int color) {
            this(texture, color, null);
        }

        /**
         * A layer whose texture is recolored from the dark color at its darkest to the light color at its brightest
         * pixels, meant for textures that are shaded but not gray, like the vanilla Elytra.
         */
        public static WingLayer gradient(ResourceLocation texture, int lightColor, int darkColor) {
            return new WingLayer(texture, lightColor, darkColor);
        }

        public boolean isGradient() {
            return darkColor != null;
        }
    }
}
