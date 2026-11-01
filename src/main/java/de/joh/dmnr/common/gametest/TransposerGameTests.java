package de.joh.dmnr.common.gametest;

import com.mna.items.ItemInit;
import com.mojang.authlib.GameProfile;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.block.TransposerBlock;
import de.joh.dmnr.common.block.entity.TransposerBlockEntity;
import de.joh.dmnr.common.block.entity.TransposerStatus;
import de.joh.dmnr.common.init.BlockInit;
import de.joh.dmnr.common.util.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * GameTests of the Transposer. Run with {@code ./gradlew runGameTestServer}; the class is only loaded when GameTests
 * are enabled, which only happens in development runs.
 * <p>
 * The empty template is 7x5x7. With a Minor Ritual Focus (radius 1 by default), the 3x3x3 areas of both Transposers
 * fit into it without overlapping.
 * @author Joh0210
 */
@GameTestHolder(DragonMagicAndRelics.MOD_ID)
@PrefixGameTestTemplate(false)
public class TransposerGameTests {
    private static final String TEMPLATE = "empty";
    private static final int TIMEOUT = 100;

    private static final BlockPos FIRST = new BlockPos(1, 1, 1);
    private static final BlockPos SECOND = new BlockPos(5, 1, 5);
    // Next to the first Transposer, outside of its area
    private static final BlockPos SWITCH = FIRST.west();

    private static final GameProfile ALICE = testProfile("Alice");
    private static final GameProfile BOB = testProfile("Bob");

    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void checksBothOwnersInBothAreas(GameTestHelper helper) {
        TransposerBlockEntity transposer = setUp(helper);
        ((TransposerBlockEntity) helper.getBlockEntity(SECOND)).setOwner(BOB);
        transposer.setOwner(ALICE);
        helper.setBlock(FIRST.above(), Blocks.GOLD_BLOCK);

        // Like a claim mod: one owner may not break one position (a block in the first area, air in the second one)
        for (GameProfile owner : List.of(ALICE, BOB)) {
            for (BlockPos protectedPos : List.of(FIRST.above(), SECOND.offset(1, 3, 1))) {
                BlockPos absolutePos = helper.absolutePos(protectedPos);
                Consumer<BlockEvent.BreakEvent> claim = event -> {
                    if (event.getPos().equals(absolutePos) && event.getPlayer().getUUID().equals(owner.getId())) {
                        event.setCanceled(true);
                    }
                };
                MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, BlockEvent.BreakEvent.class, claim);
                try {
                    helper.assertFalse(transposer.transpose(), owner.getName() + " could change the protected position " + protectedPos);
                } finally {
                    MinecraftForge.EVENT_BUS.unregister(claim);
                }
                assertStatus(helper, transposer, TransposerStatus.AREA_PROTECTED);
                helper.assertBlockPresent(Blocks.GOLD_BLOCK, FIRST.above());
            }
        }

