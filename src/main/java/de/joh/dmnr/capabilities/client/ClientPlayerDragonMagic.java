package de.joh.dmnr.capabilities.client;

public class ClientPlayerDragonMagic {
    private static boolean hasMajorFireResistance;
    private static boolean hasBurningFrenzy;

    public static void setMajorFireResistance(boolean hasMajorFireResistance) {
        ClientPlayerDragonMagic.hasMajorFireResistance = hasMajorFireResistance;
    }
    public static void setBurningFrenzy(boolean hasBurningFrenzy) {
        ClientPlayerDragonMagic.hasBurningFrenzy = hasBurningFrenzy;
    }

    public static boolean hasMajorFireResistance() {
        return hasMajorFireResistance;
    }

    public static boolean hasBurningFrenzy() {
        return hasBurningFrenzy;
    }
}
