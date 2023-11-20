package de.joh.dmnr.gui;

import de.joh.dmnr.DragonMagicAndRelics;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ContainerInit {
    public static final DeferredRegister<MenuType<?>> CONTAINERS;
    static final String BRACELET_OF_FRIENDSHIP_ID = "dmnr:bracelet_of_friendship";
    @ObjectHolder(BRACELET_OF_FRIENDSHIP_ID)
    public static final MenuType<ContainerBraceletOfFriendship> BRACELET_OF_FRIENDSHIP;

    static final String ABYSSAL_DRAGON_MAGE_CHESTPLATE_ID = "dmnr:abyssal_dragon_mage_chestplate";
    @ObjectHolder(ABYSSAL_DRAGON_MAGE_CHESTPLATE_ID)
    public static final MenuType<ContainerAbyssalDragonMageArmor> ABYSSAL_DRAGON_MAGE_CHESTPLATE;

    static final String ARCH_DRAGON_MAGE_CHESTPLATE_ID = "dmnr:arch_dragon_mage_chestplate";
    @ObjectHolder(ARCH_DRAGON_MAGE_CHESTPLATE_ID)
    public static final MenuType<ContainerArchDragonMageArmor> ARCH_DRAGON_MAGE_CHESTPLATE;

    static final String INFERNAL_DRAGON_MAGE_CHESTPLATE_ID = "dmnr:infernal_dragon_mage_chestplate";
    @ObjectHolder(INFERNAL_DRAGON_MAGE_CHESTPLATE_ID)
    public static final MenuType<ContainerInfernalDragonMageArmor> INFERNAL_DRAGON_MAGE_CHESTPLATE;

    static final String WILD_DRAGON_MAGE_CHESTPLATE_ID = "dmnr:wild_dragon_mage_chestplate";
    @ObjectHolder(WILD_DRAGON_MAGE_CHESTPLATE_ID)
    public static final MenuType<ContainerWildDragonMageArmor> WILD_DRAGON_MAGE_CHESTPLATE;

    public ContainerInit() {
    }

    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<MenuType<?>> event) {
        IForgeRegistry<MenuType<?>> r = event.getRegistry();
        r.register((new MenuType<>(ContainerBraceletOfFriendship::new)).setRegistryName(BRACELET_OF_FRIENDSHIP_ID));
        r.register((new MenuType<>(ContainerAbyssalDragonMageArmor::new)).setRegistryName(ABYSSAL_DRAGON_MAGE_CHESTPLATE_ID));
        r.register((new MenuType<>(ContainerArchDragonMageArmor::new)).setRegistryName(ARCH_DRAGON_MAGE_CHESTPLATE_ID));
        r.register((new MenuType<>(ContainerInfernalDragonMageArmor::new)).setRegistryName(INFERNAL_DRAGON_MAGE_CHESTPLATE_ID));
        r.register((new MenuType<>(ContainerWildDragonMageArmor::new)).setRegistryName(WILD_DRAGON_MAGE_CHESTPLATE_ID));
    }

    static {
        CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, DragonMagicAndRelics.MOD_ID);
        BRACELET_OF_FRIENDSHIP = null;
        ABYSSAL_DRAGON_MAGE_CHESTPLATE = null;
        ARCH_DRAGON_MAGE_CHESTPLATE = null;
        INFERNAL_DRAGON_MAGE_CHESTPLATE = null;
        WILD_DRAGON_MAGE_CHESTPLATE = null;
    }
}
