package de.joh.dmnr.networking.packet;

import de.joh.dmnr.capabilities.client.ClientCurioBoost;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ToggleCurioBoostEnabledS2CPacket {
    private final boolean isEnabled;

    public ToggleCurioBoostEnabledS2CPacket(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public ToggleCurioBoostEnabledS2CPacket(FriendlyByteBuf buf) {
        this.isEnabled = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(isEnabled);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> ClientCurioBoost.setEnabled(isEnabled));
        return true;
    }
}
