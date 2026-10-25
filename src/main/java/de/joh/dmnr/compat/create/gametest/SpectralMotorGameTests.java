package de.joh.dmnr.compat.create.gametest;

import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.spells.crafting.ModifiedSpellPart;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.compat.create.CreateInit;
import de.joh.dmnr.compat.create.block.SpectralMotorBlock;
import de.joh.dmnr.compat.create.blockentity.SpectralMotorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * GameTests of the Spectral Motor component and the Spectral Motor. Run with {@code ./gradlew runGameTestServer}; the
 * class is only loaded when GameTests are enabled, which only happens in development runs.
 * @author Joh0210
 */
@GameTestHolder(DragonMagicAndRelics.MOD_ID)
@PrefixGameTestTemplate(false)
public class SpectralMotorGameTests {
    private static final String TEMPLATE = "empty";
    private static final int TIMEOUT = 100;
    private static final int LONG_DURATION = 2000;

    private static final BlockPos MOTOR = new BlockPos(2, 1, 2);
    private static final BlockPos NEIGHBOR = MOTOR.east();

    /**
     * A shaft on the axis of the motor turns at the configured speed, and the network has the configured stress
     * capacity independent of the speed.
     */
    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void motorDrivesShaftWithConfiguredValues(GameTestHelper helper) {
        placeMotor(helper, Direction.Axis.X).configure(64, 1024, false, LONG_DURATION);
        helper.setBlock(NEIGHBOR, createBlock("shaft").defaultBlockState().setValue(RotatedPillarKineticBlock.AXIS, Direction.Axis.X));

        helper.succeedWhen(() -> {
            KineticBlockEntity shaft = kinetic(helper, NEIGHBOR);
            assertSpeed(helper, shaft, 64);
            float capacity = shaft.getOrCreateNetwork().calculateCapacity();
            helper.assertTrue(Math.abs(capacity - 1024) < 0.01F, "Network capacity is " + capacity + " SU instead of 1024 SU");
        });
    }

    /**
     * Precision reverses the direction of rotation.
     */
    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void reversedMotorTurnsBackwards(GameTestHelper helper) {
        placeMotor(helper, Direction.Axis.X).configure(32, 256, true, LONG_DURATION);
        helper.setBlock(NEIGHBOR, createBlock("shaft").defaultBlockState().setValue(RotatedPillarKineticBlock.AXIS, Direction.Axis.X));

        helper.succeedWhen(() -> assertSpeed(helper, kinetic(helper, NEIGHBOR), -32));
    }

    /**
     * The sides of the motor mesh with a small cogwheel on a parallel axis, which turns the other way.
     */
    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void motorMeshesWithSmallCogwheel(GameTestHelper helper) {
        placeMotor(helper, Direction.Axis.Y).configure(16, 256, false, LONG_DURATION);
        helper.setBlock(NEIGHBOR, createBlock("cogwheel").defaultBlockState().setValue(RotatedPillarKineticBlock.AXIS, Direction.Axis.Y));

        helper.succeedWhen(() -> assertSpeed(helper, kinetic(helper, NEIGHBOR), -16));
    }

    /**
     * The motor removes itself once its duration has run out.
     */
    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void motorVanishesAfterDuration(GameTestHelper helper) {
        placeMotor(helper, Direction.Axis.Y).configure(16, 256, false, 20);

        helper.startSequence()
                .thenExecuteAfter(10, () -> helper.assertBlockPresent(CreateInit.SPECTRAL_MOTOR.get(), MOTOR))
                .thenWaitUntil(() -> helper.assertBlockNotPresent(CreateInit.SPECTRAL_MOTOR.get(), MOTOR))
                .thenSucceed();
    }

    /**
     * The sounds of M&A used for appearing and vanishing exist.
     */
    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void soundsAreRegistered(GameTestHelper helper) {
        SoundEvent appear = CreateInit.SPECTRAL_MOTOR_COMPONENT.SoundEffect();
        SoundEvent vanish = CreateInit.SPECTRAL_MOTOR.get().defaultBlockState().getSoundType().getBreakSound();

        helper.assertTrue(appear != null && appear.getLocation().equals(new ResourceLocation("mna", "entity_spectral_horse_summon")), "Wrong sound for appearing: " + appear);
        helper.assertTrue(vanish != null && vanish.getLocation().equals(new ResourceLocation("mna", "event_faerie_leave")), "Wrong sound for vanishing: " + vanish);
        helper.succeed();
    }

