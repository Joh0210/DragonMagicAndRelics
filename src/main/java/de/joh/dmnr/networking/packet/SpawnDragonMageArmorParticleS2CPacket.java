package de.joh.dmnr.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.Random;
import java.util.function.Supplier;

/**
 * Packet to spawn particles and play sound when a player receives the Dragon Mage Armor.
 * @author Joh0210
 */
public class SpawnDragonMageArmorParticleS2CPacket {
    private final int playerId;

    public SpawnDragonMageArmorParticleS2CPacket(int playerId) {
        this.playerId = playerId;
    }

    public SpawnDragonMageArmorParticleS2CPacket(FriendlyByteBuf buf) {
        this.playerId = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(playerId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientLevel world = Minecraft.getInstance().level;
            if (world != null) {
                Player player = (Player) world.getEntity(playerId);
                if (player != null) {
                    Random random = new Random();
                    world.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 1.0F, 1.0F, false);
                    for (int i = 0; i < 200; i++) {
                        double d0 = player.getX() + (random.nextDouble() - 0.5D) * 1.5D;
                        double d1 = player.getY() + random.nextDouble() * 2.0D;
                        double d2 = player.getZ() + (random.nextDouble() - 0.5D) * 1.5D;
                        world.addParticle(ParticleTypes.DRAGON_BREATH, d0, d1, d2, (random.nextDouble() - 0.5D) * 0.15D, 0.15D, (random.nextDouble() - 0.5D) * 0.15D);
                        if (i % 5 == 0) {
                            world.addParticle(ParticleTypes.EXPLOSION_EMITTER, player.getX() + (random.nextDouble() - 0.5D) * 0.5D, player.getY() + 0.5D + random.nextDouble() * 1.5D, player.getZ() + (random.nextDouble() - 0.5D) * 0.5D, 0, 0, 0);
                        }
                        if (i % 2 == 0) {
                            world.addParticle(ParticleTypes.END_ROD, d0, d1, d2, (random.nextDouble() - 0.5D) * 0.05D, 0.1D, (random.nextDouble() - 0.5D) * 0.05D);
                        }
                        if (i % 4 == 0) {
                            world.addParticle(ParticleTypes.WITCH, d0, d1, d2, (random.nextDouble() - 0.5D) * 0.2D, 0.2D, (random.nextDouble() - 0.5D) * 0.2D);
                        }
                        if (i % 10 == 0) {
                            world.addParticle(ParticleTypes.LARGE_SMOKE, player.getX(), player.getY() + 1.0D, player.getZ(), (random.nextDouble() - 0.5D) * 0.1D, 0.1D, (random.nextDouble() - 0.5D) * 0.1D);
                        }
                    }
                }
            }
        });
        return true;
    }
}
