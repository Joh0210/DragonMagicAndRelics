package de.joh.dmnr.capabilities.dragonmagic;

import de.joh.dmnr.common.spell.component.AlternativeRecallComponent;
import de.joh.dmnr.common.spell.component.MarkComponent;
import de.joh.dmnr.common.spell.shape.AtMarkShape;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Generate, load and save the NBT data from the player for PlayerDragonMagic
 * @see PlayerDragonMagic
 * @see MarkComponent
 * @see AlternativeRecallComponent
 * @see AtMarkShape
 * @author Joh0210
 */
public class PlayerDragonMagicProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerDragonMagic> PLAYER_DRAGON_MAGIC = CapabilityManager.get(new CapabilityToken<>() {
    });

    private PlayerDragonMagic secondChance = null;
    private final LazyOptional<PlayerDragonMagic> optional = LazyOptional.of(this::createPlayerDragonMagic);

    private PlayerDragonMagic createPlayerDragonMagic() {
        if(this.secondChance == null){
            this.secondChance = new PlayerDragonMagic();
        }

        return this.secondChance;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == PLAYER_DRAGON_MAGIC){
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerDragonMagic().saveNBT(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerDragonMagic().loadNBT(nbt);
    }
}
