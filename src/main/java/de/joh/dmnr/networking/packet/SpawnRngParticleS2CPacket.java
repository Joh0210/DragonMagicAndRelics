package de.joh.dmnr.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Random;
import java.util.function.Supplier;

public class SpawnRngParticleS2CPacket {
    private final BlockPos pos;

    public SpawnRngParticleS2CPacket(BlockPos pos) {
        this.pos = pos;
    }

    public SpawnRngParticleS2CPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientLevel world = Minecraft.getInstance().level;
            if (world != null) {
                Random random = new Random();
                for(int j = 0; j<8; j++){
                    world.addParticle(ParticleTypes.PORTAL, pos.getX()+random.nextFloat(), pos.getY()+random.nextFloat(), pos.getZ()+random.nextFloat(), 0, 0, 0);
                }
            }
        });
        return true;
    }
}

