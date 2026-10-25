package de.joh.dmnr.compat.create;

import com.mna.Registries;
import com.mna.api.spells.parts.SpellEffect;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.util.RLoc;
import de.joh.dmnr.compat.create.block.SpectralMotorBlock;
import de.joh.dmnr.compat.create.blockentity.SpectralMotorBlockEntity;
import de.joh.dmnr.compat.create.spell.SpectralMotorComponent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DragonMagicAndRelics.MOD_ID);

    /**
     * Only placed by {@link #SPECTRAL_MOTOR_COMPONENT}, so there is no item for it.
     */
    public static final RegistryObject<SpectralMotorBlock> SPECTRAL_MOTOR = BLOCKS.register("spectral_motor", SpectralMotorBlock::new);
    public static final RegistryObject<BlockEntityType<SpectralMotorBlockEntity>> SPECTRAL_MOTOR_ENTITY = BLOCK_ENTITIES.register("spectral_motor_entity",
            () -> BlockEntityType.Builder.of(SpectralMotorBlockEntity::new, SPECTRAL_MOTOR.get()).build(null));

    public static final SpellEffect SPECTRAL_MOTOR_COMPONENT = new SpectralMotorComponent(RLoc.create("textures/spell/component/spectral_motor.png"));

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
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
