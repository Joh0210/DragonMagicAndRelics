package de.joh.dmnr.common.spell.shape;

import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import de.joh.dmnr.api.util.MarkSave;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

/**
 * This shape casts the spell at the position marked with the Rune of Marking in the caster's offhand.
 * @author Joh0210
 */
public class AtMarkShape extends Shape {
    public AtMarkShape(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.RADIUS, 0.0F, 0.0F, 3.0F, 1.0F), new AttributeValuePair(Attribute.RANGE, 1.0F, 1.0F, 5.0F, 1.0F, 50.0F));
    }

    @Override
    public List<SpellTarget> Target(SpellSource source, Level world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition iSpellDefinition) {
        if(!world.isClientSide() && source.getCaster() != null){
            MarkSave mark = MarkSave.getMark(source.getCaster(), world);
            if (mark != null && mark.getPosition() != null) {
                double dist = source.getCaster().blockPosition().distSqr(mark.getPosition());
                double maxDist = modificationData.getValue(Attribute.RANGE) * 500.0F;
                if (!(dist > maxDist * maxDist)) {
                    //block-targets in the area
                    List<SpellTarget> targets = new ArrayList<>();
                    targets.add(new SpellTarget(mark.getPosition(), mark.getDirection()));
                    int radius = (int)Math.floor(modificationData.getValue(Attribute.RADIUS));
                    if (radius > 0) {
                        SpellTarget tgt = targets.get(0);
                        if (tgt != SpellTarget.NONE) {
                            if (tgt.isBlock()) {
                                targets = this.targetBlocksRadius(tgt, radius);
                            }
                        }
                    }

                    //entity-targets in the area
                    List<SpellTarget> targetsEntity = world.getEntities((Entity) null, (new AABB(mark.getPosition())).inflate(radius), (entity) -> entity.isPickable() && entity.isAlive() && entity != source.getCaster()).stream().map(SpellTarget::new).toList();
                    targets.addAll(targetsEntity);


                    return targets;
                } else if(source.getPlayer() != null) {
                    source.getPlayer().displayClientMessage(Component.translatable("mna:components/recall.too_far"), true);
                }
            } else {
                if (source.getPlayer() != null) {
                    source.getPlayer().displayClientMessage(Component.translatable("dmnr.shapes.atmark.nomark.error"), true);
                }
            }
        }

        return List.of(SpellTarget.NONE);
    }

    /**
     * calculation if the spell uses a larger radius.
     * @return List of all blocks and in the radius
     */
    private List<SpellTarget> targetBlocksRadius(SpellTarget origin, int radius) {
        Direction face = origin.getBlockFace(null);
        BlockPos targetPos = origin.getBlock();
        if (face == null) {
            return List.of(SpellTarget.NONE);
        } else {
            ArrayList<SpellTarget> targets = new ArrayList<>();
            int x;
            int y;
            int z;
            for(x = -radius; x <= radius; ++x) {
                for(y = -radius; y <= radius; ++y) {
                    for(z = -radius; z <= radius; ++z) {
                        targets.add(new SpellTarget(targetPos.offset(x, y, z), face));
                    }
                }
            }
            return targets;
        }
    }

    @Override
    public float initialComplexity() {
        return 20;
    }

    @Override
    public int requiredXPForRote() {
        return 500;
    }
}
