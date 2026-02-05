package de.joh.dmnr.common.item;

import com.mna.api.faction.IFaction;
import com.mna.api.items.IFactionSpecific;
import com.mna.factions.Factions;
import de.joh.dmnr.common.init.EffectInit;
import net.minecraft.world.effect.MobEffectInstance;
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
        revengeTarget.setSecondsOnFire(10);
        if(this.level > 1){
            revengeTarget.addEffect(new MobEffectInstance((EffectInit.HELLFIRE_EFFECT.get()), 200, 0, true, true, true));
        }
        usedByPlayer(revengeSource);
    }

    public IFaction getFaction() {
        return Factions.DEMONS;
    }

}
