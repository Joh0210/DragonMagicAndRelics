package de.joh.dragonmagicandrelics.events.additional;

import com.mna.api.faction.IFaction;
import com.mna.factions.Factions;
import com.mna.items.armor.*;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.armorupgrades.types.ArmorUpgrade;
import de.joh.dragonmagicandrelics.commands.Commands;
import de.joh.dragonmagicandrelics.item.ItemInit;
import de.joh.dragonmagicandrelics.item.items.dragonmagearmor.DragonMageArmor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.HashMap;

/**
 * This event is called when the player gets the Dragon Mage Armor.
 * If you want to add more Dragon Mage Armos for new factions, this event must be caught
 * and entered via setAlternativeArmorValues() (if the condition is met)
 * @see de.joh.dragonmagicandrelics.rituals.contexts.DragonMageArmorRitual
 * @see Commands
 * @author Joh0210
 */
public class DragonUpgradeEvent extends PlayerEvent {
    private boolean canBeUpgraded;
    private final IFaction targetFaction;
    private HashMap<ArmorUpgrade, Integer> initialUpgrades;
    private final ItemStack headOld;
    private final ItemStack chestOld;
    private final ItemStack legsOld;
    private final ItemStack feetOld;
    private ItemStack headNew = null;
    private ItemStack chestNew = null;
    private ItemStack legsNew = null;
    private ItemStack feetNew = null;

    /**
     * When the event is called, the Dragon Mage Armors will be set for the default factions
     * @param player Player who gets the armor
     * @param targetFaction Target faction you want the armor to have
     */
    public DragonUpgradeEvent(Player player, IFaction targetFaction){
        super(player);
        this.targetFaction = targetFaction;
        initialUpgrades = getBaseInitialUpgrades(targetFaction);
        headOld = player.getItemBySlot(EquipmentSlot.HEAD);
        chestOld = player.getItemBySlot(EquipmentSlot.CHEST);
        legsOld = player.getItemBySlot(EquipmentSlot.LEGS);
        feetOld = player.getItemBySlot(EquipmentSlot.FEET);


        if ((chestOld.getItem() instanceof BoneArmorItem || chestOld.getItem() instanceof CouncilArmorItem || chestOld.getItem() instanceof DemonArmorItem || chestOld.getItem() instanceof FeyArmorItem || chestOld.getItem() instanceof DragonMageArmor) && ((ISetItem) chestOld.getItem()).isSetEquipped(player)){
            ((ISetItem)player.getItemBySlot(EquipmentSlot.CHEST).getItem()).removeSetBonus(player, EquipmentSlot.CHEST);
        }else{
            canBeUpgraded = false;
            return;
        }

        if(targetFaction == Factions.UNDEAD){
            headNew = new ItemStack(ItemInit.ABYSSAL_DRAGON_MAGE_HELMET.get());
            chestNew = new ItemStack(ItemInit.ABYSSAL_DRAGON_MAGE_CHESTPLATE.get());
            legsNew = new ItemStack(ItemInit.ABYSSAL_DRAGON_MAGE_LEGGING.get());
            feetNew = new ItemStack(ItemInit.ABYSSAL_DRAGON_MAGE_BOOTS.get());
        }
        else if(targetFaction == Factions.COUNCIL){
            headNew = new ItemStack(ItemInit.ARCH_DRAGON_MAGE_HELMET.get());
            chestNew = new ItemStack(ItemInit.ARCH_DRAGON_MAGE_CHESTPLATE.get());
            legsNew = new ItemStack(ItemInit.ARCH_DRAGON_MAGE_LEGGING.get());
            feetNew = new ItemStack(ItemInit.ARCH_DRAGON_MAGE_BOOTS.get());
        }
        else if(targetFaction == Factions.FEY){
            headNew = new ItemStack(ItemInit.WILD_DRAGON_MAGE_HELMET.get());
            chestNew = new ItemStack(ItemInit.WILD_DRAGON_MAGE_CHESTPLATE.get());
            legsNew = new ItemStack(ItemInit.WILD_DRAGON_MAGE_LEGGING.get());
            feetNew = new ItemStack(ItemInit.WILD_DRAGON_MAGE_BOOTS.get());
        }
        else if (targetFaction == Factions.DEMONS){
            headNew = new ItemStack(ItemInit.INFERNAL_DRAGON_MAGE_HELMET.get());
            chestNew = new ItemStack(ItemInit.INFERNAL_DRAGON_MAGE_CHESTPLATE.get());
            legsNew = new ItemStack(ItemInit.INFERNAL_DRAGON_MAGE_LEGGING.get());
            feetNew = new ItemStack(ItemInit.INFERNAL_DRAGON_MAGE_BOOTS.get());
        }

        canBeUpgraded = true;
    }

