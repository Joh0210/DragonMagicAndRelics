package de.joh.dmnr.common.item;

import com.mna.api.events.MasteryGainedEvent;
import com.mna.api.events.RoteProgressGainedEvent;
import com.mna.api.items.IRelic;
import com.mna.capabilities.playerdata.rote.PlayerRoteSpellsProvider;
import de.joh.tnl.api.item.IDragonMagicItem;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.common.init.SpellInit;
import de.joh.dmnr.common.util.RLoc;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
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

public class JournalOfMysteriesItem extends Item implements IRelic, ICurioItem, IDragonMagicItem {
    public JournalOfMysteriesItem() {
        super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC));
    }

    public static void roteBoost(RoteProgressGainedEvent event) {
        if (CuriosApi.getCuriosHelper().findFirstCurio(event.getPlayer(), ItemInit.JOURNAL_OF_MYSTERIES.get()).isPresent()) {
            event.setAmount(event.getAmount() * 2.5f * (ItemInit.JOURNAL_OF_MYSTERIES.get() instanceof IDragonMagicItem dmItem && dmItem.hasDragonMagic(event.getPlayer()) ? 2 : 1));
        }
    }

    public static void masteryBoost(MasteryGainedEvent event) {
        if (CuriosApi.getCuriosHelper().findFirstCurio(event.getPlayer(), ItemInit.JOURNAL_OF_MYSTERIES.get()).isPresent()) {
            event.setAmount(event.getAmount() * 2.5f * (ItemInit.JOURNAL_OF_MYSTERIES.get() instanceof IDragonMagicItem dmItem && dmItem.hasDragonMagic(event.getPlayer()) ? 2 : 1));
        }
    }

    @Override
    public String dragonMagicID() {
        return "item.dmnr.journal_of_mysteries.dragonmagic";
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return (this.isEnabled() && this.hasDragonMagic());
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (this.hasDragonMagic(player)) {
            player.getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent((p) -> {
                if(!p.isRote(SpellInit.SORCERERS_PRIDE)) {
                    p.setRoteXP(RLoc.create("components/sorcerers_pride"), SpellInit.SORCERERS_PRIDE.requiredXPForRote()+1);
                    player.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);
                }
            });
        }

        if (!level.isClientSide()) {
            MutableComponent text = Component.translatable("item.dmnr.journal_of_mysteries.chat");
            if (!this.hasDragonMagic(player)) {
                text.withStyle(style -> style.withFont(new ResourceLocation("minecraft", "alt")));
            }
            player.sendSystemMessage(text);
        }

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.dmnr.journal_of_mysteries.lore").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.dmnr.journal_of_mysteries.lore_2").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("  "));
        this.tooltipAddition(tooltip);
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}
