package de.joh.dragonmagicandrelics.rituals.contexts;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.items.armor.*;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgrade;
import de.joh.dragonmagicandrelics.config.InitialUpgradesConfigs;
import de.joh.dragonmagicandrelics.item.ItemInit;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

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
        ItemStack head = context.getCaster().getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chest = context.getCaster().getItemBySlot(EquipmentSlot.CHEST);
        ItemStack legs = context.getCaster().getItemBySlot(EquipmentSlot.LEGS);
        ItemStack feet = context.getCaster().getItemBySlot(EquipmentSlot.FEET);

        //Only possible in creative:
        if ((chest.getItem() instanceof BoneArmorItem || chest.getItem() instanceof CouncilArmorItem || chest.getItem() instanceof DemonArmorItem || chest.getItem() instanceof FeyArmorItem) && ((ISetItem) chest.getItem()).isSetEquipped(context.getCaster())){
            ((ISetItem)context.getCaster().getItemBySlot(EquipmentSlot.CHEST).getItem()).removeSetBonus(context.getCaster(), EquipmentSlot.CHEST);
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
            CompoundTag nbtData = new CompoundTag();
            nbtData.put("Enchantments", head.getEnchantmentTags());
            headNew.getTag().merge(nbtData);
        }
        if(chest.hasTag()){
            CompoundTag nbtData = new CompoundTag();
            nbtData.put("Enchantments", chest.getEnchantmentTags());
            chestNew.getTag().merge(nbtData);
        }
        if(legs.hasTag()){
            CompoundTag nbtData = new CompoundTag();
            nbtData.put("Enchantments", legs.getEnchantmentTags());
            legsNew.getTag().merge(nbtData);
        }
        if(feet.hasTag()){
            CompoundTag nbtData = new CompoundTag();
            nbtData.put("Enchantments", feet.getEnchantmentTags());
            feetNew.getTag().merge(nbtData);
        }

        context.getCaster().setItemSlot(EquipmentSlot.HEAD, headNew);
        context.getCaster().setItemSlot(EquipmentSlot.CHEST, chestNew);
        context.getCaster().setItemSlot(EquipmentSlot.LEGS, legsNew);
        context.getCaster().setItemSlot(EquipmentSlot.FEET, feetNew);


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
        ((DragonMageArmor)context.getCaster().getItemBySlot(EquipmentSlot.CHEST).getItem()).setUpgradeLevel(upgrade, level, context.getCaster());
    }

    @Override
    public boolean applyStartCheckInCreative() {
        return true;
    }

    @Override
    public Component canRitualStart(IRitualContext context) {
        Player player = context.getCaster();
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);

        if (!(chest.getItem() instanceof BoneArmorItem || chest.getItem() instanceof CouncilArmorItem || chest.getItem() instanceof DemonArmorItem || chest.getItem() instanceof FeyArmorItem) || !((ISetItem) chest.getItem()).isSetEquipped(player)){
            return new TranslatableComponent("dragonmagicandrelics.ritual.output.dragonmagearmorritual.wrong.armor.error");
        }
        final boolean[] isLevel75 = {false};

        player.getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> isLevel75[0] = 75 <= m.getMagicLevel());

        return isLevel75[0] ? null : new TranslatableComponent("dragonmagicandrelics.ritual.output.dragonmagearmorritual.to.low.level.error");
    }

    @Override
    protected int getApplicationTicks(IRitualContext iRitualContext) {
        return 0;
    }
}
