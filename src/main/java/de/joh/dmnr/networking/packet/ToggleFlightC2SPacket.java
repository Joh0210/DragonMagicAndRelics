package de.joh.dmnr.networking.packet;

import com.mna.ManaAndArtifice;
import de.joh.dmnr.client.event.ClientEventHandler;
import de.joh.dmnr.common.init.EffectInit;
import de.joh.dmnr.common.init.KeybindInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * This class is the package that is sent to the server when the DISABLE_FLIGHT_KEY is pressed.
 * @see KeybindInit
 * @see ClientEventHandler
 * @see EffectInit
 * @author Joh0210
 */
public class ToggleFlightC2SPacket {
    public ToggleFlightC2SPacket(){}

    public ToggleFlightC2SPacket(FriendlyByteBuf buf){}

    public void toBytes(FriendlyByteBuf buf){}

    /**
     * Processing of the event on the SERVER SIDE!
     */
    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()->{
            ServerPlayer player = context.getSender();

            if(player.hasEffect(EffectInit.FLY_DISABLED.get())){
                player.removeEffect(EffectInit.FLY_DISABLED.get());
            }else{
                player.addEffect(new MobEffectInstance(EffectInit.FLY_DISABLED.get(), -1, 0, false, false, true));

                ManaAndArtifice.instance.proxy.setFlySpeed(player, 0.05F);
                ManaAndArtifice.instance.proxy.setFlightEnabled(player, false);
            }
        });
        return true;
    }
}
