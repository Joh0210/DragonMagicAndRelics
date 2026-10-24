package de.joh.dmnr.compat.ae2.logic;

import com.mna.blocks.tileentities.wizard_lab.ArcanaAltarTile;
import com.mna.items.runes.ItemRuneMarking;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Resolves the Altar of Arcana a Rune of Marking points to.
 * @param status result of the lookup
 * @param altar the altar, only present if the status is {@link AltarLinkStatus#LINKED}
 * @author Joh0210
 */
public record AltarLink(@NotNull AltarLinkStatus status, @Nullable ArcanaAltarTile altar) {
    /**
     * Radius in which the Altar of Arcana searches for inventories (see {@code BlockPosCache} in its constructor).
     * Inventories beyond that are never asked for items.
     */
    public static final int ALTAR_SEARCH_RADIUS = 8;

    /**
     * A failed lookup, without an altar.
     */
    private AltarLink(@NotNull AltarLinkStatus status) {
        this(status, null);
    }

    public static @NotNull AltarLink resolve(@Nullable Level level, @NotNull BlockPos interfacePos, @NotNull ItemStack rune) {
        if (level == null || !(rune.getItem() instanceof ItemRuneMarking marking)) {
            return new AltarLink(AltarLinkStatus.NO_RUNE);
        }

        BlockPos altarPos = marking.getLocation(rune);
        if (altarPos == null) {
            return new AltarLink(AltarLinkStatus.RUNE_WITHOUT_POSITION);
        }

        // Never load chunks from here, the interface is ticked by the grid.
        if (!level.isLoaded(altarPos)) {
            return new AltarLink(AltarLinkStatus.ALTAR_NOT_LOADED);
        }

        if (!(level.getBlockEntity(altarPos) instanceof ArcanaAltarTile altar)) {
            return new AltarLink(AltarLinkStatus.NO_ALTAR);
        }

        if (Math.abs(altarPos.getX() - interfacePos.getX()) > ALTAR_SEARCH_RADIUS
                || Math.abs(altarPos.getY() - interfacePos.getY()) > ALTAR_SEARCH_RADIUS
                || Math.abs(altarPos.getZ() - interfacePos.getZ()) > ALTAR_SEARCH_RADIUS) {
            return new AltarLink(AltarLinkStatus.OUT_OF_RANGE);
        }

        return new AltarLink(AltarLinkStatus.LINKED, altar);
    }
}
