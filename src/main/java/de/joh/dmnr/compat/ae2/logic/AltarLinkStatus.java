package de.joh.dmnr.compat.ae2.logic;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * State of the link between an {@link de.joh.dmnr.compat.ae2.blockentity.AltarInterfaceBlockEntity} and the
 * Altar of Arcana of its Rune of Marking.
 * @author Joh0210
 */
public enum AltarLinkStatus {
    /** The altar was found and is in reach. */
    LINKED,
    /** The rune slot is empty. */
    NO_RUNE,
    /** The rune has not been bound to a block yet. */
    RUNE_WITHOUT_POSITION,
    /** The marked position is in an unloaded chunk. */
    ALTAR_NOT_LOADED,
    /** There is no Altar of Arcana at the marked position. */
    NO_ALTAR,
    /** The altar does not scan the position of the interface for inventories. */
    OUT_OF_RANGE;

    private static final AltarLinkStatus[] VALUES = values();

    public boolean isLinked() {
        return this == LINKED;
    }

    public @NotNull Component getText() {
        return Component.translatable("gui.dmnr.altar_me_interface.status." + name().toLowerCase(Locale.ROOT));
    }

    /**
     * @param ordinal as synchronized to the client by the menu
     */
    public static @NotNull AltarLinkStatus byOrdinal(int ordinal) {
        return ordinal >= 0 && ordinal < VALUES.length ? VALUES[ordinal] : NO_RUNE;
    }
}
