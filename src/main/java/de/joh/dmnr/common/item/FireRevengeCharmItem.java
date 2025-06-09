package de.joh.dmnr.common.item;

import com.mna.api.faction.IFaction;
import com.mna.api.items.IFactionSpecific;
import com.mna.factions.Factions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

/**
 * RevengeCharmItem that ignites the attacker
 * @author Joh0210
 */
public class FireRevengeCharmItem extends RevengeCharmItem implements IFactionSpecific {
    public FireRevengeCharmItem(int level) {
        super(level, 2);
    }

    @Override
    public void revenge(Player revengeSource, LivingEntity revengeTarget, float damage) {
        revengeTarget.setSecondsOnFire((int) (5 * Math.pow(5, this.level-1)));
        usedByPlayer(revengeSource);
    }

    public IFaction getFaction() {
        return Factions.DEMONS;
    }

}
