package de.joh.dmnr.common.spell.component;

import com.mna.api.affinity.Affinity;
import com.mna.api.entities.DamageHelper;
import com.mna.api.sound.SFX;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IDamageComponent;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.config.GeneralModConfig;
import com.mna.effects.EffectInit;
import com.mna.interop.CuriosInterop;
import com.mna.items.ItemInit;
import de.joh.dmnr.common.util.RLoc;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import top.theillusivec4.curios.api.SlotTypePreset;

import java.util.List;

/**
 * This spell deals physical damage to opponents, some of which can be absorbed by armor.
 * In addition, the opponent is thrown back a bit, which can cause additional impact damage.
 * <br>By increasing the Magnitude, the factor that the armor absorbs decreases more and more.
 * At maximum strength, they take even more damage than if they were not wearing armor.
 * @author Joh0210
 */
public class ForceDamageComponent extends SpellEffect implements IDamageComponent {
    public ForceDamageComponent(ResourceLocation icon) {
        super(icon, new AttributeValuePair(Attribute.DAMAGE, 5.0F, 1.0F, 20.0F, 0.5F, 3.0F), new AttributeValuePair(Attribute.LESSER_MAGNITUDE, 1.0F, 1.0F, 3.0F, 1.0F, 5.0F), new AttributeValuePair(Attribute.MAGNITUDE, 0.0F, 0.0F, 6.0F, 1.0F, 6.0F));
    }

    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (target.isEntity() && target.getEntity() instanceof LivingEntity entity) {
            //determination of the damage
            float damage = modificationData.getValue(Attribute.DAMAGE) * GeneralModConfig.getDamageMultiplier();


//            //Uses Armor + Prot.
//            float reduction = 0;
//            int k = EnchantmentHelper.getDamageProtection(entity.getArmorSlots(), DamageSource.FLY_INTO_WALL.getMsgId());
//            if (k > 0) {
//                reduction = 1 - CombatRules.getDamageAfterMagicAbsorb(CombatRules.getDamageAfterAbsorb(damage, (float)entity.getArmorValue(), (float)entity.getAttributeValue(Attributes.ARMOR_TOUGHNESS)), (float)k)/damage;
//            } else {
//                reduction = 1 - CombatRules.getDamageAfterAbsorb(damage, (float)entity.getArmorValue(), (float)entity.getAttributeValue(Attributes.ARMOR_TOUGHNESS))/damage;
//            }

            //Uses Armor
            float reduction = 1 - CombatRules.getDamageAfterAbsorb(damage, (float)entity.getArmorValue(), (float)entity.getAttributeValue(Attributes.ARMOR_TOUGHNESS))/damage;
            damage = damage * (1 - reduction + reduction * modificationData.getValue(Attribute.MAGNITUDE)/3); //+15% to Iron; +45% to Dia
            //injure
            target.getEntity().hurt(DamageHelper.createSourcedType(getForceDamage(), context.getLevel().registryAccess(), source.getCaster()), damage);

            //recoil
            if(entity != source.getCaster()){
                float speed = modificationData.getValue(Attribute.LESSER_MAGNITUDE)/2.0f;
                float max_velocity;
                max_velocity = 2.0F;
                MobEffectInstance effect = entity.getEffect(EffectInit.ENLARGE.get());
                if (effect != null) {
                    speed = (float)((double)speed * (1.0 - 0.1 * (double)(effect.getAmplifier() + 1)));
                    max_velocity *= 0.5F;
                }

                effect = entity.getEffect(EffectInit.REDUCE.get());
                if (effect != null) {
                    speed = (float)((double)speed * (1.0 + 0.2 * (double)(effect.getAmplifier() + 1)));
                    max_velocity = (float)((double)max_velocity * (1.0 + 0.2 * (double)(effect.getAmplifier() + 1)));
                }

                float mX = (float)(source.getOrigin().x() - target.getLivingEntity().getX());
                float mZ = (float)(source.getOrigin().z() - target.getLivingEntity().getZ());
                if (source.getCaster().getVehicle() == entity) {
                    mX = (float)(-source.getCaster().getForward().x);
                    mZ = (float)(-source.getCaster().getForward().z);
                }

                flingTarget(entity, new Vec3(mX, speed, mZ), speed, max_velocity);
            }
            return ComponentApplicationResult.SUCCESS;
        }

