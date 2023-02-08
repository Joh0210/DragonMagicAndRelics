package de.joh.dragonmagicandrelics.utils;

import com.ma.items.ItemInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import javax.annotation.Nullable;

/**
 * This class is a replacement for the Rune of Marking. Up to one instance of this class is saved per player and per dimension.
 * Furthermore, an instance can serve as a helper to pass marks.
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
     * @param nbt
     */
    public MarkSave(CompoundNBT nbt) {
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

    public CompoundNBT saveNBT() {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putInt("x", this.position.getX());
        nbt.putInt("y", this.position.getY());
        nbt.putInt("z", this.position.getZ());
        nbt.putString("direction", this.direction.getName2());

        return nbt;
    }

    /**
     * Call to obtain either a Rune of Marking or the Dragon Magic Mark when searching for a Mark from a Player.
     * @param source Which player should be checked for the mark?
     * @param world In which world is the mark looking for?
     * @return Returns the Mark to use. The mark in hand has the highest priority. Then the Dragon Magic Mark.
     */
    @Nullable
    public static MarkSave getMark(PlayerEntity source, World world){
        if(source == null){ //Wasn't executed by a player?
            return null;
        }

        ItemStack markingRune = source.getHeldItemMainhand().getItem() != ItemInit.RUNE_MARKING.get() ? source.getHeldItemOffhand() : source.getHeldItemMainhand();

        if (markingRune.getItem() == ItemInit.RUNE_MARKING.get()) {
            return new MarkSave(ItemInit.RUNE_MARKING.get().getLocation(markingRune), ItemInit.RUNE_MARKING.get().getFace(markingRune));
        }

//        AtomicReference<MarkSave> playerMark = new AtomicReference<>();
//        AtomicBoolean isNotNull = new AtomicBoolean(false);
//        source.getCapability(PlayerDragonMagicProvider.PLAYER_DRAGON_MAGIC).ifPresent(magic -> {
//            if(magic.hasValidMark(world)){
//                isNotNull.set(true);
//                playerMark.set(magic.getMark(world));
//            }
//        });
//        return isNotNull.get() ? playerMark.get() : null;
        return null;
    }
}
