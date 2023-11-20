package de.joh.dmnr.events;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.effects.EffectInit;
import de.joh.dmnr.item.ItemInit;
import de.joh.dmnr.item.items.WeatherFairyStaff;
import de.joh.dmnr.item.items.dragonmagearmor.DragonMageArmor;
import de.joh.dmnr.networking.ModMessages;
import de.joh.dmnr.networking.packet.IncrementWeatherC2SPacket;
import de.joh.dmnr.networking.packet.ToggleFlightC2SPacket;
import de.joh.dmnr.networking.packet.ToggleNightVisionC2SPacket;
import de.joh.dmnr.utils.KeybindInit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.caelus.api.RenderCapeEvent;
import top.theillusivec4.curios.api.CuriosApi;

/**
 * These event handlers take care of processing events which are on client side only.
 * Functions marked with @SubscribeEvent are called by the forge event bus handler.
 */
public class ClientEvents {
    @Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents{
        private static Player getPlayer() {
            return DragonMagicAndRelics.instance.getClientPlayer();
        }

        @SubscribeEvent
        public static void onMouseScroll(InputEvent.MouseScrollEvent event){
            if (getPlayer() != null
                    && getPlayer().getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof WeatherFairyStaff staff
                    && getPlayer().isShiftKeyDown())
            {
                ModMessages.sendToServer(new IncrementWeatherC2SPacket(event.getScrollDelta() < 0));
                staff.incrementIterator(getPlayer().getItemBySlot(EquipmentSlot.MAINHAND), event.getScrollDelta() < 0, getPlayer());
                event.setCanceled(true);
            }
        }

        /**
         * Has the button been pressed that activates Night Vision or DM&R Flight?
         * @see ToggleNightVisionC2SPacket
         * @see ToggleFlightC2SPacket
         * @see de.joh.dmnr.armorupgrades.ArmorUpgradeInit
         */
        @SubscribeEvent
        public static void onKeyRegister(InputEvent.KeyInputEvent event){
            if(KeybindInit.TOGGLE_NIGHT_VISION_KEY.consumeClick()){
                ModMessages.sendToServer(new ToggleNightVisionC2SPacket());
            }
            else if(KeybindInit.TOGGLE_FLIGHT_KEY.consumeClick()){
                ModMessages.sendToServer(new ToggleFlightC2SPacket());
            }
        }

        /**
         * If the player is wearing Elytra, no cape will be rendered
         * @see de.joh.dmnr.item.client.armor.WingLayer
         */
        @SubscribeEvent
        public static void renderCape(RenderCapeEvent event) {
            if (event.getEntity() instanceof Player player){
                ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
                if (
                        (!chest.isEmpty() && chest.getItem() instanceof DragonMageArmor && chest.hasTag() && player.hasEffect(EffectInit.ELYTRA.get()))
                        || CuriosApi.getCuriosHelper().findEquippedCurio(ItemInit.ANGEL_RING.get(), player).isPresent()
                        || CuriosApi.getCuriosHelper().findEquippedCurio(ItemInit.FALLEN_ANGEL_RING.get(), player).isPresent()
                ) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
