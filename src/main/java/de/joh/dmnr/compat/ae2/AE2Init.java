package de.joh.dmnr.compat.ae2;

import appeng.api.upgrades.Upgrades;
import appeng.blockentity.AEBaseBlockEntity;
import appeng.core.definitions.AEItems;
import appeng.menu.MenuOpener;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.compat.ae2.block.AltarInterfaceBlock;
import de.joh.dmnr.compat.ae2.blockentity.AltarInterfaceBlockEntity;
import de.joh.dmnr.compat.ae2.menu.AltarInterfaceMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registrations of the Applied Energistics 2 integration. Only loaded if AE2 is installed.
 * @see AE2Compat
 * @author Joh0210
 */
public class AE2Init {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DragonMagicAndRelics.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DragonMagicAndRelics.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DragonMagicAndRelics.MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, DragonMagicAndRelics.MOD_ID);

    public static final RegistryObject<AltarInterfaceBlock> ALTAR_INTERFACE = BLOCKS.register("altar_me_interface", AltarInterfaceBlock::new);
    public static final RegistryObject<Item> ALTAR_INTERFACE_ITEM = ITEMS.register("altar_me_interface",
            () -> new BlockItem(ALTAR_INTERFACE.get(), new Item.Properties()));
    public static final RegistryObject<BlockEntityType<AltarInterfaceBlockEntity>> ALTAR_INTERFACE_ENTITY = BLOCK_ENTITIES.register("altar_me_interface_entity",
            () -> BlockEntityType.Builder.of(AltarInterfaceBlockEntity::new, ALTAR_INTERFACE.get()).build(null));
    public static final RegistryObject<MenuType<AltarInterfaceMenu>> ALTAR_INTERFACE_MENU = MENUS.register("altar_me_interface",
            () -> IForgeMenuType.create(AltarInterfaceMenu::fromNetwork));

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        BLOCK_ENTITIES.register(modBus);
        MENUS.register(modBus);
        modBus.addListener(AE2Init::onCommonSetup);
    }

    /**
     * Connects the registered content to the framework of AE2, which has its own lookups for block entities,
     * upgrades and menus.
     */
    private static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ALTAR_INTERFACE.get().setBlockEntity(AltarInterfaceBlockEntity.class, ALTAR_INTERFACE_ENTITY.get(), null, null);
            AEBaseBlockEntity.registerBlockEntityItem(ALTAR_INTERFACE_ENTITY.get(), ALTAR_INTERFACE_ITEM.get());

            // One of each card, see AltarInterfaceLogic.UPGRADE_SLOTS
            Upgrades.add(AEItems.CRAFTING_CARD, ALTAR_INTERFACE_ITEM.get(), 1);
            Upgrades.add(AEItems.REDSTONE_CARD, ALTAR_INTERFACE_ITEM.get(), 1);

            MenuOpener.addOpener(ALTAR_INTERFACE_MENU.get(), AltarInterfaceMenu::open);
        });
    }
}
