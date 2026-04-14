package de.joh.dmnr.common.spell.component;

import com.mna.api.affinity.Affinity;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.spells.components.PotionEffectComponent;
import de.joh.dmnr.common.effects.beneficial.SorcerersPrideMobEffect;
import de.joh.dmnr.common.init.EffectInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * This spell adds the SorcerersPride potion effect to the target.
 * @see SorcerersPrideMobEffect
 * @author Joh0210
 */
public class SorcerersPrideComponent extends PotionEffectComponent {
    public SorcerersPrideComponent(final ResourceLocation guiIcon) {
        super(guiIcon, EffectInit.SORCERERS_PRIDE, new AttributeValuePair(Attribute.DURATION, 30.0F, 30.0F, 600.0F, 30.0F, 10.0F), new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1, 3.0F, 1.0F, 20.0F));
        this.addReagent(new ItemStack(Items.NETHER_STAR), false, false, true);
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
        return 30.0F;
    }
}
