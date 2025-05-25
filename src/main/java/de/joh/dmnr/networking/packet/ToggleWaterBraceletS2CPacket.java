package de.joh.dmnr.networking.packet;

import de.joh.dmnr.capabilities.client.ClientPlayerDragonMagic;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ToggleWaterBraceletS2CPacket {
    private final boolean hasWaterBracelet;

    public ToggleWaterBraceletS2CPacket(boolean hasWaterBracelet) {
        this.hasWaterBracelet = hasWaterBracelet;
    }

    public ToggleWaterBraceletS2CPacket(FriendlyByteBuf buf) {
        this.hasWaterBracelet = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(hasWaterBracelet);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> ClientPlayerDragonMagic.setWaterBracelet(hasWaterBracelet));
        return true;
    }
}

