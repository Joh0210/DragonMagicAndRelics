package de.joh.dragonmagicandrelics.events;


import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.block.entity.BlockEntitieInit;
import de.joh.dragonmagicandrelics.block.entity.client.RiftEmitterRenderer;
import de.joh.dragonmagicandrelics.item.client.armor.DragonMageArmorRenderer;
import de.joh.dragonmagicandrelics.item.items.dragonmagearmor.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {
    @SubscribeEvent
    public static void registerArmorRenderers(final EntityRenderersEvent.AddLayers event) {
        GeoArmorRenderer.registerArmorRenderer(AbyssalDragonMageArmor.class, DragonMageArmorRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(ArchDragonMageArmor.class, DragonMageArmorRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(InfernalDragonMageArmor.class, DragonMageArmorRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(WildDragonMageArmor.class, DragonMageArmorRenderer::new);
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
