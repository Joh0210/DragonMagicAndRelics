package de.joh.dmnr.client.event;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.client.block.entity.RiftEmitterRenderer;
import de.joh.dmnr.common.init.BlockEntitieInit;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.common.item.DragonMageArmorItem;
import net.minecraftforge.api.distmarker.Dist;
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

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {

        event.register((stack, tintIndex) -> {
                    if (tintIndex == 0) {
                        return ((DragonMageArmorItem) stack.getItem()).getColor(stack);
                    }
                    return 0xFFFFFFFF;

                },
                ItemInit.DRAGON_MAGE_HELMET.get(),
                ItemInit.DRAGON_MAGE_CHESTPLATE.get(),
                ItemInit.DRAGON_MAGE_LEGGING.get(),
                ItemInit.DRAGON_MAGE_BOOTS.get());
    }
}
