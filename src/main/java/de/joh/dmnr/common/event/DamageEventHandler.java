package de.joh.dmnr.common.event;

import de.joh.dmnr.common.effects.harmful.HellfireMobEffect;
import de.joh.dmnr.common.item.*;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.api.item.DragonMageArmorItem;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
        Entity sourceEntity = event.getSource().getEntity();
        LivingEntity targetEntity = event.getEntity();
        if (OcelotCurioItem.eventHandleKineticProtection(event)){
            return;
        }
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
        DragonCoreItem.eventHandleDragonDeath(event);
    }
}
