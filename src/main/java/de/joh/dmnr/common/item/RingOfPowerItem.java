package de.joh.dmnr.common.item;

import de.joh.dmnr.client.gui.NamedRingOfPower;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;

public class RingOfPowerItem extends SpellRingBaseItem<RingOfPowerItem> {
    public RingOfPowerItem() {
        super();
    }

    public static void eventHandleAttack(LivingHurtEvent event){
        if(event.getEntity() instanceof Player defender){
            CuriosApi.getCuriosInventory(defender).ifPresent(curiosProvider -> curiosProvider.getCurios().forEach((identifier, stackHandler) -> {
                for (int k = 0; k < stackHandler.getSlots(); k++) {
                    ItemStack stack = stackHandler.getStacks().getStackInSlot(k);
                    if (!stack.isEmpty() && stack.getItem() instanceof RingOfPowerItem) {
                        if(event.getSource().getEntity() instanceof LivingEntity attacker && attacker != defender){
                            SpellRingBaseItem.applySpell(stack, true, defender, attacker);
                        }
                    }
                }
            }));
        }
    }

    @Override
    public MenuProvider getProvider(ItemStack itemStack) {
        return new NamedRingOfPower(itemStack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("item.dmnr.ring_of_power.description").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.dmnr.ring_of_power.description2").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, world, tooltip, flag);
    }
}
