package de.joh.dmnr.common.event;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.api.item.DragonMageArmorItem;
import de.joh.dmnr.common.effects.beneficial.ElytraMobEffect;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.common.item.DisappearingTiaraItem;
import de.joh.dmnr.common.item.KeyOfHomestead;
import de.joh.dmnr.common.item.NightGogglesItem;
import de.joh.dmnr.common.item.OcelotCurioItem;
import de.joh.dmnr.networking.ModMessages;
import de.joh.dmnr.networking.packet.ToggleWaterBraceletS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

/**
 * These event handlers take care of processing events which are on the server and client. (No damage events)
 * Functions marked with @SubscribeEvent are called by the forge event bus handler.
 */
@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEventHandler {
    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        OcelotCurioItem.ocelotJump(event);
    }

    /**
     * Causes DMArmor effects to be removed when armor is removed.
     */
    @SubscribeEvent
    public static void onLivingEquipmentChange(LivingEquipmentChangeEvent event){
        LivingEntity entity = event.getEntity();
        if(entity != null && event.getSlot().getType() == EquipmentSlot.Type.ARMOR){
            Item fromItem = event.getFrom().getItem();
            Item toItem = event.getTo().getItem();


            if(fromItem instanceof NightGogglesItem){
                ((NightGogglesItem)fromItem).onDiscard(entity);
            }

            if(fromItem instanceof DisappearingTiaraItem){
                ((DisappearingTiaraItem)fromItem).onDiscard(entity);
            }

            if(fromItem instanceof DragonMageArmorItem){
                ((DragonMageArmorItem)fromItem).onDiscard(event.getFrom(), entity);
            }
            if(toItem instanceof DragonMageArmorItem){
                ((DragonMageArmorItem)toItem).onEquip(event.getTo(), entity);
            }
        }
    }

    /**
     * If the player has the Elytra effect, he can fly through this event like with an Elytra.
     * @see ElytraMobEffect
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onGlideTick(TickEvent.PlayerTickEvent event){
        ElytraMobEffect.eventHandleElytraFly(event);
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if(!event.getLevel().isClientSide()) {
            if(event.getEntity() instanceof ServerPlayer player) {
                // ModMessages.sendToPlayer(new ToggleMajorFireResS2CPacket((ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.MAJOR_FIRE_RESISTANCE)) >= 1), player);

                ModMessages.sendToPlayer(new ToggleWaterBraceletS2CPacket(!CuriosApi.getCuriosHelper().findCurios(player, ItemInit.BRACELET_OF_WATER.get()).isEmpty() || !CuriosApi.getCuriosHelper().findCurios(player, ItemInit.BRACELET_OF_WATER_GREATER.get()).isEmpty()), player);
            }
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        KeyOfHomestead.onPlayerLeftClick(event);
    }
}
