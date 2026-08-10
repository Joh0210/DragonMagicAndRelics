package de.joh.dmnr.common.event;

import de.joh.dmnr.common.effects.harmful.HellfireMobEffect;
import de.joh.dmnr.common.item.*;
import de.joh.dmnr.DragonMagicAndRelics;
import net.minecraftforge.event.entity.living.*;
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
     * Processing of the damage boost and damage resistance upgrades.
     * Casts a spell on the player or the source when the wearer of the Dragon Mage Armor takes damage.
     * <br> - Glass Cannon Belt
     * <br> - Sturdy Belt
     */
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        RingOfPowerItem.eventHandleAttack(event);
        RingOfRulingItem.eventHandleDefense(event);
        RevengeCharmItem.handleRevengeCharm(event);
        FactionAmuletItem.eventHandleDeclarationOfWar(event);

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

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event){
//        DragonCoreItem.eventHandleDragonDeath(event);
        DragonTotemItem.eventHandleDragonDeath(event);
        DragonTotemItem.eventHandlePlayerDeath(event);
    }
}
