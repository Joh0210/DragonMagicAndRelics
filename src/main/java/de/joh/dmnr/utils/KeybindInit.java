package de.joh.dmnr.utils;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
    public static void init(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_NIGHT_VISION_KEY);
        event.register(TOGGLE_FLIGHT_KEY);
    }
}