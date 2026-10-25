package de.joh.dmnr.compat.create.client;

import de.joh.dmnr.common.util.RLoc;
import de.joh.dmnr.compat.create.blockentity.SpectralMotorBlockEntity;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Parts of the rotor of the Spectral Motor, all modelled along the Y axis. The core (connections to the neighbouring
 * shafts, gear and floating shaft) stays in place and turns with the shaft, so it meshes with neighbouring cogwheels.
 * The layers in between float up and down along the axis like the spectral blocks of M&A, and the frames among them
 * also spin on their own, independent of the speed of the motor.
 * <p>
 * Flywheel loads every partial model that exists before the models are baked, and only keeps weak references to them,
 * so they are constants, and {@link #init()} has to be called during the construction of the mod.
 * @author Joh0210
 */
public class SpectralMotorModel {
    public static final PartialModel CORE = part("core");
    private static final PartialModel FLOATING_FRAME = part("floating_frame");
    private static final PartialModel SHAFT_SQUARE = part("shaft_square");
    private static final PartialModel SHAFT_FRAME = part("shaft_frame");

    /**
     * Duration of one up and down movement in ticks, like the spectral blocks of M&A.
     */
    private static final float PERIOD = 120.0F;
    /**
     * How far the layers move away from their resting position, in pixels.
     */
    private static final float AMPLITUDE = 0.5F;
    /**
     * Speed of the frames that spin on their own, in RPM: half a turn per {@link #PERIOD}, like the spectral blocks
     * of M&A.
     */
    private static final float SPIN = 5.0F;

    /**
     * A floating layer. Its model lies in the centre of the block and is moved to its position along the axis.
     * @param model model of the layer
     * @param position resting position relative to the centre of the block, in pixels
     * @param phase offset of the movement, as a fraction of {@link #PERIOD}
     * @param spin own speed in RPM, relative to the direction of rotation of the motor (negative = against it), or
     *             null if the layer turns with the shaft
     */
    public record Layer(PartialModel model, float position, float phase, @Nullable Float spin) {
        /**
         * @param renderTime render time of the level in ticks
         * @return current position relative to the centre of the block, in blocks
         */
        public float offset(float renderTime) {
            float movement = AMPLITUDE * Mth.sin((renderTime / PERIOD + phase) * Mth.TWO_PI);
            // Both halves move mirrored, so the rotor seems to breathe.
            return (position + Math.signum(position) * movement) / 16.0F;
        }

        /**
         * @return current speed of the layer in RPM
         */
        public float speed(SpectralMotorBlockEntity motor) {
            if (spin == null) {
                return motor.getSpeed();
            }
            // The direction of the motor, not its current speed, so the frames also spin when the network is overstressed.
            return Math.signum(motor.getGeneratedSpeed()) * spin;
        }
    }

    /**
     * From the centre outwards, every layer follows the previous one a little later. The floating frames spin against
     * the shaft, the frames of the shaft core with it.
     */
    public static final List<Layer> LAYERS = List.of(
            new Layer(FLOATING_FRAME, -2.5F, 0.0F, -SPIN),
            new Layer(FLOATING_FRAME, 2.5F, 0.0F, -SPIN),
            new Layer(SHAFT_SQUARE, -4.0F, 1.0F / 6.0F, null),
            new Layer(SHAFT_SQUARE, 4.0F, 1.0F / 6.0F, null),
            new Layer(SHAFT_FRAME, -5.5F, 2.0F / 6.0F, SPIN),
            new Layer(SHAFT_FRAME, 5.5F, 2.0F / 6.0F, SPIN));

    private static PartialModel part(String name) {
        return PartialModel.of(RLoc.create("block/spectral_motor/" + name));
    }

    /**
     * Loads this class, and with it the partial models.
     */
    public static void init() {
    }
}
