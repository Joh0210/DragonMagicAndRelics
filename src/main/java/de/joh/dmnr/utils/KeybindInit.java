package de.joh.dmnr.utils;

import de.joh.dmnr.DragonMagicAndRelics;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

/**
 * Client-side initialization of the keybindings.
 * @author Joh0210
 */
public class KeybindInit {
    public KeybindInit() {
    }

    public static final KeyMapping TOGGLE_NIGHT_VISION_KEY = new KeyMapping("dmnr.key.togglenightvision", GLFW.GLFW_KEY_O, "key.categories.mna");

    public static final KeyMapping TOGGLE_FLIGHT_KEY = new KeyMapping("dmnr.key.toggleflight", GLFW.GLFW_KEY_Y, "key.categories.mna");

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(TOGGLE_NIGHT_VISION_KEY);
        ClientRegistry.registerKeyBinding(TOGGLE_FLIGHT_KEY);
        DragonMagicAndRelics.LOGGER.info("DM&R -> Keybindings Registered");
    }
}