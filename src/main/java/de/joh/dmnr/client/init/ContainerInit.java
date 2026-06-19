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
    static final String POTION_OF_INFINITY_ID = "potion_of_infinity";
    public static final RegistryObject<MenuType<ContainerPotionOfInfinity>> POTION_OF_INFINITY;

    static final String RING_OF_POWER_ID = "ring_of_power";
    public static final RegistryObject<MenuType<ContainerRingOfPower>> RING_OF_POWER;

    static final String RING_OF_SPELL_STORING_ID = "ring_of_spell_storing";
    public static final RegistryObject<MenuType<ContainerRingOfNormalSpellStoring>> RING_OF_SPELL_STORING;

    static final String RING_OF_SPELL_STORING_COOLDOWN_ID = "ring_of_spell_storing_cooldown";
    public static final RegistryObject<MenuType<ContainerRingOfCooldownSpellStoring>> RING_OF_SPELL_STORING_COOLDOWN;

    public ContainerInit() {
    }

    static <T extends Block> String of(RegistryObject<T> block) {
        return block.getId().getPath();
    }

    static {
        CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, DragonMagicAndRelics.MOD_ID);
        BRACELET_OF_FRIENDSHIP = CONTAINERS.register(BRACELET_OF_FRIENDSHIP_ID,
                () -> IForgeMenuType.create(ContainerBraceletOfFriendship::new));
        POTION_OF_INFINITY = CONTAINERS.register(POTION_OF_INFINITY_ID,
                () -> IForgeMenuType.create(ContainerPotionOfInfinity::new));

        RING_OF_POWER = CONTAINERS.register(RING_OF_POWER_ID,
                () -> IForgeMenuType.create(ContainerRingOfPower::new));

        RING_OF_SPELL_STORING = CONTAINERS.register(RING_OF_SPELL_STORING_ID,
                () -> IForgeMenuType.create(ContainerRingOfNormalSpellStoring::new));

        RING_OF_SPELL_STORING_COOLDOWN = CONTAINERS.register(RING_OF_SPELL_STORING_COOLDOWN_ID,
                () -> IForgeMenuType.create(ContainerRingOfCooldownSpellStoring::new));
    }

    public static void register(IEventBus eventBus){
        CONTAINERS.register(eventBus);
    }
}
