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
 * Dragon Mage Armor. It also carries the wings, which only the chestplate takes over.
 * Only accepted while the player has the dyeing GUI open.
 * @see GuiDragonMageArmorDye
 * @see DragonMageArmorItem#getDyeTargets
 * @author Joh0210
 */
public class DyeDragonMageArmorC2SPacket {
    private final int primaryColor;
    private final int secondaryColor;
    private final DragonMageArmorItem.WingDesign wingDesign;
    private final int wingPrimaryColor;
    private final int wingSecondaryColor;
    private final boolean wingCape;
    private final EnumSet<ArmorItem.Type> pieces;

    /**
     * @param primaryColor RGB color of the ornaments
     * @param secondaryColor RGB color of the plates
     * @param wingDesign wings of the chestplate
     * @param wingPrimaryColor primary RGB color of the wings (membrane, leaves or tips)
     * @param wingSecondaryColor secondary RGB color of the wings (bones, branches or the part near the back)
     * @param wingCape whether a wearer with a visible cape shows it on the wings instead of the design
     * @param pieces the armor types to dye, other pieces keep their colors (and the chestplate its wings)
     */
    public DyeDragonMageArmorC2SPacket(int primaryColor, int secondaryColor, DragonMageArmorItem.WingDesign wingDesign,
                                       int wingPrimaryColor, int wingSecondaryColor, boolean wingCape, Set<ArmorItem.Type> pieces){
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.wingDesign = wingDesign;
        this.wingPrimaryColor = wingPrimaryColor;
        this.wingSecondaryColor = wingSecondaryColor;
        this.wingCape = wingCape;
        this.pieces = pieces.isEmpty() ? EnumSet.noneOf(ArmorItem.Type.class) : EnumSet.copyOf(pieces);
    }

    public DyeDragonMageArmorC2SPacket(FriendlyByteBuf buf){
        this.primaryColor = buf.readInt();
        this.secondaryColor = buf.readInt();
        this.wingDesign = buf.readEnum(DragonMageArmorItem.WingDesign.class);
        this.wingPrimaryColor = buf.readInt();
        this.wingSecondaryColor = buf.readInt();
        this.wingCape = buf.readBoolean();
        this.pieces = buf.readEnumSet(ArmorItem.Type.class);
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(primaryColor);
        buf.writeInt(secondaryColor);
        buf.writeEnum(wingDesign);
        buf.writeInt(wingPrimaryColor);
        buf.writeInt(wingSecondaryColor);
        buf.writeBoolean(wingCape);
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
                        DragonMageArmorItem.setWings(stack, wingDesign, wingPrimaryColor, wingSecondaryColor, wingCape);
                    }
                });
            }
        });
        return true;
    }
}
