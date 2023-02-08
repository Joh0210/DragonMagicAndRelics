package de.joh.dragonmagicandrelics.spells.components;

import com.ma.api.affinity.Affinity;
import com.ma.api.capabilities.Faction;
import com.ma.api.spells.ComponentApplicationResult;
import com.ma.api.spells.SpellPartTags;
import com.ma.api.spells.attributes.Attribute;
import com.ma.api.spells.attributes.AttributeValuePair;
import com.ma.api.spells.base.IModifiedSpellPart;
import com.ma.api.spells.parts.Component;
import com.ma.api.spells.targeting.SpellContext;
import com.ma.api.spells.targeting.SpellSource;
import com.ma.api.spells.targeting.SpellTarget;
import com.ma.tools.TeleportHelper;
import de.joh.dragonmagicandrelics.utils.MarkSave;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;

public class ComponentAlternativeRecall extends Component {
    public ComponentAlternativeRecall(ResourceLocation registryName, ResourceLocation guiIcon) {
        super(registryName, guiIcon, new AttributeValuePair[]{new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 5.0F, 1.0F, 50.0F)});
    }

    public int requiredXPForRote() {
        return 200;
    }

    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<Component> modificationData, SpellContext context) {
        if (source.hasCasterReference() && target.isEntity() && target.getEntity().isAlive() && target.getEntity().canChangeDimension()) {
            MarkSave markSave = MarkSave.getMark(source.getPlayer(), source.getCaster().getEntityWorld());
            if (markSave != null) {
                BlockPos pos = markSave.getPosition();
                double dist = target.getEntity().getPosition().distanceSq(pos);
                double maxDist = modificationData.getValue(Attribute.MAGNITUDE) * 1000.0F;
                if (!(dist > maxDist * maxDist)) {
                    int magnitude = (int)modificationData.getValue(Attribute.MAGNITUDE);
                    if (this.magnitudeHealthCheck(source, target, magnitude, 20)) {
                        TeleportHelper.teleportEntity(target.getEntity(), context.getWorld().getDimensionKey(), new Vector3d((double)pos.getX() + 0.5, pos.getY() + 1, (double)pos.getZ() + 0.5));
                        return ComponentApplicationResult.SUCCESS;
                    }

                    return ComponentApplicationResult.FAIL;
                }

                if (source.isPlayerCaster()) {
                    source.getPlayer().sendMessage(new TranslationTextComponent("mna:components/recall.too_far"), Util.DUMMY_UUID);
                }
            } else {
                if (source.isPlayerCaster()) {
                    source.getPlayer().sendMessage(new TranslationTextComponent("dragonmagicandrelics.shapes.atmark.nomark.error"), Util.DUMMY_UUID);
                }
            }
        }


        return ComponentApplicationResult.FAIL;
    }



    @Override
    public boolean targetsBlocks() {
        return false;
    }

    public Affinity getAffinity() {
        return Affinity.ARCANE;
    }

    @Override
    public Faction getFactionRequirement() {
        return Faction.ANCIENT_WIZARDS;
    }

    public float initialComplexity() {
        return 50.0F;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.FRIENDLY;
    }
}