package de.joh.dmnr.common.item;

import com.mna.api.events.RoteProgressGainedEvent;
import com.mna.api.items.IRelic;
import de.joh.dmnr.common.init.ItemInit;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JournalOfMysteriesItem extends Item implements IRelic, ICurioItem {
    public JournalOfMysteriesItem() {
        super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.RARE));
    }

    public static void roteBoost(RoteProgressGainedEvent event) {
        if (CuriosApi.getCuriosHelper().findFirstCurio(event.getPlayer(), ItemInit.JOURNAL_OF_MYSTERIES.get()).isPresent()) {
            event.setAmount(event.getAmount() * 2.5f);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.dmnr.journal_of_mysteries.lore").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.dmnr.journal_of_mysteries.lore_2").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("  "));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}
