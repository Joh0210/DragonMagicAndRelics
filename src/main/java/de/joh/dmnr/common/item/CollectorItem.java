package de.joh.dmnr.common.item;

import com.mna.api.items.TieredItem;
import de.joh.dmnr.DragonMagicAndRelics;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.UUID;

/**
 * Allows the user to ware additional curios of a given type.
 * @author Joh0210
 */
public class CollectorItem extends TieredItem implements ICurioItem {
    private final String curioType;
    private final UUID curiosID;
    private final AttributeModifier curiosMod;

    /**
     * @param curioType type of the additional curios
     * @param count number of additional curios
     */
    public CollectorItem(String curioType, int count) {
        super(new Item.Properties().stacksTo(1));
        this.curioType = curioType;
        String id = DragonMagicAndRelics.MOD_ID + "_collector_of_" + curioType + "_item";
        this.curiosID = UUID.nameUUIDFromBytes(id.getBytes());
        this.curiosMod = new AttributeModifier(curiosID, id, count, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        var opt = CuriosApi.getCuriosInventory(slotContext.entity()).resolve()
                .flatMap(x -> x.getStacksHandler(curioType));
        opt.ifPresent(
                iCurioStacksHandler -> iCurioStacksHandler.addTransientModifier(curiosMod));
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        var opt = CuriosApi.getCuriosInventory(slotContext.entity()).resolve()
                .flatMap(x -> x.getStacksHandler(curioType));
        opt.ifPresent(iCurioStacksHandler -> iCurioStacksHandler.removeModifier(curiosID));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("item.dmnr.collectors_amulet.description." + this.curioType).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("  "));
        super.appendHoverText(stack, world, tooltip, flag);
    }
}

