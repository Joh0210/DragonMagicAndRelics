package de.joh.dragonmagicandrelics.item.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Item that drops from the Ender Dragon and is required for crafting
 * @author Joh0210
 */
public class DragonCore extends Item {
    public DragonCore(Properties pProperties) {
        super(pProperties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(ItemStack itemStack){
        return true;
    }
}
