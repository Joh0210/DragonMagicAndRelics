package de.joh.dragonmagicandrelics.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;

/**
 * Water version of IComponentConjureFluid.
 * With a larger magnitude, the water can also be placed in ultra hot dimensions (e.g. Nether).
 * @author Joh0210
 */
public class ComponentConjureWater extends IComponentConjureFluid {

    public ComponentConjureWater(final ResourceLocation registryName, final ResourceLocation guiIcon) {
        super(registryName, guiIcon, Fluids.WATER, new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 2.0F, 1.0F, 225.0F));
    }

    @Override
    public SoundEvent SoundEffect() {
        return SoundEvents.PLAYER_SPLASH;
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

    @Override
    @Nullable
    public Block getCauldronType(){
        return Blocks.WATER_CAULDRON;
    }

    @Override
    @Nullable
    public BlockState getCauldronBlockState(){
        return getCauldronType().defaultBlockState().setValue(BlockStateProperties.LEVEL_CAULDRON, 3);
    }

    /**
     * Allow water to be placed in Nether when magnitude has been increased.
     */
    @Override
    public boolean tryPlaceFluid(Player player, Level world, BlockPos blockPos, IModifiedSpellPart<SpellEffect> modificationData, boolean ignoreVaporize) {
        return super.tryPlaceFluid(player, world, blockPos, modificationData, 1.5F <= (modificationData.getValue(Attribute.MAGNITUDE)));
    }
}
