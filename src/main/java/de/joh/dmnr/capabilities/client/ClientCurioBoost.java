package de.joh.dmnr.capabilities.client;

import java.util.HashSet;
import java.util.Set;

public class ClientCurioBoost {
    private static boolean enabled = false;
    private static final Set<String> blacklist = new HashSet<>();

    public static void setEnabled(boolean enabled) {
        ClientCurioBoost.enabled = enabled;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void blacklistID(String element) {
        blacklist.add(element);
    }

    public static void whitelistID(String element) {
        blacklist.remove(element);
    }

    public static boolean isBlacklisted(String element) {
        return blacklist.contains(element);
    }
}
