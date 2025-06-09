package de.joh.dmnr.common.item;

import de.joh.dmnr.api.item.BaseTieredItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

/**
 * Represents a special type of belt item that modifies incoming and outgoing damage
 * based on the item the entity is wearing (e.g., GlassCannonBelt x2 or SturdyBelt x0.5).
 * @author Joh0210
 */
public class DamageAdjustmentBelt extends BaseTieredItem implements IDamageAdjustmentItem {
    private final float modifier;

    public DamageAdjustmentBelt(float modifier) {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
        this.modifier = modifier;
    }


    @Override
    public boolean canAdjustDefending(LivingHurtEvent event, Player defender, ItemStack damageAdjustmentItem) {
        return true;
    }

    @Override
    public float adjustDefending(LivingHurtEvent event, Player defender, ItemStack damageAdjustmentItem, float amount) {
        return amount * modifier;
    }

    @Override
    public boolean canAdjustAttacking(LivingHurtEvent event, Player attacker, ItemStack damageAdjustmentItem) {
        return true;
    }

    @Override
    public float adjustAttacking(LivingHurtEvent event, Player attacker, ItemStack damageAdjustmentItem, float amount) {
        return amount * modifier;
    }
}
