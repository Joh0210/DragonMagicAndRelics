package de.joh.dmnr.client.event;


import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.init.BlockEntitieInit;
import de.joh.dmnr.client.block.entity.RiftEmitterRenderer;
import de.joh.dmnr.client.item.armor.DragonMageArmorRenderer;
import de.joh.dmnr.common.item.dragonmagearmor.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEventHandler {
    @SubscribeEvent
    public static void registerArmorRenderers(final EntityRenderersEvent.AddLayers event) {
        GeoArmorRenderer.registerArmorRenderer(AbyssalDragonMageArmorItem.class, DragonMageArmorRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(ArchDragonMageArmorItem.class, DragonMageArmorRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(InfernalDragonMageArmorItem.class, DragonMageArmorRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(WildDragonMageArmorItem.class, DragonMageArmorRenderer::new);
    }

//    @SubscribeEvent
//    public static void registerArmorRenderers(final EntityRenderersEvent.AddLayers event) {
//        GeoArmorRenderer.registerArmorRenderer(DragonMageArmor.class, new DragonMageArmorRenderer());
//    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntitieInit.RIFT_EMITTER_ENTITY.get(), RiftEmitterRenderer::new);
    }
}
