package de.joh.dmnr.common.item;

import com.mna.api.faction.IFaction;
import com.mna.api.items.IFactionSpecific;
import com.mna.api.items.TieredItem;
import com.mna.factions.Factions;
import com.mna.items.ItemInit;
import de.joh.dmnr.common.event.MagicEventHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeItem;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;

/**
 * {@link ItemInit#HELLFIRE_STAFF HellfireStaff} as a Ring
 * @see MagicEventHandler
 * @author Joh0210
 */
public class DevilRingItem extends TieredItem implements IForgeItem, ICurioItem, IFactionSpecific {
    private int _tier = -1;

    @Override
    public void setCachedTier(int tier) {
        this._tier = tier;
    }

    @Override
    public int getCachedtier() {
        return this._tier;
    }

    public DevilRingItem(Properties itemProperties) {
        super(itemProperties);
    }

    @Override
    public IFaction getFaction() {
        return  Factions.DEMONS;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@Nullable ItemStack itemStack){
        return true;
    }
}
