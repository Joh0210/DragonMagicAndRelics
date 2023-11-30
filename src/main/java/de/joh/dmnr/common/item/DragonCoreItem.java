package de.joh.dmnr.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

/**
 * Item that drops from the Ender Dragon and is required for crafting
 * @author Joh0210
 */
public class DragonCoreItem extends Item {
    public DragonCoreItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@NotNull ItemStack itemStack){
        return true;
    }
}
