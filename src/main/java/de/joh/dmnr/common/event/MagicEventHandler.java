package de.joh.dmnr.common.event;

import com.mna.api.affinity.Affinity;
import com.mna.api.events.ComponentApplyingEvent;
import com.mna.api.events.SpellCastEvent;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.Shape;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.capabilities.dragonmagic.ArmorUpgradeHelper;
import de.joh.dmnr.common.init.ArmorUpgradeInit;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.common.item.BraceletOfFriendshipItem;
import de.joh.dmnr.common.item.CurseProtectionAmuletItem;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
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
        if(event.getEffectInstance().getDuration() < 6000
                && event.getEffectInstance().getDuration() > 0
                && event.getEffectInstance().getEffect().getCategory() == MobEffectCategory.HARMFUL
                && event.getEffectInstance().getEffect().getCurativeItems().stream().anyMatch(s -> s.getItem() == Items.MILK_BUCKET)
        ){
            int amount = (int) Math.min(event.getEffectInstance().getDuration() * (event.getEffectInstance().getAmplifier() +1) / 20.0f, 150.0f);
            if(((CurseProtectionAmuletItem) ItemInit.CURSE_PROTECTION_AMULET.get()).isEquippedAndHasMana(event.getEntity(), amount, true)){
                event.setResult(Event.Result.DENY);
            }
        }
    }

    /**
     * Prevents friends from getting hurt
     * @see BraceletOfFriendshipItem
     */
    @SubscribeEvent
    public static void onComponentApplying(ComponentApplyingEvent event){
        BraceletOfFriendshipItem.eventHandleProtectFriends(event);
    }

    /**
     * Processing of {@link ArmorUpgradeInit#SORCERERS_PRIDE}
     * Processing of {@link ItemInit#DEVIL_RING}
     */
    @SubscribeEvent
    public static void onSpellCast(SpellCastEvent event){
        Player caster = event.getSource().getPlayer();
        if (caster != null){
            if(CuriosApi.getCuriosHelper().findFirstCurio(caster, ItemInit.DEVIL_RING.get()).isPresent() &&
                    (event.getSpell().getHighestAffinity() == Affinity.FIRE || event.getSpell().getHighestAffinity() == Affinity.LIGHTNING)){
                event.getSpell().setOverrideAffinity(Affinity.HELLFIRE);
            }

            IModifiedSpellPart<Shape> shape = event.getSpell().getShape();

            int level = ArmorUpgradeHelper.getUpgradeLevel(caster, ArmorUpgradeInit.SORCERERS_PRIDE);
            if(level > 0 && shape != null){
                shape.getContainedAttributes().stream()
                        .filter(attribute -> attribute == Attribute.MAGNITUDE)
                        .forEach(attribute -> shape.setValue(attribute, shape.getValue(attribute) + Math.round(level * 0.5f)));
                shape.getContainedAttributes().stream()
                        .filter(attribute -> attribute == Attribute.DAMAGE)
                        .forEach(attribute -> shape.setValue(attribute, shape.getValue(attribute) + level * 3));
                shape.getContainedAttributes().stream()
                        .filter(attribute -> attribute == Attribute.DURATION)
                        .forEach(attribute -> shape.setValue(attribute, shape.getValue(attribute) * (1 + level * 0.3f)));

                event.getSpell().getComponents().forEach(modifiedSpellPart -> modifiedSpellPart.getContainedAttributes().stream()
                        .filter(attribute -> attribute == Attribute.MAGNITUDE)
                        .forEach(attribute -> modifiedSpellPart.setValue(attribute, modifiedSpellPart.getValue(attribute) + Math.round(level * 0.5f))));
                event.getSpell().getComponents().forEach(modifiedSpellPart -> modifiedSpellPart.getContainedAttributes().stream()
                        .filter(attribute -> attribute == Attribute.DAMAGE)
                        .forEach(attribute -> modifiedSpellPart.setValue(attribute, modifiedSpellPart.getValue(attribute) + level * 3)));
                event.getSpell().getComponents().forEach(modifiedSpellPart -> modifiedSpellPart.getContainedAttributes().stream()
                        .filter(attribute -> attribute == Attribute.DURATION)
                        .forEach(attribute -> modifiedSpellPart.setValue(attribute, modifiedSpellPart.getValue(attribute) * (1 + level * 0.3f))));
            }
        }
    }
}
