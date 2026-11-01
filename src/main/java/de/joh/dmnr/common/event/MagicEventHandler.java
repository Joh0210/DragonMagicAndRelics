package de.joh.dmnr.common.event;

import com.mna.api.events.ComponentApplyingEvent;
import com.mna.api.events.MasteryGainedEvent;
import com.mna.api.events.RitualCompleteEvent;
import com.mna.api.events.RoteProgressGainedEvent;
import com.mna.api.events.SpellCastEvent;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.api.spell.ChronomancyProgress;
import de.joh.dmnr.common.dragonmagic.PrismaticAmuletDragonMagic;
import de.joh.dmnr.common.dragonmagic.RingOfSilkDragonMagic;
import de.joh.dmnr.common.effects.beneficial.SorcerersPrideMobEffect;
import de.joh.dmnr.common.item.*;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handels Events with a Magic Theme
 * @author Joh0210
 */
@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MagicEventHandler {
    @SubscribeEvent
    public static void onPotionAdded(MobEffectEvent.Added event) {
        DragonTotemItem.eventHandleSorcerersPride(event);
        DragonTotemItem.eventHandleEnduredCurses(event);
    }

    /**
     * Processing of {@link CurseProtectionAmuletItem}
     */
    @SubscribeEvent
    public static void onPotionAdded(MobEffectEvent.Applicable event){
        CurseProtectionAmuletItem.eventHandleDenyHarmful(event);
    }

    /**
     * Prevents friends from getting hurt
     * @see BraceletOfFriendshipItem
     * @see AmuletOfHellfire
     * @see RingOfSilkDragonMagic
     */
    @SubscribeEvent
    public static void onComponentApplying(ComponentApplyingEvent event){
        BraceletOfFriendshipItem.eventHandleProtectFriends(event);
        AmuletOfHellfire.eventHandleHellfire(event);
        RingOfSilkDragonMagic.eventHandleRefill(event);
    }

    /**
     * Counts the spell parts that grant rote progress for Accelerate Time.
     * Lowest priority, so components canceled by other handlers don't count.
     * @see ChronomancyProgress
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onComponentApplyingUncanceled(ComponentApplyingEvent event){
        ChronomancyProgress.eventHandleComponentApplying(event);
    }

    /**
     * Counts the rituals that grant rote progress for Accelerate Time.
     * @see ChronomancyProgress
     */
    @SubscribeEvent
    public static void onRitualComplete(RitualCompleteEvent event){
        ChronomancyProgress.eventHandleRitualComplete(event);
    }

    @SubscribeEvent
    public static void onSpellCast(SpellCastEvent event){
        DevilRingItem.eventHandleTurnIntoHellfire(event);
        SorcerersPrideMobEffect.spellBoost(event);
    }

    /**
     * Lowest priority, so spells canceled by other handlers don't get extra projectiles.
     * @see PrismaticAmuletDragonMagic
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onSpellCastUncanceled(SpellCastEvent event){
        PrismaticAmuletDragonMagic.eventHandleExtraProjectiles(event);
    }

    @SubscribeEvent
    public static void onRoteProgressGained(RoteProgressGainedEvent event) {
        JournalOfMysteriesItem.roteBoost(event);
    }

    @SubscribeEvent
    public static void onMasteryProgressGained(MasteryGainedEvent event) {
        JournalOfMysteriesItem.masteryBoost(event);
    }
}
