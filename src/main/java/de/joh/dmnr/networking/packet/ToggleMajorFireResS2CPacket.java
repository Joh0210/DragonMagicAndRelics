package de.joh.dmnr.networking.packet;

import de.joh.dmnr.capabilities.client.ClientPlayerDragonMagic;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class ToggleMajorFireResS2CPacket {
    private final boolean hasMajorFireResistance;

    public ToggleMajorFireResS2CPacket(boolean hasMajorFireResistance) {
        this.hasMajorFireResistance = hasMajorFireResistance;
    }

    public ToggleMajorFireResS2CPacket(FriendlyByteBuf buf) {
        this.hasMajorFireResistance = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(hasMajorFireResistance);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> ClientPlayerDragonMagic.setMajorFireResistance(hasMajorFireResistance));
        return true;
    }
}

