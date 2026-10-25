package de.joh.dmnr.compat.create.client;

import de.joh.dmnr.compat.create.CreateInit;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Client-only part of the integration with Create.
 * Only loaded on the physical client and only if Create is installed.
 * @author Joh0210
 */
public class CreateClientCompat {
    public static void init(@NotNull IEventBus modBus) {
        SpectralMotorModel.init();
        EngineModel.init();
        modBus.addListener(CreateClientCompat::onRegisterRenderers);
        modBus.addListener(CreateClientCompat::onClientSetup);
    }

    /**
     * Fallback if Flywheel is not available, e.g. with some shader packs.
     */
    private static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CreateInit.SPECTRAL_MOTOR_ENTITY.get(), SpectralMotorRenderer::new);
        event.registerBlockEntityRenderer(CreateInit.ENGINE_ENTITY.get(), EngineRenderer::new);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            SimpleBlockEntityVisualizer.builder(CreateInit.SPECTRAL_MOTOR_ENTITY.get())
                    .factory(SpectralMotorVisual::new)
                    .skipVanillaRender(be -> true)
                    .apply();

            SimpleBlockEntityVisualizer.builder(CreateInit.ENGINE_ENTITY.get())
                    .factory(EngineVisual::new)
                    .skipVanillaRender(be -> true)
                    .apply();
        });
    }
}
