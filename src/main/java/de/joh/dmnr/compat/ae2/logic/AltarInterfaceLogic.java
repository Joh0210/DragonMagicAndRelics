package de.joh.dmnr.compat.ae2.logic;

import appeng.api.config.Actionable;
import appeng.api.config.RedstoneMode;
import appeng.api.config.Settings;
import appeng.api.config.YesNo;
import appeng.api.inventories.InternalInventory;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.crafting.ICraftingLink;
import appeng.api.networking.crafting.ICraftingRequester;
import appeng.api.networking.crafting.ICraftingService;
import appeng.api.networking.energy.IEnergyService;
import appeng.api.networking.security.IActionSource;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import appeng.api.storage.StorageHelper;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.IUpgradeableObject;
import appeng.api.upgrades.UpgradeInventories;
import appeng.api.util.IConfigManager;
import appeng.api.util.IConfigurableObject;
import appeng.core.definitions.AEItems;
import appeng.me.helpers.MachineSource;
import appeng.util.ConfigManager;
import appeng.util.inv.AppEngInternalInventory;
import appeng.util.inv.InternalInventoryHost;
import com.google.common.collect.ImmutableSet;
import de.joh.dmnr.compat.ae2.AE2Init;
import de.joh.dmnr.compat.ae2.blockentity.AltarInterfaceBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Behavior of the {@link AltarInterfaceBlockEntity}: while the Altar of Arcana of the inserted Rune of Marking waits for
 * items, they are moved from the ME network into the internal inventory, where the altar takes them from. Everything
 * the altar does not need (any more) is moved back into the network.
 * <p>
 * The altar finds the inventory of this block through the item handler capability of the block entity, as it does
 * with chests, as long as the interface is in the {@link AltarLink#ALTAR_SEARCH_RADIUS} of the altar.
 * @author Joh0210
 */
public class AltarInterfaceLogic implements ICraftingRequester, IUpgradeableObject, IConfigurableObject, InternalInventoryHost {
    public static final int STORAGE_SLOTS = 9;
    /** Upgrades the block can hold: one crafting card and one redstone card. */
    public static final int UPGRADE_SLOTS = 2;

    private static final int MIN_TICK_RATE = 5;
    private static final int MAX_TICK_RATE = 20;

    private static final String TAG_STORAGE = "storage";
    private static final String TAG_RUNE = "rune";
    private static final String TAG_UPGRADES = "upgrades";

    private final AltarInterfaceBlockEntity host;
    private final IManagedGridNode mainNode;
    private final IActionSource actionSource;
    private final AltarCraftingTracker craftingTracker = new AltarCraftingTracker(this);
    private final ConfigManager configManager = new ConfigManager(this::onConfigChanged);
    private final IUpgradeInventory upgrades;
    /** Items that were moved out of the network. Filled and emptied by this logic only, the altar takes items out of it. */
    private final AppEngInternalInventory storage = new AppEngInternalInventory(this, STORAGE_SLOTS);
    /** Holds the Rune of Marking that is linked to the altar. */
    private final AppEngInternalInventory rune = new AppEngInternalInventory(this, 1, 1);

    private boolean wasPowered;
    private boolean pulsePending;

    public AltarInterfaceLogic(@NotNull IManagedGridNode mainNode, @NotNull AltarInterfaceBlockEntity host) {
        this.host = host;
        this.mainNode = mainNode
                .setFlags(GridFlags.REQUIRE_CHANNEL)
                .addService(IGridTickable.class, new Ticker())
                .addService(ICraftingRequester.class, this);
        this.actionSource = new MachineSource(this);
        this.upgrades = UpgradeInventories.forMachine(AE2Init.ALTAR_INTERFACE_ITEM.get(), UPGRADE_SLOTS, this::onUpgradesChanged);

        configManager.registerSetting(Settings.REDSTONE_CONTROLLED, RedstoneMode.IGNORE);
        configManager.registerSetting(Settings.CRAFT_ONLY, YesNo.NO);
    }

    public @NotNull AppEngInternalInventory getStorage() {
        return storage;
    }

    public @NotNull AppEngInternalInventory getRuneInventory() {
        return rune;
    }

    @Override
    public @NotNull IUpgradeInventory getUpgrades() {
        return upgrades;
    }

    @Override
    public @NotNull IConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * Looks up the altar of the inserted rune. Cheap enough to be called whenever the status is displayed.
     */
    public @NotNull AltarLink evaluateLink() {
        return AltarLink.resolve(host.getLevel(), host.getBlockPos(), rune.getStackInSlot(0));
    }

    private boolean hasCraftingUpgrade() {
        return upgrades.isInstalled(AEItems.CRAFTING_CARD);
    }

    // region persistence

    public void writeToNBT(@NotNull CompoundTag tag) {
        storage.writeToNBT(tag, TAG_STORAGE);
        rune.writeToNBT(tag, TAG_RUNE);
        upgrades.writeToNBT(tag, TAG_UPGRADES);
        configManager.writeToNBT(tag);
        craftingTracker.writeToNBT(tag);
    }

    public void readFromNBT(@NotNull CompoundTag tag) {
        storage.readFromNBT(tag, TAG_STORAGE);
        rune.readFromNBT(tag, TAG_RUNE);
        upgrades.readFromNBT(tag, TAG_UPGRADES);
        configManager.readFromNBT(tag);
        craftingTracker.readFromNBT(tag);
    }

    public void addDrops(@NotNull List<ItemStack> drops) {
        // The iterator of an InternalInventory skips empty slots.
        for (InternalInventory inventory : List.of(upgrades, storage, rune)) {
            for (ItemStack stack : inventory) {
                drops.add(stack);
            }
        }
    }

    public void clearContent() {
        upgrades.clear();
        storage.clear();
        rune.clear();
    }

    // endregion

    // region InternalInventoryHost

    @Override
    public void saveChanges() {
        host.saveChanges();
    }

    @Override
    public void onChangeInventory(InternalInventory inv, int slot) {
        host.saveChanges();
    }

    @Override
    public boolean isClientSide() {
        Level level = host.getLevel();
        return level == null || level.isClientSide();
    }

    // endregion

    // region callbacks

    private void onConfigChanged() {
        host.saveChanges();
        wakeUp();
    }

    private void onUpgradesChanged() {
        host.saveChanges();
        if (!hasCraftingUpgrade()) {
            craftingTracker.cancel();
        }
        wakeUp();
    }

    /**
     * Called by the block when a neighbor changed, to follow the redstone signal.
     */
    public void onNeighborChanged() {
        Level level = host.getLevel();
        if (level == null || level.isClientSide()) {
            return;
        }

        boolean powered = level.hasNeighborSignal(host.getBlockPos());
        if (powered && !wasPowered) {
            pulsePending = true;
        }
        wasPowered = powered;
        wakeUp();
    }

    private void wakeUp() {
        mainNode.ifPresent((grid, node) -> grid.getTickManager().alertDevice(node));
    }

    // endregion

    // region ICraftingRequester

    @Override
    public @NotNull ImmutableSet<ICraftingLink> getRequestedJobs() {
        return craftingTracker.getRequestedJobs();
    }

    @Override
    public long insertCraftedItems(ICraftingLink link, AEKey what, long amount, Actionable mode) {
        if (!(what instanceof AEItemKey key)) {
            return 0;
        }

        int count = (int) Math.min(amount, Integer.MAX_VALUE);
        ItemStack remainder = storage.addItems(key.toStack(count), mode == Actionable.SIMULATE);
        return count - remainder.getCount();
    }

    @Override
    public void jobStateChange(ICraftingLink link) {
        craftingTracker.jobStateChange(link);
    }

    @Override
    public @Nullable IGridNode getActionableNode() {
        return mainNode.getNode();
    }

    // endregion

    private class Ticker implements IGridTickable {
        @Override
        public TickingRequest getTickingRequest(IGridNode node) {
            return new TickingRequest(MIN_TICK_RATE, MAX_TICK_RATE, false, true);
        }

        @Override
        public TickRateModulation tickingRequest(IGridNode node, int ticksSinceLastCall) {
            if (!mainNode.isActive() || !isEnabledByRedstone()) {
                return TickRateModulation.SLOWER;
            }
            return exchangeItems() ? TickRateModulation.FASTER : TickRateModulation.SLOWER;
        }
    }

    private boolean isEnabledByRedstone() {
        if (!upgrades.isInstalled(AEItems.REDSTONE_CARD)) {
            return true;
        }

        Level level = host.getLevel();
        if (level == null) {
            return false;
        }

        boolean powered = level.hasNeighborSignal(host.getBlockPos());
        return switch (configManager.getSetting(Settings.REDSTONE_CONTROLLED)) {
            case IGNORE -> true;
            case LOW_SIGNAL -> !powered;
            case HIGH_SIGNAL -> powered;
            case SIGNAL_PULSE -> {
                // one exchange per rising edge
                boolean pulse = pulsePending;
                pulsePending = false;
                yield pulse;
            }
        };
    }

    /**
     * One exchange with the network: moves everything the altar does not need into the network, then fetches what it
     * is missing (or requests crafting for it).
     * @return true if something was moved or requested
     */
    private boolean exchangeItems() {
        IGrid grid = mainNode.getGrid();
        Level level = host.getLevel();
        if (grid == null || level == null) {
            return false;
        }

        craftingTracker.removeEndedJobs();

        AltarLink link = evaluateLink();
        List<Shortage> shortages = link.status().isLinked()
                ? AltarNeed.collect(link.altar()).stream().map(Shortage::new).toList()
                : List.of();

        boolean changed = returnSurplus(shortages, grid);
        changed |= fetchMissing(shortages, grid, level);
        return changed;
    }

    /**
     * Moves everything into the network that is not needed. Reduces the shortages by what is already in the inventory.
     */
    private boolean returnSurplus(@NotNull List<Shortage> shortages, @NotNull IGrid grid) {
        IEnergyService energy = grid.getEnergyService();
        MEStorage network = grid.getStorageService().getInventory();
        boolean changed = false;

        for (int slot = 0; slot < storage.size(); slot++) {
            ItemStack stack = storage.getStackInSlot(slot);
            if (stack.isEmpty()) {
                continue;
            }

            AEItemKey key = AEItemKey.of(stack);
            int keep = 0;
            for (Shortage shortage : shortages) {
                if (shortage.missing > 0 && shortage.need.accepts(key)) {
                    keep = Math.min(stack.getCount(), shortage.missing);
                    shortage.missing -= keep;
                    break;
                }
            }

            int surplus = stack.getCount() - keep;
            if (surplus > 0) {
                long inserted = StorageHelper.poweredInsert(energy, network, key, surplus, actionSource);
                if (inserted > 0) {
                    storage.extractItem(slot, (int) inserted, false);
                    changed = true;
                }
            }
        }

        return changed;
    }

    private boolean fetchMissing(@NotNull List<Shortage> shortages, @NotNull IGrid grid, @NotNull Level level) {
        IEnergyService energy = grid.getEnergyService();
        MEStorage network = grid.getStorageService().getInventory();
        boolean craftingAllowed = hasCraftingUpgrade();
        boolean craftOnly = craftingAllowed && configManager.getSetting(Settings.CRAFT_ONLY) == YesNo.YES;
        boolean changed = false;

        for (Shortage shortage : shortages) {
            if (shortage.missing <= 0) {
                continue;
            }

            if (!craftOnly) {
                for (AEItemKey key : shortage.need.candidates(grid.getStorageService().getCachedInventory())) {
                    int fetched = fetch(key, shortage.missing, energy, network);
                    shortage.missing -= fetched;
                    changed |= fetched > 0;
                    if (shortage.missing <= 0) {
                        break;
                    }
                }
            }

            if (shortage.missing > 0 && craftingAllowed) {
                changed |= requestCrafting(shortage.need, shortage.missing, grid, level);
            }
        }

        return changed;
    }

    /**
     * @return how many items were moved from the network into the inventory
     */
    private int fetch(@NotNull AEItemKey key, int amount, @NotNull IEnergyService energy, @NotNull MEStorage network) {
        long available = StorageHelper.poweredExtraction(energy, network, key, amount, actionSource, Actionable.SIMULATE);
        if (available <= 0) {
            return 0;
        }

        // Only take what fits, so nothing has to be given back.
        int wanted = (int) Math.min(available, amount);
        ItemStack notFitting = storage.addItems(key.toStack(wanted), true);
        int fits = wanted - notFitting.getCount();
        if (fits <= 0) {
            return 0;
        }

        long extracted = StorageHelper.poweredExtraction(energy, network, key, fits, actionSource);
        if (extracted <= 0) {
            return 0;
        }

        ItemStack leftover = storage.addItems(key.toStack((int) extracted));
        if (!leftover.isEmpty()) {
            // Can not happen after the simulation, but items must never be lost.
            StorageHelper.poweredInsert(energy, network, key, leftover.getCount(), actionSource);
        }
        return (int) extracted - leftover.getCount();
    }

    /**
     * Requests the crafting of an item that satisfies the need. Items the altar accepts can be crafted by patterns whose
     * result has NBT or is damaged, so not only the plain item is looked for.
     * @return true if a crafting job was submitted
     */
    private boolean requestCrafting(@NotNull AltarNeed need, int amount, @NotNull IGrid grid, @NotNull Level level) {
        ICraftingService crafting = grid.getCraftingService();

        // A job that is under way has to be advanced (its plan is calculated asynchronously and submitted afterwards),
        // so the same item is requested again until the job runs.
        AEKey busy = craftingTracker.findBusy(key -> key instanceof AEItemKey itemKey && need.accepts(itemKey));
        if (busy != null) {
            return craftingTracker.handleCrafting(busy, amount, level, crafting, actionSource);
        }

        AEItemKey craftable = findCraftable(need, crafting);
        return craftable != null && craftingTracker.handleCrafting(craftable, amount, level, crafting, actionSource);
    }

    private @Nullable AEItemKey findCraftable(@NotNull AltarNeed need, @NotNull ICraftingService crafting) {
        for (AEItemKey option : need.options()) {
            if (crafting.isCraftable(option)) {
                return option;
            }
        }

        if (!need.ignoreNbt()) {
            return null;
        }

        AEItemKey best = null;
        for (AEKey key : crafting.getCraftables(candidate -> candidate instanceof AEItemKey itemKey && need.accepts(itemKey))) {
            AEItemKey itemKey = (AEItemKey) key;
            if (best == null || need.preference(itemKey) < need.preference(best)) {
                best = itemKey;
            }
        }
        return best;
    }

    /**
     * A need of the altar and how many of its items are still missing during one exchange.
     */
    private static final class Shortage {
        private final AltarNeed need;
        private int missing;

        private Shortage(@NotNull AltarNeed need) {
            this.need = need;
            this.missing = need.count();
        }
    }
}
