package de.joh.dragonmagicandrelics.capabilities.dragonmagic;

import de.joh.dragonmagicandrelics.utils.MarkSave;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class contains the permanent data that is stored on a player for the magic components of this mod.
 * Each function here is called on a player-specific basis, since every player owns an instance of this class.
 * Currently includes: Player specific save for the Rune of Marking
 * @see de.joh.dragonmagicandrelics.spells.components.ComponentMark
 * @see de.joh.dragonmagicandrelics.spells.components.ComponentAlternativeRecall
 * @see de.joh.dragonmagicandrelics.spells.shapes.ShapeAtMark
 * @author Joh0210
 */
public class PlayerDragonMagic {
    /**
     * List of dimensions with corresponding mark, for ComponentMark, ComponentAlternativeRecall, ...
     */
    private HashMap<ResourceKey<Level>, MarkSave> markMap = new HashMap<>();

    /**
     * Save this position as a mark, in this dimension, for the player who executes it.
     */
    public void mark(BlockPos position, Direction direction, Level world){
        markMap.put(world.dimension(), new MarkSave(position, direction));
    }

    /**
     * @return Is there a mark from the player in this dimension?
     */
    public boolean hasValidMark(Level world){
        return (markMap.get(world.dimension()) != null);
    }

    /**
     * @return Mark of this dimension from the player
     */
    @Nullable
    public MarkSave getMark(Level world){
        return markMap.get(world.dimension());
    }

    public void copyFrom(PlayerDragonMagic source) {
        this.markMap = source.getMarkMap();
    }

    public HashMap<ResourceKey<Level>, MarkSave> getMarkMap() {
        return markMap;
    }

    public void saveNBT(CompoundTag compound) {
        CompoundTag nbt = new CompoundTag();

        nbt.putInt("mark_map_size", markMap.size());

        int i = 0;

        for(Map.Entry<ResourceKey<Level>, MarkSave> entry : markMap.entrySet()) {
            nbt.putString("mark_map_dimension_" + i, entry.getKey().location().toString());
            nbt.put("mark_map_mark_save_" + i, entry.getValue().saveNBT());

            i++;
        }

        compound.put("dragon_magic_data", nbt);
    }

    public void loadNBT(CompoundTag compound) {
        if (compound.contains("dragon_magic_data")) {
            CompoundTag nbt = compound.getCompound("dragon_magic_data");

            if(nbt.contains("mark_map_size")){
                for(int i = 0; i< nbt.getInt("mark_map_size"); i++){
                    markMap.put(
                            ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(nbt.getString("mark_map_dimension_" + i))),
                            new MarkSave(nbt.getCompound("mark_map_mark_save_" + i))
                    );
                }
            }
        }
    }
}

