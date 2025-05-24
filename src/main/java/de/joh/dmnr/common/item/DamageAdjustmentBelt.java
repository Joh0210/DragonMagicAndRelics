package de.joh.dmnr.common.item;

import de.joh.dmnr.api.item.BaseTieredItem;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.common.util.CommonConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import top.theillusivec4.curios.api.CuriosApi;

/**
 * Represents a special type of belt item that modifies incoming and outgoing damage
 * based on the item the entity is wearing (e.g., GlassCannonBelt x2 or SturdyBelt x0.5).
 * @author Joh0210
 */
public class DamageAdjustmentBelt extends BaseTieredItem {
    public DamageAdjustmentBelt() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    private static boolean applyEffectIfWearing(LivingHurtEvent event, Item item, float multiplier) {
        float damage = event.getAmount();
        Entity source = event.getSource().getEntity();
        LivingEntity target = event.getEntity();

        if (source instanceof LivingEntity && !CuriosApi.getCuriosHelper().findCurios((LivingEntity) source, item).isEmpty()) {
            damage *= multiplier;
        }

        if (target != null && !CuriosApi.getCuriosHelper().findCurios(target, item).isEmpty()) {
            damage *= multiplier;
        }

        if (damage < 1.0f){
            event.setCanceled(true);
            return true;
        }

        event.setAmount(damage);
        return false;
    }

    /**
     * increases (x2) damage dealt and received if Glass Cannon Belt is worn
     * @see CommonConfig
     */
    public static boolean eventHandleGlassCannon(LivingHurtEvent event){
        return applyEffectIfWearing(event, ItemInit.GLASS_CANNON_BELT.get(), CommonConfig.MINOTAUR_BELT_MULTIPLICATION.get());
    }

    /**
     * reduces  (x2) damage dealt and received if Sturdy Belt is worn
     * @see CommonConfig
     */
    public static boolean eventHandleSturdy(LivingHurtEvent event){
        return applyEffectIfWearing(event, ItemInit.STURDY_BELT.get(), 0.5f);
    }
}
