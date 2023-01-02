package de.joh.dragonmagicandrelics.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.Faction;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.items.ItemInit;
import com.mna.items.runes.MarkBookItem;
import com.mna.tools.TeleportHelper;
import de.joh.dragonmagicandrelics.utils.MarkSave;
import de.joh.dragonmagicandrelics.capabilities.dragonmagic.PlayerDragonMagicProvider;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ComponentAlternativeRecall extends SpellEffect {
    public ComponentAlternativeRecall(ResourceLocation registryName, ResourceLocation guiIcon) {
        super(registryName, guiIcon, new AttributeValuePair[]{new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 5.0F, 1.0F, 50.0F)});
    }

    public int requiredXPForRote() {
        return 200;
    }

    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (source.hasCasterReference() && target.isEntity() && target.getEntity().isAlive() && target.getEntity().canChangeDimensions()) {
            MarkSave markSave = MarkSave.getMark(source.getPlayer(), source.getCaster().getCommandSenderWorld());
            if (markSave != null) {
                BlockPos pos = markSave.getPosition();
                double dist = target.getEntity().blockPosition().distSqr(pos);
                double maxDist = (double)(modificationData.getValue(Attribute.MAGNITUDE) * 1000.0F);
                if (!(dist > maxDist * maxDist)) {
                    int magnitude = (int)modificationData.getValue(Attribute.MAGNITUDE);
                    if (this.magnitudeHealthCheck(source, target, magnitude, 20)) {
                        TeleportHelper.teleportEntity(target.getEntity(), context.getWorld().dimension(), new Vec3((double)pos.getX() + 0.5, (double)(pos.getY() + 1), (double)pos.getZ() + 0.5));
                        return ComponentApplicationResult.SUCCESS;
                    }

                    return ComponentApplicationResult.FAIL;
                }

                if (source.isPlayerCaster()) {
                    source.getPlayer().sendMessage(new TranslatableComponent("mna:components/recall.too_far"), Util.NIL_UUID);
                }
            } else {
                if (source.isPlayerCaster()) {
                    source.getPlayer().sendMessage(new TranslatableComponent("dragonmagicandrelics.shapes.atmark.nomark.error"), Util.NIL_UUID);
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