package de.joh.dmnr.client.init;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.client.gui.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegisterEvent;

/**
 * Inits of the container of container-items/blocks
 * @author Joh0210
 */
@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ContainerInit {
    public static final DeferredRegister<MenuType<?>> CONTAINERS;
    static final String BRACELET_OF_FRIENDSHIP_ID = "dmnr:bracelet_of_friendship";
    @ObjectHolder(registryName = "menu", value = BRACELET_OF_FRIENDSHIP_ID)
    public static final MenuType<ContainerBraceletOfFriendship> BRACELET_OF_FRIENDSHIP;

    static final String ABYSSAL_DRAGON_MAGE_CHESTPLATE_ID = "dmnr:abyssal_dragon_mage_chestplate";
    @ObjectHolder(registryName = "menu", value = ABYSSAL_DRAGON_MAGE_CHESTPLATE_ID)
    public static final MenuType<ContainerAbyssalDragonMageArmor> ABYSSAL_DRAGON_MAGE_CHESTPLATE;

    static final String ARCH_DRAGON_MAGE_CHESTPLATE_ID = "dmnr:arch_dragon_mage_chestplate";
    @ObjectHolder(registryName = "menu", value = ARCH_DRAGON_MAGE_CHESTPLATE_ID)
    public static final MenuType<ContainerArchDragonMageArmor> ARCH_DRAGON_MAGE_CHESTPLATE;

    static final String INFERNAL_DRAGON_MAGE_CHESTPLATE_ID = "dmnr:infernal_dragon_mage_chestplate";
    @ObjectHolder(registryName = "menu", value = INFERNAL_DRAGON_MAGE_CHESTPLATE_ID)
    public static final MenuType<ContainerInfernalDragonMageArmor> INFERNAL_DRAGON_MAGE_CHESTPLATE;

    static final String WILD_DRAGON_MAGE_CHESTPLATE_ID = "dmnr:wild_dragon_mage_chestplate";
    @ObjectHolder(registryName = "menu", value = WILD_DRAGON_MAGE_CHESTPLATE_ID)
    public static final MenuType<ContainerWildDragonMageArmor> WILD_DRAGON_MAGE_CHESTPLATE;

    public ContainerInit() {
    }

    @SubscribeEvent
    public static void registerContainers(RegisterEvent event) {
        event.register(ForgeRegistries.MENU_TYPES.getRegistryKey(), (helper) -> {
            helper.register(new ResourceLocation(BRACELET_OF_FRIENDSHIP_ID), new MenuType<>(ContainerBraceletOfFriendship::new));
            helper.register(new ResourceLocation(ABYSSAL_DRAGON_MAGE_CHESTPLATE_ID), new MenuType<>(ContainerAbyssalDragonMageArmor::new));
            helper.register(new ResourceLocation(ARCH_DRAGON_MAGE_CHESTPLATE_ID), new MenuType<>(ContainerArchDragonMageArmor::new));
            helper.register(new ResourceLocation(INFERNAL_DRAGON_MAGE_CHESTPLATE_ID), new MenuType<>(ContainerInfernalDragonMageArmor::new));
            helper.register(new ResourceLocation(WILD_DRAGON_MAGE_CHESTPLATE_ID), new MenuType<>(ContainerWildDragonMageArmor::new));
        });
    }

    static {
        CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, DragonMagicAndRelics.MOD_ID);
        BRACELET_OF_FRIENDSHIP = null;
        ABYSSAL_DRAGON_MAGE_CHESTPLATE = null;
        ARCH_DRAGON_MAGE_CHESTPLATE = null;
        INFERNAL_DRAGON_MAGE_CHESTPLATE = null;
        WILD_DRAGON_MAGE_CHESTPLATE = null;
    }
}
