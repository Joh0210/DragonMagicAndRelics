package de.joh.dragonmagicandrelics.spells.components;

import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import de.joh.dragonmagicandrelics.config.CommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.BlockWrapper;

import javax.annotation.Nullable;

/**
 * This spell places a liquid, pouring it into a container or cauldron.
 * @author Joh0210
 */
public abstract class IComponentConjureFluid extends SpellEffect {
    /**
     * Liquid the spell places
     */
    private final FluidStack fluidStack;

    public IComponentConjureFluid(final ResourceLocation registryName, final ResourceLocation guiIcon, Fluid fluid, AttributeValuePair... attributeValuePairs) {
        super(registryName, guiIcon, attributeValuePairs);
        this.fluidStack = new FluidStack(fluid, 1000);
    }

    public Fluid getFluid(){
        return fluidStack.getFluid();
    }

    @Override
    public boolean targetsEntities() {
        return false;
    }

    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        Level world = source.getCaster().getCommandSenderWorld();

        if(!target.isBlock()){
            return ComponentApplicationResult.FAIL;
        }

        if(!world.isClientSide){
            Block block = world.getBlockState(target.getBlock()).getBlock();
            if (getCauldronType() != null && (block == Blocks.CAULDRON || block == getCauldronType())){
                world.setBlockAndUpdate(target.getBlock(), getCauldronBlockState());

                return ComponentApplicationResult.SUCCESS;
            }
            IFluidHandler destination = getFluidHandler(world, target.getBlock(), null);
            if(destination != null && this.tryInsertFluid(destination, false)){
                boolean result = this.tryInsertFluid(destination, true);
                if (result) {
                    return ComponentApplicationResult.SUCCESS;
                }
            }
            IFluidHandler destinationSide = getFluidHandler(world, target.getBlock(), target.getBlockFace(null));
            if (destinationSide != null && this.tryInsertFluid(destinationSide, false)) {
                boolean result = this.tryInsertFluid(destinationSide, true);
                if (result) {
                    return ComponentApplicationResult.SUCCESS;
                }
            }

            if (destination == null && destinationSide == null) {
                BlockPos targetPos = target.getBlock().relative(target.getBlockFace(null));
                if (world.getBlockState(targetPos).getBlock() != Blocks.AIR && world.getBlockState(targetPos).getBlock() != Blocks.CAVE_AIR){
                    return ComponentApplicationResult.FAIL;
                }
                if (source.getCaster() instanceof Player player && tryPlaceFluid(player, world, targetPos, modificationData, false)) {
                    return ComponentApplicationResult.SUCCESS;
                }
            }
        }

        return ComponentApplicationResult.FAIL;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }

    /**
     * @return Couldron filled with the special liquid. Null if the block doesn't exist as such.
     */
    @Nullable
    public Block getCauldronType(){
        return null;
    }

    /**
     * @return Couldron with the appropriate Fluid-level. Null if the block doesn't exist as such.
     */
    @Nullable
    public BlockState getCauldronBlockState(){
        return (getCauldronType() != null) ? getCauldronType().defaultBlockState() : null;
    }

    private IFluidHandler getFluidHandler(Level world, BlockPos blockPos, @Nullable Direction side) {
        return FluidUtil.getFluidHandler(world, blockPos, side).orElse(null);
    }

    /**
     * Try adding the liquid to a container.
     * @param destination FluidHandler of the target.
     * @param doTransfer Should the liquid be added?
     * @return Can Liquid be added?
     */
    private boolean tryInsertFluid(IFluidHandler destination, boolean doTransfer) {
        if (destination == null) {
            return false;
        } else {
            return destination.fill(this.fluidStack, doTransfer ? IFluidHandler.FluidAction.EXECUTE : IFluidHandler.FluidAction.SIMULATE) > 0;
        }
    }

    /**
     * Place the liquid in the world.
     * @param ignoreVaporize Should it be possible to place evaporating liquids (e.g. water) in ultra hot dimensions (e.g. nether)?
     * @return Liquid can be placed (even if it evaporates)
     */
    public boolean tryPlaceFluid(Player player, Level world, BlockPos blockPos, IModifiedSpellPart<SpellEffect> modificationData, boolean ignoreVaporize) {
        FluidStack resource = this.fluidStack;
        BlockState state = this.fluidStack.getFluid().getAttributes().getBlock(world, blockPos, this.fluidStack.getFluid().defaultFluidState());
        BlockWrapper wrapper = new BlockWrapper(state, world, blockPos);
        if (world.dimensionType().ultraWarm() && resource.getFluid().getAttributes().doesVaporize(world, blockPos, resource) && !(ignoreVaporize && CommonConfigs.CAN_CONJURE_FLUID_IGNORE_VAPORIZE.get())) {
            resource.getFluid().getAttributes().vaporize(player, world, blockPos, resource);
            return true;
        } else {
            return wrapper.fill(this.fluidStack, IFluidHandler.FluidAction.EXECUTE) > 0;
        }
    }
}
