package de.joh.dragonmagicandrelics.networking;

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
    }

    public static <MSG> void sendToServer(MSG message){
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player){
        INSTANCE.send(PacketDistributor.PLAYER.with(()->player), message);
    }


}
