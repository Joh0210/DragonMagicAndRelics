package de.joh.dmnr.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.SpellReagent;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.spells.components.PotionEffectComponent;
import de.joh.dmnr.effects.EffectInit;
import de.joh.dmnr.item.items.dragonmagearmor.DragonMageArmor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import javax.annotation.Nullable;
import java.util.List;

/**
 * This spell adds the UltimateArmor potion effect to the target.
 * @see de.joh.dmnr.effects.beneficial.EffectUltimateArmor
 * @author Joh0210
 */
public class ComponentUltimateArmor extends PotionEffectComponent {

    public ComponentUltimateArmor(final ResourceLocation guiIcon) {
        super(guiIcon, EffectInit.ULTIMATE_ARMOR, new AttributeValuePair(Attribute.DURATION, 30.0F, 30.0F, 600.0F, 30.0F, 10.0F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (ComponentApplicationResult.FAIL == super.ApplyEffect(source, target, modificationData, context)){
            return ComponentApplicationResult.FAIL;
        }

        if(target.isEntity()){
            Entity targetEntity = target.getEntity();

            if (targetEntity instanceof Player targetPlayer){
                ItemStack chest = targetPlayer.getItemBySlot(EquipmentSlot.CHEST);
                if (chest.getItem() instanceof DragonMageArmor mmaArmor){
                    mmaArmor.applySetBonus(targetPlayer, EquipmentSlot.CHEST);
                }
            }
        }

        return ComponentApplicationResult.SUCCESS;
    }

    public int requiredXPForRote() {
        return 200;
    }

    public Affinity getAffinity() {
        return Affinity.ARCANE;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.FRIENDLY;
    }

    public float initialComplexity() {
        return 50.0F;
    }

    @Override
    public List<SpellReagent> getRequiredReagents(@Nullable Player caster, @Nullable InteractionHand hand) {
        return List.of(new SpellReagent(new ItemStack(Items.NETHER_STAR), false, true, true));
    }
}