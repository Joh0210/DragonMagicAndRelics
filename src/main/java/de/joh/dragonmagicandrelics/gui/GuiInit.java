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
        MenuScreens.register(ContainerInit.ABYSSAL_DRAGON_MAGE_CHESTPLATE, GuiDragonMageArmor<ContainerAbyssalDragonMageArmor>::new);
        MenuScreens.register(ContainerInit.ARCH_DRAGON_MAGE_CHESTPLATE, GuiDragonMageArmor<ContainerArchDragonMageArmor>::new);
        MenuScreens.register(ContainerInit.INFERNAL_DRAGON_MAGE_CHESTPLATE, GuiDragonMageArmor<ContainerInfernalDragonMageArmor>::new);
        MenuScreens.register(ContainerInit.WILD_DRAGON_MAGE_CHESTPLATE, GuiDragonMageArmor<ContainerWildDragonMageArmor>::new);
        //HUDOverlayRenderer.instance = new HUDOverlayRenderer();
    }
}
