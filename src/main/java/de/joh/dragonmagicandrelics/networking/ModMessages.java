package de.joh.dragonmagicandrelics.networking;

import de.joh.dragonmagicandrelics.networking.packet.ToggleBurningFrenzyS2CPacket;
import de.joh.dragonmagicandrelics.networking.packet.ToggleFlightC2SPacket;
import de.joh.dragonmagicandrelics.networking.packet.ToggleMajorFireResS2CPacket;
import de.joh.dragonmagicandrelics.networking.packet.ToggleNightVisionC2SPacket;
import de.joh.dragonmagicandrelics.utils.RLoc;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * Communication between client and server with packages of this mod.
 * @author Joh0210
 */
public class ModMessages {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;

    /**
     * Each massage has its own ID
     * @return ID from this message
     */
    private static int id(){
        return packetId++;
    }

    public static void register(){
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(RLoc.create("messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(ToggleNightVisionC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ToggleNightVisionC2SPacket::new)
                .encoder((ToggleNightVisionC2SPacket::toBytes))
                .consumer(ToggleNightVisionC2SPacket::handle)
                .add();

        net.messageBuilder(ToggleFlightC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ToggleFlightC2SPacket::new)
                .encoder((ToggleFlightC2SPacket::toBytes))
                .consumer(ToggleFlightC2SPacket::handle)
                .add();

        net.messageBuilder(ToggleMajorFireResS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ToggleMajorFireResS2CPacket::new)
                .encoder(ToggleMajorFireResS2CPacket::toBytes)
                .consumer(ToggleMajorFireResS2CPacket::handle)
                .add();

        net.messageBuilder(ToggleBurningFrenzyS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ToggleBurningFrenzyS2CPacket::new)
                .encoder(ToggleBurningFrenzyS2CPacket::toBytes)
                .consumer(ToggleBurningFrenzyS2CPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message){
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player){
        INSTANCE.send(PacketDistributor.PLAYER.with(()->player), message);
    }


}
