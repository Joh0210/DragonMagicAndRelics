package de.joh.dragonmagicandrelics.events;

import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.item.client.armor.WingLayer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * This class overhauls the player renderer
 */
@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerRenderHandler {
    private static boolean addedDragonWingLayer = false;

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onPlayerRenderPre(RenderPlayerEvent.Pre event) {
        if (!addedDragonWingLayer) {
            event.getRenderer().addLayer(new WingLayer(event.getRenderer(), Minecraft.getInstance().getEntityModels()));
            addedDragonWingLayer = true;
        }
    }
}
