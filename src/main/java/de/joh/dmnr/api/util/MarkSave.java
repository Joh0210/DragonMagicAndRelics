package de.joh.dmnr.api.util;

import com.mna.api.items.IPositionalItem;
import com.mna.items.ItemInit;
import com.mna.items.ritual.ItemPlayerCharm;
import de.joh.dmnr.capabilities.dragonmagic.PlayerDragonMagic;
import de.joh.dmnr.capabilities.dragonmagic.PlayerDragonMagicProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class is a replacement for the Rune of Marking. Up to one instance of this class is saved per player and per dimension.
 * Furthermore, an instance can serve as a helper to pass marks.
 * @see PlayerDragonMagic
 * @see com.mna.items.runes.MarkBookItem
 * @author Joh0210
 */
public class MarkSave {
    private BlockPos position;
    private Direction direction;

    /**
     * Storing the position by the actual values.
     */
    public MarkSave(BlockPos position, Direction direction){
        this.position = position;
        this.direction = direction;
    }

    /**
     * Saving the position by passed NBT data.
     * Format must correspond to saveNBT()!
     */
    public MarkSave(CompoundTag nbt) {
        if (nbt.contains("x") && nbt.contains("y") && nbt.contains("z") && nbt.contains("direction")) {
            this.position = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
            this.direction = Direction.byName(nbt.getString("direction"));
        }
    }

    @Nullable
    public BlockPos getPosition(){
        return position;
    }

    @Nullable
    public Direction getDirection(){
        return direction;
    }

    public CompoundTag saveNBT() {
        CompoundTag nbt = new CompoundTag();

        nbt.putInt("x", this.position.getX());
        nbt.putInt("y", this.position.getY());
        nbt.putInt("z", this.position.getZ());
        nbt.putString("direction", this.direction.getName());

        return nbt;
    }

    /**
     * Call to obtain either a Rune of Marking or the Dragon Magic Mark when searching for a Mark from a Player.
     * @param source Which player should be checked for the mark?
     * @param world In which world is the mark looking for?
     * @return Returns the Mark to use. The mark in hand has the highest priority. Then the Dragon Magic Mark.
     */
    @Nullable
    public static MarkSave getMark(@NotNull Player source,@NotNull Level world){
        return getMark(source, world, false);
    }

    @Nullable
    public static MarkSave getMark(@NotNull Player source,@NotNull Level world, boolean allowPlayerCharm){
        // Rune of Marking:
        ItemStack markingRune = source.getMainHandItem().getItem() != ItemInit.RUNE_MARKING.get() && source.getMainHandItem().getItem() != ItemInit.BOOK_MARKS.get() ? source.getOffhandItem() : source.getMainHandItem();
        if (markingRune.getItem() instanceof IPositionalItem) {
            return new MarkSave(((IPositionalItem)markingRune.getItem()).getLocation(markingRune), ((IPositionalItem)markingRune.getItem()).getFace(markingRune));
        }

        // Player Charm:
        ItemStack playerCharm = source.getMainHandItem().getItem() != ItemInit.PLAYER_CHARM.get() ? source.getOffhandItem() : source.getMainHandItem();
        if(allowPlayerCharm && playerCharm.getItem() instanceof ItemPlayerCharm) {
            Player target = (((ItemPlayerCharm) playerCharm.getItem()).GetPlayerTarget(playerCharm, world));
            if(target != null){
                return new MarkSave(target.blockPosition().above(-1), Direction.UP);
            }
        }

            AtomicReference<MarkSave> playerMark = new AtomicReference<>();
        AtomicBoolean isNotNull = new AtomicBoolean(false);
        source.getCapability(PlayerDragonMagicProvider.PLAYER_DRAGON_MAGIC).ifPresent(magic -> {
            if(magic.hasValidMark(world)){
                isNotNull.set(true);
                playerMark.set(magic.getMark(world));
            }
        });
        return isNotNull.get() ? playerMark.get() : null;
    }
}
