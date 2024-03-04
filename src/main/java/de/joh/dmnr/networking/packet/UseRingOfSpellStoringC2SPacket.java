package de.joh.dmnr.networking.packet;

import de.joh.dmnr.common.item.RingOfSpellStoringItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UseRingOfSpellStoringC2SPacket {
    public UseRingOfSpellStoringC2SPacket(){}

    public UseRingOfSpellStoringC2SPacket(FriendlyByteBuf buf){}

    public void toBytes(FriendlyByteBuf buf){}

    /**
     * Processing of the event on the SERVER SIDE!
     */
    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()->{
            ServerPlayer player = context.getSender();
            if(player != null){
                RingOfSpellStoringItem.castSpell(player);
            }
        });
        return true;
    }
}
