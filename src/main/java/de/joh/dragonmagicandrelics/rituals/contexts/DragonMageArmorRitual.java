package de.joh.dragonmagicandrelics.rituals.contexts;

import com.ma.api.rituals.IRitualContext;
import com.ma.api.rituals.RitualEffect;
import com.ma.capabilities.playerdata.magic.PlayerMagicProvider;
import com.ma.items.armor.*;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgrade;
import de.joh.dragonmagicandrelics.config.InitialUpgradesConfigs;
import de.joh.dragonmagicandrelics.item.ItemInit;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * This ritual upgrades faction armor to Dragon Mage Armor.
 * Depending on the faction armor, different initial upgrades will be installed.
 * @see DragonMageArmor
 * @author Joh0210
 */
public class DragonMageArmorRitual extends RitualEffect {
    public DragonMageArmorRitual(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        ItemStack head = context.getCaster().getItemStackFromSlot(EquipmentSlotType.HEAD);
        ItemStack chest = context.getCaster().getItemStackFromSlot(EquipmentSlotType.CHEST);
        ItemStack legs = context.getCaster().getItemStackFromSlot(EquipmentSlotType.LEGS);
        ItemStack feet = context.getCaster().getItemStackFromSlot(EquipmentSlotType.FEET);

        //Only possible in creative:
        if ((chest.getItem() instanceof BoneArmorItem || chest.getItem() instanceof CouncilArmorItem || chest.getItem() instanceof DemonArmorItem || chest.getItem() instanceof FeyArmorItem) && ((ISetItem) chest.getItem()).isSetEquipped(context.getCaster())){
            ((ISetItem)context.getCaster().getItemStackFromSlot(EquipmentSlotType.CHEST).getItem()).removeSetBonus(context.getCaster(), EquipmentSlotType.CHEST);
        }else{
            //If you remove your armor while the ritual is active, it will fail
            //The ingredients will be lost
            return false;
        }

        ItemStack headNew = null;
        ItemStack chestNew = null;
        ItemStack legsNew = null;
        ItemStack feetNew = null;

        if(chest.getItem() instanceof BoneArmorItem){
            headNew = new ItemStack(ItemInit.ABYSSAL_DRAGON_MAGE_HELMET.get());
            chestNew = new ItemStack(ItemInit.ABYSSAL_DRAGON_MAGE_CHESTPLATE.get());
            legsNew = new ItemStack(ItemInit.ABYSSAL_DRAGON_MAGE_LEGGING.get());
            feetNew = new ItemStack(ItemInit.ABYSSAL_DRAGON_MAGE_BOOTS.get());
        }
        else if(chest.getItem() instanceof CouncilArmorItem){
            headNew = new ItemStack(ItemInit.ARCH_DRAGON_MAGE_HELMET.get());
            chestNew = new ItemStack(ItemInit.ARCH_DRAGON_MAGE_CHESTPLATE.get());
            legsNew = new ItemStack(ItemInit.ARCH_DRAGON_MAGE_LEGGING.get());
            feetNew = new ItemStack(ItemInit.ARCH_DRAGON_MAGE_BOOTS.get());
        }
        else if(chest.getItem() instanceof FeyArmorItem){
            headNew = new ItemStack(ItemInit.WILD_DRAGON_MAGE_HELMET.get());
            chestNew = new ItemStack(ItemInit.WILD_DRAGON_MAGE_CHESTPLATE.get());
            legsNew = new ItemStack(ItemInit.WILD_DRAGON_MAGE_LEGGING.get());
            feetNew = new ItemStack(ItemInit.WILD_DRAGON_MAGE_BOOTS.get());
        }
        else {
            //DemonArmorItem
            headNew = new ItemStack(ItemInit.INFERNAL_DRAGON_MAGE_HELMET.get());
            chestNew = new ItemStack(ItemInit.INFERNAL_DRAGON_MAGE_CHESTPLATE.get());
            legsNew = new ItemStack(ItemInit.INFERNAL_DRAGON_MAGE_LEGGING.get());
            feetNew = new ItemStack(ItemInit.INFERNAL_DRAGON_MAGE_BOOTS.get());
        }


        if(head.hasTag()){
            CompoundNBT nbtData = new CompoundNBT();
            nbtData.put("Enchantments", head.getEnchantmentTagList());
            headNew.getTag().merge(nbtData);
        }
        if(chest.hasTag()){
            CompoundNBT nbtData = new CompoundNBT();
            nbtData.put("Enchantments", chest.getEnchantmentTagList());
            chestNew.getTag().merge(nbtData);
        }
        if(legs.hasTag()){
            CompoundNBT nbtData = new CompoundNBT();
            nbtData.put("Enchantments", legs.getEnchantmentTagList());
            legsNew.getTag().merge(nbtData);
        }
        if(feet.hasTag()){
            CompoundNBT nbtData = new CompoundNBT();
            nbtData.put("Enchantments", feet.getEnchantmentTagList());
            feetNew.getTag().merge(nbtData);
        }

        context.getCaster().setItemStackToSlot(EquipmentSlotType.HEAD, headNew);
        context.getCaster().setItemStackToSlot(EquipmentSlotType.CHEST, chestNew);
        context.getCaster().setItemStackToSlot(EquipmentSlotType.LEGS, legsNew);
        context.getCaster().setItemStackToSlot(EquipmentSlotType.FEET, feetNew);


        if(chest.getItem() instanceof BoneArmorItem){
            InitialUpgradesConfigs.getBoneInitEffects().forEach((key, value) -> upgrade(context, (ArmorUpgrade) key, (int)value));
        }
        else if(chest.getItem() instanceof CouncilArmorItem){
            InitialUpgradesConfigs.getCouncilInitEffects().forEach((key, value) -> upgrade(context, (ArmorUpgrade) key, (int)value));
        }
        else if(chest.getItem() instanceof DemonArmorItem){
            InitialUpgradesConfigs.getDemonInitEffects().forEach((key, value) -> upgrade(context, (ArmorUpgrade) key, (int)value));
        }
        else if(chest.getItem() instanceof FeyArmorItem){
            InitialUpgradesConfigs.getFeyInitEffects().forEach((key, value) -> upgrade(context, (ArmorUpgrade) key, (int)value));
        }

        return  true;
    }

    public void upgrade(IRitualContext context, ArmorUpgrade upgrade, int level){
        ((DragonMageArmor)context.getCaster().getItemStackFromSlot(EquipmentSlotType.CHEST).getItem()).setUpgradeLevel(upgrade, level, context.getCaster());
    }

    @Override
    public boolean applyStartCheckInCreative() {
        return true;
    }

    @Override
    public TextComponent canRitualStart(IRitualContext context) {
        PlayerEntity player = context.getCaster();
        ItemStack chest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);

        if (!(chest.getItem() instanceof BoneArmorItem || chest.getItem() instanceof CouncilArmorItem || chest.getItem() instanceof DemonArmorItem || chest.getItem() instanceof FeyArmorItem) || !((ISetItem) chest.getItem()).isSetEquipped(player)){
            return new TranslationTextComponent("dragonmagicandrelics.ritual.output.dragonmagearmorritual.wrong.armor.error");
        }
        final boolean[] isLevel75 = {false};

        player.getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> isLevel75[0] = 75 <= m.getMagicLevel());

        return isLevel75[0] ? null : new TranslationTextComponent("dragonmagicandrelics.ritual.output.dragonmagearmorritual.to.low.level.error");
    }

    @Override
    protected int getApplicationTicks(IRitualContext iRitualContext) {
        return 0;
    }
}
