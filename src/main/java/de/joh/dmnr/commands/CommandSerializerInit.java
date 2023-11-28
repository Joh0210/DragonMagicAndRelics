package de.joh.dmnr.commands;

import de.joh.dmnr.DragonMagicAndRelics;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CommandSerializerInit {
    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENTS;
    public static final RegistryObject<ArgumentTypeInfo<?, ?>> ARMOR_UPGRADE;

    public CommandSerializerInit() {
    }

    static {
        ARGUMENTS = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, DragonMagicAndRelics.MOD_ID);
        ARMOR_UPGRADE = ARGUMENTS.register("armor_upgrade", () -> ArgumentTypeInfos.registerByClass(ArmorUpgradeArgument.class, SingletonArgumentInfo.contextFree(ArmorUpgradeArgument::armorUpgrade)));
    }

    public static void register(IEventBus eventBus) {
        ARGUMENTS.register(eventBus);
    }
}
