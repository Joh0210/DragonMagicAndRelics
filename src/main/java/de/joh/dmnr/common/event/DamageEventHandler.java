package de.joh.dmnr.common.event;

import com.mna.api.config.GeneralConfigValues;
import com.mna.api.entities.DamageHelper;
import com.mna.api.entities.IFactionEnemy;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.effects.EffectInit;
import com.mna.entities.sorcery.EntityDecoy;
import com.mna.factions.Factions;
import com.mna.tools.ProjectileHelper;
import com.mna.tools.SummonUtils;
import de.joh.dmnr.common.effects.harmful.HellfireMobEffect;
import de.joh.dmnr.common.item.*;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.api.item.DragonMageArmorItem;
import de.joh.dmnr.capabilities.dragonmagic.ArmorUpgradeHelper;
import de.joh.dmnr.common.command.Commands;
import de.joh.dmnr.common.init.ArmorUpgradeInit;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.common.util.CommonConfig;
import de.joh.dmnr.common.util.ProjectileReflectionHelper;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.mutable.MutableFloat;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * These event handlers take care of processing damage events.
 * Functions marked with @SubscribeEvent are called by the forge event bus handler.
 * @author Joh0210
 */
@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DamageEventHandler {

    /**
     * Processing of damage shifts
     * @see HellfireMobEffect
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLivingHurtFast(LivingHurtEvent event) {
    }

    /**
     * Processing of the damage boost and damage resistance upgrades.
     * Casts a spell on the player or the source when the wearer of the Dragon Mage Armor takes damage.
     * <br> - Glass Cannon Belt
     * <br> - Sturdy Belt
     * @see ArmorUpgradeInit
     * @see Commands
     */
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        Entity sourceEntity = event.getSource().getEntity();
        LivingEntity targetEntity = event.getEntity();

        RevengeCharmItem.handleRevengeCharm(event);

        if(targetEntity instanceof Player player){
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);

            //Spell
            if(!chest.isEmpty() && !player.level().isClientSide && chest.getItem() instanceof DragonMageArmorItem dragonMageArmor && dragonMageArmor.isSetEquipped(player)) {
                DragonMageArmorItem.applySpell(chest, false, player, sourceEntity);
                if(sourceEntity != null && sourceEntity != player){
                    DragonMageArmorItem.applySpell(chest, true, player, sourceEntity);
                }
            }

            //Damage Resistance
            event.setAmount(event.getAmount() * (1.0f - (float) ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.DAMAGE_RESISTANCE)* CommonConfig.getDamageResistanceDamageReductionPerLevel()));
        }

        //Damage Boost
        if (sourceEntity instanceof Player player){
            event.setAmount(event.getAmount() * (1.0f + ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.DAMAGE_BOOST)*0.25f));
        }

        FactionAmuletItem.eventHandleDeclarationOfWar(event);

        if(IDamageAdjustmentItem.eventHandleDamageAdjustment(event)){
            return;
        }
    }

    @SubscribeEvent
    public static void onLivingChangeTarget(LivingChangeTargetEvent event) {
        FactionAmuletItem.eventHandlePeaceOffering(event);
    }

    /**
     * Processing of the projectile reflection, fire resistance, explosion resistance and kinetic resistance upgrades resistance through jumpboost.
     * @see ArmorUpgradeInit
     * @see AngelRingItem
     */
    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        HellfireMobEffect.handleHellfire(event);
        DamageSource source = event.getSource();
        if (VoidfeatherCharmItem.eventHandleVoidProtection(event)) {
            return;
        }

        if(event.getEntity() instanceof Player player){

            if (!player.level().isClientSide) {
                //protection against fire
                if (source.is(DamageTypeTags.IS_FIRE) && ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.MAJOR_FIRE_RESISTANCE) >= 1) {
                    event.setCanceled(true);
                    return;
                } else if (source.is(DamageTypeTags.IS_FIRE) && ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.MINOR_FIRE_RESISTANCE) >= 1) {
                    AtomicBoolean doReturn = new AtomicBoolean(false);
                    player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(magic -> {
                        if (magic.getCastingResource().hasEnoughAbsolute(player, CommonConfig.FIRE_RESISTANCE_MANA_PER_FIRE_DAMAGE.get()/5.0f)) {
                            magic.getCastingResource().consume(player, CommonConfig.FIRE_RESISTANCE_MANA_PER_FIRE_DAMAGE.get()/5.0f);
                            event.setCanceled(true);
                            if(player.isOnFire()){
                                player.clearFire();
                            }
                            doReturn.set(true);
                        }
                    });
                    if(doReturn.get()){
                        return;
                    }
                }

                //protection against explosions
                if (source.is(DamageTypeTags.IS_EXPLOSION) && ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.EXPLOSION_RESISTANCE) == 1) {
                    event.setCanceled(true);
                    return;
                }

                //Protection from falling through jumpboost
                if(source.is(DamageTypeTags.IS_FALL)){
                    if(ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.JUMP) >= 1){
                        if((event.getAmount() - ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.JUMP) * 3) <= 0){
                            event.setCanceled(true);
                            return;
                        }
                    }
                    else if(ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.BURNING_FRENZY) >= 1){
                        if((event.getAmount() - ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.BURNING_FRENZY) * 9) <= 0){
                            event.setCanceled(true);
                            return;
                        }
                    }
                }

                //Protection from kinetic energy
                if((source.is(DamageTypeTags.IS_FALL) || source.is(DamageTypes.FLY_INTO_WALL)) && ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.KINETIC_RESISTANCE) == 1){
                    event.setCanceled(true);
                    return;
                }

                //Projectile Reflection
                if(source.getDirectEntity() instanceof Projectile && ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.PROJECTILE_REFLECTION) > 0 && ProjectileReflectionHelper.consumeReflectCharge(player)){
                    event.setCanceled(true);
                    ProjectileHelper.ReflectProjectile(player, (Projectile)source.getDirectEntity(), true, 10.0F);
                    return;
                }
            }

            //Protection from kinetic energy
            if((source.is(DamageTypeTags.IS_FALL) || source.is(DamageTypes.FLY_INTO_WALL)) && CuriosApi.getCuriosHelper().findFirstCurio(player, ItemInit.ANGEL_RING.get()).isPresent()){
                event.setCanceled(true);
                return;
            }
        }

        BraceletOfFriendshipItem.eventHandleProtectFriends(event);
    }

    /**
     * Processing resistance through jumpboost and the received Souls through Mana Reagen for Undeads
     * @see ArmorUpgradeInit
     */
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if(event.getEntity() instanceof Player player){
            if (!player.level().isClientSide) {

                //Protection from falling through jumpboost
                if(event.getSource().is(DamageTypeTags.IS_FALL)){
                    if(ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.JUMP) >= 1){
                        int amount = (int)event.getAmount() - ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.JUMP) * 3;
                        if(amount > 0){
                            event.setAmount(amount);
                        }
                    }
                    else if(ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.BURNING_FRENZY) >= 1){
                        int amount = (int)event.getAmount() - ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.BURNING_FRENZY) * 9;
                        if(amount > 0){
                            event.setAmount(amount);
                        }
                    }
                }

                if (ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.MIST_FORM) >= 1 && !event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) && player.getHealth() > 1.0F && event.getAmount() > player.getHealth()) {
                    player.addEffect(new MobEffectInstance(EffectInit.MIST_FORM.get(), 200, 0, true, true));
                    player.setHealth(1.0F);
                    event.setCanceled(true);
                }
            }
        }

        //Receiving Souls through Mana Reagen for Undeads
        LivingEntity living = event.getEntity();
        Entity source = event.getSource().getEntity();
        if (source instanceof LivingEntity && source != event.getEntity() && source instanceof Player sourcePlayer) {
            sourcePlayer.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent((p) -> {
                if (p.getAlliedFaction() == Factions.UNDEAD) {
                    if (!sourcePlayer.level().isClientSide) {
                        int manaRegenLevel = ArmorUpgradeHelper.getUpgradeLevel(sourcePlayer, ArmorUpgradeInit.MANA_REGEN);
                        if (manaRegenLevel > 0) {
                            float souls = getSoulsRestored(sourcePlayer, living);
                            if (souls > 0.0F) {
                                sourcePlayer.getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> {
                                    m.getCastingResource().restore(souls * 0.05F * manaRegenLevel);
                                    m.getCastingResource().setNeedsSync();
                                });
                            }
                        }
                    }
                }
            });
        }
        if (event.getAmount() <= 0.0F) {
            event.setCanceled(true);
        }
    }

    /** Provides the souls given by a monster to an Undead upon death
     * @param soulRecipient Player attacking the target
     * @param target Target to be checked
     * @return number of souls
     */
    private static float getSoulsRestored(Player soulRecipient, Entity target) {
        if (soulRecipient == null) {
            return 0.0F;
        } else if (target instanceof LivingEntity && (!(target instanceof PathfinderMob) || !SummonUtils.isSummon(target))) {
            if (target instanceof EntityDecoy) {
                return 0.0F;
            } else {
                MutableFloat restoreAmount = new MutableFloat(1.0F);
                if (target instanceof Player) {
                    restoreAmount.setValue(GeneralConfigValues.SoulsForPlayerKill);
                } else if (target instanceof Villager) {
                    restoreAmount.setValue(GeneralConfigValues.SoulsForVillagerKill);
                } else if (target instanceof IFactionEnemy) {
                    restoreAmount.setValue(GeneralConfigValues.SoulsForFactionMobKill);
                } else if (((LivingEntity)target).isInvertedHealAndHarm()) {
                    restoreAmount.setValue(GeneralConfigValues.SoulsForUndeadKill);
                } else if (target instanceof Animal) {
                    restoreAmount.setValue(GeneralConfigValues.SoulsForAnimalKill);
                } else if (target instanceof AbstractGolem) {
                    restoreAmount.setValue(0.0F);
                } else if (target instanceof Mob) {
                    restoreAmount.setValue(GeneralConfigValues.SoulsForMobKill);
                }

                if (((LivingEntity)target).hasEffect(EffectInit.SOUL_VULNERABILITY.get())) {
                    restoreAmount.setValue(restoreAmount.getValue() * 5.0F);
                }

                if (com.mna.items.ItemInit.BONE_RING.get().isEquippedAndHasMana(soulRecipient, 3.5F, true)) {
                    restoreAmount.setValue(restoreAmount.getValue() * 2.25F);
                }

                return restoreAmount.getValue();
            }
        } else {
            return 0.0F;
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event){
        DragonCoreItem.eventHandleDragonDeath(event);
    }
}
