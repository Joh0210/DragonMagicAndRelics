package de.joh.dragonmagicandrelics.capabilities.client;

import net.minecraft.nbt.CompoundTag;

public class ClientPlayerDragonMagic {
    private static boolean hasMajorFireResistance;

    public static void setMajorFireResistance(boolean hasMajorFireResistance) {
        ClientPlayerDragonMagic.hasMajorFireResistance = hasMajorFireResistance;
    }

    public static boolean hasMajorFireResistance() {
        return hasMajorFireResistance;
    }
}
