package de.joh.dmnr.common.item;

import com.mna.api.entities.DamageHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import static de.joh.dmnr.common.spell.component.ForceDamageComponent.getRevengeDamage;

/**
 * RevengeCharmItem that reflects a small portion of the damage
 * @author Joh0210
 */
public class ForceRevengeCharmItem extends RevengeCharmItem {
    public ForceRevengeCharmItem(int level) {
        super(level, 10);
    }

    @Override
    public void revenge(Player revengeSource, LivingEntity revengeTarget, float damage) {

        float revengeDamage = damage * 0.1f * this.level;
        if(this.level >= 2){
            revengeDamage *= 1.5f;
        }
        if(revengeDamage >= 1.0f){
            revengeTarget.hurt(DamageHelper.createSourcedType(getRevengeDamage(), revengeSource.level().registryAccess(), revengeSource), revengeDamage);
        }
    }
}
