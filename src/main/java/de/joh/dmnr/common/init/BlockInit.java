package de.joh.dmnr.common.init;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.block.RiftEmitterBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

/**
 * Inits of all mod Blocks.
 * Registration through DragonMagicAndRelics
 * @see DragonMagicAndRelics
 * @author Joh0210
 */
public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DragonMagicAndRelics.MOD_ID);

    public static final RegistryObject<Block> RIFT_EMITTER = registerBlockWithoutBlockItem("rift_emitter",
            () -> new RiftEmitterBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(12.5F, 600.0F).requiresCorrectToolForDrops().noOcclusion()));


    /**
     * Registration of blocks without automatic generation of drop items
     * <br>e.g. with GeckoLib Blocks
     */
    private static <T extends Block> RegistryObject<T> registerBlockWithoutBlockItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    /**
     * In addition to the block, the items are also automatically registered.
     */
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
        return ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
