package de.joh.dmnr.networking.packet;

import de.joh.dmnr.capabilities.client.ClientCurioBoost;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ToggleCurioBoostStringS2CPacket {
    private final String id;
    private final boolean isBlacklisted;

    public ToggleCurioBoostStringS2CPacket(String id, boolean isBlacklisted) {
        this.id = id;
        this.isBlacklisted = isBlacklisted;
    }

    public ToggleCurioBoostStringS2CPacket(FriendlyByteBuf buf) {
        this.id = buf.readUtf();
        this.isBlacklisted = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(id);
        buf.writeBoolean(isBlacklisted);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if (isBlacklisted) {
                ClientCurioBoost.blacklistID(id);
            } else {
                ClientCurioBoost.whitelistID(id);
            }
        });
        return true;
    }
}
