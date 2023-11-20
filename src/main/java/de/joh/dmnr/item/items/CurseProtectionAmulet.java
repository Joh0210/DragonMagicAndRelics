package de.joh.dmnr.item.items;

import com.mna.api.items.ChargeableItem;
import com.mna.items.artifice.curio.IPreEnchantedItem;
import de.joh.dmnr.CreativeModeTab;
import de.joh.dmnr.events.CommonEventHandler;
import de.joh.dmnr.spells.shapes.ShapeCurse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

/**
 * Protects the wearer of Negativ Potion Effects (which do not last longer then 5 min and can be removed with milk) and of the {@link ShapeCurse Curse Shape}
 * <br> todo: cool down for the Negativ Potion thing?
 * @see CommonEventHandler
 * @author Joh0210
 */
public class CurseProtectionAmulet extends ChargeableItem implements IPreEnchantedItem<CurseProtectionAmulet> {
    public CurseProtectionAmulet() {
        super((new Item.Properties()).setNoRepair().tab(CreativeModeTab.CreativeModeTab).rarity(Rarity.EPIC), 2000.0F);
    }

    protected boolean tickEffect(ItemStack stack, Player player, Level world, int slot, float mana, boolean selected) {
        return false;
    }

    protected boolean tickCurio() {
        return false;
    }
}
