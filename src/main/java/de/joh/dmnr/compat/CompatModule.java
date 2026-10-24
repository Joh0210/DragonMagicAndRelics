package de.joh.dmnr.compat;

import net.minecraftforge.eventbus.api.IEventBus;
import org.jetbrains.annotations.NotNull;

/**
 * Entry point of the integration with one optional mod.
 * <p>
 * Implementations may freely reference classes of their mod, because they are only loaded by {@link ModCompat}
 * after it has checked that the mod is installed. Client-only code belongs in a separate class that the
 * implementation calls via {@link net.minecraftforge.fml.DistExecutor}.
 * @author Joh0210
 */
public interface CompatModule {
    /**
     * Called once during the construction of this mod, on both sides.
     * Register DeferredRegisters and event listeners here.
     * @param modBus event bus of this mod
     * @param forgeBus {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS}
     */
    void init(@NotNull IEventBus modBus, @NotNull IEventBus forgeBus);
}
