package de.joh.dragonmagicandrelics.spells.components;

import com.ma.api.affinity.Affinity;
import com.ma.api.spells.ComponentApplicationResult;
import com.ma.api.spells.SpellPartTags;
import com.ma.api.spells.SpellReagent;
import com.ma.api.spells.attributes.Attribute;
import com.ma.api.spells.attributes.AttributeValuePair;
import com.ma.api.spells.base.IModifiedSpellPart;
import com.ma.api.spells.parts.Component;
import com.ma.api.spells.targeting.SpellContext;
import com.ma.api.spells.targeting.SpellSource;
import com.ma.api.spells.targeting.SpellTarget;
import com.ma.spells.components.PotionEffectComponent;
import de.joh.dragonmagicandrelics.effects.EffectInit;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * This spell adds the UltimateArmor potion effect to the target.
 * @see de.joh.dragonmagicandrelics.effects.beneficial.EffectUltimateArmor
 * @author Joh0210
 */
public class ComponentUltimateArmor extends PotionEffectComponent {

    public ComponentUltimateArmor(final ResourceLocation registryName, final ResourceLocation guiIcon) {
        super(registryName, guiIcon, EffectInit.ULTIMATE_ARMOR, new AttributeValuePair[]{new AttributeValuePair(Attribute.DURATION, 30.0F, 30.0F, 600.0F, 30.0F, 10.0F)});
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<Component> modificationData, SpellContext context) {
        if (ComponentApplicationResult.FAIL == super.ApplyEffect(source, target, modificationData, context)){
            return ComponentApplicationResult.FAIL;
        }

        if(target.isEntity()){
            Entity targetEntity = target.getEntity();

            if (targetEntity instanceof PlayerEntity ){
                ItemStack chest = ((PlayerEntity)targetEntity).getItemStackFromSlot(EquipmentSlotType.CHEST);
                if (chest.getItem() instanceof DragonMageArmor){
                    ((DragonMageArmor)chest.getItem()).applySetBonus((PlayerEntity)targetEntity, EquipmentSlotType.CHEST);
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
    public List<SpellReagent> getRequiredReagents(@Nullable PlayerEntity caster) {
        ArrayList list = new ArrayList();
        list.add(new SpellReagent(new ItemStack(Items.NETHER_STAR), false, true, true));
        return list;
    }
}
