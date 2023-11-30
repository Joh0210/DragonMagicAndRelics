package de.joh.dmnr.common.item;

import com.mna.api.items.TieredItem;
import com.mna.tools.InventoryUtilities;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;

public class VoidfeatherCharmItem extends TieredItem {

    public VoidfeatherCharmItem(Properties itemProperties) {
        super(itemProperties);
    }

    public boolean consume(ServerPlayer player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() != this) {
            return false;
        } else {
            stack.hurtAndBreak(1, player, (e) -> e.broadcastBreakEvent(hand));
            return stack.isEmpty() || player.isCreative();
        }
    }

    public boolean consume(ServerPlayer player) {
        return InventoryUtilities.removeItemFromInventory(new ItemStack(this), true, true, new InvWrapper(player.getInventory()));
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }
}
