package de.joh.dragonmagicandrelics.events;

import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.networking.ModMessages;
import de.joh.dragonmagicandrelics.networking.packet.ToggleFlightC2SPacket;
import de.joh.dragonmagicandrelics.networking.packet.ToggleNightVisionC2SPacket;
import de.joh.dragonmagicandrelics.utils.KeybindInit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * These event handlers take care of processing events which are on client side only.
 * Functions marked with @SubscribeEvent are called by the forge event bus handler.
 */
public class ClientEvents {
    @Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents{

        /**
         * Has the button been pressed that activates Night Vision or DM&R Flight?
         * @see ToggleNightVisionC2SPacket
         * @see ToggleFlightC2SPacket
         * @see de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit
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
    }
}
