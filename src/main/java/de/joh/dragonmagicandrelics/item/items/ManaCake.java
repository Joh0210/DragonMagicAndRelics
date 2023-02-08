package de.joh.dragonmagicandrelics.item.items;

import com.ma.api.capabilities.IPlayerMagic;
import com.ma.capabilities.playerdata.magic.PlayerMagicProvider;
import de.joh.dragonmagicandrelics.CreativeModeTab;
import de.joh.dragonmagicandrelics.item.ItemInit;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.world.World;

/**
 * A cake to eat endlessly with mana consumption.
 */
public class ManaCake  extends Item {
    /**
     * Mana cost to "repair" the cake
     */
    private static final int MANA_COSTS = 60;

    /**
     * How much nutrition does the cake give?
     */
    private static final int NUTRITION = 4;

    /**
     * How much saturation does the cake give?
     */
    private static final float SATURATION = 0.6f;

    public ManaCake() {
        super(new Item.Properties().group(CreativeModeTab.CreativeModeTab).maxStackSize(1).rarity(Rarity.COMMON)
                .food((new Food.Builder()).hunger(NUTRITION).saturation(SATURATION).build()));
    }

    /**
     * If null is returned, the item will be consumed because there was not enough mana
     * Call from the game itself.
     */
    @Override
    public ItemStack onItemUseFinish(ItemStack itemstack, World world, LivingEntity entity) {
        ItemStack retval = new ItemStack(ItemInit.MANA_CAKE.get());
        super.onItemUseFinish(itemstack, world, entity);
        IPlayerMagic magic = entity.getCapability(PlayerMagicProvider.MAGIC).orElse(null);

        if (itemstack.isEmpty() && entity instanceof PlayerEntity && magic != null && magic.getCastingResource().getAmount() > MANA_COSTS) {
            magic.getCastingResource().consume(MANA_COSTS);
            return retval;
        } else {
            return itemstack;
        }
    }

}