        helper.assertTrue(transposer.transpose(), "The transposition failed without protection");
        helper.assertBlockPresent(Blocks.GOLD_BLOCK, SECOND.above());
        assertStatus(helper, transposer, TransposerStatus.READY);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void movedRedstoneDoesNotSwapBack(GameTestHelper helper) {
        setUp(helper);
        // Both Transposers are bound to each other
        TransposerBlockEntity other = (TransposerBlockEntity) helper.getBlockEntity(SECOND);
        ItemStack rune = new ItemStack(ItemInit.RUNE_MARKING.get());
        ItemInit.RUNE_MARKING.get().setLocation(rune, helper.absolutePos(FIRST), Direction.UP);
        other.getInventory().setStackInSlot(TransposerBlockEntity.SLOT_RUNE, rune);

        // Activates the first Transposer, and powers the second one once it is moved
        helper.setBlock(FIRST.above(), Blocks.REDSTONE_BLOCK);

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertBlockPresent(Blocks.REDSTONE_BLOCK, SECOND.above()))
                .thenIdle(4)
                .thenExecute(() -> helper.assertBlockPresent(Blocks.REDSTONE_BLOCK, SECOND.above()))
                .thenIdle(1)
                .thenExecute(() -> helper.assertBlockPresent(Blocks.REDSTONE_BLOCK, SECOND.above()))
                // A new rising signal activates the second Transposer
                .thenExecute(() -> helper.setBlock(SECOND.above(), Blocks.AIR))
                .thenIdle(1)
                .thenExecute(() -> helper.setBlock(SECOND.above(), Blocks.REDSTONE_BLOCK))
                .thenWaitUntil(() -> helper.assertBlockPresent(Blocks.REDSTONE_BLOCK, FIRST.above()))
                .thenIdle(4)
                .thenExecute(() -> helper.assertBlockPresent(Blocks.REDSTONE_BLOCK, FIRST.above()))
                .thenIdle(1)
                .thenExecute(() -> helper.assertBlockPresent(Blocks.REDSTONE_BLOCK, FIRST.above()))
                .thenSucceed();
    }

    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void requiresSameOwnerOnlyIfConfigured(GameTestHelper helper) {
        TransposerBlockEntity transposer = setUp(helper);
        TransposerBlockEntity other = (TransposerBlockEntity) helper.getBlockEntity(SECOND);
        transposer.setOwner(ALICE);
        other.setOwner(BOB);
        assertStatus(helper, transposer, TransposerStatus.READY);

        boolean previous = CommonConfig.TRANSPOSER_REQUIRE_SAME_OWNER.get();
        CommonConfig.TRANSPOSER_REQUIRE_SAME_OWNER.set(true);
        try {
            assertStatus(helper, transposer, TransposerStatus.DIFFERENT_OWNER);
            other.setOwner(null);
            assertStatus(helper, transposer, TransposerStatus.DIFFERENT_OWNER);
            other.setOwner(ALICE);
            assertStatus(helper, transposer, TransposerStatus.READY);
        } finally {
            CommonConfig.TRANSPOSER_REQUIRE_SAME_OWNER.set(previous);
        }
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void swapsBlocksAndBlockEntities(GameTestHelper helper) {
        TransposerBlockEntity transposer = setUp(helper);

        helper.setBlock(FIRST.above(), Blocks.CHEST);
        ChestBlockEntity chest = (ChestBlockEntity) helper.getBlockEntity(FIRST.above());
        chest.setItem(0, new ItemStack(Items.DIAMOND, 5));
        helper.setBlock(FIRST.offset(1, 2, 1), Blocks.GOLD_BLOCK);
        helper.setBlock(SECOND.above(), Blocks.DIAMOND_BLOCK);
        helper.setBlock(SECOND.offset(-1, 3, -1), Blocks.EMERALD_BLOCK);

        helper.assertTrue(transposer.getStatus(false) == TransposerStatus.READY, "Expected READY, but was " + transposer.getStatus(false));
        helper.setBlock(SWITCH, Blocks.REDSTONE_BLOCK);

        helper.succeedWhen(() -> {
            helper.assertBlockPresent(Blocks.CHEST, SECOND.above());
            ChestBlockEntity movedChest = (ChestBlockEntity) helper.getBlockEntity(SECOND.above());
            ItemStack content = movedChest.getItem(0);
            helper.assertTrue(content.is(Items.DIAMOND) && content.getCount() == 5, "The chest lost its content: " + content);
            helper.assertBlockPresent(Blocks.GOLD_BLOCK, SECOND.offset(1, 2, 1));
            helper.assertBlockPresent(Blocks.AIR, SECOND.offset(-1, 3, -1));

            helper.assertBlockPresent(Blocks.DIAMOND_BLOCK, FIRST.above());
            helper.assertBlockPresent(Blocks.EMERALD_BLOCK, FIRST.offset(-1, 3, -1));
            helper.assertBlockPresent(Blocks.AIR, FIRST.offset(1, 2, 1));

            // Nothing was dropped while the chest was replaced
            helper.assertItemEntityNotPresent(Items.DIAMOND, FIRST.above(), 2);
        });
    }

    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void swapsEntities(GameTestHelper helper) {
        setUp(helper);
        helper.spawnWithNoFreeWill(EntityType.PIG, FIRST.above());
        helper.spawnWithNoFreeWill(EntityType.COW, SECOND.above());

        helper.setBlock(SWITCH, Blocks.REDSTONE_BLOCK);

        helper.succeedWhen(() -> {
            helper.assertEntityPresent(EntityType.PIG, SECOND.above());
            helper.assertEntityNotPresent(EntityType.PIG, FIRST.above());
            helper.assertEntityPresent(EntityType.COW, FIRST.above());
            helper.assertEntityNotPresent(EntityType.COW, SECOND.above());
        });
    }

    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void keepsUnbreakableBlocks(GameTestHelper helper) {
        setUp(helper);
        helper.setBlock(FIRST.above(), Blocks.BEDROCK);
        helper.setBlock(SECOND.above(), Blocks.STONE);
        // Shows that the transposition happened
        helper.setBlock(FIRST.offset(1, 1, 1), Blocks.GOLD_BLOCK);

        helper.setBlock(SWITCH, Blocks.REDSTONE_BLOCK);

        helper.succeedWhen(() -> {
            helper.assertBlockPresent(Blocks.GOLD_BLOCK, SECOND.offset(1, 1, 1));
            helper.assertBlockPresent(Blocks.BEDROCK, FIRST.above());
            helper.assertBlockPresent(Blocks.STONE, SECOND.above());
        });
    }

    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void swapsSingleBlockWithoutFocus(GameTestHelper helper) {
        TransposerBlockEntity transposer = setUp(helper);
        transposer.getInventory().setStackInSlot(TransposerBlockEntity.SLOT_FOCUS, ItemStack.EMPTY);
        helper.setBlock(FIRST.above(), Blocks.GOLD_BLOCK);
        // Would be in the area of a Minor Ritual Focus
        helper.setBlock(FIRST.offset(1, 1, 0), Blocks.EMERALD_BLOCK);
        helper.setBlock(SECOND.above(), Blocks.DIAMOND_BLOCK);

        helper.setBlock(SWITCH, Blocks.REDSTONE_BLOCK);

        helper.succeedWhen(() -> {
            helper.assertBlockPresent(Blocks.GOLD_BLOCK, SECOND.above());
            helper.assertBlockPresent(Blocks.DIAMOND_BLOCK, FIRST.above());
            helper.assertBlockPresent(Blocks.EMERALD_BLOCK, FIRST.offset(1, 1, 0));
            helper.assertBlockPresent(Blocks.AIR, SECOND.offset(1, 1, 0));
        });
    }

    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void reportsStatus(GameTestHelper helper) {
        helper.setBlock(FIRST, BlockInit.TRANSPOSER.get());
        TransposerBlockEntity transposer = (TransposerBlockEntity) helper.getBlockEntity(FIRST);
        assertStatus(helper, transposer, TransposerStatus.NO_RUNE);

        ItemStack rune = new ItemStack(ItemInit.RUNE_MARKING.get());
        transposer.getInventory().setStackInSlot(TransposerBlockEntity.SLOT_RUNE, rune);
        assertStatus(helper, transposer, TransposerStatus.RUNE_WITHOUT_POSITION);

        ItemInit.RUNE_MARKING.get().setLocation(rune, helper.absolutePos(FIRST), Direction.UP);
        assertStatus(helper, transposer, TransposerStatus.BOUND_TO_ITSELF);

        // Without a focus, the area is a single block
        ItemInit.RUNE_MARKING.get().setLocation(rune, helper.absolutePos(SECOND), Direction.UP);
        assertStatus(helper, transposer, TransposerStatus.NO_TRANSPOSER);

        // 7x7x7 areas, 4 blocks apart
        transposer.getInventory().setStackInSlot(TransposerBlockEntity.SLOT_FOCUS, new ItemStack(ItemInit.RITUAL_FOCUS_GREATER.get()));
        assertStatus(helper, transposer, TransposerStatus.AREAS_OVERLAP);

        transposer.getInventory().setStackInSlot(TransposerBlockEntity.SLOT_FOCUS, new ItemStack(ItemInit.RITUAL_FOCUS_MINOR.get()));
        assertStatus(helper, transposer, TransposerStatus.NO_TRANSPOSER);

        helper.setBlock(SECOND, BlockInit.TRANSPOSER.get());
        assertStatus(helper, transposer, TransposerStatus.READY);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void acceptsOnlyRunesAndFoci(GameTestHelper helper) {
        helper.setBlock(FIRST, BlockInit.TRANSPOSER.get());
        TransposerBlockEntity transposer = (TransposerBlockEntity) helper.getBlockEntity(FIRST);
        helper.assertTrue(transposer.getAreaSize() == 1, "Expected a 1x1x1 area without a Ritual Focus, but was " + transposer.getAreaSize());

        ItemStack rest = transposer.getInventory().insertItem(TransposerBlockEntity.SLOT_FOCUS, new ItemStack(Items.DIAMOND), false);
        helper.assertTrue(rest.getCount() == 1, "The focus slot took a diamond");
        rest = transposer.getInventory().insertItem(TransposerBlockEntity.SLOT_RUNE, new ItemStack(ItemInit.RITUAL_FOCUS_MINOR.get()), false);
        helper.assertTrue(rest.getCount() == 1, "The rune slot took a Ritual Focus");
        rest = transposer.getInventory().insertItem(TransposerBlockEntity.SLOT_FOCUS, new ItemStack(ItemInit.RITUAL_FOCUS_LESSER.get(), 2), false);
        helper.assertTrue(rest.getCount() == 1, "The focus slot took more than one Ritual Focus");
        helper.assertTrue(transposer.getAreaSize() == 5, "Expected a 5x5x5 area with a Lesser Ritual Focus, but was " + transposer.getAreaSize());
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void showsContentOnTheBlock(GameTestHelper helper) {
        helper.setBlock(FIRST, BlockInit.TRANSPOSER.get());
        TransposerBlockEntity transposer = (TransposerBlockEntity) helper.getBlockEntity(FIRST);
        assertShows(helper, false, 0);

        // A rune without a position or bound to the Transposer itself does not link it
        ItemStack rune = new ItemStack(ItemInit.RUNE_MARKING.get());
        transposer.getInventory().setStackInSlot(TransposerBlockEntity.SLOT_RUNE, rune);
        assertShows(helper, false, 0);
        ItemStack ownRune = rune.copy();
        ItemInit.RUNE_MARKING.get().setLocation(ownRune, helper.absolutePos(FIRST), Direction.UP);
        transposer.getInventory().setStackInSlot(TransposerBlockEntity.SLOT_RUNE, ownRune);
        assertShows(helper, false, 0);

        // Only the rune is checked, there is no Transposer at the marked position
        ItemStack boundRune = rune.copy();
        ItemInit.RUNE_MARKING.get().setLocation(boundRune, helper.absolutePos(SECOND), Direction.UP);
        transposer.getInventory().setStackInSlot(TransposerBlockEntity.SLOT_RUNE, boundRune);
        assertShows(helper, true, 0);

        transposer.getInventory().setStackInSlot(TransposerBlockEntity.SLOT_FOCUS, new ItemStack(ItemInit.RITUAL_FOCUS_LESSER.get()));
        assertShows(helper, true, 2);
        transposer.getInventory().setStackInSlot(TransposerBlockEntity.SLOT_RUNE, ItemStack.EMPTY);
        assertShows(helper, false, 2);
        helper.succeed();
    }

    /**
     * Places both Transposers and gives the first one a rune bound to the second one and a Minor Ritual Focus.
     */
    private static TransposerBlockEntity setUp(GameTestHelper helper) {
        helper.setBlock(FIRST, BlockInit.TRANSPOSER.get());
        helper.setBlock(SECOND, BlockInit.TRANSPOSER.get());
        TransposerBlockEntity transposer = (TransposerBlockEntity) helper.getBlockEntity(FIRST);

        ItemStack rune = new ItemStack(ItemInit.RUNE_MARKING.get());
        ItemInit.RUNE_MARKING.get().setLocation(rune, helper.absolutePos(SECOND), Direction.UP);
        transposer.getInventory().setStackInSlot(TransposerBlockEntity.SLOT_RUNE, rune);
        transposer.getInventory().setStackInSlot(TransposerBlockEntity.SLOT_FOCUS, new ItemStack(ItemInit.RITUAL_FOCUS_MINOR.get()));
        return transposer;
    }

    private static GameProfile testProfile(String name) {
        return new GameProfile(UUID.nameUUIDFromBytes(("dmnr:transposer_test/" + name).getBytes(StandardCharsets.UTF_8)), name);
    }

    private static void assertStatus(GameTestHelper helper, TransposerBlockEntity transposer, TransposerStatus expected) {
        TransposerStatus status = transposer.getDisplayStatus();
        helper.assertTrue(status == expected, "Expected " + expected + ", but was " + status);
    }

    /**
     * Checks the block state of the first Transposer, and that it only gives light while it is linked.
     */
    private static void assertShows(GameTestHelper helper, boolean linked, int focus) {
        BlockState state = helper.getBlockState(FIRST);
        helper.assertTrue(state.getValue(TransposerBlock.LINKED) == linked && state.getValue(TransposerBlock.FOCUS) == focus
                        && state.getLightEmission() == (linked ? 7 : 0),
                "Expected linked=" + linked + " and focus=" + focus + ", but was " + state);
    }
}
