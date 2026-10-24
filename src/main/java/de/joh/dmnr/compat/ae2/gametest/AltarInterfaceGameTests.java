package de.joh.dmnr.compat.ae2.gametest;

import appeng.api.config.Actionable;
import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.networking.IGrid;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.MEStorage;
import appeng.blockentity.crafting.PatternProviderBlockEntity;
import appeng.blockentity.storage.ChestBlockEntity;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import com.mna.blocks.BlockInit;
import com.mna.blocks.tileentities.wizard_lab.ArcanaAltarTile;
import com.mna.items.ItemInit;
import com.mna.items.runes.ItemRuneMarking;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.compat.ae2.AE2Init;
import de.joh.dmnr.compat.ae2.blockentity.AltarInterfaceBlockEntity;
import de.joh.dmnr.compat.ae2.logic.AltarInterfaceLogic;
import de.joh.dmnr.compat.ae2.logic.AltarLink;
import de.joh.dmnr.compat.ae2.logic.AltarLinkStatus;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestSequence;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * GameTests of the Altar ME Interface. Run with {@code ./gradlew runGameTestServer}; the class is only loaded when
 * GameTests are enabled, which only happens in development runs.
 * <p>
 * The tests build a small ME network (creative energy cell, ME chest with a cell, interface) and an Altar of Arcana
 * and put the altar into the state in which it waits for reagents, instead of playing through a whole spell crafting.
 * @author Joh0210
 */
@GameTestHolder(DragonMagicAndRelics.MOD_ID)
@PrefixGameTestTemplate(false)
public class AltarInterfaceGameTests {
    private static final String TEMPLATE = "empty";
    private static final int TIMEOUT = 400;
    private static final int CRAFTING_TIMEOUT = 1200;

    private static final BlockPos CELL = new BlockPos(1, 1, 1);
    private static final BlockPos CHEST = new BlockPos(2, 1, 1);
    private static final BlockPos INTERFACE = new BlockPos(3, 1, 1);
    private static final BlockPos CPU = new BlockPos(4, 1, 1);
    private static final BlockPos PROVIDER = new BlockPos(5, 1, 1);
    private static final BlockPos ASSEMBLER = new BlockPos(6, 1, 1);
    private static final BlockPos ALTAR = new BlockPos(3, 1, 4);

    private static final Item REAGENT = Items.REDSTONE;
    private static final int NETWORK_STOCK = 64;
    private static final int REAGENT_COUNT = 3;

    private record Scenario(AltarInterfaceBlockEntity altarInterface, ArcanaAltarTile altar) {
        AltarInterfaceLogic logic() {
            return altarInterface.getLogic();
        }

        IGrid grid() {
            return altarInterface.getMainNode().getGrid();
        }
    }

    /**
     * The altar takes the reagents it waits for out of the interface, and nothing else is left over.
     */
    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void suppliesAltarAndKeepsRestInNetwork(GameTestHelper helper) {
        Scenario scenario = buildNetworkWithAltar(helper);

        startWithStock(helper, scenario, stack(REAGENT, NETWORK_STOCK))
                .thenExecute(() -> altarWaitsFor(scenario, REAGENT, REAGENT_COUNT))
                .thenWaitUntil(() -> assertCounts(helper, scenario, REAGENT_COUNT, NETWORK_STOCK - REAGENT_COUNT))
                .thenExecute(() -> {
                    // What the altar does: ask the inventory by capability, without a side, and take the items out.
                    IItemHandler handler = scenario.altarInterface().getCapability(ForgeCapabilities.ITEM_HANDLER, null)
                            .orElseThrow(() -> new GameTestAssertException("The interface offers no item handler without a side"));
                    int taken = 0;
                    for (int slot = 0; slot < handler.getSlots(); slot++) {
                        taken += handler.extractItem(slot, REAGENT_COUNT - taken, false).getCount();
                    }
                    helper.assertTrue(taken == REAGENT_COUNT, "The altar could only take " + taken + " of " + REAGENT_COUNT + " items");
                    scenario.altar().getRemainingReagents().clear();
                })
                .thenIdle(60)
                .thenExecute(() -> assertCounts(helper, scenario, 0, NETWORK_STOCK - REAGENT_COUNT))
                .thenSucceed();
    }

