package de.joh.dmnr.common.item;

import com.mna.api.affinity.Affinity;
import com.mna.api.events.ComponentApplyingEvent;
import com.mna.api.faction.IFaction;
import com.mna.api.items.IFactionSpecific;
import com.mna.api.items.TieredItem;
import com.mna.factions.Factions;
import de.joh.dmnr.common.init.EffectInit;
import de.joh.dmnr.common.init.ItemInit;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeItem;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

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
        LivingEntity caster = event.getSource().getCaster();
        if(caster != null && event.getContext().getSpell().getHighestAffinity() == Affinity.HELLFIRE && CuriosApi.getCuriosHelper().findFirstCurio(caster, ItemInit.AMULET_OF_HELLFIRE.get()).isPresent()){
            LivingEntity entity= event.getTarget().getLivingEntity();
            if(entity != null){
                entity.addEffect(new MobEffectInstance((EffectInit.HELLFIRE_EFFECT.get()), 400, 0, false, true, true));
            }
        }
    }



    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("item.dmnr.amulet_of_hellfire.description").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("  "));
        super.appendHoverText(stack, world, tooltip, flag);
    }
}
