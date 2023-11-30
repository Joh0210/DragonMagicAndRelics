package de.joh.dmnr.api.item;

import com.mna.api.items.ITieredItem;
import net.minecraft.world.item.Item;

/**
 * Base Item for Tiered Items with no additional Functionality inside the Item Class
 * @author Joh0210
 */
public class BaseTieredItem extends Item implements ITieredItem<BaseTieredItem> {
    private int _tier = -1;

    public BaseTieredItem(Properties pProperties) {
        super(pProperties);
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