    /**
     * Items the altar does not need any more are moved back into the network.
     */
    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void returnsItemsThatAreNoLongerNeeded(GameTestHelper helper) {
        Scenario scenario = buildNetworkWithAltar(helper);

        startWithStock(helper, scenario, stack(REAGENT, NETWORK_STOCK))
                .thenExecute(() -> altarWaitsFor(scenario, REAGENT, REAGENT_COUNT))
                .thenWaitUntil(() -> assertCounts(helper, scenario, REAGENT_COUNT, NETWORK_STOCK - REAGENT_COUNT))
                .thenExecute(() -> scenario.altar().getRemainingReagents().clear())
                .thenWaitUntil(() -> assertCounts(helper, scenario, 0, NETWORK_STOCK))
                .thenSucceed();
    }

    /**
     * The altar accepts any variant of a reagent, so a damaged Flint and Steel or an enchanted Crossbow are supplied
     * as well, even though they are different items for the ME network than the plain ones.
     */
    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void suppliesItemsWithDurabilityAndNbt(GameTestHelper helper) {
        Scenario scenario = buildNetworkWithAltar(helper);
        ItemStack flintAndSteel = new ItemStack(Items.FLINT_AND_STEEL);
        flintAndSteel.setDamageValue(30);
        ItemStack crossbow = new ItemStack(Items.CROSSBOW);
        crossbow.enchant(Enchantments.QUICK_CHARGE, 2);

        startWithStock(helper, scenario, new GenericStack(AEItemKey.of(flintAndSteel), 1), new GenericStack(AEItemKey.of(crossbow), 1))
                .thenExecute(() -> {
                    altarWaitsFor(scenario, Items.FLINT_AND_STEEL, 1);
                    altarWaitsFor(scenario, Items.CROSSBOW, 1);
                })
                .thenWaitUntil(() -> {
                    boolean hasFlintAndSteel = false;
                    boolean hasCrossbow = false;
                    for (ItemStack stack : scenario.logic().getStorage()) {
                        hasFlintAndSteel |= ItemStack.matches(stack, flintAndSteel);
                        hasCrossbow |= ItemStack.matches(stack, crossbow);
                    }
                    helper.assertTrue(hasFlintAndSteel, "The damaged Flint and Steel was not supplied");
                    helper.assertTrue(hasCrossbow, "The enchanted Crossbow was not supplied");
                })
                .thenSucceed();
    }

    /**
     * With a Crafting Card the interface starts the autocrafting of a reagent the network does not have.
     */
    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void startsAutocraftingForMissingReagent(GameTestHelper helper) {
        assertRequestsCraftingOfFlint(helper, AEItemKey.of(Items.FLINT));
    }

