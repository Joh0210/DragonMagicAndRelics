package de.joh.dmnr.api.util;

import com.mna.items.filters.ItemFilter;
import com.mna.items.filters.ItemFilterGroup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;

public class PotionFilter extends ItemFilter {
    public static final ItemFilterGroup ANY_POTION = new ItemFilterGroup(new PotionFilter());

    public boolean IsValidItem(ItemStack stack) {
        return stack.getItem() instanceof PotionItem;
    }
}
