package de.joh.dragonmagicandrelics.block.entity;

import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.block.BlockInit;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Inits of all mod Blocks-Entities.
 * Registration through DragonMagicAndRelics
 * @see DragonMagicAndRelics
 * @author Joh0210
 */
public class BlockEntitieInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, DragonMagicAndRelics.MOD_ID);

    public static final RegistryObject<BlockEntityType<RiftEmitterEntity>> RIFT_EMITTER_ENTITY =
            BLOCK_ENTITIES.register("rift_emitter_entity", () ->
                    BlockEntityType.Builder.of(RiftEmitterEntity::new,
                            BlockInit.RIFT_EMITTER.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
