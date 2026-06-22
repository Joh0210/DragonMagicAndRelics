package de.joh.dmnr.api.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BaseDragonMagicItem extends BaseTieredItem implements IDragonMagicItem {
    public BaseDragonMagicItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        this.tooltipAddition(tooltip);
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack pStack) {
        return this.isEnabled();
    }
}
