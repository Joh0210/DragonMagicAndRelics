package de.joh.dmnr.compat.ae2.client;

import appeng.client.gui.style.StyleManager;
import de.joh.dmnr.compat.ae2.AE2Init;
import de.joh.dmnr.compat.ae2.menu.AltarInterfaceMenu;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Client-only part of the integration with Applied Energistics 2.
 * Only loaded on the physical client and only if AE2 is installed.
 * @author Joh0210
 */
public class AE2ClientCompat {
    public static void init(@NotNull IEventBus modBus) {
        modBus.addListener(AE2ClientCompat::onClientSetup);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(AE2Init.ALTAR_INTERFACE_MENU.get(), AE2ClientCompat::createAltarInterfaceScreen));
    }

    private static AltarInterfaceScreen createAltarInterfaceScreen(AltarInterfaceMenu menu, Inventory inventory, Component title) {
        return new AltarInterfaceScreen(menu, inventory, title, StyleManager.loadStyleDoc(AltarInterfaceScreen.STYLE));
    }
}
