package de.joh.dmnr.networking.packet;

import de.joh.dmnr.client.gui.ContainerDragonMageArmorDye;
import de.joh.dmnr.client.gui.GuiDragonMageArmorDye;
import de.joh.dmnr.common.item.DragonMageArmorItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ArmorItem;
import net.minecraftforge.network.NetworkEvent;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * This class is the package that is sent to the server when colors are applied in the dyeing GUI of the
 * Dragon Mage Armor. Only accepted while the player has the dyeing GUI open.
 * @see GuiDragonMageArmorDye
 * @see DragonMageArmorItem#getDyeTargets
 * @author Joh0210
 */
public class DyeDragonMageArmorC2SPacket {
    private final int primaryColor;
    private final int secondaryColor;
    private final EnumSet<ArmorItem.Type> pieces;

    /**
     * @param primaryColor RGB color of the ornaments
     * @param secondaryColor RGB color of the plates
     * @param pieces the armor types to dye, other pieces keep their colors
     */
    public DyeDragonMageArmorC2SPacket(int primaryColor, int secondaryColor, Set<ArmorItem.Type> pieces){
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.pieces = pieces.isEmpty() ? EnumSet.noneOf(ArmorItem.Type.class) : EnumSet.copyOf(pieces);
    }

    public DyeDragonMageArmorC2SPacket(FriendlyByteBuf buf){
        this.primaryColor = buf.readInt();
        this.secondaryColor = buf.readInt();
        this.pieces = buf.readEnumSet(ArmorItem.Type.class);
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(primaryColor);
        buf.writeInt(secondaryColor);
        buf.writeEnumSet(pieces, ArmorItem.Type.class);
    }

    /**
     * Processing of the event on the SERVER SIDE!
     */
    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()->{
            ServerPlayer player = context.getSender();

            if(player != null && player.containerMenu instanceof ContainerDragonMageArmorDye menu){
                DragonMageArmorItem.getDyeTargets(player, menu.getHand()).forEach((type, stack) -> {
                    if(pieces.contains(type)){
                        DragonMageArmorItem.setColors(stack, primaryColor, secondaryColor);
                    }
                });
            }
        });
        return true;
    }
}
