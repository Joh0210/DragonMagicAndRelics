package de.joh.dmnr.item.items.dragonmagearmor;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.armorupgrades.ArmorUpgradeInit;
import de.joh.dmnr.gui.NamedArchDragonMageArmor;
import de.joh.dmnr.utils.RLoc;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;

/**
 * This armor is the main item of this mod.
 * This armor defaults to netherite armor, which can be enhanced with ugprades.
 * The list of upgrades and their effects can be found in ArmorUpgradeInit.
 * In addition, it can be enhanced with spells that are cast when the wearer takes damage. (see DamageEventHandler)
 * @see ArmorUpgradeInit
 * @see de.joh.dmnr.events.DamageEventHandler
 * @author Joh0210
 */
public class ArchDragonMageArmor extends DragonMageArmor {
    public ArchDragonMageArmor(EquipmentSlot pSlot) {
        super(pSlot, RLoc.create(DragonMagicAndRelics.MOD_ID + "_arch_armor_set_bonus"));
    }

    @Override
    public MenuProvider getProvider(ItemStack itemStack) {
        return this.slot == EquipmentSlot.CHEST ? new NamedArchDragonMageArmor(itemStack) : null;
    }

    @Override
    public ResourceLocation getWingTextureLocation() {
        return RLoc.create("textures/models/armor/arch_dragon_wing.png");
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return RLoc.create("textures/models/armor/arch_dragon_mage_armor_texture.png");
    }
}
