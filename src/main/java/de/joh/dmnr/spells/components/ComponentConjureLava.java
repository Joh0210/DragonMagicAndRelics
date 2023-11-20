package de.joh.dmnr.spells.components;

import com.mna.api.affinity.Affinity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;

/**
 * Lava version of IComponentConjureFluid.
 * @author Joh0210
 */
public class ComponentConjureLava extends IComponentConjureFluid {

    public ComponentConjureLava(final ResourceLocation registryName, final ResourceLocation guiIcon) {
        super(registryName, guiIcon, Fluids.LAVA);
    }

    @Override
    public SoundEvent SoundEffect() {
        return SoundEvents.BUCKET_EMPTY_LAVA;
    }

    public Affinity getAffinity() {
        return Affinity.FIRE;
    }

    public float initialComplexity() {
        return 250.0f;
    }

    public int requiredXPForRote() {
        return 500;
    }

    @Override
    @Nullable
    public Block getCauldronType(){
        return Blocks.LAVA_CAULDRON;
    }

    @Override
    public boolean canBeOnRandomStaff() {
        return false;
    }
}