        return ComponentApplicationResult.FAIL;
    }

    @Override
    public boolean targetsBlocks() {
        return false;
    }

    @Override
    public SoundEvent SoundEffect() {
        return SFX.Spell.Impact.Single.EARTH;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.EARTH;
    }

    @Override
    public float initialComplexity() {
        return 10.0F;
    }

    @Override
    public int requiredXPForRote() {
        return 500;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.HARMFUL;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return List.of(Affinity.EARTH);
    }

    public static void flingTarget(LivingEntity target, Vec3 direction, float strength, float kbResistFactor) {
        float max_velocity = 2.0F;

        if (target != null) {
            MobEffectInstance effect = target.getEffect(EffectInit.ENLARGE.get());
            if (effect != null) {
                strength = (float)((double)strength * (1.0 - 0.1 * (double)(effect.getAmplifier() + 1)));
                max_velocity *= 0.5F;
            }

            effect = target.getEffect(EffectInit.REDUCE.get());
            if (effect != null) {
                strength = (float)((double)strength * (1.0 + 0.2 * (double)(effect.getAmplifier() + 1)));
                max_velocity = (float)((double)max_velocity * (1.0 + 0.2 * (double)(effect.getAmplifier() + 1)));
            }

            if (target instanceof Player targetPlayer) {
                Vec3 motion = direction.normalize().scale(strength);
                targetPlayer.push(motion.x, motion.y, motion.z);
                motion = targetPlayer.getDeltaMovement();
                if (motion.length() > (double)max_velocity) {
                    double scale = (double)max_velocity / motion.length();
                    targetPlayer.setDeltaMovement(motion.scale(scale));
                }

                targetPlayer.hurtMarked = true;
                ((ServerPlayer)targetPlayer).connection.send(new ClientboundSetEntityMotionPacket(targetPlayer));
                if (!CuriosInterop.IsItemInCurioSlot(ItemInit.AIR_CAST_RING.get(), targetPlayer, SlotTypePreset.RING)) {
                    setFlags(targetPlayer, strength);
                }
            } else {
                LivingKnockBackEvent event = ForgeHooks.onLivingKnockBack(target, strength, direction.x, direction.z);
                if (!event.isCanceled()) {
                    double kbRes = target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
                    strength = event.getStrength();
                    direction = new Vec3(event.getRatioX(), 0.0, event.getRatioZ());
                    strength = (float)((double)strength * (1.0 - kbRes * (double)kbResistFactor));
                    if (!((double)strength <= 0.0)) {
                        target.hasImpulse = true;
                        Vec3 vec3 = target.getDeltaMovement();
                        Vec3 vec31 = (new Vec3(direction.x, 0.0, direction.z)).normalize().scale(strength);
                        target.setDeltaMovement(vec3.x / 2.0 - vec31.x, target.onGround() ? Math.min(0.4, vec3.y / 2.0 + (double)strength) : vec3.y, vec3.z / 2.0 - vec31.z);
                    }
                }

                target.push(0.0, 0.2F * strength, 0.0);
                if (target instanceof PathfinderMob) {
                    ((PathfinderMob)target).getNavigation().stop();
                }

                setFlags(target, strength);
            }

        }
    }

    private static void setFlags(LivingEntity le, float strength) {
        le.getPersistentData().putFloat("mna:flung", strength);
        le.getPersistentData().putLong("mna:fling_time", le.level().getGameTime());
        le.hasImpulse = true;
    }

    public static ResourceKey<DamageType> getForceDamage(){
        return ResourceKey.create(Registries.DAMAGE_TYPE, RLoc.create("spell_force"));
    }
}

