package de.joh.dmnr.common.block.entity;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * Can a {@link TransposerBlockEntity} swap its area with the one of the Transposer its Rune of Marking is bound to?
 * @author Joh0210
 */
public enum TransposerStatus {
    /** Everything is set up, a redstone signal swaps the areas. */
    READY,
    /** The rune slot is empty. */
    NO_RUNE,
    /** The rune has not been bound to a block yet. */
    RUNE_WITHOUT_POSITION,
    /** The rune is bound to the Transposer it is in. */
    BOUND_TO_ITSELF,
    /** The focus slot is empty and the config requires a Ritual Focus, so there is no area. */
    NO_FOCUS,
    /** The areas of both Transposers overlap. */
    AREAS_OVERLAP,
    /** The marked position is in an unloaded chunk. It is loaded and checked when the Transposer is activated. */
    TARGET_NOT_LOADED,
    /** There is no Transposer at the marked position. */
    NO_TRANSPOSER,
    /** The config requires the same owner, but the other Transposer was placed by another player (or not by a player). */
    DIFFERENT_OWNER,
    /**
     * Only shown in the GUI: the last activation was prevented, since an owner may not change one of the areas.
     * @see TransposerBlockEntity#getDisplayStatus
     */
    AREA_PROTECTED;

    private static final TransposerStatus[] VALUES = values();

    /**
     * @return whether the status allows an activation (a target that is not loaded is checked on activation)
     */
    public boolean canActivate() {
        return this == READY || this == TARGET_NOT_LOADED;
    }

    public @NotNull Component getText() {
        return Component.translatable("gui.dmnr.transposer.status." + name().toLowerCase(Locale.ROOT));
    }

    /**
     * @param ordinal as synchronized to the client by the menu
     */
    public static @NotNull TransposerStatus byOrdinal(int ordinal) {
        return ordinal >= 0 && ordinal < VALUES.length ? VALUES[ordinal] : NO_RUNE;
    }
}
