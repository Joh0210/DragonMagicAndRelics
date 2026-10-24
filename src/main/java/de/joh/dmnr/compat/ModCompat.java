package de.joh.dmnr.compat;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.compat.ae2.AE2Compat;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Optional mods this mod integrates with on the code level.
 * <p>
 * This class must never reference classes of an optional mod directly, because it is always loaded. All such code
 * lives in the {@link CompatModule} of the mod, which is only reached through the doubly wrapped supplier: the
 * lambda {@code () -> AE2Compat::new} is compiled into a method of this class whose body, and with it
 * {@code AE2Compat}, is not resolved before it is executed.
 * <p>
 * Pure data integrations (recipes, tags, loot) do not need an entry here, but their files must also load without the
 * mod. Recipes use the {@code forge:mod_loaded} condition (see the MagiChem recipes), tag entries are marked with
 * {@code "required": false}. Loot tables have no conditions in this Forge version, and an unknown item name in them
 * is logged as an error on every reload, so blocks of an integration drop themselves through a {@code minecraft:tag}
 * entry with an item tag that contains the block optionally (see the loot table of the Altar ME Interface).
 * @author Joh0210
 */
public enum ModCompat {
    /**
     * <a href="https://www.curseforge.com/minecraft/mc-mods/applied-energistics-2">Applied Energistics 2</a>
     */
    AE2("ae2", () -> AE2Compat::new);

    private final String modId;
    private final Supplier<Supplier<CompatModule>> module;

    ModCompat(String modId, Supplier<Supplier<CompatModule>> module) {
        this.modId = modId;
        this.module = module;
    }

    public boolean isLoaded() {
        return ModList.get().isLoaded(modId);
    }

    /**
     * Initializes the {@link CompatModule} of every installed optional mod.
     * Must be called from the constructor of {@link DragonMagicAndRelics}.
     * @param modBus event bus of this mod
     * @param forgeBus {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS}
     */
    public static void init(@NotNull IEventBus modBus, @NotNull IEventBus forgeBus) {
        for (ModCompat compat : values()) {
            if (!compat.isLoaded()) {
                continue;
            }

            DragonMagicAndRelics.LOGGER.info(DragonMagicAndRelics.MOD_ID + ": loading compatibility with " + compat.modId);
            try {
                compat.module.get().get().init(modBus, forgeBus);
            } catch (RuntimeException | LinkageError e) {
                // Fail loudly instead of continuing with a half-initialized integration.
                throw new IllegalStateException(DragonMagicAndRelics.MOD_ID + ": failed to load compatibility with " + compat.modId, e);
            }
        }
    }
}
