package de.joh.dmnr.api.util;

import de.joh.dmnr.common.init.ItemInit;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Creation of a creative tap for the items of this mod.
 * @author Joh0210
 */
public class CreativeModeTab {
    public static final net.minecraft.world.item.CreativeModeTab CreativeModeTab = new net.minecraft.world.item.CreativeModeTab("dmnr"){
        @Override
        public @NotNull ItemStack makeIcon(){
            return new ItemStack(ItemInit.INFERNAL_DRAGON_MAGE_HELMET.get());
        }
    };

    public static final net.minecraft.world.item.CreativeModeTab ArmorUpgradeModeTab = new net.minecraft.world.item.CreativeModeTab("armorupgrades"){
        @Override
        public @NotNull ItemStack makeIcon(){
            return new ItemStack(ItemInit.UPGRADE_SEAL_FLY.get());
        }
    };
}
