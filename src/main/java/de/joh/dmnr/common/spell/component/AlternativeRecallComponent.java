package de.joh.dmnr.common.spell.component;

import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.factions.Factions;
import com.mna.tools.TeleportHelper;
import de.joh.dmnr.api.util.MarkSave;
import de.joh.dmnr.common.util.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

/**
 * Revision of M&As recall
 * @see com.mna.spells.components.ComponentRecall
 * @author Joh0210
 */
public class AlternativeRecallComponent extends SpellEffect {
    public AlternativeRecallComponent(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.RANGE, 1.0F, 1.0F, 5.0F, 1.0F, 25.0F));
    }

    public int requiredXPForRote() {
        return 200;
    }

    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (target.getEntity() != null && target.getEntity().isAlive() && source.getPlayer() != null && target.getEntity().canChangeDimensions()) {
            MarkSave markSave = MarkSave.getMark(source.getPlayer(), source.getPlayer().getCommandSenderWorld(), CommonConfig.RECALL_SUPPORT_PLAYERCHARM.get());
            if (markSave != null && markSave.getPosition() != null) {
                BlockPos pos = markSave.getPosition();

                double dist = target.getEntity().blockPosition().distSqr(pos);
                double maxDist = modificationData.getValue(Attribute.RANGE) * 1000.0F;
                if (!(dist > maxDist * maxDist)) {
                    int magnitude = (int)modificationData.getValue(Attribute.RANGE);
                    if (this.magnitudeHealthCheck(source, target, magnitude, 20)) {
                        TeleportHelper.teleportEntity(target.getEntity(), context.getLevel().dimension(), new Vec3((double)pos.getX() + 0.5, pos.getY() + 1, (double)pos.getZ() + 0.5));
                        return ComponentApplicationResult.SUCCESS;
                    }

                    return ComponentApplicationResult.FAIL;
                }

                if (source.getPlayer() != null) {
                    source.getPlayer().displayClientMessage(Component.translatable("mna:components/recall.too_far"), true);
                }
            } else {
                if (source.getPlayer() != null) {
                    source.getPlayer().displayClientMessage(Component.translatable("dmnr.shapes.atmark.nomark.error"),true);
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
    public IFaction getFactionRequirement() {
        return Factions.COUNCIL;
    }

    public float initialComplexity() {
        return 50.0F;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.FRIENDLY;
    }
}