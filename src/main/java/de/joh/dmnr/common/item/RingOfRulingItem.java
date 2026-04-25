package de.joh.dmnr.common.item;

import de.joh.dmnr.api.item.BaseTieredItem;
import net.minecraft.world.item.Rarity;

// todo: on Attack revenge spell
public class RingOfRulingItem extends BaseTieredItem {
    public RingOfRulingItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant());
    }
}
