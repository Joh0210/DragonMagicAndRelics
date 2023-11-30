package de.joh.dmnr.common.item;

import com.mna.inventory.ItemInventoryBase;
import com.mna.items.ItemInit;
import com.mna.items.base.ItemBagBase;
import com.mna.items.filters.ItemFilterGroup;
import com.mna.items.ritual.ItemPlayerCharm;
import de.joh.dmnr.client.gui.NamedBraceletOfFriendship;
import de.joh.dmnr.api.util.PlayerCharmFilter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.ArrayList;

public class BraceletOfFriendshipItem extends ItemBagBase implements ICurioItem {

    public BraceletOfFriendshipItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return false;
    }

    public Player[] getPlayerTargets(ItemStack stack, Level world) {
        ArrayList<Player> ret = new ArrayList<>();
        ItemInventoryBase inv = new ItemInventoryBase(stack);
        for(int i = 0; i < 6; i++){
            ItemStack item = inv.getStackInSlot(i);
            if (item.getItem() == ItemInit.PLAYER_CHARM.get() && ((ItemPlayerCharm)item.getItem()).GetPlayerTarget(item, world) != null) {
                ret.add(((ItemPlayerCharm)item.getItem()).GetPlayerTarget(item, world));
            }
        }
        return ret.toArray(new Player[0]);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {
            ItemStack held = player.getItemInHand(hand);
            if (this.openGuiIfModifierPressed(held, player, world)) {
                return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
            }
        }

        return new InteractionResultHolder<>(InteractionResult.PASS, player.getItemInHand(hand));
    }

    @Override
    public ItemFilterGroup filterGroup() {
        return PlayerCharmFilter.ANY_PLAYER_CHARM;
    }

    @Override
    public MenuProvider getProvider(ItemStack itemStack) {
        return new NamedBraceletOfFriendship(itemStack);
    }
}
