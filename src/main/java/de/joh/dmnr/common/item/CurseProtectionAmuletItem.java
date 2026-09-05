package de.joh.dmnr.common.item;

import com.mna.items.artifice.curio.IPreEnchantedItem;
import de.joh.tnl.api.item.ChargeableDragonMagicItem;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.common.spell.shape.CurseShape;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Protects the wearer of Negativ Potion Effects (which do not last longer then 5 min and can be removed with milk) and of the {@link CurseShape Curse Shape}
 * <br>Dragon Magic Upgrade: allows to block any negative Potion Effect regadles of the duration (mana drain equal to the current maximum duration)
 * @author Joh0210
 */
public class CurseProtectionAmuletItem extends ChargeableDragonMagicItem implements IPreEnchantedItem<CurseProtectionAmuletItem> {
    public CurseProtectionAmuletItem() {
        super((new Item.Properties()).setNoRepair().rarity(Rarity.EPIC).fireResistant(), 2000.0F);
    }

    @Override
    public String dragonMagicID() {
        return "item.dmnr.curse_protection_amulet.dragonmagic";
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.dmnr.curse_protection_amulet").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("  "));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean tickEffect(ItemStack stack, Player player, Level world, int slot, float mana, boolean selected) {
        return false;
    }

    protected boolean tickCurio() {
        return false;
    }

    /**
     * Protects the wearer of Negativ Potion Effects (which do not last longer then 5 min and can be removed with milk) if amulet is equipped
     * <br>Dragon Magic Upgrade: allows to block any negative Potion Effect regadles of the duration (mana drain equal to the current maximum duration)
     */
    public static void eventHandleDenyHarmful(MobEffectEvent.Applicable event) {
        MobEffectInstance effectInstance = event.getEffectInstance();

        if (effectInstance.getEffect().getCategory() != MobEffectCategory.HARMFUL) {
            return;
        }

        LivingEntity wearer = event.getEntity();
        CurseProtectionAmuletItem amulet = (CurseProtectionAmuletItem) ItemInit.CURSE_PROTECTION_AMULET.get();

        boolean hasDM = amulet.hasDragonMagic(wearer);
        int duration = effectInstance.getDuration();


        boolean isMilkCurable = effectInstance.getEffect().getCurativeItems().stream()
                .anyMatch(s -> s.getItem() == Items.MILK_BUCKET);
        boolean isBlockableWithoutMagic = duration > 0 && duration < 6000 && isMilkCurable;


        if (!hasDM && !isBlockableWithoutMagic) {
            return;
        }

        int amplifier = effectInstance.getAmplifier() + 1;
        int baseAmount = (int) ((duration * amplifier) / 20.0f);
        int amount = hasDM ? (int)Math.min(baseAmount*0.75f, 200) : baseAmount;
        if (amulet.isEquippedAndHasMana(wearer, amount, true)) {
            event.setResult(Event.Result.DENY);
        }
    }
}
