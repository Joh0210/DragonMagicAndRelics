package de.joh.dmnr.common.item;

import com.mna.api.affinity.Affinity;
import com.mna.api.events.ComponentApplyingEvent;
import com.mna.api.faction.IFaction;
import com.mna.api.items.IFactionSpecific;
import com.mna.api.items.TieredItem;
import com.mna.factions.Factions;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.init.EffectInit;
import de.joh.dmnr.common.init.ItemInit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.extensions.IForgeItem;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class AmuletOfHellfire extends TieredItem implements IForgeItem, ICurioItem, IFactionSpecific {
    public AmuletOfHellfire() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public IFaction getFaction() {
        return Factions.DEMONS;
    }

    /**
     * Applies the Hellfire Effect on the target of Hellfire-Spells
     */
    public static void eventHandleHellfire(ComponentApplyingEvent event){
        DragonMagicAndRelics.LOGGER.warn("Component:" + event.getComponent().getAffinity().toString());
        LivingEntity caster = event.getSource().getCaster();
        if(caster != null && event.getContext().getSpell().getHighestAffinity() == Affinity.HELLFIRE && CuriosApi.getCuriosHelper().findFirstCurio(caster, ItemInit.AMULET_OF_HELLFIRE.get()).isPresent()){
            LivingEntity entity= event.getTarget().getLivingEntity();
            if(entity != null){
                entity.addEffect(new MobEffectInstance((EffectInit.HELLFIRE_EFFECT.get()), 400, 0, false, true, true));
            }
        }
    }
}
