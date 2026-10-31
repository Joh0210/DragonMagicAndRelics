package de.joh.dmnr.common.item;

import com.mna.api.faction.IFaction;
import com.mna.api.items.IFactionSpecific;
import com.mna.api.items.TieredItem;
import com.mna.factions.Factions;
import de.joh.dmnr.common.util.RLoc;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeItem;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.List;

/**
 * The wearer gets visible wings.
 * @author Joh0210
 */
public class FallenAngelRingItem extends TieredItem implements IForgeItem, ICurioItem, IFactionSpecific, IWingItem {
    private static final List<WingLayer> WINGS = List.of(new WingLayer(RLoc.create("textures/models/fallen_angel_ring_wing.png")));

    public FallenAngelRingItem(Properties itemProperties) {
        super(itemProperties);
    }

    @Override
    public @NotNull List<WingLayer> getWingLayers(ItemStack stack) {
        return WINGS;
    }

    @Override
    public IFaction getFaction() {
        return Factions.UNDEAD;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.dmnr.fallen_angel_ring").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("  "));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        slotContext.entity().getPersistentData().putBoolean("bone_armor_set_bonus", true);
        onEquip(slotContext.identifier(), slotContext.index(), slotContext.entity(), stack);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        slotContext.entity().getPersistentData().putBoolean("bone_armor_set_bonus", false);
        onUnequip(slotContext.identifier(), slotContext.index(), slotContext.entity(), stack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@Nullable ItemStack itemStack){
        return true;
    }
}
