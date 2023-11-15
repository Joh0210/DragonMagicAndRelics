package de.joh.dragonmagicandrelics.spells.shapes;

import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.spells.shapes.ShapeRaytrace;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ShapeTrueTouch extends ShapeRaytrace {
    public ShapeTrueTouch(ResourceLocation registryName, ResourceLocation icon) {
        super(registryName, icon, new AttributeValuePair(Attribute.RANGE, 3.0F, 3.0F, 16.0F, 1.0F, 1.0F), new AttributeValuePair(Attribute.RADIUS, 0.0F, 0.0F, 3.0F, 1.0F));
    }

    public List<SpellTarget> Target(SpellSource source, Level world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition recipe) {
        if (source.getCaster() != null && source.getCaster().isShiftKeyDown()){
            return List.of(new SpellTarget(source.getCaster()));
        }

        List<SpellTarget> targets = super.Target(source, world, modificationData, recipe);
        int radius = (int)Math.floor(modificationData.getValue(Attribute.RADIUS));
        if (radius > 0) {
            SpellTarget tgt = targets.get(0);
            if (tgt != SpellTarget.NONE) {
                if (tgt.isBlock()) {
                    targets = this.targetBlocksRadius(tgt, radius);
                } else if (tgt.isEntity()) {
                    targets = this.targetBlocksEntity(source, tgt, radius, world);
                }
            }
        }

        return targets;
    }

    private List<SpellTarget> targetBlocksRadius(SpellTarget origin, int radius) {
        Direction face = origin.getBlockFace(null);
        BlockPos targetPos = origin.getBlock();
        if (face == null) {
            return List.of(SpellTarget.NONE);
        } else {
            ArrayList<SpellTarget> targets = new ArrayList<>();
            int h;
            int y;
            if (face.getAxis() != Direction.Axis.X && face.getAxis() != Direction.Axis.Z) {
                for(h = -radius; h <= radius; ++h) {
                    for(y = -radius; y <= radius; ++y) {
                        targets.add(new SpellTarget(targetPos.offset(h, 0, y), face));
                    }
                }
            } else {
                for(h = -radius; h <= radius; ++h) {
                    for(y = -1; y <= 2 * radius - 1; ++y) {
                        if (face.getAxis() == Direction.Axis.X) {
                            targets.add(new SpellTarget(targetPos.offset(0, y, h), face));
                        } else {
                            targets.add(new SpellTarget(targetPos.offset(h, y, 0), face));
                        }
                    }
                }
            }

            return targets;
        }
    }

    private List<SpellTarget> targetBlocksEntity(SpellSource source, SpellTarget center, int radius, Level world) {
        return world.getEntities(source.getCaster(), center.getEntity().getBoundingBox().inflate(radius), (entity) -> entity.isPickable() && entity.isAlive() && entity != source.getCaster()).stream().map(SpellTarget::new).collect(Collectors.toList());
    }

    @Override
    public float initialComplexity() {
        return 15;
    }

    @Override
    public int requiredXPForRote() {
        return 500;
    }
}
