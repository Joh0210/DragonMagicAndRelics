package de.joh.dragonmagicandrelics.item.items;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.capabilities.Faction;
import com.mna.api.items.ChargeableItem;
import com.mna.capabilities.playerdata.progression.PlayerProgression;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeItem;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

/**
 * This item (when equipped) prevents raids and prevents monsters from other factions from attacking you. If the player attacks, he declares war.
 * @see de.joh.dragonmagicandrelics.events.DamageEventHandler
 * @see de.joh.dragonmagicandrelics.effects.beneficial.PeaceEffect
 * @see de.joh.dragonmagicandrelics.effects.harmful.BrokenPeaceEffect
 * @author Joh0210
 */
public class FactionAmulet extends ChargeableItem implements IForgeItem, ICurioItem {

    public FactionAmulet(Properties itemProperties) {
        super(itemProperties, 2500.0F);
    }

    @Override
    protected boolean tickEffect(ItemStack stack, Player player, Level world, int slot, float mana, boolean selected) {
        player.getCapability(ManaAndArtificeMod.getProgressionCapability()).ifPresent((p)->{
            for(Faction faction : Faction.values()){
                if(p.getRaidChance(faction) >= 0.5*PlayerProgression.RAID_IRE && this.isEquippedAndHasMana(player,1.0F, true)){
                    p.setRaidChance(faction, 0);
                }
            }
        });

        return false;
    }

    protected boolean tickCurio() {
        return true;
    }
}
