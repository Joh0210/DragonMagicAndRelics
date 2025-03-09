package de.joh.dmnr.common.item;

import com.mna.items.ritual.WizardChalk;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeItem;

import java.util.List;
import java.util.function.Consumer;

/**
 * An indestructible chalk.
 * @author Joh210
 */
public class BrimstoneChalkItem extends WizardChalk implements IForgeItem {

    public BrimstoneChalkItem() {
        super();
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return super.damageItem(stack, 0, entity, onBroken);
    }

    @Override
    public void setDamage(ItemStack stack, int damage){}

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {}
}
