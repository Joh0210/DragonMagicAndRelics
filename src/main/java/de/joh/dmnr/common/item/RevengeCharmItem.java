package de.joh.dmnr.common.item;

import com.mna.api.items.ChargeableItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

/**
 * Items of this type will apply an effect on an attacker if the defender has this item
 * @author Joh0210
 */
public abstract class RevengeCharmItem extends ChargeableItem implements ICurioItem {
    private int tier;
    private final int amount;
    protected final int level;

    public RevengeCharmItem(int level, int amount) {
        super(new Item.Properties().stacksTo(1), (float) (150 * Math.pow(10, level-1)));
        this.amount = amount;
        this.level = level;
    }

    public abstract void revenge(Player revengeSource, LivingEntity revengeTarget, float damage);

//    public void addParticle(Level level, BlockPos pos){
//        if(level.isClientSide){
//            for(int j = 0; j < 360; j += 20) {
//                level.addParticle(ParticleTypes.LAVA,
//                        pos.getX() + 0.5d, pos.getY() + 1, pos.getZ() + 0.5d,
//                        Math.cos(j) * 0.15d, 0.15d, Math.sin(j) * 0.15d);
//
//            }
//        }
//    }

    public static void handleRevengeCharm(LivingHurtEvent event){
        if(event.getEntity() instanceof Player defender && event.getSource().getEntity() instanceof LivingEntity attacker){
            CuriosApi.getCuriosInventory(defender).ifPresent(curiosProvider -> {
                curiosProvider.getCurios().forEach((identifier, stackHandler) -> {
                    for (int k = 0; k < stackHandler.getSlots(); k++) {
                        ItemStack stack = stackHandler.getStacks().getStackInSlot(k);
                        if (!stack.isEmpty() && stack.getItem() instanceof RevengeCharmItem revengeItem) {
                            if(revengeItem.isEquippedAndHasMana(event.getEntity(), revengeItem.amount, true)){
                                revengeItem.revenge(defender, attacker, event.getAmount());
                                // todo: add Particle?
                            }
                        }
                    }
                });
            });
        }
    }

    @Override
    public void setCachedTier(int tier) {
        this.tier = tier;
    }

    @Override
    public int getCachedTier() {
        return this.tier;
    }

    @Override
    protected boolean tickEffect(ItemStack itemStack, Player player, Level level, int i, float v, boolean b) {
        return false;
    }
}
