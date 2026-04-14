package de.joh.dmnr.common.event;

import com.mna.api.events.ComponentApplyingEvent;
import com.mna.api.events.SpellCastEvent;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.effects.beneficial.SorcerersPrideMobEffect;
import de.joh.dmnr.common.item.AmuletOfHellfire;
import de.joh.dmnr.common.item.BraceletOfFriendshipItem;
import de.joh.dmnr.common.item.CurseProtectionAmuletItem;
import de.joh.dmnr.common.item.DevilRingItem;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handels Events with a Magic Theme
 * @author Joh0210
 */
@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MagicEventHandler {
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
     */
    @SubscribeEvent
    public static void onComponentApplying(ComponentApplyingEvent event){
        BraceletOfFriendshipItem.eventHandleProtectFriends(event);
        AmuletOfHellfire.eventHandleHellfire(event);
    }

    @SubscribeEvent
    public static void onSpellCast(SpellCastEvent event){
        DevilRingItem.eventHandleTurnIntoHellfire(event);
        SorcerersPrideMobEffect.spellBoost(event);
    }
}
