package de.joh.dmnr.common.spell.shape;

import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.items.ItemInit;
import com.mna.items.ritual.ItemPlayerCharm;
import de.joh.dmnr.common.item.CurseProtectionAmuletItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * This shape casts a spell on the player whose PlayerCharm the caster is holding when they are within range.
 * The player can protect himself with the {@link CurseProtectionAmuletItem}.
 * @see CurseProtectionAmuletItem
 * @author Joh0210
 */
public class CurseShape extends Shape {
    public CurseShape(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.RANGE, 1.0F, 1.0F, 10.0F, 1.0F, 10.0F));
    }

    @Override
    public List<SpellTarget> Target(SpellSource source, Level level, IModifiedSpellPart<Shape> modifiedSpellPart, ISpellDefinition iSpellDefinition) {
        LivingEntity caster = source.getCaster();

        if(caster != null ){
            ItemStack playerCharm = caster.getMainHandItem().getItem() != ItemInit.PLAYER_CHARM.get() ? caster.getOffhandItem() : caster.getMainHandItem();
            if(playerCharm.getItem() instanceof ItemPlayerCharm){
                Player target = (((ItemPlayerCharm)playerCharm.getItem()).GetPlayerTarget(playerCharm, level));
                if(target != null){
                    double dist = source.getCaster().blockPosition().distSqr(target.blockPosition());
                    double maxDist = modifiedSpellPart.getValue(Attribute.RANGE) * 20.0F;
                    if (!(dist > maxDist * maxDist)) {
                        //Mana is not consumed, otherwise the caster can simply spam to be able to cast the spell
                        if(!((CurseProtectionAmuletItem) de.joh.dmnr.common.init.ItemInit.CURSE_PROTECTION_AMULET.get()).isEquippedAndHasMana(target, 120, false)){
                            //todo sound?
                            playerCharm.shrink(1);
                            return List.of(new SpellTarget(target));
                        } else if (source.getPlayer() != null && !level.isClientSide()) {
                            source.getPlayer().displayClientMessage(Component.translatable("dmnr.shapes.curse.protection.error"), true);
                        }
                    } else if (source.getPlayer() != null && !level.isClientSide()) {
                        source.getPlayer().displayClientMessage(Component.translatable("dmnr.shapes.curse.too_far.error"), true);
                    }
                } else if (source.getPlayer() != null && !level.isClientSide()) {
                    source.getPlayer().displayClientMessage(Component.translatable("dmnr.shapes.curse.no_target.error"), true);
                }
            }
            else if (source.getPlayer() != null && !level.isClientSide()) {
                source.getPlayer().displayClientMessage(Component.translatable("dmnr.shapes.curse.no_charm.error"), true);
            }
        }

        return List.of(SpellTarget.NONE);
    }

    @Override
    public float initialComplexity() {
        return 10;
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }
}
