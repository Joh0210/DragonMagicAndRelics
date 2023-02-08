package de.joh.dragonmagicandrelics.events;


import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.client.armor.DragonMageArmorRenderer;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {
    @SubscribeEvent
    public static void registerArmorRenderers(final FMLClientSetupEvent event) {
        GeoArmorRenderer.registerArmorRenderer(DragonMageArmor.class, DragonMageArmorRenderer::new);
    }
}
