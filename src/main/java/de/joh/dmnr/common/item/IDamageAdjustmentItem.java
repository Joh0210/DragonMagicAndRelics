package de.joh.dmnr.common.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Interface for a special type of curios item that modifies incoming and outgoing damage based on conditions.
 * @author Joh0210
 */
public interface IDamageAdjustmentItem {

//    if(revengeItem.isEquippedAndHasMana(event.getEntity(), revengeItem.amount, true)){
//        revengeItem.revenge(defender, attacker, event.getAmount());
//        // todo: add Particle?
//    }

    /**
     * Defines if an item should apply it's mod on DEFENDS.
     * <br> All costs must be paid within this method.
     * @return if true apply the mod
     */
    boolean canAdjustDefending(LivingHurtEvent event, Player defender, ItemStack damageAdjustmentItem);

    /**
     * Adjustment for Defending
     * @param amount the amount od dmg before the adjustment
     * @return @return the amount of dmg, after the adjustment
     */
    float adjustDefending(LivingHurtEvent event, Player defender, ItemStack damageAdjustmentItem, float amount);

    /**
     * Defines if an item should apply it's mod on ATTACKS.
     * <br> All costs must be paid within this method.
     * @return if true apply the mod
     */
    boolean canAdjustAttacking(LivingHurtEvent event, Player attacker, ItemStack damageAdjustmentItem);


    /**
     * Adjustment for Attacking
     * @param amount the amount od dmg before the adjustment
     * @return @return the amount of dmg, after the adjustment
     */
    float adjustAttacking(LivingHurtEvent event, Player attacker, ItemStack damageAdjustmentItem, float amount);

    static boolean eventHandleDamageAdjustment(LivingHurtEvent event){
        if(event.isCanceled()) return true;

        AtomicReference<Float> amount = new AtomicReference<>(event.getAmount());
        AtomicReference<Boolean> adjusted = new AtomicReference<>(false);

        if(event.getEntity() instanceof Player defender){
            CuriosApi.getCuriosInventory(defender).ifPresent(curiosProvider -> curiosProvider.getCurios().forEach((identifier, stackHandler) -> {
                for (int k = 0; k < stackHandler.getSlots(); k++) {
                    ItemStack stack = stackHandler.getStacks().getStackInSlot(k);
                    if (!stack.isEmpty() && stack.getItem() instanceof IDamageAdjustmentItem damageAdjustmentItem) {
                        if(damageAdjustmentItem.canAdjustDefending(event, defender, stack)){
                            amount.set(damageAdjustmentItem.adjustDefending(event, defender, stack, amount.get()));
                            adjusted.set(true);
                        }
                    }
                }
            }));
        }

        if(event.getSource().getEntity() instanceof Player attacker){
            CuriosApi.getCuriosInventory(attacker).ifPresent(curiosProvider -> curiosProvider.getCurios().forEach((identifier, stackHandler) -> {
                for (int k = 0; k < stackHandler.getSlots(); k++) {
                    ItemStack stack = stackHandler.getStacks().getStackInSlot(k);
                    if (!stack.isEmpty() && stack.getItem() instanceof IDamageAdjustmentItem damageAdjustmentItem) {
                        if(damageAdjustmentItem.canAdjustAttacking(event, attacker, stack)){
                            amount.set(damageAdjustmentItem.adjustAttacking(event, attacker, stack, amount.get()));
                            adjusted.set(true);
                        }
                    }
                }
            }));
        }

        if (adjusted.get() && amount.get() < 0.5f){
            event.setCanceled(true);
            return true;
        }

        event.setAmount(amount.get());
        return false;
    }
}