    /**
     * The event alone does not give the player the armor.
     * After the event has been processed by the handlers,
     * this function must be carried out so that the player receives the armor
     * @param addBaseUpgrades Should the factions' start upgrades be added?
     */
    public void performUpgrade(boolean addBaseUpgrades){
        if(!canBeUpgraded){
            return;
        }

        if(headOld.hasTag()){
            CompoundTag nbtData = new CompoundTag();
            nbtData.put("Enchantments", headOld.getEnchantmentTags());
            headNew.getTag().merge(nbtData);
        }
        if(chestOld.hasTag()){
            CompoundTag nbtData = new CompoundTag();
            nbtData.put("Enchantments", chestOld.getEnchantmentTags());
            chestNew.getTag().merge(nbtData);
        }
        if(legsOld.hasTag()){
            CompoundTag nbtData = new CompoundTag();
            nbtData.put("Enchantments", legsOld.getEnchantmentTags());
            legsNew.getTag().merge(nbtData);
        }
        if(feetOld.hasTag()){
            CompoundTag nbtData = new CompoundTag();
            nbtData.put("Enchantments", feetOld.getEnchantmentTags());
            feetNew.getTag().merge(nbtData);
        }

        this.getPlayer().setItemSlot(EquipmentSlot.HEAD, headNew);
        this.getPlayer().setItemSlot(EquipmentSlot.CHEST, chestNew);
        this.getPlayer().setItemSlot(EquipmentSlot.LEGS, legsNew);
        this.getPlayer().setItemSlot(EquipmentSlot.FEET, feetNew);

        if(addBaseUpgrades){
            this.initialUpgrades.forEach((key, value) -> addUpgrade((ArmorUpgrade) key, (int)value));
        }
    }

    /**
     * Like the performUpgrade() function, except that the wearer is already wearing a full DM armor and wants to swap it into a different set.
     */
    public void performUpgradeFromDMArmor(){
        performUpgrade(false);
        if(chestOld.getItem() instanceof DragonMageArmor && chestOld.hasTag()){
            chestNew.setTag(chestOld.getTag());
        }
    }

    private void addUpgrade(ArmorUpgrade upgrade, int level){
        ItemStack itemStack = this.getPlayer().getItemBySlot(EquipmentSlot.CHEST);
        ((DragonMageArmor)itemStack.getItem()).addDragonMagicToItem(itemStack, upgrade, level, true);
    }

    /**
     * @return Can the performUpgrade/performUpgradeFromDMArmor function be performed?
     */
    public boolean canBeUpgraded() {
        return canBeUpgraded;
    }

    /**
     * @return Which faction color should have the armor
     */
    public IFaction getTargetFaction(){
        return targetFaction;
    }


    /**
     * If you want to give the wearer a different DM armor due to a new faction,
     * you have to catch the event and add the armor with this function
     * @param initialUpgrades HashMap with the initial upgrades the armor gets
     */
    public void setAlternativeArmorValues(ItemStack headNew, ItemStack chestNew, ItemStack legsNew, ItemStack feetNew, HashMap<ArmorUpgrade, Integer> initialUpgrades){
        this.headNew = headNew;
        this.chestNew = chestNew;
        this.legsNew = legsNew;
        this.feetNew = feetNew;
        this.initialUpgrades = initialUpgrades;

        canBeUpgraded = true;
    }

    private static HashMap<ArmorUpgrade, Integer> getBaseInitialUpgrades(IFaction targetFaction){
        HashMap<ArmorUpgrade, Integer> ret = new HashMap<>();
        if(targetFaction == Factions.UNDEAD){
            ret.put(ArmorUpgradeInit.MIST_FORM, 1);
            ret.put(ArmorUpgradeInit.DAMAGE_BOOST, 2);
            ret.put(ArmorUpgradeInit.DAMAGE_RESISTANCE, 1);
        }
        else if(targetFaction == Factions.COUNCIL){
            ret.put(ArmorUpgradeInit.FLY, 1);
            ret.put(ArmorUpgradeInit.PROJECTILE_REFLECTION, 3);
            ret.put(ArmorUpgradeInit.MAJOR_MANA_BOOST, 4);
            ret.put(ArmorUpgradeInit.MANA_REGEN, 3);
        }
        else if(targetFaction == Factions.FEY){
            ret.put(ArmorUpgradeInit.ANGEL_FLIGHT, 1);
            ret.put(ArmorUpgradeInit.SATURATION, 1);
            ret.put(ArmorUpgradeInit.REACH_DISTANCE, 1);
        }
        else if (targetFaction == Factions.DEMONS){
            ret.put(ArmorUpgradeInit.BURNING_FRENZY, 1);
            ret.put(ArmorUpgradeInit.DAMAGE_BOOST, 2);
            ret.put(ArmorUpgradeInit.MAJOR_FIRE_RESISTANCE, 1);
        }
        return ret;
    }
}
