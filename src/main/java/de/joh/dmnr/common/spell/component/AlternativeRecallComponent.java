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
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedEvent;
import com.mna.factions.Factions;
import com.mna.items.ritual.WorldCharm;
import com.mna.tools.TeleportHelper;
import de.joh.dmnr.api.util.MarkSave;
import de.joh.dmnr.capabilities.dragonmagic.PlayerDragonMagicProvider;
import de.joh.dmnr.common.util.CommonConfig;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Revision of M&As recall
 * @see com.mna.spells.components.ComponentRecall
 * @author Joh0210
 */
public class AlternativeRecallComponent extends SpellEffect {
    public AlternativeRecallComponent(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.RANGE, 1.0F, 1.0F, 5.0F, 1.0F, 25.0F), new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 5.0F, 1.0F, 10.0F), new AttributeValuePair(Attribute.PRECISION, 0.0F, 0.0F, 1.0F, 10.0F));
    }

    public int requiredXPForRote() {
        return 200;
    }

    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        Vec3 targetPosition = null;
        // Blink
        if (modificationData.getValue(Attribute.PRECISION) == 1.0F && target.getLivingEntity() != source.getCaster() && source.getCaster() != null && context.getServerLevel() != null){
            // to Entity
            if (target.getLivingEntity() != null) {
                float step = 1.5F;
                Vec3 delta = target.getLivingEntity().getForward().normalize().scale(step);
                int tries = 0;

                do {
                    ++tries;
                    targetPosition = target.getLivingEntity().position().subtract(delta);
                    if (targetPosition.distanceTo(source.getCaster().position()) < (double)step) {
                        source.getCaster().sendSystemMessage(Component.translatable("mna:components/blink.failed"));
                        return ComponentApplicationResult.FAIL;
                    }
                } while(tries <= 10 && !TeleportHelper.coordsValidForBlink(context.getServerLevel(), (int)targetPosition.x(), (int)targetPosition.y(), (int)targetPosition.z()));

                if(source.getPlayer() != null) {
                    Vec3 targetEye = target.getLivingEntity().getEyePosition();
                    Player looker = source.getPlayer();
                    DelayedEventQueue.pushEvent(context.getLevel(), new TimedDelayedEvent<>("look", 1, targetEye, (k, v) -> looker.lookAt(EntityAnchorArgument.Anchor.EYES, targetEye)));
                }

            }
            // to Block
            else {
                if (!target.isBlock()) {
                    return ComponentApplicationResult.FAIL;
                }

                BlockPos check = target.getBlock().offset(target.getBlockFace(this).getNormal());
                if (TeleportHelper.coordsValidForBlink(context.getServerLevel(), check.getX(), check.getY(), check.getZ())) {
                    targetPosition = Vec3.atBottomCenterOf(check);
                }
            }

            if (!this.isValidDistance(targetPosition, source.getCaster(), modificationData)) {
                if (source.getPlayer() != null) {
                    source.getPlayer().sendSystemMessage(Component.translatable("mna:components/blink.toofar"));
                }
                return ComponentApplicationResult.FAIL;
            }

            if (!context.getServerLevel().isClientSide()) {
                EntityTeleportEvent tpEvent = new EntityTeleportEvent(source.getCaster(), targetPosition.x, targetPosition.y, targetPosition.z);
                if (!MinecraftForge.EVENT_BUS.post(tpEvent)) {
                    source.getCaster().teleportTo(targetPosition.x, targetPosition.y, targetPosition.z);
                }

                return ComponentApplicationResult.SUCCESS;
            } else {
                return ComponentApplicationResult.FAIL;
            }
        }

        // TP
        else if (target.getLivingEntity() != null && source.getCaster() != null && target.getLivingEntity().canChangeDimensions()) {
            // Used WorldCharm
            ItemStack worldCharm = source.getCaster().getMainHandItem().getItem() instanceof WorldCharm ? source.getCaster().getMainHandItem() : source.getCaster().getOffhandItem();

            if (source.getCaster() != null && worldCharm.getItem() instanceof WorldCharm wc) {
                if(modificationData.getValue(Attribute.MAGNITUDE) == 5.0F && modificationData.getValue(Attribute.RANGE) == 5.0F){
                    ResourceKey<Level> targetWorld =  wc.GetWorldTarget(worldCharm);
                    if (targetWorld != null) {
                        if (source.getCaster() instanceof Player) {
                            AtomicReference<MarkSave> playerMark = new AtomicReference<>(null);
                            source.getCaster().getCapability(PlayerDragonMagicProvider.PLAYER_DRAGON_MAGIC).ifPresent(magic -> {
                                if(magic.hasValidMark(targetWorld)){
                                    playerMark.set(magic.getMark(targetWorld));
                                }
                            });
                            if(playerMark.get() != null) {
                                targetPosition = playerMark.get().getPosition().getCenter();
                                TeleportHelper.teleportEntity(target.getLivingEntity(), targetWorld, new Vec3(targetPosition.x, targetPosition.y + 1, targetPosition.z));
                                return ComponentApplicationResult.SUCCESS;
                            }
                            source.getPlayer().displayClientMessage(Component.translatable("dmnr.shapes.atmark.nomark.error"),true);
                            return ComponentApplicationResult.FAIL;
                        }
                    }
                    else {
                        source.getPlayer().displayClientMessage(Component.translatable("dmnr.shapes.atmark.noworld.error"),true);
                    }
                }
                else {
                    source.getPlayer().displayClientMessage(Component.translatable("dmnr.shapes.atmark.toweak.error"),true);
                }
                return ComponentApplicationResult.FAIL;
            }

            MarkSave markSave = MarkSave.getMark(source.getCaster(), source.getCaster().getCommandSenderWorld(), CommonConfig.RECALL_SUPPORT_PLAYERCHARM.get());

            if (markSave != null && markSave.getPosition() != null) {
                targetPosition = markSave.getPosition().getCenter();
                if (this.isValidDistance(targetPosition, target.getLivingEntity(), modificationData)) {
                    if (target.getLivingEntity() == source.getCaster() || this.magnitudeHealthCheck(source, target, (int)modificationData.getValue(Attribute.MAGNITUDE), 20)) {
                        TeleportHelper.teleportEntity(target.getLivingEntity(), context.getLevel().dimension(), new Vec3(targetPosition.x, targetPosition.y + 1, targetPosition.z));
                        return ComponentApplicationResult.SUCCESS;
                    } else {
                        if (source.getPlayer() != null) {
                            source.getPlayer().displayClientMessage(Component.translatable("mna:generic.too_powerful"), true);
                        }
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

    private boolean isValidDistance(@Nullable Vec3 targetPosition, @NotNull LivingEntity caster, @NotNull IModifiedSpellPart<SpellEffect> modificationData){
        if(targetPosition == null){
            return false;
        }

        else if(CommonConfig.RECALL_UNLIMITED_RANGE.get() && modificationData.getValue(Attribute.RANGE) >= modificationData.getMaximumValue(Attribute.RANGE)){
            return true;
        }

        return !(targetPosition.distanceTo(caster.position()) > modificationData.getValue(Attribute.RANGE) * 1000.0F);
    }

    @Override
    public boolean targetsBlocks() {
        return true;
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
        return SpellPartTags.UTILITY;
    }
}