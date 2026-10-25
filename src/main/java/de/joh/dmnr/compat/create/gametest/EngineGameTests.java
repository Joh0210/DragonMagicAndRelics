package de.joh.dmnr.compat.create.gametest;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.util.CommonConfig;
import de.joh.dmnr.compat.create.CreateInit;
import de.joh.dmnr.compat.create.block.EngineBlock;
import de.joh.dmnr.compat.create.blockentity.EngineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

/**
 * GameTests of the Arcane Engine and the Brimstone Engine. Run with {@code ./gradlew runGameTestServer}; the class is
 * only loaded when GameTests are enabled, which only happens in development runs.
 * @author Joh0210
 */
@GameTestHolder(DragonMagicAndRelics.MOD_ID)
@PrefixGameTestTemplate(false)
public class EngineGameTests {
    private static final String TEMPLATE = "empty";
    private static final int TIMEOUT = 100;

    private static final BlockPos ENGINE = new BlockPos(2, 1, 2);
    private static final BlockPos NEIGHBOR = ENGINE.east();

    /**
     * A shaft on the axis of the Arcane Engine turns at the configured speed, and the network has the configured
     * stress capacity.
     */
    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void arcaneEngineDrivesShaft(GameTestHelper helper) {
        assertDrivesShaft(helper, CreateInit.ARCANE_ENGINE.get(), CommonConfig.ARCANE_ENGINE_SPEED.get(), CommonConfig.ARCANE_ENGINE_STRESS.get());
    }

    /**
     * Same for the Brimstone Engine.
     */
    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void brimstoneEngineDrivesShaft(GameTestHelper helper) {
        assertDrivesShaft(helper, CreateInit.BRIMSTONE_ENGINE.get(), CommonConfig.BRIMSTONE_ENGINE_SPEED.get(), CommonConfig.BRIMSTONE_ENGINE_STRESS.get());
    }

    /**
     * The sides of an engine mesh with a small cogwheel on a parallel axis, which turns the other way.
     */
    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void engineMeshesWithSmallCogwheel(GameTestHelper helper) {
        helper.setBlock(ENGINE, CreateInit.ARCANE_ENGINE.get().defaultBlockState().setValue(EngineBlock.AXIS, Direction.Axis.Y));
        helper.setBlock(NEIGHBOR, createBlock("cogwheel").defaultBlockState().setValue(RotatedPillarKineticBlock.AXIS, Direction.Axis.Y));

        helper.succeedWhen(() -> assertSpeed(helper, kinetic(helper, NEIGHBOR), -CommonConfig.ARCANE_ENGINE_SPEED.get()));
    }

    /**
     * Unlike the temporary Spectral Motor, an engine keeps turning.
     */
    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void engineStaysPlaced(GameTestHelper helper) {
        helper.setBlock(ENGINE, CreateInit.ARCANE_ENGINE.get().defaultBlockState());

        helper.runAfterDelay(60, () -> {
            helper.assertBlockPresent(CreateInit.ARCANE_ENGINE.get(), ENGINE);
            helper.succeed();
        });
    }

    /**
     * Breaking an engine drops its item, which is defined without the item itself so the loot table also loads without
     * Create.
     */
    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void enginesDropThemselves(GameTestHelper helper) {
        assertDrops(helper, CreateInit.ARCANE_ENGINE.get(), CreateInit.ARCANE_ENGINE_ITEM.get());
        assertDrops(helper, CreateInit.BRIMSTONE_ENGINE.get(), CreateInit.BRIMSTONE_ENGINE_ITEM.get());
        helper.succeed();
    }

    /**
     * The recipes, which are only loaded if Create is installed, craft the engines.
     */
    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void recipesCraftTheEngines(GameTestHelper helper) {
        assertRecipe(helper, "create/arcane_engine", CreateInit.ARCANE_ENGINE_ITEM.get());
        assertRecipe(helper, "create/brimstone_engine", CreateInit.BRIMSTONE_ENGINE_ITEM.get());
        helper.succeed();
    }

    private static void assertDrivesShaft(GameTestHelper helper, Block engine, int speed, int stressUnits) {
        helper.setBlock(ENGINE, engine.defaultBlockState().setValue(EngineBlock.AXIS, Direction.Axis.X));
        helper.setBlock(NEIGHBOR, createBlock("shaft").defaultBlockState().setValue(RotatedPillarKineticBlock.AXIS, Direction.Axis.X));

        helper.succeedWhen(() -> {
            KineticBlockEntity shaft = kinetic(helper, NEIGHBOR);
            assertSpeed(helper, shaft, speed);
            float capacity = shaft.getOrCreateNetwork().calculateCapacity();
            helper.assertTrue(Math.abs(capacity - stressUnits) < 0.01F, "Network capacity is " + capacity + " SU instead of " + stressUnits + " SU");

            EngineBlockEntity be = (EngineBlockEntity) helper.getBlockEntity(ENGINE);
            helper.assertTrue(be.getGeneratedSpeed() == speed, "Engine generates " + be.getGeneratedSpeed() + " RPM instead of " + speed + " RPM");
        });
    }

    private static void assertDrops(GameTestHelper helper, Block block, Item expected) {
        helper.setBlock(ENGINE, block);
        List<ItemStack> drops = Block.getDrops(helper.getBlockState(ENGINE), helper.getLevel(), helper.absolutePos(ENGINE), helper.getBlockEntity(ENGINE));
        helper.assertTrue(drops.size() == 1 && drops.get(0).is(expected), block + " drops " + drops + " instead of " + expected);
    }

    private static void assertRecipe(GameTestHelper helper, String path, Item expected) {
        Recipe<?> recipe = helper.getLevel().getRecipeManager().byKey(new ResourceLocation(DragonMagicAndRelics.MOD_ID, path)).orElse(null);
        helper.assertTrue(recipe != null, "Recipe " + path + " is not loaded");
        ItemStack result = recipe.getResultItem(helper.getLevel().registryAccess());
        helper.assertTrue(result.is(expected), "Recipe " + path + " crafts " + result + " instead of " + expected);
    }

    private static Block createBlock(String name) {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation("create", name));
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
}
