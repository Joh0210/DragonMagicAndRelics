package de.joh.dmnr.common.item;

import com.mna.api.items.TieredItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

/**
 * A piece of coal that can burn endlessly
 * @author Joh0210
 */
public class BrimstoneCoalItem extends TieredItem {

    public BrimstoneCoalItem() {
        super(new Item.Properties().stacksTo(1).fireResistant());
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack)
    {
        ItemStack returnStack = new ItemStack(this);
        returnStack.setTag(stack.getTag());
        return returnStack;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack)
    {
        return true;
    }

    @Override
    public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType)
    {
        return 200;
    }
}
