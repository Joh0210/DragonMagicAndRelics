package de.joh.dragonmagicandrelics.item.items;

import com.mna.api.items.TieredItem;
import com.mna.tools.InventoryUtilities;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.items.wrapper.InvWrapper;

public class VoidfeatherCharm extends TieredItem {

    public VoidfeatherCharm(Properties itemProperties) {
        super(itemProperties);
    }

    public boolean consume(ServerPlayer player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() != this) {
            return false;
        } else {
            stack.hurtAndBreak(1, player, (e) -> {
                e.broadcastBreakEvent(hand);
            });
            return stack.isEmpty() || player.isCreative();
        }
    }

    public boolean consume(ServerPlayer player) {
        return InventoryUtilities.removeItemFromInventory(new ItemStack(this), true, true, new InvWrapper(player.getInventory()));
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    public int getItemEnchantability(ItemStack stack) {
        return 0;
    }

    public boolean isEnchantable(ItemStack p_77616_1_) {
        return false;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }
}
