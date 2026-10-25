package de.joh.dmnr.compat.create;

import com.mna.Registries;
import com.mna.api.spells.parts.SpellEffect;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.util.CommonConfig;
import de.joh.dmnr.common.util.RLoc;
import de.joh.dmnr.compat.create.block.EngineBlock;
import de.joh.dmnr.compat.create.block.SpectralMotorBlock;
import de.joh.dmnr.compat.create.blockentity.EngineBlockEntity;
import de.joh.dmnr.compat.create.blockentity.SpectralMotorBlockEntity;
import de.joh.dmnr.compat.create.spell.SpectralMotorComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registrations of the Create integration. Only loaded if Create is installed.
 * @see CreateCompat
 * @author Joh0210
 */
public class CreateInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DragonMagicAndRelics.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DragonMagicAndRelics.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DragonMagicAndRelics.MOD_ID);

    /**
     * Only placed by {@link #SPECTRAL_MOTOR_COMPONENT}, so there is no item for it.
     */
    public static final RegistryObject<SpectralMotorBlock> SPECTRAL_MOTOR = BLOCKS.register("spectral_motor", SpectralMotorBlock::new);
    public static final RegistryObject<BlockEntityType<SpectralMotorBlockEntity>> SPECTRAL_MOTOR_ENTITY = BLOCK_ENTITIES.register("spectral_motor_entity",
            () -> BlockEntityType.Builder.of(SpectralMotorBlockEntity::new, SPECTRAL_MOTOR.get()).build(null));

    public static final SpellEffect SPECTRAL_MOTOR_COMPONENT = new SpectralMotorComponent(RLoc.create("textures/spell/component/spectral_motor.png"));

    public static final RegistryObject<EngineBlock> ARCANE_ENGINE = BLOCKS.register("arcane_engine", () -> new EngineBlock("arcane_engine",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(3.5F, 6.0F)
                    .sound(SoundType.AMETHYST)
                    .noOcclusion(),
            CommonConfig.ARCANE_ENGINE_SPEED::get, CommonConfig.ARCANE_ENGINE_STRESS::get));
    public static final RegistryObject<Item> ARCANE_ENGINE_ITEM = ITEMS.register("arcane_engine",
            () -> new BlockItem(ARCANE_ENGINE.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));

    /**
     * The upgrade of the {@link #ARCANE_ENGINE}.
     */
    public static final RegistryObject<EngineBlock> BRIMSTONE_ENGINE = BLOCKS.register("brimstone_engine", () -> new EngineBlock("brimstone_engine",
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .strength(3.5F, 6.0F)
                    .sound(SoundType.NETHER_BRICKS)
                    .lightLevel(state -> 11)
                    .noOcclusion(),
            CommonConfig.BRIMSTONE_ENGINE_SPEED::get, CommonConfig.BRIMSTONE_ENGINE_STRESS::get));
    public static final RegistryObject<Item> BRIMSTONE_ENGINE_ITEM = ITEMS.register("brimstone_engine",
            () -> new BlockItem(BRIMSTONE_ENGINE.get(), new Item.Properties().rarity(Rarity.RARE)));

    /**
     * Shared by both engines.
     */
    public static final RegistryObject<BlockEntityType<EngineBlockEntity>> ENGINE_ENTITY = BLOCK_ENTITIES.register("engine_entity",
            () -> BlockEntityType.Builder.of(EngineBlockEntity::new, ARCANE_ENGINE.get(), BRIMSTONE_ENGINE.get()).build(null));

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        BLOCK_ENTITIES.register(modBus);
        modBus.addListener(CreateInit::onRegister);
    }

    /**
     * The tier of a spell component is the tier of the recipe with the same name, so the recipe has to be
     * {@code data/dmnr/recipes/components/spectral_motor.json}.
     */
    private static void onRegister(RegisterEvent event) {
        event.register(Registries.SpellEffect.get().getRegistryKey(),
                helper -> helper.register(RLoc.create("components/spectral_motor"), SPECTRAL_MOTOR_COMPONENT));
    }
}
