package de.joh.dmnr.common.item;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.items.ITieredItem;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import de.joh.dmnr.common.init.ItemInit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * A cake to eat endlessly with mana consumption.
 */
public class ManaCakeItem extends Item implements ITieredItem<ManaCakeItem> {
    /**
     * Mana cost to "repair" the cake
     */
    private static final int MANA_COSTS = 80;

    /**
     * How much nutrition does the cake give?
     */
    private static final int NUTRITION = 4;

    /**
     * How much saturation does the cake give?
     */
    private static final float SATURATION = 0.4f;

    /**
     * M&A Item Tier:
     * Determined by the crafting recipe Tier
     */
    private int _tier = -1;

    public ManaCakeItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON)
                .food((new FoodProperties.Builder()).nutrition(NUTRITION).saturationMod(SATURATION).alwaysEat().build()));
    }

    /**
     * If null is returned, the item will be consumed because there was not enough mana
     * Call from the game itself.
     */
    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemstack, @NotNull Level world, @NotNull LivingEntity entity) {
        ItemStack retval = new ItemStack(ItemInit.MANA_CAKE.get());
        super.finishUsingItem(itemstack, world, entity);
        IPlayerMagic magic = entity.getCapability(PlayerMagicProvider.MAGIC).orElse(null);

        if (itemstack.isEmpty() && entity instanceof Player && magic != null && magic.getCastingResource().hasEnoughAbsolute(entity, MANA_COSTS)) {
            magic.getCastingResource().consume(entity, MANA_COSTS);
            return retval;
        } else {
            return itemstack;
        }
    }

    @Override
    public void setCachedTier(int tier) {
        this._tier = tier;
    }

    @Override
    public int getCachedtier() {
        return this._tier;
    }

}
