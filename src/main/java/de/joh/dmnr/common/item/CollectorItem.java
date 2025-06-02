package de.joh.dmnr.common.item;

import com.mna.api.items.TieredItem;
import de.joh.dmnr.DragonMagicAndRelics;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

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
}

