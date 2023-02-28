package de.joh.dragonmagicandrelics.item.items;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.capabilities.Faction;
import com.mna.api.items.TieredItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItem;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class FactionAmulet extends TieredItem implements IForgeItem, ICurioItem {

    public FactionAmulet(Properties itemProperties) {
        super(itemProperties);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player){
            slotContext.entity().getCapability(ManaAndArtificeMod.getProgressionCapability()).ifPresent((p)->{
                p.setRaidChance(Faction.ANCIENT_WIZARDS, 0);
                p.setRaidChance(Faction.FEY_COURT, 0);
                p.setRaidChance(Faction.UNDEAD, 0);
                p.setRaidChance(Faction.DEMONS, 0);
            });
        }

        curioTick(slotContext.identifier(), slotContext.index(), slotContext.entity(), stack);
    }
}
