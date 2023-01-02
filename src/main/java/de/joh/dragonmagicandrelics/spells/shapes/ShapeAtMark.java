package de.joh.dragonmagicandrelics.spells.shapes;

import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.items.ItemInit;
import com.mna.items.runes.MarkBookItem;
import de.joh.dragonmagicandrelics.utils.MarkSave;
import de.joh.dragonmagicandrelics.capabilities.dragonmagic.PlayerDragonMagicProvider;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
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
    public List<SpellTarget> Target(SpellSource source, Level world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition iSpellDefinition) {
        if(!world.isClientSide()){
            MarkSave mark = MarkSave.getMark(source.getPlayer(), world);
            if (mark != null) {
                double dist = source.getCaster().blockPosition().distSqr(mark.getPosition());
                double maxDist = (double)(modificationData.getValue(Attribute.MAGNITUDE) * 500.0F);
                if (!(dist > maxDist * maxDist)) {
                    //block-targets in the area
                    List<SpellTarget> targets = new ArrayList();
                    targets.add(new SpellTarget(mark.getPosition(), mark.getDirection()));
                    int radius = (int)Math.floor((double)modificationData.getValue(Attribute.RADIUS));
                    if (radius > 0) {
                        SpellTarget tgt = (SpellTarget)targets.get(0);
                        if (tgt != SpellTarget.NONE) {
                            if (tgt.isBlock()) {
                                targets = this.targetBlocksRadius(tgt, radius);
                            }
                        }
                    }

                    //entity-targets in the area
                    List<SpellTarget> targetsEntity = world.getEntities((Entity) null, (new AABB(mark.getPosition())).inflate((double)radius), (entity) -> {
                        return entity.isPickable() && entity.isAlive() && entity != source.getCaster();
                    }).stream().map((e) -> {
                        return new SpellTarget(e);
                    }).collect(Collectors.toList());
                    targets.addAll(targetsEntity);


                    return targets;
                } else {
                    source.getPlayer().sendMessage(new TranslatableComponent("mna:components/recall.too_far"), Util.NIL_UUID);
                }
            } else {
                if (source.isPlayerCaster()) {
                    source.getPlayer().sendMessage(new TranslatableComponent("dragonmagicandrelics.shapes.atmark.nomark.error"), Util.NIL_UUID);
                }
            }
        }

        return Arrays.asList(SpellTarget.NONE);
    }

    /**
     * calculation if the spell uses a larger radius.
     * @return List of all blocks and in the radius
     */
    private List<SpellTarget> targetBlocksRadius(SpellTarget origin, int radius) {
        Direction face = origin.getBlockFace((SpellEffect)null);
        BlockPos targetPos = origin.getBlock();
        if (face == null) {
            return Arrays.asList(SpellTarget.NONE);
        } else {
            ArrayList<SpellTarget> targets = new ArrayList();
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
