package de.joh.dmnr.common.item;

import com.mna.api.items.ChargeableItem;
import com.mna.items.artifice.curio.IPreEnchantedItem;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.common.spell.shape.CurseShape;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Protects the wearer of Negativ Potion Effects (which do not last longer then 5 min and can be removed with milk) and of the {@link CurseShape Curse Shape}
 * @author Joh0210
 */
public class CurseProtectionAmuletItem extends ChargeableItem implements IPreEnchantedItem<CurseProtectionAmuletItem> {
    public CurseProtectionAmuletItem() {
        super((new Item.Properties()).setNoRepair().rarity(Rarity.EPIC), 2000.0F);
    }

    protected boolean tickEffect(ItemStack stack, Player player, Level world, int slot, float mana, boolean selected) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.dmnr.curse_protection_amulet").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("  "));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    protected boolean tickCurio() {
        return false;
    }

    /**
     * Protects the wearer of Negativ Potion Effects (which do not last longer then 5 min and can be removed with milk) if amulet is equipped
     */
    public static void eventHandleDenyHarmful(MobEffectEvent.Applicable event){
        if(event.getEffectInstance().getEffect().getCategory() == MobEffectCategory.HARMFUL
                && event.getEffectInstance().getDuration() < 6000
                && event.getEffectInstance().getDuration() > 0
                && event.getEffectInstance().getEffect().getCurativeItems().stream().anyMatch(s -> s.getItem() == Items.MILK_BUCKET)
        ){
            int amount = (int) Math.min(event.getEffectInstance().getDuration() * (event.getEffectInstance().getAmplifier() +1) / 20.0f, 150.0f);
            if(((CurseProtectionAmuletItem) ItemInit.CURSE_PROTECTION_AMULET.get()).isEquippedAndHasMana(event.getEntity(), amount, true)){
                event.setResult(Event.Result.DENY);
            }
        }
    }
}