    /**
     * The spell places the motor in front of the targeted face, with its shaft pointing into the targeted block, and
     * applies its attributes.
     */
    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void spellConjuresMotorAtTargetedFace(GameTestHelper helper) {
        helper.setBlock(MOTOR, Blocks.STONE);

        ComponentApplicationResult result = cast(helper, MOTOR, Direction.EAST, 48, 3, 60, 1);

        helper.assertTrue(result == ComponentApplicationResult.SUCCESS, "Spell failed: " + result);
        BlockState state = helper.getBlockState(NEIGHBOR);
        helper.assertTrue(state.is(CreateInit.SPECTRAL_MOTOR.get()), "No motor in front of the targeted face");
        helper.assertTrue(state.getValue(SpectralMotorBlock.AXIS) == Direction.Axis.X, "Shaft does not point into the targeted block");
        assertMotorValues(helper, motor(helper, NEIGHBOR), -48, 1024);
        helper.succeed();
    }

    /**
     * Cast on an existing motor, the spell changes its values instead of placing another one.
     */
    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void spellReconfiguresExistingMotor(GameTestHelper helper) {
        placeMotor(helper, Direction.Axis.X).configure(16, 256, false, 40);

        ComponentApplicationResult result = cast(helper, MOTOR, Direction.EAST, 128, 7, 600, 0);

        helper.assertTrue(result == ComponentApplicationResult.SUCCESS, "Spell failed: " + result);
        helper.assertBlockNotPresent(CreateInit.SPECTRAL_MOTOR.get(), NEIGHBOR);
        assertMotorValues(helper, motor(helper, MOTOR), 128, 16384);
        // The restarted duration outlasts the original one of 40 ticks.
        helper.runAfterDelay(60, () -> {
            helper.assertBlockPresent(CreateInit.SPECTRAL_MOTOR.get(), MOTOR);
            helper.succeed();
        });
    }

    private static ComponentApplicationResult cast(GameTestHelper helper, BlockPos target, Direction face,
                                                   float speed, float magnitude, float duration, float precision) {
        ModifiedSpellPart<SpellEffect> part = new ModifiedSpellPart<>(CreateInit.SPECTRAL_MOTOR_COMPONENT);
        part.setValue(Attribute.SPEED, speed);
        part.setValue(Attribute.MAGNITUDE, magnitude);
        part.setValue(Attribute.DURATION, duration);
        part.setValue(Attribute.PRECISION, precision);

        return CreateInit.SPECTRAL_MOTOR_COMPONENT.ApplyEffect(
                new SpellSource(helper.makeMockPlayer(), InteractionHand.MAIN_HAND),
                new SpellTarget(helper.absolutePos(target), face),
                part,
                new SpellContext(helper.getLevel(), null));
    }

    private static SpectralMotorBlockEntity placeMotor(GameTestHelper helper, Direction.Axis axis) {
        helper.setBlock(MOTOR, CreateInit.SPECTRAL_MOTOR.get().defaultBlockState().setValue(SpectralMotorBlock.AXIS, axis));
        return motor(helper, MOTOR);
    }

    private static Block createBlock(String name) {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation("create", name));
    }

    private static SpectralMotorBlockEntity motor(GameTestHelper helper, BlockPos pos) {
        if (helper.getBlockEntity(pos) instanceof SpectralMotorBlockEntity motor) {
            return motor;
        }
        helper.fail("No Spectral Motor", pos);
        throw new IllegalStateException();
    }

    private static KineticBlockEntity kinetic(GameTestHelper helper, BlockPos pos) {
        if (helper.getBlockEntity(pos) instanceof KineticBlockEntity kinetic) {
            return kinetic;
        }
        helper.fail("No kinetic block", pos);
        throw new IllegalStateException();
    }

    private static void assertSpeed(GameTestHelper helper, KineticBlockEntity be, float expected) {
        helper.assertTrue(be.getSpeed() == expected, "Turns at " + be.getSpeed() + " RPM instead of " + expected + " RPM");
    }

    private static void assertMotorValues(GameTestHelper helper, SpectralMotorBlockEntity motor, float speed, float stressUnits) {
        helper.assertTrue(motor.getGeneratedSpeed() == speed, "Motor generates " + motor.getGeneratedSpeed() + " RPM instead of " + speed + " RPM");
        float capacity = motor.calculateAddedStressCapacity() * Math.abs(speed);
        helper.assertTrue(Math.abs(capacity - stressUnits) < 0.01F, "Motor provides " + capacity + " SU instead of " + stressUnits + " SU");
    }
}
