package de.joh.dmnr.common.event;

import de.joh.dmnr.common.dragonmagic.BattlemageAmuletDragonMagic;
import de.joh.dmnr.common.dragonmagic.EldritchOrbDragonMagic;
import de.joh.dmnr.common.dragonmagic.FortifyingAmuletDragonMagic;
import de.joh.dmnr.common.dragonmagic.RingOfBrittleBoneDragonMagic;
import de.joh.dmnr.common.dragonmagic.WardingAmuletDragonMagic;
import de.joh.dmnr.common.dragonmagic.WitherguardAmuletDragonMagic;
import de.joh.dmnr.common.effects.harmful.HellfireMobEffect;
import de.joh.dmnr.common.item.*;
import de.joh.dmnr.DragonMagicAndRelics;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * These event handlers take care of processing damage events.
 * Functions marked with @SubscribeEvent are called by the forge event bus handler.
 * @author Joh0210
 */
@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DamageEventHandler {
    /**
     * Runs before the other handlers (including those of Mana and Artifice) change the damage.
     * <br> - Eldritch Orb (Dragon Magic)
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLivingHurtUnchanged(LivingHurtEvent event) {
        EldritchOrbDragonMagic.eventHandleBoltDamage(event);
    }

    /**
     * Processing of the damage boost and damage resistance upgrades.
     * Casts a spell on the player or the source when the wearer of the Dragon Mage Armor takes damage.
     * <br> - Glass Cannon Belt
     * <br> - Sturdy Belt
     * <br> - Amulet of the Battlemage (Dragon Magic)
     * <br> - Witherguard Amulet (Dragon Magic)
     */
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        RingOfPowerItem.eventHandleAttack(event);
        RingOfRulingItem.eventHandleDefense(event);
        RevengeCharmItem.handleRevengeCharm(event);
        FactionAmuletItem.eventHandleDeclarationOfWar(event);
        BattlemageAmuletDragonMagic.eventHandleMeleeDamage(event);
        WitherguardAmuletDragonMagic.eventHandleWitherAttacker(event);

        if (OcelotCurioItem.eventHandleKineticProtection(event)){
            return;
        }

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
     * @see AngelRingItem
     */
    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        HellfireMobEffect.handleHellfire(event);
        if (VoidfeatherCharmItem.eventHandleVoidProtection(event)) {
            return;
        }

        if (FireResistanceBraceletItem.eventHandleFireAndExplosionProtection(event)) {
            return;
        }

        if (AngelRingItem.eventHandleKineticProtection(event)) {
            return;
        }

        if (OcelotCurioItem.eventHandleKineticProtection(event)) {
            return;
        }

        if (ProjectileReflectionRingItem.tryReflect(event)){
            return;
        }

        BraceletOfFriendshipItem.eventHandleProtectFriends(event);
    }

    /**
     * Runs before Mana and Artifice, which consumes the charge of its items in the same event.
     * <br> - Warding Amulet (Dragon Magic)
     * <br> - Ring of Brittle Bone (Dragon Magic)
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLivingDamage(LivingDamageEvent event) {
        WardingAmuletDragonMagic.eventHandleMagicProtection(event);
        RingOfBrittleBoneDragonMagic.eventHandleSoulsBefore(event);
    }

    /**
     * Runs after Mana and Artifice, also when the event was canceled, to adjust what its items did.
     * <br> - Ring of Brittle Bone (Dragon Magic)
     */
    @SubscribeEvent(priority = EventPriority.LOW, receiveCanceled = true)
    public static void onLivingDamageAfterMna(LivingDamageEvent event) {
        RingOfBrittleBoneDragonMagic.eventHandleSoulsAfter(event);
    }

    /**
     * Runs before Mana and Artifice, which consumes the charge of its items in the same event.
     * <br> - Ring of Brittle Bone (Dragon Magic)
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLivingDeathBeforeMna(LivingDeathEvent event){
        RingOfBrittleBoneDragonMagic.eventHandleSoulsBefore(event);
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event){
        DragonTotemItem.eventHandleDragonDeath(event);
        DragonTotemItem.eventHandleCursedKill(event);
        DragonTotemItem.eventHandlePlayerDeath(event);
    }

    /**
     * Runs after Mana and Artifice, also when it has already saved the player, to adjust what its items did.
     * <br> - Fortifying Amulet (Dragon Magic)
     * <br> - Ring of Brittle Bone (Dragon Magic)
     */
    @SubscribeEvent(priority = EventPriority.LOW, receiveCanceled = true)
    public static void onLivingDeathAfterMna(LivingDeathEvent event){
        FortifyingAmuletDragonMagic.eventHandleCooldownReduction(event);
        RingOfBrittleBoneDragonMagic.eventHandleSoulsAfter(event);
    }
}
