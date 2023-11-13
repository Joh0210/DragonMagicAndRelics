package de.joh.dragonmagicandrelics.networking.packet;

import de.joh.dragonmagicandrelics.item.items.WeatherFairyStaff;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class IncrementWeatherC2SPacket {
    private final boolean inverted;

    public IncrementWeatherC2SPacket(boolean inverted){
        this.inverted = inverted;
    }

    public IncrementWeatherC2SPacket(FriendlyByteBuf buf){
        this.inverted = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeBoolean(inverted);
    }

    /**
     * Processing of the event on the SERVER SIDE!
     */
    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()->{
            ServerPlayer player = context.getSender();
            if(player != null && player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof WeatherFairyStaff staff){
                staff.incrementIterator(player.getItemBySlot(EquipmentSlot.MAINHAND), inverted, player);
            }
        });
        return true;
    }
}
