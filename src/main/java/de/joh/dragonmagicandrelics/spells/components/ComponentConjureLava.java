package de.joh.dragonmagicandrelics.spells.components;

import com.ma.api.affinity.Affinity;
import com.ma.api.spells.attributes.AttributeValuePair;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

/**
 * Lava version of IComponentConjureFluid.
 * @author Joh0210
 */
public class ComponentConjureLava extends IComponentConjureFluid {

    public ComponentConjureLava(final ResourceLocation registryName, final ResourceLocation guiIcon) {
        super(registryName, guiIcon, Fluids.LAVA, new AttributeValuePair[0]);
    }

    @Override
    public SoundEvent SoundEffect() {
        return SoundEvents.ITEM_BUCKET_EMPTY_LAVA;
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
}
