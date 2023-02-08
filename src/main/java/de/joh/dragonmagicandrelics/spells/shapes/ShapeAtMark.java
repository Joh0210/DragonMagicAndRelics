package de.joh.dragonmagicandrelics.spells.shapes;

import com.ma.api.spells.attributes.Attribute;
import com.ma.api.spells.attributes.AttributeValuePair;
import com.ma.api.spells.base.IModifiedSpellPart;
import com.ma.api.spells.base.ISpellDefinition;
import com.ma.api.spells.parts.Shape;
import com.ma.api.spells.targeting.SpellSource;
import com.ma.api.spells.targeting.SpellTarget;
import de.joh.dragonmagicandrelics.utils.MarkSave;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This shape casts the spell at the position marked with the Rune of Marking in the caster's offhand.
 * @author Joh0210
 */
public class ShapeAtMark extends Shape {
    public ShapeAtMark(ResourceLocation registryName, ResourceLocation guiIcon) {
        super(registryName, guiIcon, new AttributeValuePair[]{new AttributeValuePair(Attribute.RADIUS, 0.0F, 0.0F, 3.0F, 1.0F), new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 5.0F, 1.0F, 50.0F)});
    }

    @Override
    public List<SpellTarget> Target(SpellSource source, World world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition iSpellDefinition) {
        if(!world.isRemote){
            MarkSave mark = MarkSave.getMark(source.getPlayer(), world);
            if (mark != null) {
                double dist = source.getCaster().getPosition().distanceSq(mark.getPosition());
                double maxDist = modificationData.getValue(Attribute.MAGNITUDE) * 500.0F;
                if (!(dist > maxDist * maxDist)) {
                    //block-targets in the area
                    List<SpellTarget> targets = new ArrayList();
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
                    List<SpellTarget> targetsEntity = world.getEntitiesInAABBexcluding(source.getCaster(), (new AxisAlignedBB(mark.getPosition())).grow((double)radius), (entity) -> {
                        return entity.canBeCollidedWith() && entity.isAlive() && entity != source.getCaster();
                    }).stream().map((e) -> {
                        return new SpellTarget(e);
                    }).collect(Collectors.toList());
                    targets.addAll(targetsEntity);


                    return targets;
                } else {
                    source.getPlayer().sendMessage(new TranslationTextComponent("mna:components/recall.too_far"), Util.DUMMY_UUID);
                }
            } else {
                if (source.isPlayerCaster()) {
                    source.getPlayer().sendMessage(new TranslationTextComponent("dragonmagicandrelics.shapes.atmark.nomark.error"), Util.DUMMY_UUID);
                }
            }
        }

        return Collections.singletonList(SpellTarget.NONE);
    }

    /**
     * calculation if the spell uses a larger radius.
     * @return List of all blocks and in the radius
     */
    private List<SpellTarget> targetBlocksRadius(SpellTarget origin, int radius) {
        Direction face = origin.getBlockFace(null);
        BlockPos targetPos = origin.getBlock();
        if (face == null) {
            return Collections.singletonList(SpellTarget.NONE);
        } else {
            ArrayList<SpellTarget> targets = new ArrayList();
            int x;
            int y;
            int z;
            for(x = -radius; x <= radius; ++x) {
                for(y = -radius; y <= radius; ++y) {
                    for(z = -radius; z <= radius; ++z) {
                        targets.add(new SpellTarget(targetPos.add(x, y, z), face));
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
