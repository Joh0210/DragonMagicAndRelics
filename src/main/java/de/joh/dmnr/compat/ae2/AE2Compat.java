package de.joh.dmnr.compat.ae2;

import com.mna.api.guidebook.RegisterGuidebooksEvent;
import de.joh.dmnr.common.init.CreativeModeTabInit;
import de.joh.dmnr.common.util.RLoc;
import de.joh.dmnr.compat.CompatModule;
import de.joh.dmnr.compat.ae2.client.AE2ClientCompat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;

/**
 * Integration with Applied Energistics 2. Only loaded if AE2 is installed, so classes of AE2
 * ({@code appeng.*}) may be referenced here and in the classes of this package.
 * <p>
 * Adds the {@link de.joh.dmnr.compat.ae2.block.AltarInterfaceBlock}, which supplies an Altar of Arcana from the ME network.
 * @author Joh0210
 */
public class AE2Compat implements CompatModule {
    @Override
    public void init(@NotNull IEventBus modBus, @NotNull IEventBus forgeBus) {
        AE2Init.register(modBus);
        modBus.addListener(AE2Compat::onBuildCreativeModeTabContents);
        forgeBus.addListener(AE2Compat::onRegisterGuidebooks);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> AE2ClientCompat.init(modBus));
    }

    /**
     * The entries about the content of this integration, which only exist with AE2, are kept apart from the base
     * guide ({@code assets/dmnr/guide/ae2}).
     */
    private static void onRegisterGuidebooks(RegisterGuidebooksEvent event) {
        event.getRegistry().addGuidebookPath(RLoc.create("guide/ae2"));
    }

    private static void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabInit.DMNR_TAB.getKey())) {
            event.accept(AE2Init.ALTAR_INTERFACE_ITEM);
        }
    }
}
