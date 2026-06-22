package de.joh.dmnr.capabilities.curioboost;

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

public class CurioBoostProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<CurioBoost> CURIO_BOOST = CapabilityManager.get(new CapabilityToken<>() {
    });

    private CurioBoost curioBoost = null;
    private final LazyOptional<CurioBoost> optional = LazyOptional.of(this::createCurioBoost);

    private CurioBoost createCurioBoost() {
        if(this.curioBoost == null){
            this.curioBoost = new CurioBoost();
        }

        return this.curioBoost;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == CURIO_BOOST){
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createCurioBoost().saveNBT(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createCurioBoost().loadNBT(nbt);
    }
}
