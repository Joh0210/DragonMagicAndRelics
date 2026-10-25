package de.joh.dmnr.compat.create.client;

import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import de.joh.dmnr.compat.create.blockentity.EngineBlockEntity;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.Direction;

import java.util.function.Consumer;

/**
 * Flywheel visual of the Arcane Engine and the Brimstone Engine: the rotor turns like a Create cogwheel, the crystal
 * spins at its own speed and also moves along the axis every frame.
 * @see EngineModel
 * @see EngineRenderer
 * @author Joh0210
 */
public class EngineVisual extends KineticBlockEntityVisual<EngineBlockEntity> implements SimpleDynamicVisual {
    private final RotatingInstance rotor;
    private final RotatingInstance crystal;
    private final boolean glowingCrystal;

    public EngineVisual(VisualizationContext context, EngineBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
        EngineModel.Parts parts = EngineModel.of(blockState);
        rotor = createRotor(parts.rotor(), blockEntity.getSpeed());
        crystal = createRotor(parts.crystal(), EngineModel.CRYSTAL_SPEED);
        glowingCrystal = parts.glowingCrystal();
        moveCrystal();
    }

    private RotatingInstance createRotor(PartialModel model, float speed) {
        RotatingInstance instance = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(model))
                .createInstance()
                .rotateToFace(Direction.UP, rotationAxis())
                .setup(blockEntity, rotationAxis(), speed)
                .setPosition(getVisualPosition());
        instance.setChanged();
        return instance;
    }

    private void moveCrystal() {
        float offset = EngineModel.crystalOffset(AnimationTickHolder.getRenderTime(level));
        Direction.Axis axis = rotationAxis();
        crystal.setPosition(getVisualPosition())
                .nudge((float) axis.choose(offset, 0, 0), (float) axis.choose(0, offset, 0), (float) axis.choose(0, 0, offset))
                .setChanged();
    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {
        moveCrystal();
    }

    /**
     * Only the rotor follows the network, the crystal keeps its own speed.
     */
    @Override
    public void update(float partialTick) {
        rotor.setup(blockEntity).setChanged();
    }

    @Override
    public void updateLight(float partialTick) {
        if (glowingCrystal) {
            relight(rotor);
            crystal.light(LightTexture.FULL_BRIGHT).setChanged();
        } else {
            relight(rotor, crystal);
        }
    }

    @Override
    protected void _delete() {
        rotor.delete();
        crystal.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(rotor);
        consumer.accept(crystal);
    }
}
