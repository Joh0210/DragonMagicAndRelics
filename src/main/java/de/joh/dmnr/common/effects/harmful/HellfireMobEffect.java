package de.joh.dmnr.common.effects.harmful;

import com.mna.api.entities.DamageHelper;
import de.joh.dmnr.common.init.EffectInit;
import de.joh.dmnr.common.util.RLoc;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

/**
 * Converts fire-dmg into magical dmg, ignoring resistances
 * @see de.joh.dmnr.common.item.AmuletOfHellfire
 * @author Joh0210
 */
public class HellfireMobEffect extends MobEffect {
    public HellfireMobEffect() {
        super(MobEffectCategory.HARMFUL, 0x2FBC37);
    }

    public static ResourceKey<DamageType> getHellfireDamage(){
        return ResourceKey.create(Registries.DAMAGE_TYPE, RLoc.create("spell_hellfire"));
    }

    public static void handleHellfire(LivingAttackEvent event){
        LivingEntity targetEntity = event.getEntity();
        if(event.getSource().is(DamageTypeTags.IS_FIRE) && targetEntity.hasEffect(EffectInit.HELLFIRE_EFFECT.get())){

            float amount = event.getAmount()* 2;

            event.setCanceled(true);
            targetEntity.hurt(DamageHelper.createSourcedType(getHellfireDamage(), targetEntity.level().registryAccess(), event.getSource().getEntity()), amount);
        }
    }
}
