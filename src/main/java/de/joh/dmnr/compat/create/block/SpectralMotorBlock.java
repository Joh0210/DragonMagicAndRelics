package de.joh.dmnr.compat.create.block;

import com.mna.api.sound.SFX;
import de.joh.dmnr.compat.create.CreateInit;
import de.joh.dmnr.compat.create.blockentity.SpectralMotorBlockEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.common.util.ForgeSoundType;

/**
 * Temporary Create motor, conjured by the {@link de.joh.dmnr.compat.create.spell.SpectralMotorComponent}
 * @see SpectralMotorBlockEntity
 * @author Joh0210
 */
public class SpectralMotorBlock extends AbstractMotorBlock<SpectralMotorBlockEntity> {
    private static final SoundType SOUND_TYPE = new ForgeSoundType(1.0F, 1.0F,
            () -> SFX.Event.Ritual.FAERIE_LEAVE,
            () -> SoundEvents.AMETHYST_BLOCK_STEP,
            () -> SoundEvents.AMETHYST_BLOCK_PLACE,
            () -> SoundEvents.AMETHYST_BLOCK_HIT,
            () -> SoundEvents.AMETHYST_BLOCK_FALL);

    public SpectralMotorBlock() {
        super(Properties.of()
                .mapColor(MapColor.COLOR_PURPLE)
                .strength(0.5F)
                .sound(SOUND_TYPE)
                .lightLevel(state -> 7)
                .noOcclusion()
                .noLootTable()
                .pushReaction(PushReaction.BLOCK));
    }

    // IBE:

    @Override
    public Class<SpectralMotorBlockEntity> getBlockEntityClass() {
        return SpectralMotorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SpectralMotorBlockEntity> getBlockEntityType() {
        return CreateInit.SPECTRAL_MOTOR_ENTITY.get();
    }
}
