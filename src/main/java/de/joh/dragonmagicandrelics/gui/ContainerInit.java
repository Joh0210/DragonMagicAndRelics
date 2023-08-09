package de.joh.dragonmagicandrelics.gui;

import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
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
    static final String BRACELET_OF_FRIENDSHIP_ID = "dragonmagicandrelics:bracelet_of_friendship";
    @ObjectHolder(BRACELET_OF_FRIENDSHIP_ID)
    public static final MenuType<ContainerBraceletOfFriendship> BRACELET_OF_FRIENDSHIP;

    public ContainerInit() {
    }

    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<MenuType<?>> event) {
        IForgeRegistry<MenuType<?>> r = event.getRegistry();
        r.register((new MenuType<>(ContainerBraceletOfFriendship::new)).setRegistryName(BRACELET_OF_FRIENDSHIP_ID));
    }

    static {
        CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, DragonMagicAndRelics.MOD_ID);
        BRACELET_OF_FRIENDSHIP = null;
    }
}
