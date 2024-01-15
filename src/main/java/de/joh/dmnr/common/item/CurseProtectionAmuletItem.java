package de.joh.dmnr.common.item;

import com.mna.api.items.ChargeableItem;
import com.mna.items.artifice.curio.IPreEnchantedItem;
import de.joh.dmnr.common.event.CommonEventHandler;
import de.joh.dmnr.common.spell.shape.CurseShape;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

/**
 * Protects the wearer of Negativ Potion Effects (which do not last longer then 5 min and can be removed with milk) and of the {@link CurseShape Curse Shape}
 * <br> todo: cool down for the Negativ Potion thing?
 * @see CommonEventHandler
 * @author Joh0210
 */
public class CurseProtectionAmuletItem extends ChargeableItem implements IPreEnchantedItem<CurseProtectionAmuletItem> {
    public CurseProtectionAmuletItem() {
        super((new Item.Properties()).setNoRepair().rarity(Rarity.EPIC), 2000.0F);
    }

    protected boolean tickEffect(ItemStack stack, Player player, Level world, int slot, float mana, boolean selected) {
        return false;
    }

    protected boolean tickCurio() {
        return false;
    }
}
