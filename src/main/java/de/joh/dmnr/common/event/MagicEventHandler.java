package de.joh.dmnr.common.event;

import com.mna.api.events.ComponentApplyingEvent;
import com.mna.api.events.RoteProgressGainedEvent;
import com.mna.api.events.SpellCastEvent;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.api.event.DragonMagicChangeEvent;
import de.joh.dmnr.api.item.IDragonMagicItem;
import de.joh.dmnr.common.effects.beneficial.SorcerersPrideMobEffect;
import de.joh.dmnr.common.item.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

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

    @SubscribeEvent
    public static void onRoteProgressGained(RoteProgressGainedEvent event) {
        JournalOfMysteriesItem.roteBoost(event);
    }

    @SubscribeEvent
    public static void onDragonMagicChangeEvent(DragonMagicChangeEvent event) {
        Player player = event.getEntity();

        CuriosApi.getCuriosInventory(player).ifPresent(curiosProvider -> curiosProvider.getCurios().forEach((identifier, stackHandler) -> {
            for (int k = 0; k < stackHandler.getSlots(); k++) {
                ItemStack stack = stackHandler.getStacks().getStackInSlot(k);
                if (stack.getItem() instanceof IDragonMagicItem dragonMagicItem) {
                    if (event.enabling) {
                        dragonMagicItem.onDMEquip(stack, player);
                    } else {
                        dragonMagicItem.onDMDiscard(stack, player);
                    }
                }
            }
        }));

        for (ItemStack stack : player.getArmorSlots()) {
            if (stack.getItem() instanceof IDragonMagicItem dragonMagicItem) {
                if (event.enabling) {
                    dragonMagicItem.onDMEquip(stack, player);
                } else {
                    dragonMagicItem.onDMDiscard(stack, player);
                }
            }
        }
    }
}
