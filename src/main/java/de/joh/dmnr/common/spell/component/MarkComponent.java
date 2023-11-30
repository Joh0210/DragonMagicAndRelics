package de.joh.dmnr.common.spell.component;

import com.mna.api.affinity.Affinity;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.inventory.ItemInventoryBase;
import com.mna.items.ItemInit;
import de.joh.dmnr.capabilities.dragonmagic.PlayerDragonMagicProvider;
import de.joh.dmnr.common.util.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * This spell writes the target's location in the Rune of Marking the caster is holding.
 * <br>If it hits a player, and the caster has a player Charm in ints offhand, it will select this player (can be deactivated through the config)
 * @author Joh0210
 */
public class MarkComponent extends SpellEffect {
    public MarkComponent(final ResourceLocation guiIcon) {
        super(guiIcon);
    }

    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if(source.getCaster() == null){
            return ComponentApplicationResult.FAIL;
        }

        ItemStack markingRune = source.getCaster().getMainHandItem().getItem() != ItemInit.RUNE_MARKING.get() && source.getCaster().getMainHandItem().getItem() != ItemInit.BOOK_MARKS.get() ? source.getCaster().getOffhandItem() : source.getCaster().getMainHandItem();

        if (CommonConfig.MARK_SUPPORT_PLAYERCHARM.get() && markingRune.getItem() == ItemInit.PLAYER_CHARM.get()) {
            if (target.getLivingEntity() instanceof Player playerTarget) {
                ItemInit.PLAYER_CHARM.get().SetPlayerTarget(playerTarget, markingRune);
            }
            else {
                if (source.getPlayer() != null) {
                    source.getPlayer().displayClientMessage(Component.translatable("dmnr:components/alternativerecall.no_player"), true);
                }
                return ComponentApplicationResult.FAIL;
            }
        }
        else if (markingRune.getItem() != ItemInit.RUNE_MARKING.get() && markingRune.getItem() != ItemInit.BOOK_MARKS.get()) {
            //Player specific mark
            if (source.getPlayer() != null) {
                source.getPlayer().getCapability(PlayerDragonMagicProvider.PLAYER_DRAGON_MAGIC).ifPresent(magic -> magic.mark(target.getBlock(), target.getBlockFace(null), context.getWorld()));
            }
            else {
                return ComponentApplicationResult.FAIL;
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
    public static void setPos(ItemStack stack, BlockPos pos, Direction face, Level world) {
        if (stack.getItem() == ItemInit.RUNE_MARKING.get()) {
            ItemInit.RUNE_MARKING.get().setLocation(stack, pos, face, world);
        } else {
            if (stack.getItem() == ItemInit.BOOK_MARKS.get()) {
                int index = ItemInit.BOOK_MARKS.get().getIndex(stack);
                ItemInventoryBase inv = new ItemInventoryBase(stack);
                ItemStack invStack = inv.getStackInSlot(index);
                if (invStack.getItem() == ItemInit.RUNE_MARKING.get()) {
                    ItemInit.RUNE_MARKING.get().setLocation(stack, pos, face, world);
                }
            }
        }
    }

    @Override
    public boolean canBeChanneled() {
        return false;
    }
}
