package de.joh.dragonmagicandrelics.spells.components;

import com.ma.api.affinity.Affinity;
import com.ma.api.spells.SpellPartTags;
import com.ma.api.spells.attributes.Attribute;
import com.ma.api.spells.attributes.AttributeValuePair;
import com.ma.api.spells.base.IModifiedSpellPart;
import com.ma.api.spells.parts.Component;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Water version of IComponentConjureFluid.
 * With a larger magnitude, the water can also be placed in ultra hot dimensions (e.g. Nether).
 * @author Joh0210
 */
public class ComponentConjureWater extends IComponentConjureFluid {

    public ComponentConjureWater(final ResourceLocation registryName, final ResourceLocation guiIcon) {
        super(registryName, guiIcon, Fluids.WATER, new AttributeValuePair[]{new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 2.0F, 1.0F, 225.0F)});
    }

    @Override
    public SoundEvent SoundEffect() {
        return SoundEvents.ENTITY_PLAYER_SPLASH;
    }

    public Affinity getAffinity() {
        return Affinity.WATER;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }

    public float initialComplexity() {
        return 25.0f;
    }

    public int requiredXPForRote() {
        return 250;
    }

    /**
     * Allow water to be placed in Nether when magnitude has been increased.
     */
    @Override
    public boolean tryPlaceSigilFluid(PlayerEntity player, World world, BlockPos blockPos, IModifiedSpellPart<Component> modificationData, boolean ignoreVaporize) {
        return super.tryPlaceSigilFluid(player, world, blockPos, modificationData, 1.5F <= (modificationData.getValue(Attribute.MAGNITUDE)));
    }
}
