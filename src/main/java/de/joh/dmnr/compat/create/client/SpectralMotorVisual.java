package de.joh.dmnr.compat.create.client;

import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import de.joh.dmnr.compat.create.blockentity.SpectralMotorBlockEntity;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Flywheel visual of the Spectral Motor: the parts of the rotor turn like a Create shaft or at their own speed, and the
 * floating layers also move along the axis every frame. Everything is drawn full bright like the spectral blocks of
 * M&A.
 * @see SpectralMotorModel
 * @see SpectralMotorRenderer
 * @author Joh0210
 */
public class SpectralMotorVisual extends KineticBlockEntityVisual<SpectralMotorBlockEntity> implements SimpleDynamicVisual {
    private final RotatingInstance core;
    /**
     * Instances of {@link SpectralMotorModel#LAYERS}, in the same order.
     */
    private final List<RotatingInstance> layers = new ArrayList<>();

    public SpectralMotorVisual(VisualizationContext context, SpectralMotorBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
        core = createRotor(SpectralMotorModel.CORE, blockEntity.getSpeed());
        for (SpectralMotorModel.Layer layer : SpectralMotorModel.LAYERS) {
            layers.add(createRotor(layer.model(), layer.speed(blockEntity)));
        }
        moveLayers();
    }

    private RotatingInstance createRotor(PartialModel model, float speed) {
        RotatingInstance instance = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(model))
                .createInstance()
                .rotateToFace(Direction.UP, rotationAxis())
                .setup(blockEntity, rotationAxis(), speed)
                .setPosition(getVisualPosition());
        instance.light(LightTexture.FULL_BRIGHT);
        instance.setChanged();
        return instance;
    }

    private void moveLayers() {
        float renderTime = AnimationTickHolder.getRenderTime(level);
        Direction.Axis axis = rotationAxis();
        for (int i = 0; i < layers.size(); i++) {
            float offset = SpectralMotorModel.LAYERS.get(i).offset(renderTime);
            layers.get(i).setPosition(getVisualPosition())
                    .nudge((float) axis.choose(offset, 0, 0), (float) axis.choose(0, offset, 0), (float) axis.choose(0, 0, offset))
                    .setChanged();
        }
    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {
        moveLayers();
    }

    @Override
    public void update(float partialTick) {
        core.setup(blockEntity).setChanged();
        for (int i = 0; i < layers.size(); i++) {
            layers.get(i).setup(blockEntity, rotationAxis(), SpectralMotorModel.LAYERS.get(i).speed(blockEntity)).setChanged();
        }
    }

    @Override
    public void updateLight(float partialTick) {
        for (RotatingInstance instance : instances()) {
            instance.light(LightTexture.FULL_BRIGHT).setChanged();
        }
    }

    @Override
    protected void _delete() {
        for (RotatingInstance instance : instances()) {
            instance.delete();
        }
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        instances().forEach(consumer);
    }

    private List<RotatingInstance> instances() {
        List<RotatingInstance> all = new ArrayList<>(layers);
        all.add(core);
        return all;
    }
}
