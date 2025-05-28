package de.joh.dmnr.common.item;

import com.mna.api.items.TieredItem;
import de.joh.dmnr.DragonMagicAndRelics;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class DefenseBraceletItem extends TieredItem implements ICurioItem {
    private final AttributeModifier armorMod;
    private final AttributeModifier toughnessMod;

    public DefenseBraceletItem(int armor, int toughness) {
        super(new Item.Properties().stacksTo(1));
        armorMod = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_defense_bracelet", armor, AttributeModifier.Operation.ADDITION);
        toughnessMod = new AttributeModifier(DragonMagicAndRelics.MOD_ID + "_defense_bracelet", toughness, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        ICurioItem.super.onEquip(slotContext, prevStack, stack);
        AttributeInstance armorAttribute = slotContext.entity().getAttribute(Attributes.ARMOR);
        if(armorAttribute != null && !armorAttribute.hasModifier(armorMod)) {
            armorAttribute.addTransientModifier(armorMod);
        }

        AttributeInstance toughnessAttribute = slotContext.entity().getAttribute(Attributes.ARMOR_TOUGHNESS);
        if(toughnessAttribute != null && !toughnessAttribute.hasModifier(toughnessMod)) {
            toughnessAttribute.addTransientModifier(toughnessMod);
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ICurioItem.super.onUnequip(slotContext, newStack, stack);
        AttributeInstance armorAttribute = slotContext.entity().getAttribute(Attributes.ARMOR);
        if(armorAttribute != null) {
            armorAttribute.removeModifier(armorMod);
        }

        AttributeInstance toughnessAttribute = slotContext.entity().getAttribute(Attributes.ARMOR_TOUGHNESS);
        if(toughnessAttribute != null) {
            toughnessAttribute.removeModifier(toughnessMod);
        }
    }
}