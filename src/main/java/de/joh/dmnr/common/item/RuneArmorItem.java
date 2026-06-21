package de.joh.dmnr.common.item;

import com.mna.api.items.ITieredItem;
import com.mna.items.armor.ISetItem;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.item.material.ArmorMaterials;
import de.joh.dmnr.common.util.RLoc;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;
import java.util.UUID;

public class RuneArmorItem extends ArmorItem implements ITieredItem<RuneArmorItem>, ISetItem {
    private int tier = -1;
    private final String curioType1 = "curio";
    private final String curioType2 = "necklace";
    private final UUID curiosID;
    private final AttributeModifier curiosMod;

    private final ResourceLocation setBonus;

    public RuneArmorItem(ArmorItem.Type type) {
        super(ArmorMaterials.RUNE_MATERIAL, type, new Item.Properties());
        this.setBonus = RLoc.create("rune_armor");
        String id = DragonMagicAndRelics.MOD_ID + "_rune_armor";
        this.curiosID = UUID.nameUUIDFromBytes(id.getBytes());
        this.curiosMod = new AttributeModifier(curiosID, id, 1, AttributeModifier.Operation.ADDITION);
    }


    public void onEquip(ItemStack itemStack, LivingEntity entity) {
        if(itemStack.getItem() instanceof RuneArmorItem armorItem && armorItem.type == Type.CHESTPLATE){
            var opt = CuriosApi.getCuriosInventory(entity).resolve()
                    .flatMap(x -> x.getStacksHandler(curioType2));
            opt.ifPresent(
                    iCurioStacksHandler -> iCurioStacksHandler.addTransientModifier(curiosMod));
        }
    }

    public void onDiscard(ItemStack itemStack, LivingEntity entity) {
        if(itemStack.getItem() instanceof RuneArmorItem armorItem && armorItem.type == Type.CHESTPLATE){
            var opt = CuriosApi.getCuriosInventory(entity).resolve()
                    .flatMap(x -> x.getStacksHandler(curioType2));
            opt.ifPresent(iCurioStacksHandler -> iCurioStacksHandler.removeModifier(curiosID));
        }
    }

    public void applySetBonus(LivingEntity entity, EquipmentSlot... setSlots) {
        var opt = CuriosApi.getCuriosInventory(entity).resolve()
                .flatMap(x -> x.getStacksHandler(curioType1));
        opt.ifPresent(
                iCurioStacksHandler -> iCurioStacksHandler.addTransientModifier(curiosMod));
    }

    public void removeSetBonus(LivingEntity entity, EquipmentSlot... setSlots) {
        var opt = CuriosApi.getCuriosInventory(entity).resolve()
                .flatMap(x -> x.getStacksHandler(curioType1));
        opt.ifPresent(iCurioStacksHandler -> iCurioStacksHandler.removeModifier(curiosID));
    }


    @Override
    public void setCachedTier(int tier) {
        this.tier = tier;
    }

    @Override
    public int getCachedTier() {
        return tier;
    }


    @Override
    public ResourceLocation getSetIdentifier() {
        return setBonus;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        this.addSetTooltip(tooltip);
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return true;
    }
}