    /**
     * The altar accepts any variant of a reagent, so a pattern whose result carries NBT (like many items of other
     * mods) is a way to get it.
     */
    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void startsAutocraftingForResultWithNbt(GameTestHelper helper) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("dmnr_test", 1);
        assertRequestsCraftingOfFlint(helper, AEItemKey.of(Items.FLINT, nbt));
    }

    /**
     * The whole way of autocrafting: the network does not have the reagent, a molecular assembler crafts it from a
     * pattern, and the crafted items end up in the interface.
     */
    @GameTest(template = TEMPLATE, timeoutTicks = CRAFTING_TIMEOUT)
    public static void deliversCraftedReagent(GameTestHelper helper) {
        Scenario scenario = buildNetworkWithAltar(helper);
        CraftingRecipe recipe = (CraftingRecipe) helper.getLevel().getRecipeManager().byKey(new ResourceLocation("minecraft", "stick")).orElseThrow();
        ItemStack[] grid = new ItemStack[9];
        Arrays.fill(grid, ItemStack.EMPTY);
        grid[0] = new ItemStack(Items.OAK_PLANKS);
        grid[3] = new ItemStack(Items.OAK_PLANKS);
        helper.setBlock(ASSEMBLER, AEBlocks.MOLECULAR_ASSEMBLER.block());
        addAutocrafting(helper, scenario, PatternDetailsHelper.encodeCraftingPattern(recipe, grid, new ItemStack(Items.STICK, 4), false, false));

        startWithStock(helper, scenario, stack(Items.OAK_PLANKS, 16))
                .thenExecute(() -> altarWaitsFor(scenario, Items.STICK, 4))
                .thenWaitUntil(() -> helper.assertFalse(scenario.logic().getRequestedJobs().isEmpty(), "No crafting job was requested"))
                .thenWaitUntil(() -> {
                    int sticks = 0;
                    for (ItemStack stack : scenario.logic().getStorage()) {
                        if (stack.is(Items.STICK)) {
                            sticks += stack.getCount();
                        }
                    }
                    helper.assertTrue(sticks == 4, "Expected 4 sticks in the interface, but there are " + sticks);
                })
                .thenSucceed();
    }

    /**
     * Without the rune nothing is moved, even though the altar waits for items.
     */
    @GameTest(template = TEMPLATE, timeoutTicks = TIMEOUT)
    public static void doesNothingWithoutRune(GameTestHelper helper) {
        Scenario scenario = buildNetworkWithAltar(helper);

        startWithStock(helper, scenario, stack(REAGENT, NETWORK_STOCK))
                .thenExecute(() -> {
                    altarWaitsFor(scenario, REAGENT, REAGENT_COUNT);
                    scenario.logic().getRuneInventory().setItemDirect(0, ItemStack.EMPTY);
                })
                .thenIdle(100)
                .thenExecute(() -> assertCounts(helper, scenario, 0, NETWORK_STOCK))
                .thenSucceed();
    }

    /**
     * The statuses shown in the GUI for the possible mistakes with the rune.
     */
    @GameTest(template = TEMPLATE)
    public static void reportsLinkStatus(GameTestHelper helper) {
        helper.setBlock(INTERFACE, AE2Init.ALTAR_INTERFACE.get());
        helper.setBlock(ALTAR, BlockInit.ALTAR_OF_ARCANA.get());
        BlockPos farAltar = INTERFACE.above(AltarLink.ALTAR_SEARCH_RADIUS + 1);
        helper.setBlock(farAltar, BlockInit.ALTAR_OF_ARCANA.get());

        AltarInterfaceLogic logic = ((AltarInterfaceBlockEntity) helper.getBlockEntity(INTERFACE)).getLogic();

        assertStatus(helper, logic, ItemStack.EMPTY, AltarLinkStatus.NO_RUNE);
        assertStatus(helper, logic, new ItemStack(Items.STICK), AltarLinkStatus.NO_RUNE);
        assertStatus(helper, logic, new ItemStack(ItemInit.RUNE_MARKING.get()), AltarLinkStatus.RUNE_WITHOUT_POSITION);
        assertStatus(helper, logic, rune(helper, ALTAR.east(2)), AltarLinkStatus.NO_ALTAR);
        assertStatus(helper, logic, rune(helper, farAltar), AltarLinkStatus.OUT_OF_RANGE);
        assertStatus(helper, logic, rune(helper, ALTAR), AltarLinkStatus.LINKED);
        helper.succeed();
    }

    /**
     * The block drops itself. Its loot table gets the item through an item tag, so that it also loads without AE2.
     */
    @GameTest(template = TEMPLATE)
    public static void dropsItselfWhenBroken(GameTestHelper helper) {
        helper.setBlock(INTERFACE, AE2Init.ALTAR_INTERFACE.get());
        // GameTestHelper#destroyBlock does not drop anything.
        helper.getLevel().destroyBlock(helper.absolutePos(INTERFACE), true);
        helper.succeedWhen(() -> helper.assertItemEntityPresent(AE2Init.ALTAR_INTERFACE_ITEM.get(), INTERFACE, 1.0));
    }

    /**
     * Lets the altar wait for flint, which the network does not have, but can craft from cobblestone with a pattern.
     * The pattern gets stuck in the pattern provider (there is no machine), but the crafting job is submitted, and that
     * is what counts.
     * @param flint result of the pattern
     */
    private static void assertRequestsCraftingOfFlint(GameTestHelper helper, AEItemKey flint) {
        Scenario scenario = buildNetworkWithAltar(helper);
        addAutocrafting(helper, scenario, PatternDetailsHelper.encodeProcessingPattern(
                new GenericStack[]{stack(Items.COBBLESTONE, 1)},
                new GenericStack[]{new GenericStack(flint, 1)}));

        startWithStock(helper, scenario, stack(Items.COBBLESTONE, 16))
                .thenExecute(() -> altarWaitsFor(scenario, Items.FLINT, 2))
                .thenWaitUntil(() -> helper.assertFalse(scenario.logic().getRequestedJobs().isEmpty(), "No crafting job was requested"))
                .thenSucceed();
    }

    private static void assertStatus(GameTestHelper helper, AltarInterfaceLogic logic, ItemStack rune, AltarLinkStatus expected) {
        logic.getRuneInventory().setItemDirect(0, rune);
        AltarLinkStatus actual = logic.evaluateLink().status();
        helper.assertTrue(actual == expected, "Expected the status " + expected + " but it is " + actual + " for " + rune);
    }

    private static ItemStack rune(GameTestHelper helper, BlockPos relativePos) {
        ItemStack rune = new ItemStack(ItemInit.RUNE_MARKING.get());
        ((ItemRuneMarking) rune.getItem()).setLocation(rune, helper.absolutePos(relativePos), Direction.UP);
        return rune;
    }

    private static GenericStack stack(Item item, long amount) {
        return new GenericStack(AEItemKey.of(item), amount);
    }

    private static Scenario buildNetworkWithAltar(GameTestHelper helper) {
        helper.setBlock(CELL, AEBlocks.CREATIVE_ENERGY_CELL.block());
        helper.setBlock(CHEST, AEBlocks.CHEST.block());
        helper.setBlock(INTERFACE, AE2Init.ALTAR_INTERFACE.get());
        helper.setBlock(ALTAR, BlockInit.ALTAR_OF_ARCANA.get());

        ChestBlockEntity chest = (ChestBlockEntity) helper.getBlockEntity(CHEST);
        chest.setCell(AEItems.ITEM_CELL_1K.stack());

        Scenario scenario = new Scenario((AltarInterfaceBlockEntity) helper.getBlockEntity(INTERFACE), (ArcanaAltarTile) helper.getBlockEntity(ALTAR));
        scenario.logic().getRuneInventory().setItemDirect(0, rune(helper, ALTAR));
        return scenario;
    }

    /**
     * Adds a crafting CPU and a pattern provider with the pattern to the network, and a Crafting Card to the interface.
     */
    private static void addAutocrafting(GameTestHelper helper, Scenario scenario, ItemStack pattern) {
        helper.setBlock(CPU, AEBlocks.CRAFTING_STORAGE_1K.block());
        helper.setBlock(PROVIDER, AEBlocks.PATTERN_PROVIDER.block());
        PatternProviderBlockEntity provider = (PatternProviderBlockEntity) helper.getBlockEntity(PROVIDER);
        provider.getLogic().getPatternInv().setItemDirect(0, pattern);
        scenario.logic().getUpgrades().addItems(AEItems.CRAFTING_CARD.stack());
    }

    /**
     * Starts the sequence of a test: waits until the network is ready (powered, with a channel for the interface and
     * the cell of the ME chest mounted), then puts the stock into it.
     */
    private static GameTestSequence startWithStock(GameTestHelper helper, Scenario scenario, GenericStack... stock) {
        return helper.startSequence().thenWaitUntil(() -> {
            helper.assertTrue(scenario.altarInterface().getMainNode().isActive(), "The network of the interface is not active yet");

            // Only simulated first, so a retry in the next tick does not insert anything twice.
            MEStorage network = scenario.grid().getStorageService().getInventory();
            for (GenericStack stack : stock) {
                long accepted = network.insert(stack.what(), stack.amount(), Actionable.SIMULATE, IActionSource.empty());
                helper.assertTrue(accepted == stack.amount(), "Only " + accepted + " of " + stack + " fit into the network");
            }
            for (GenericStack stack : stock) {
                network.insert(stack.what(), stack.amount(), Actionable.MODULATE, IActionSource.empty());
            }
        });
    }

    /**
     * Puts the altar in the state in which it waits for the reagent. Its crafting is not played through, because that
     * needs a player with the progression for the spell.
     */
    private static void altarWaitsFor(Scenario scenario, Item reagent, int count) {
        try {
            Field state = ArcanaAltarTile.class.getDeclaredField("state");
            state.setAccessible(true);
            state.set(scenario.altar(), ArcanaAltarTile.States.GATHERING_REAGENTS);
        } catch (ReflectiveOperationException e) {
            throw new GameTestAssertException("Could not set the state of the altar: " + e);
        }
        scenario.altar().getRemainingReagents().put(ForgeRegistries.ITEMS.getKey(reagent), count);
    }

    private static void assertCounts(GameTestHelper helper, Scenario scenario, int inInterface, int inNetwork) {
        int actualInterface = 0;
        for (ItemStack stack : scenario.logic().getStorage()) {
            if (stack.is(REAGENT)) {
                actualInterface += stack.getCount();
            } else if (!stack.isEmpty()) {
                throw new GameTestAssertException("Unexpected item in the interface: " + stack);
            }
        }

        KeyCounter network = new KeyCounter();
        scenario.grid().getStorageService().getInventory().getAvailableStacks(network);
        long actualNetwork = network.get(AEItemKey.of(REAGENT));

        helper.assertTrue(actualInterface == inInterface && actualNetwork == inNetwork,
                "Expected " + inInterface + " in the interface and " + inNetwork + " in the network, but there are "
                        + actualInterface + " and " + actualNetwork);
    }
}
