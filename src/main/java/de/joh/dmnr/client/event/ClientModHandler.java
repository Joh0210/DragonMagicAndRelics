package de.joh.dmnr.client.event;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.client.block.entity.RiftEmitterRenderer;
import de.joh.dmnr.client.item.WingGradientTextures;
import de.joh.dmnr.client.item.WingRenderLayer;
import de.joh.dmnr.common.init.BlockEntitieInit;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.common.item.DragonMageArmorItem;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModHandler {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){
        BlockEntityRenderers.register(BlockEntitieInit.RIFT_EMITTER_ENTITY.get(), RiftEmitterRenderer::new);
    }

    /**
     * Adds the wings to every player renderer (default and slim skin) and to the armor stand renderer, which can wear
     * the Dragon Mage chestplate.
     * <br> This event is fired again whenever the renderers are rebuilt (e.g. on a resource reload), so the layer is never lost.
     * @see WingRenderLayer
     */
    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        for (String skin : event.getSkins()) {
            if (event.getSkin(skin) instanceof PlayerRenderer renderer) {
                renderer.addLayer(new WingRenderLayer<>(renderer, event.getEntityModels()));
            }
        }

        if (event.getRenderer(EntityType.ARMOR_STAND) instanceof ArmorStandRenderer renderer) {
            renderer.addLayer(new WingRenderLayer<>(renderer, event.getEntityModels()));
        }
    }

    /**
     * The baked gradient textures of wings depend on the resource packs.
     * @see WingGradientTextures
     */
    @SubscribeEvent
    public static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(WingGradientTextures.INSTANCE);
    }

    /**
     * Models that are not used by a block or item, but rendered directly.
     */
    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(RiftEmitterRenderer.BOTTLE_CONTENT);
    }

    /**
     * The item models of the Dragon Mage Armor have the ornaments in layer 0 and the plates in layer 1.
     */
    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {

        event.register((stack, tintIndex) -> switch (tintIndex) {
                    case 0 -> DragonMageArmorItem.getPrimaryColor(stack);
                    case 1 -> DragonMageArmorItem.getSecondaryColor(stack);
                    default -> 0xFFFFFFFF;
                },
                ItemInit.DRAGON_MAGE_HELMET.get(),
                ItemInit.DRAGON_MAGE_CHESTPLATE.get(),
                ItemInit.DRAGON_MAGE_LEGGING.get(),
                ItemInit.DRAGON_MAGE_BOOTS.get());
    }
}
