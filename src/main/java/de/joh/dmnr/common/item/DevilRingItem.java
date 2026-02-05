package de.joh.dmnr.common.item;

import com.mna.api.affinity.Affinity;
import com.mna.api.events.SpellCastEvent;
import com.mna.api.faction.IFaction;
import com.mna.api.items.IFactionSpecific;
import com.mna.api.items.TieredItem;
import com.mna.factions.Factions;
import de.joh.dmnr.common.event.MagicEventHandler;
import de.joh.dmnr.common.init.ItemInit;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeItem;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.List;

/**
 * {@link com.mna.items.ItemInit#HELLFIRE_STAFF HellfireStaff} as a Ring
 * @see MagicEventHandler
 * @author Joh0210
 */
public class DevilRingItem extends TieredItem implements IForgeItem, ICurioItem, IFactionSpecific {
    private int _tier = -1;

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.dmnr.devil_ring").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("  "));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void setCachedTier(int tier) {
        this._tier = tier;
    }

    @Override
    public int getCachedTier() {
        return this._tier;
    }

    public DevilRingItem(Properties itemProperties) {
        super(itemProperties);
    }

    @Override
    public IFaction getFaction() {
        return  Factions.DEMONS;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@Nullable ItemStack itemStack){
        return true;
    }

    /**
     * Turns Fire and Lightningspells into Hellfire if the Ring is equipped
     */
    public static void eventHandleTurnIntoHellfire(SpellCastEvent event){
        LivingEntity caster = event.getSource().getCaster();
        if (caster != null) {
            if (CuriosApi.getCuriosHelper().findFirstCurio(caster, ItemInit.DEVIL_RING.get()).isPresent() &&
                    (event.getSpell().getHighestAffinity() == Affinity.FIRE || event.getSpell().getHighestAffinity() == Affinity.LIGHTNING)) {
                event.getSpell().setOverrideAffinity(Affinity.HELLFIRE);
            }
        }
    }
}
