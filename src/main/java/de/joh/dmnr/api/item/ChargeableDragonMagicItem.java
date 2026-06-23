package de.joh.dmnr.api.item;

import com.mna.api.items.ChargeableItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ChargeableDragonMagicItem extends ChargeableItem implements IDragonMagicItem {
    public ChargeableDragonMagicItem(Item.Properties properties, float maxMana) {
        super(properties, maxMana);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        this.tooltipAddition(tooltip);
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack pStack) {
        return this.isEnabled() && this.hasDragonMagic();
    }
}

