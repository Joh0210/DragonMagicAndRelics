package de.joh.dragonmagicandrelics.item.items;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import de.joh.dragonmagicandrelics.CreativeModeTab;
import de.joh.dragonmagicandrelics.item.ItemInit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

/**
 * A cake to eat endlessly with mana consumption.
 */
public class ManaCake  extends Item {
    /**
     * Mana cost to "repair" the cake
     */
    private static int MANA_COSTS = 60;

    /**
     * How much nutrition does the cake give?
     */
    private static int NUTRITION = 4;

    /**
     * How much saturation does the cake give?
     */
    private static float SATURATION = 0.6f;

    public ManaCake() {
        super(new Item.Properties().tab(CreativeModeTab.CreativeModeTab).stacksTo(1).rarity(Rarity.COMMON)
                .food((new FoodProperties.Builder()).nutrition(NUTRITION).saturationMod(SATURATION).build()));
    }

    /**
     * If null is returned, the item will be consumed because there was not enough mana
     * Call from the game itself.
     */
    @Override
    public ItemStack finishUsingItem(ItemStack itemstack, Level world, LivingEntity entity) {
        ItemStack retval = new ItemStack(ItemInit.MANA_CAKE.get());
        super.finishUsingItem(itemstack, world, entity);
        IPlayerMagic magic = (IPlayerMagic)((Player)entity).getCapability(PlayerMagicProvider.MAGIC).orElse((IPlayerMagic) null);

        if (itemstack.isEmpty() && entity instanceof Player && magic != null && magic.getCastingResource().hasEnoughAbsolute(entity, MANA_COSTS)) {
            magic.getCastingResource().consume(entity, MANA_COSTS);
            return retval;
        } else {
            return itemstack;
        }
    }

}
