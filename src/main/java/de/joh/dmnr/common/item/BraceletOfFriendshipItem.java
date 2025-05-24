package de.joh.dmnr.common.item;

import com.mna.api.events.ComponentApplyingEvent;
import com.mna.api.items.ITieredItem;
import com.mna.api.spells.SpellPartTags;
import com.mna.inventory.ItemInventoryBase;
import com.mna.items.ItemInit;
import com.mna.items.base.ItemBagBase;
import com.mna.items.filters.ItemFilterGroup;
import com.mna.items.ritual.PlayerCharm;
import de.joh.dmnr.client.gui.NamedBraceletOfFriendship;
import de.joh.dmnr.api.util.PlayerCharmFilter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * Friendly Fire off -> Protects friends from negative effects and dmg by the user.
 * <br>Friends are the user's pets and the pets of his friends.
 * Player-friends can be added by putting PlayerCharms them into the Bracele
 */
public class BraceletOfFriendshipItem extends ItemBagBase implements ICurioItem, ITieredItem<BraceletOfFriendshipItem> {

    private int _tier = -1;

    @Override
    public void setCachedTier(int tier) {
        this._tier = tier;
    }

    @Override
    public int getCachedTier() {
        return this._tier;
    }

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
            if (item.getItem() == ItemInit.PLAYER_CHARM.get() && ((PlayerCharm)item.getItem()).GetPlayerTarget(item, world) != null) {
                ret.add(((PlayerCharm)item.getItem()).GetPlayerTarget(item, world));
            }
        }
        return ret.toArray(new Player[0]);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player player, @NotNull InteractionHand hand) {
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


    /**
     * Returns the player if the entity is a player or is owned by a player.
     *
     * @param entity The entity to resolve.
     * @return The owning player, or null if none.
     */
    @Nullable
    public static Player playerOrOwner(Entity entity){
        while(entity instanceof OwnableEntity){
            entity = ((OwnableEntity)entity).getOwner();
        }

        return (entity instanceof Player) ? (Player) entity : null;
    }

    private static boolean shouldProtect(LivingEntity sourceEntity, Entity  target){
        // can't protect the user from itself
        if (sourceEntity == target) return false;

        Player referenceTarget = playerOrOwner(target);

        // can't protect nonPlayer entities without owners
        if (referenceTarget == null) return false;

        for (SlotResult result : CuriosApi.getCuriosHelper().findCurios(sourceEntity, de.joh.dmnr.common.init.ItemInit.BRACELET_OF_FRIENDSHIP.get())) {

            ItemStack stack = result.stack();
            Item item = stack.getItem();

            if (!(item instanceof BraceletOfFriendshipItem bracelet)) continue;

            // Protect pets of the user
            if (referenceTarget == sourceEntity) {
                return true;
            }

            // Protect Friends and their pets
            for (Player friend : bracelet.getPlayerTargets(stack, sourceEntity.level())) {
                if (friend == referenceTarget) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Friendly Fire off: Protects friends from negative effects by the caster if BraceletOfFriendshipItem is worn.
     */
    public static void eventHandleProtectFriends(ComponentApplyingEvent event){
        if(event.getComponent().getUseTag() == SpellPartTags.HARMFUL && event.getTarget().isLivingEntity()){
            if (shouldProtect(event.getSource().getCaster(), event.getTarget().getEntity())){
                event.setCanceled(true);
            }
        }
    }

    /**
     * Friendly Fire off: Protects friends from dmg by the user if BraceletOfFriendshipItem is worn.
     */
    public static void eventHandleProtectFriends(LivingAttackEvent event){
        if(event.getSource().getEntity() instanceof LivingEntity sourceEntity && shouldProtect(sourceEntity, event.getEntity())){
            event.setCanceled(true);
        }
    }
}
