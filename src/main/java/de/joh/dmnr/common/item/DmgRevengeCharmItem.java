package de.joh.dmnr.common.item;

import com.mna.api.entities.DamageHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import static de.joh.dmnr.common.spell.component.ForceDamageComponent.getRevengeDamage;

/**
 * RevengeCharmItem that applies a bit of damage to the attacker
 * @author Joh0210
 */
public class DmgRevengeCharmItem extends RevengeCharmItem {
    public DmgRevengeCharmItem(int level) {
        super(level, 6);
    }

    @Override
    public void revenge(Player revengeSource, LivingEntity revengeTarget, float damage) {
        int revengeDamage = (int) Math.pow(3, this.level) * (this.hasDragonMagic(revengeSource)?2:1);

        if(revengeDamage >= 0.5f){
            revengeTarget.hurt(DamageHelper.createSourcedType(getRevengeDamage(), revengeSource.level().registryAccess(), revengeSource), revengeDamage);
        }
    }

    @Override
    public String dragonMagicID() {
        return "item.dmnr.revenge_charm_dmg.dragonmagic";
    }
}
