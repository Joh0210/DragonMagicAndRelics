package de.joh.dragonmagicandrelics.gui;

import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GuiInit {
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        MenuScreens.register(ContainerInit.BRACELET_OF_FRIENDSHIP, GuiBraceletOfFriendship::new);
        //HUDOverlayRenderer.instance = new HUDOverlayRenderer();
    }
}
