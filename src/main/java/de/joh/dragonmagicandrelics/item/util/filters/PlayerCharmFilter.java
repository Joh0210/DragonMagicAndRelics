package de.joh.dragonmagicandrelics.item.util.filters;

import com.mna.items.ItemInit;
import com.mna.items.filters.ItemFilter;
import com.mna.items.filters.ItemFilterGroup;
import net.minecraft.world.item.ItemStack;

public class PlayerCharmFilter extends ItemFilter {
    public static final ItemFilterGroup ANY_PLAYER_CHARM = new ItemFilterGroup(new PlayerCharmFilter());

    public boolean IsValidItem(ItemStack stack) {
        return stack.getItem() == ItemInit.PLAYER_CHARM.get() && ItemInit.PLAYER_CHARM.get().getPlayerUUID(stack) != null;
    }
}