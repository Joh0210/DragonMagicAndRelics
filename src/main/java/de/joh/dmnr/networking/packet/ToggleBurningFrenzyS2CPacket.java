package de.joh.dmnr.networking.packet;

import de.joh.dmnr.capabilities.client.ClientPlayerDragonMagic;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ToggleBurningFrenzyS2CPacket {
    private final boolean hasBurningFrenzy;

    public ToggleBurningFrenzyS2CPacket(boolean hasBurningFrenzy) {
        this.hasBurningFrenzy = hasBurningFrenzy;
    }

    public ToggleBurningFrenzyS2CPacket(FriendlyByteBuf buf) {
        this.hasBurningFrenzy = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(hasBurningFrenzy);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> ClientPlayerDragonMagic.setBurningFrenzy(hasBurningFrenzy));
        return true;
    }
}
