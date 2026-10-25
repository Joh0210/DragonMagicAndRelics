package de.joh.dmnr.compat.create;

import com.mna.api.guidebook.RegisterGuidebooksEvent;
import de.joh.dmnr.common.util.RLoc;
import de.joh.dmnr.compat.CompatModule;
import de.joh.dmnr.compat.create.client.CreateClientCompat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;

/**
 * Integration with Create. Only loaded if Create is installed, so classes of Create
 * ({@code com.simibubi.create.*}) and of its libraries Catnip and Flywheel may be referenced here and in the classes
 * of this package.
 * <p>
 * Adds the spell component {@link de.joh.dmnr.compat.create.spell.SpectralMotorComponent}, which conjures a temporary
 * {@link de.joh.dmnr.compat.create.block.SpectralMotorBlock}.
 * @author Joh0210
 */
public class CreateCompat implements CompatModule {
    @Override
    public void init(@NotNull IEventBus modBus, @NotNull IEventBus forgeBus) {
        CreateInit.register(modBus);
        forgeBus.addListener(CreateCompat::onRegisterGuidebooks);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> CreateClientCompat.init(modBus));
    }

    /**
     * The entries about the content of this integration, which only exist with Create, are kept apart from the base
     * guide ({@code assets/dmnr/guide/create}).
     */
    private static void onRegisterGuidebooks(RegisterGuidebooksEvent event) {
        event.getRegistry().addGuidebookPath(RLoc.create("guide/create"));
    }
}
