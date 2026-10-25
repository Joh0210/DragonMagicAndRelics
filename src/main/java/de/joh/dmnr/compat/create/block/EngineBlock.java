package de.joh.dmnr.compat.create.block;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.compat.create.CreateInit;
import de.joh.dmnr.compat.create.blockentity.EngineBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

/**
 * Permanent Create motor: a spinning shaft whose sides grip like a small cogwheel, with a fixed speed and stress
 * capacity from the config. Exists as the Arcane Engine and, as its upgrade, the Brimstone Engine.
 * @see EngineBlockEntity
 * @see de.joh.dmnr.common.util.CommonConfig
 * @author Joh0210
 */
public class EngineBlock extends AbstractMotorBlock<EngineBlockEntity> {
    private final String descriptionKey;
    private final Supplier<Integer> speed;
    private final Supplier<Integer> stressUnits;

    /**
     * @param name registry name of the block, for the key of the tooltip
     * @param speed rotation speed in RPM
     * @param stressUnits stress capacity in SU, the same at every speed
     */
    public EngineBlock(String name, Properties properties, Supplier<Integer> speed, Supplier<Integer> stressUnits) {
        super(properties);
        this.descriptionKey = "item." + DragonMagicAndRelics.MOD_ID + "." + name + ".description";
        this.speed = speed;
        this.stressUnits = stressUnits;
    }

    /**
     * @return rotation speed in RPM, as configured
     */
    public int getSpeed() {
        return speed.get();
    }

    /**
     * @return stress capacity in SU, as configured
     */
    public int getStressUnits() {
        return stressUnits.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable(descriptionKey).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("block.dmnr.engine.output", getSpeed(), getStressUnits()).withStyle(ChatFormatting.DARK_PURPLE));
    }

    // IBE:

    @Override
    public Class<EngineBlockEntity> getBlockEntityClass() {
        return EngineBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends EngineBlockEntity> getBlockEntityType() {
        return CreateInit.ENGINE_ENTITY.get();
    }
}
