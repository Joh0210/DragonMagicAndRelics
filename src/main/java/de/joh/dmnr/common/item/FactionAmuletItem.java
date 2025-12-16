package de.joh.dmnr.common.item;

import com.mna.Registries;
import com.mna.api.ManaAndArtificeMod;
import com.mna.api.entities.IFactionEnemy;
import com.mna.api.faction.IFaction;
import com.mna.api.items.ChargeableItem;
import com.mna.capabilities.playerdata.progression.PlayerProgression;
import de.joh.dmnr.common.effects.beneficial.PeaceMobEffect;
import de.joh.dmnr.common.effects.harmful.BrokenPeaceMobEffect;
import de.joh.dmnr.common.init.EffectInit;
import de.joh.dmnr.common.init.ItemInit;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.concurrent.atomic.AtomicReference;

/**
 * This item (when equipped) prevents raids and prevents monsters from other factions from attacking you. If the player attacks, he declares war.
 * @see PeaceMobEffect
 * @see BrokenPeaceMobEffect
 * @author Joh0210
 */
public class FactionAmuletItem extends ChargeableItem implements IForgeItem, ICurioItem {

    public FactionAmuletItem(Properties itemProperties) {
        super(itemProperties, 2500.0F);
    }

    @Override
    protected boolean tickEffect(ItemStack stack, Player player, Level world, int slot, float mana, boolean selected) {
        player.getCapability(ManaAndArtificeMod.getProgressionCapability()).ifPresent((p)-> Registries.Factions.get().getValues().forEach((faction) -> {
            if(p.getRaidChance(faction) >= 0.5*PlayerProgression.RAID_IRE && this.isEquippedAndHasMana(player,1.0F, true)){
                p.setRaidChance(faction, 0);
            }
        }));

        return false;
    }

    protected boolean tickCurio() {
        return true;
    }

    /**
     * Prevents another faction from becoming aggressive towards the user of the FactionAmulet, for a small mana cost.
     */
    public static void eventHandlePeaceOffering(LivingChangeTargetEvent event) {
        if(event.getEntity() instanceof IFactionEnemy){
            if(event.getNewTarget() == null){
                return;
            }

            if(event.getNewTarget().hasEffect(EffectInit.PEACE_EFFECT.get())){
                event.setNewTarget(null);
            } else if(!event.getNewTarget().hasEffect(EffectInit.BROKEN_PEACE_EFFECT.get())
                    && ((ChargeableItem)(ItemInit.FACTION_AMULET.get())).isEquippedAndHasMana(event.getNewTarget(), 50.0F, true)){
                event.getNewTarget().addEffect(new MobEffectInstance((EffectInit.PEACE_EFFECT.get()), 600, 0, false, false, true));
                event.setNewTarget(null);
            }
        }
    }

    /**
     * If a player has the Peace effect and still starts fighting with a faction, the effect will be broken.
     * @see PeaceMobEffect
     * @see BrokenPeaceMobEffect
     */
    public static void eventHandleDeclarationOfWar(LivingHurtEvent event){
        if(event.getSource().getEntity() instanceof LivingEntity sourceEntity && sourceEntity.hasEffect(EffectInit.PEACE_EFFECT.get())){
            LivingEntity targetEntity = event.getEntity();

            AtomicReference<IFaction> faction = new AtomicReference<>(null);
            if(targetEntity instanceof Player player){
                player.getCapability(ManaAndArtificeMod.getProgressionCapability()).ifPresent((p)-> faction.set(p.getAlliedFaction()));
            }

            if((targetEntity instanceof IFactionEnemy || faction.get() != null) && sourceEntity instanceof LivingEntity){
                sourceEntity.removeEffect(EffectInit.PEACE_EFFECT.get());
                sourceEntity.addEffect(new MobEffectInstance((EffectInit.BROKEN_PEACE_EFFECT.get()), 12000));
                if(sourceEntity instanceof Player){
                    sourceEntity.level().playSeededSound(null, sourceEntity.getX(), sourceEntity.getY(), sourceEntity.getZ(), SoundEvents.RAID_HORN.get(), SoundSource.PLAYERS, 1F, 0.9F + (float)Math.random() * 0.2F, 0);
                }
            }
        }
    }
}
