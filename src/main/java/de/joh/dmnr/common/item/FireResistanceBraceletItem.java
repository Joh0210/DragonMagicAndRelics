package de.joh.dmnr.common.item;

import com.mna.api.faction.IFaction;
import com.mna.api.items.IFactionSpecific;
import com.mna.api.items.TieredItem;
import com.mna.factions.Factions;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.networking.ModMessages;
import de.joh.dmnr.networking.packet.ToggleMajorFireResS2CPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

/**
 * Grants Immunity against Fire and Explosion Dmg
 * Allows to see in lava
 * @author Joh0210
 */
public class FireResistanceBraceletItem extends TieredItem implements ICurioItem, IFactionSpecific {
    public FireResistanceBraceletItem() {
        super(new Item.Properties().fireResistant().stacksTo(1));
    }

    public static boolean eventHandleFireAndExplosionProtection(LivingAttackEvent event) {
        DamageSource source = event.getSource();
        if(event.getEntity() instanceof Player player){
            if (!player.level().isClientSide && (source.is(DamageTypeTags.IS_EXPLOSION) || source.is(DamageTypeTags.IS_FIRE)) && CuriosApi.getCuriosHelper().findFirstCurio(player, ItemInit.FIRE_RESISTANCE_BRACELET.get()).isPresent()) {
                event.setCanceled(true);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if(slotContext.entity() instanceof ServerPlayer) {
            ModMessages.sendToPlayer(new ToggleMajorFireResS2CPacket(true), (ServerPlayer) slotContext.entity());
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if(slotContext.entity() instanceof ServerPlayer) {
            ModMessages.sendToPlayer(new ToggleMajorFireResS2CPacket(false), (ServerPlayer) slotContext.entity());
        }
    }


    @Override
    public IFaction getFaction() {
        return  Factions.DEMONS;
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("item.dmnr.fire_resistance_bracelet.description").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("  "));
        super.appendHoverText(stack, world, tooltip, flag);
    }
}
