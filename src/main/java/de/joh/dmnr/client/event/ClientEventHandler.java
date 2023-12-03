package de.joh.dmnr.client.event;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.client.item.armor.WingRenderLayer;
import de.joh.dmnr.common.init.ArmorUpgradeInit;
import de.joh.dmnr.common.init.EffectInit;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.common.item.WeatherFairyStaffItem;
import de.joh.dmnr.api.item.DragonMageArmorItem;
import de.joh.dmnr.networking.ModMessages;
import de.joh.dmnr.networking.packet.IncrementWeatherC2SPacket;
import de.joh.dmnr.networking.packet.ToggleFlightC2SPacket;
import de.joh.dmnr.networking.packet.ToggleNightVisionC2SPacket;
import de.joh.dmnr.common.init.KeybindInit;
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
public class ClientEventHandler {
    @Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents{
        @SubscribeEvent
        public static void onMouseScroll(InputEvent.MouseScrollEvent event){
            if (Minecraft.getInstance().player != null
                    && Minecraft.getInstance().player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof WeatherFairyStaff staff
                    && Minecraft.getInstance().player.isShiftKeyDown())
            {
                ModMessages.sendToServer(new IncrementWeatherC2SPacket(event.getScrollDelta() < 0));
                staff.incrementIterator(Minecraft.getInstance().player.getItemBySlot(EquipmentSlot.MAINHAND), event.getScrollDelta() < 0, Minecraft.getInstance().player);
                event.setCanceled(true);
            }
        }

        /**
         * Has the button been pressed that activates Night Vision or DM&R Flight?
         * @see ToggleNightVisionC2SPacket
         * @see ToggleFlightC2SPacket
         * @see ArmorUpgradeInit
         */
        @SubscribeEvent
        public static void onKeyRegister(InputEvent.Key event){
            if(KeybindInit.TOGGLE_NIGHT_VISION_KEY.consumeClick()){
                ModMessages.sendToServer(new ToggleNightVisionC2SPacket());
            }
            else if(KeybindInit.TOGGLE_FLIGHT_KEY.consumeClick()){
                ModMessages.sendToServer(new ToggleFlightC2SPacket());
            }
        }

        /**
         * If the player is wearing Elytra, no cape will be rendered
         * @see WingRenderLayer
         */
        @SubscribeEvent
        public static void renderCape(RenderCapeEvent event) {
            Player player = event.getEntity();
            if (player != null){
                ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
                if (
                        (!chest.isEmpty() && chest.getItem() instanceof DragonMageArmorItem && chest.hasTag() && player.hasEffect(EffectInit.ELYTRA.get()))
                        || CuriosApi.getCuriosHelper().findEquippedCurio(ItemInit.ANGEL_RING.get(), player).isPresent()
                        || CuriosApi.getCuriosHelper().findEquippedCurio(ItemInit.FALLEN_ANGEL_RING.get(), player).isPresent()
                ) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
