package de.joh.dmnr.capabilities.secondchance;

import de.joh.dmnr.common.event.CommonEventHandler;
import de.joh.dmnr.common.ritual.PhoenixRitual;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Saves the player's position upon death for the Phoenix ritual.
 * @see PhoenixRitual
 * @see CommonEventHandler
 * @author Joh0210
 */
public class PlayerSecondChance {
    private BlockPos position;
    private ResourceKey<Level> dimension;
    private boolean isValid = false;

    public void setSecondChance(Player player) {
        setSecondChance(player.blockPosition(), player.level().dimension());
    }

    public void setSecondChance(BlockPos position, ResourceKey<Level> dimension){
        this.position = position;
        this.dimension = dimension;
        this.isValid = true;
    }

    public boolean isValid() {
        return isValid;
    }

    public BlockPos getPosition(){
        return position;
    }

    public ResourceKey<Level> getDimension(){
        return dimension;
    }

    public void saveNBT(CompoundTag compound) {
        if (this.isValid) {
            CompoundTag nbt = new CompoundTag();
            nbt.putInt("x", this.position.getX());
            nbt.putInt("y", this.position.getY());
            nbt.putInt("z", this.position.getZ());
            nbt.putString("dimension_key_type", this.dimension.registry().toString());
            nbt.putString("dimension_key_value", this.dimension.location().toString());
            compound.put("second_chance_data", nbt);
        }
    }

    public void loadNBT(CompoundTag compound) {
        if (compound.contains("second_chance_data")) {
            CompoundTag nbt = compound.getCompound("second_chance_data");
            if (nbt.contains("x") && nbt.contains("y") && nbt.contains("z") && nbt.contains("dimension_key_type") && nbt.contains("dimension_key_value")) {
                this.position = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
                this.dimension = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(nbt.getString("dimension_key_value")));
                this.isValid = true;
            }
        }
    }
}

