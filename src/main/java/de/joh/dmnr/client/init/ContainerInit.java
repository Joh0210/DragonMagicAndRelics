package de.joh.dmnr.client.init;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.client.gui.*;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Inits of the container of container-items/blocks
 * @author Joh0210
 */
@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ContainerInit {
    public static final DeferredRegister<MenuType<?>> CONTAINERS;
    static final String BRACELET_OF_FRIENDSHIP_ID = "bracelet_of_friendship";
    public static final RegistryObject<MenuType<ContainerBraceletOfFriendship>> BRACELET_OF_FRIENDSHIP;

    static final String ABYSSAL_DRAGON_MAGE_CHESTPLATE_ID = "abyssal_dragon_mage_chestplate";
    public static final RegistryObject<MenuType<ContainerAbyssalDragonMageArmor>> ABYSSAL_DRAGON_MAGE_CHESTPLATE;

    static final String ARCH_DRAGON_MAGE_CHESTPLATE_ID = "arch_dragon_mage_chestplate";
    public static final RegistryObject<MenuType<ContainerArchDragonMageArmor>>ARCH_DRAGON_MAGE_CHESTPLATE;

    static final String INFERNAL_DRAGON_MAGE_CHESTPLATE_ID = "infernal_dragon_mage_chestplate";
    public static final RegistryObject<MenuType<ContainerInfernalDragonMageArmor>> INFERNAL_DRAGON_MAGE_CHESTPLATE;

    static final String WILD_DRAGON_MAGE_CHESTPLATE_ID = "wild_dragon_mage_chestplate";
    public static final RegistryObject<MenuType<ContainerWildDragonMageArmor>> WILD_DRAGON_MAGE_CHESTPLATE;

    public ContainerInit() {
    }

    static <T extends Block> String of(RegistryObject<T> block) {
        return block.getId().getPath();
    }

    static {
        CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, DragonMagicAndRelics.MOD_ID);
        BRACELET_OF_FRIENDSHIP = CONTAINERS.register(BRACELET_OF_FRIENDSHIP_ID,
                () -> IForgeMenuType.create(ContainerBraceletOfFriendship::new));
        ABYSSAL_DRAGON_MAGE_CHESTPLATE = CONTAINERS.register(ABYSSAL_DRAGON_MAGE_CHESTPLATE_ID,
                () -> IForgeMenuType.create(ContainerAbyssalDragonMageArmor::new));

        ARCH_DRAGON_MAGE_CHESTPLATE = CONTAINERS.register(ARCH_DRAGON_MAGE_CHESTPLATE_ID,
                () -> IForgeMenuType.create(ContainerArchDragonMageArmor::new));

        INFERNAL_DRAGON_MAGE_CHESTPLATE = CONTAINERS.register(INFERNAL_DRAGON_MAGE_CHESTPLATE_ID,
                () -> IForgeMenuType.create(ContainerInfernalDragonMageArmor::new));

        WILD_DRAGON_MAGE_CHESTPLATE = CONTAINERS.register(WILD_DRAGON_MAGE_CHESTPLATE_ID,
                () -> IForgeMenuType.create(ContainerWildDragonMageArmor::new));
    }

    public static void register(IEventBus eventBus){
        CONTAINERS.register(eventBus);
    }
}
