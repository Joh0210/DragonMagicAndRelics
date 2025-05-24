package de.joh.dmnr.common.item;

import com.mna.entities.utility.PresentItem;
import de.joh.dmnr.common.init.ItemInit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
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

    /**
     * Drops the Dragon Core when the Ender Dragon dies
     */
    public static void eventHandleDragonDeath(LivingDeathEvent event){
        if(event.getEntity().getType() == EntityType.ENDER_DRAGON){
            Level world = event.getEntity().level();
            PresentItem item = new PresentItem(world, event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), new ItemStack(ItemInit.DRAGON_CORE.get()));
            world.addFreshEntity(item);
        }
    }
}
