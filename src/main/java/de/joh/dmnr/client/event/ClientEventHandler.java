package de.joh.dmnr.client.event;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.client.item.armor.WingRenderLayer;
import de.joh.dmnr.common.init.EffectInit;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.common.item.DragonMageArmorItem;
import de.joh.dmnr.networking.ModMessages;
import de.joh.dmnr.networking.packet.ToggleFlightC2SPacket;
import de.joh.dmnr.common.init.KeybindInit;
import de.joh.dmnr.networking.packet.UseRingOfSpellStoringC2SPacket;
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
        /**
         * Has the button been pressed that activates Night Vision or DM&R Flight?
         * @see ToggleFlightC2SPacket
         */
        @SubscribeEvent
        public static void onKeyRegister(InputEvent.Key event){
            if(KeybindInit.USE_SPELL.consumeClick()){
                ModMessages.sendToServer(new UseRingOfSpellStoringC2SPacket());
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
                        || CuriosApi.getCuriosHelper().findFirstCurio(player, ItemInit.ANGEL_RING.get()).isPresent()
                        || CuriosApi.getCuriosHelper().findFirstCurio(player, ItemInit.FALLEN_ANGEL_RING.get()).isPresent()
                ) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
