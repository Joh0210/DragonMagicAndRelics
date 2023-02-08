package de.joh.dragonmagicandrelics.spells.components;

import com.ma.api.affinity.Affinity;
import com.ma.api.spells.ComponentApplicationResult;
import com.ma.api.spells.SpellPartTags;
import com.ma.api.spells.attributes.AttributeValuePair;
import com.ma.api.spells.base.IModifiedSpellPart;
import com.ma.api.spells.parts.Component;
import com.ma.api.spells.targeting.SpellContext;
import com.ma.api.spells.targeting.SpellSource;
import com.ma.api.spells.targeting.SpellTarget;
import com.ma.items.ItemInit;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This spell writes the target's location in the Rune of Marking the caster is holding.
 * todo: Insert Capabilities
 * @author Joh0210
 */
public class ComponentMark extends Component {
    public ComponentMark(final ResourceLocation registryName, final ResourceLocation guiIcon) {
        super(registryName, guiIcon, new AttributeValuePair[0]);
    }

    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<Component> modificationData, SpellContext context) {
        ItemStack markingRune = source.getCaster().getHeldItemMainhand().getItem() == ItemInit.RUNE_MARKING.get() ? source.getCaster().getHeldItemMainhand() : source.getCaster().getHeldItemOffhand();
        if (markingRune.getItem() != ItemInit.RUNE_MARKING.get()) {
            //Player specific mark
            if (source.isPlayerCaster()) {
                //todo: Insert Capabilities
                //source.getPlayer().getCapability(PlayerDragonMagicProvider.PLAYER_DRAGON_MAGIC).ifPresent(magic -> magic.mark(target.getBlock(), target.getBlockFace(null), context.getWorld()));
            }
        }
        else{
            setPos(markingRune, target.getBlock(), target.getBlockFace(null), context.getWorld());
        }

        return ComponentApplicationResult.SUCCESS;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ARCANE;
    }

    @Override
    public float initialComplexity() {
        return 10;
    }

    @Override
    public int requiredXPForRote() {
        return 100;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }

    /**
     * @param stack Rune on which the position must be written.
     * @param pos position of the target.
     * @param face Directorate from which the spell hits.
     * @param world The world of magic.
     */
    public static void setPos(ItemStack stack, BlockPos pos, Direction face, World world) {
        if (stack.getItem() == ItemInit.RUNE_MARKING.get()) {
            ItemInit.RUNE_MARKING.get().setLocation(stack, pos, face, world);
        }
    }
}
