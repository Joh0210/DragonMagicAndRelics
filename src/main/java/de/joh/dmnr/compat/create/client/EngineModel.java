package de.joh.dmnr.compat.create.client;

import de.joh.dmnr.common.util.RLoc;
import de.joh.dmnr.compat.create.CreateInit;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Parts of the rotors of the Arcane Engine and the Brimstone Engine, all modelled along the Y axis. The rotor (a hollow
 * cogwheel and the caps of the shaft) turns with the network like a Create cogwheel. The crystal in its middle, like
 * the one of the Eldrin Matrix of M&A, floats up and down along the axis and slowly spins on its own, independent of
 * the speed of the network.
 * <p>
 * Flywheel loads every partial model that exists before the models are baked, and only keeps weak references to them,
 * so they are constants, and {@link #init()} has to be called during the construction of the mod.
 * @author Joh0210
 */
public class EngineModel {
    /**
     * Speed of the crystal in RPM: a quarter of the default speed of the Arcane Engine, whatever the network does.
     */
    public static final float CRYSTAL_SPEED = 4.0F;
    /**
     * Duration of one up and down movement of the crystal in ticks.
     */
    private static final float PERIOD = 120.0F;
    /**
     * How far the crystal moves away from the centre of the block, in pixels.
     */
    private static final float AMPLITUDE = 0.5F;

    /**
     * @param rotor turns with the network
     * @param crystal floats in the middle of the rotor
     * @param glowingCrystal whether the crystal is drawn full bright, regardless of the light around it
     */
    public record Parts(PartialModel rotor, PartialModel crystal, boolean glowingCrystal) {
    }

    private static final Parts ARCANE_ENGINE = new Parts(part("arcane_engine/rotor"), part("arcane_engine/crystal"), false);
    /**
     * The same model as the Arcane Engine, black and gold with a crystal of lava, which glows like lava.
     */
    private static final Parts BRIMSTONE_ENGINE = new Parts(part("brimstone_engine/rotor"), part("brimstone_engine/crystal"), true);

    /**
     * @param state state of an engine
     * @return the parts of its rotor
     */
    public static Parts of(BlockState state) {
        return state.is(CreateInit.BRIMSTONE_ENGINE.get()) ? BRIMSTONE_ENGINE : ARCANE_ENGINE;
    }

    /**
     * @param renderTime render time of the level in ticks
     * @return current position of the crystal along the axis, relative to the centre of the block, in blocks
     */
    public static float crystalOffset(float renderTime) {
        return AMPLITUDE * Mth.sin(renderTime / PERIOD * Mth.TWO_PI) / 16.0F;
    }

    private static PartialModel part(String name) {
        return PartialModel.of(RLoc.create("block/" + name));
    }

    /**
     * Loads this class, and with it the partial models.
     */
    public static void init() {
    }
}
