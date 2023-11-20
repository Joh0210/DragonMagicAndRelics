package de.joh.dmnr.item.items;

import com.mna.api.items.ITieredItem;
import de.joh.dmnr.CreativeModeTab;
import de.joh.dmnr.events.DamageEventHandler;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

/**
 * The user deals twice the amount of damage, but also takes twice the amount of damage
 * @see DamageEventHandler
 * @author Joh0210
 */
public class GlassCannonBelt extends Item implements ICurioItem, ITieredItem<GlassCannonBelt> {
    private int _tier = -1;

    public GlassCannonBelt() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).tab(CreativeModeTab.CreativeModeTab));
    }

    @Override
    public void setCachedTier(int tier) {
        this._tier = tier;
    }

    @Override
    public int getCachedtier() {
        return this._tier;
    }
}
