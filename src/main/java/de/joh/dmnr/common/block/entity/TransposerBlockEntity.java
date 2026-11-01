package de.joh.dmnr.common.block.entity;

import com.mna.items.ItemInit;
import com.mna.api.sound.SFX;
import com.mna.items.runes.ItemRuneMarking;
import com.mojang.authlib.GameProfile;
import de.joh.dmnr.client.gui.ContainerTransposer;
import de.joh.dmnr.common.block.TransposerBlock;
import de.joh.dmnr.common.init.BlockEntitieInit;
import de.joh.dmnr.common.util.CommonConfig;
import de.joh.dmnr.common.util.TranspositionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

/**
 * BlockEntity of the Transposer. It holds a Rune of Marking, which is bound to another Transposer, and optionally a
 * Ritual Focus, which enlarges the area (by default a single block without a focus). When the Transposer is activated,
 * it swaps the area above it with the area of the same size above the other Transposer. The other Transposer needs
 * neither a rune nor a focus.
 * <br>The Rune of Marking does not store a dimension, so the other Transposer is always in the dimension of this one.
 * <p>
 * Every Transposer remembers the player who placed it. Nothing is swapped unless the owners of both Transposers may
 * break every block in both areas, so claim and protection mods can prevent a transposition. If enabled in the config,
 * both Transposers must also have the same owner.
 * @see de.joh.dmnr.common.block.TransposerBlock
 * @see TranspositionHelper
 * @author Joh0210
 */
public class TransposerBlockEntity extends BlockEntity implements MenuProvider {
    public static final int SLOT_RUNE = 0;
    public static final int SLOT_FOCUS = 1;
    public static final int SLOT_COUNT = 2;

    /** Index of the {@link TransposerStatus} in the data synchronized to the menu. */
    public static final int DATA_STATUS = 0;
    /** Index of the edge length of the area in the data synchronized to the menu, 0 if there is no area. */
    public static final int DATA_AREA_SIZE = 1;
    public static final int DATA_COUNT = 2;

    private static final String TAG_INVENTORY = "inventory";
    private static final String TAG_OWNER = "owner";

    private final ItemStackHandler inventory = createInventory(this::onInventoryChanged);

    // Player who placed the Transposer, null if it was not placed by a player (e.g. by a command)
    private @Nullable GameProfile owner;

    // Result of the last activation, shown in the GUI until the content changes or an activation succeeds
    private boolean blockedByProtection = false;

    // Set while the Transposer takes part in a transposition, the moved blocks can't activate it then
    private boolean transposing = false;

    // Only the server can check the other Transposer, and the size of the area depends on its config.
    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case DATA_STATUS -> getDisplayStatus().ordinal();
                case DATA_AREA_SIZE -> getAreaSize();
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
        }

        @Override
        public int getCount() {
            return DATA_COUNT;
        }
    };

    public TransposerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitieInit.TRANSPOSER_ENTITY.get(), pPos, pBlockState);
    }

    /**
     * Inventory with a slot for the Rune of Marking and one for the Ritual Focus. Also used by the menu on the client.
     * @param onChanged called whenever the content changes
     */
    public static @NotNull ItemStackHandler createInventory(@NotNull Runnable onChanged) {
        return new ItemStackHandler(SLOT_COUNT) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return switch (slot) {
                    case SLOT_RUNE -> stack.getItem() instanceof ItemRuneMarking;
                    case SLOT_FOCUS -> radiusConfig(stack) != null;
                    default -> false;
                };
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            protected void onContentsChanged(int slot) {
                onChanged.run();
            }
        };
    }

    private void onInventoryChanged() {
        blockedByProtection = false;
        setChanged();
        updateBlockState();
    }

    /**
     * Shows the content on the block: whether the rune is bound ({@link TransposerBlock#LINKED}) and the tier of the
     * Ritual Focus ({@link TransposerBlock#FOCUS}).
     */
    private void updateBlockState() {
        if (level == null || level.isClientSide()) {
            return;
        }

        BlockState state = level.getBlockState(worldPosition);
        if (!state.hasProperty(TransposerBlock.LINKED)) {
            return;
        }
        BlockState updated = state.setValue(TransposerBlock.LINKED, isLinked())
                .setValue(TransposerBlock.FOCUS, getFocusTier(inventory.getStackInSlot(SLOT_FOCUS)));
        if (updated != state) {
            level.setBlock(worldPosition, updated, Block.UPDATE_CLIENTS);
        }
    }

    /**
     * @return the inventory with the Rune of Marking ({@link #SLOT_RUNE}) and the Ritual Focus ({@link #SLOT_FOCUS})
     */
    public @NotNull ItemStackHandler getInventory() {
        return inventory;
    }

    /**
     * @return the player who placed the Transposer, null if it was not placed by a player (e.g. by a command)
     */
    public @Nullable GameProfile getOwner() {
        return owner;
    }

    public void setOwner(@Nullable GameProfile owner) {
        this.owner = owner;
        setChanged();
    }

    private static boolean isSameOwner(@Nullable GameProfile first, @Nullable GameProfile second) {
        return first != null && second != null && first.getId() != null && first.getId().equals(second.getId());
    }

    /**
     * @return the fake player that acts for the owner in the protection checks. Without an owner, it is the anonymous
     * fake player of Forge, which M&A uses as well.
     */
    private @NotNull Player getOwnerPlayer(@NotNull ServerLevel level) {
        return owner == null ? FakePlayerFactory.getMinecraft(level) : FakePlayerFactory.get(level, owner);
    }

    /**
     * @param focus content of the focus slot
     * @return radius of the area that the given Ritual Focus sets, -1 if it is no Ritual Focus.
     * An empty slot results in the radius without a focus (by default 0, a single block), or -1 if the config
     * requires a focus.
     * @see TranspositionHelper#getArea
     */
    public static int getRadius(@NotNull ItemStack focus) {
        if (focus.isEmpty()) {
            return CommonConfig.TRANSPOSER_RADIUS_NO_FOCUS.get();
        }
        ForgeConfigSpec.ConfigValue<Integer> config = radiusConfig(focus);
        return config == null ? -1 : config.get();
    }

    private static @Nullable ForgeConfigSpec.ConfigValue<Integer> radiusConfig(ItemStack focus) {
        return switch (getFocusTier(focus)) {
            case 1 -> CommonConfig.TRANSPOSER_RADIUS_MINOR;
            case 2 -> CommonConfig.TRANSPOSER_RADIUS_LESSER;
            case 3 -> CommonConfig.TRANSPOSER_RADIUS_GREATER;
            default -> null;
        };
    }

    /**
     * @param focus content of the focus slot
     * @return tier of the Ritual Focus (1 minor, 2 lesser, 3 greater), 0 if it is no Ritual Focus
     */
    public static int getFocusTier(@NotNull ItemStack focus) {
        Item item = focus.getItem();
        if (item == ItemInit.RITUAL_FOCUS_MINOR.get()) {
            return 1;
        } else if (item == ItemInit.RITUAL_FOCUS_LESSER.get()) {
            return 2;
        } else if (item == ItemInit.RITUAL_FOCUS_GREATER.get()) {
            return 3;
        }
        return 0;
    }

    /**
     * @return radius of the area of this Transposer, -1 if it has no area (no Ritual Focus, and the config requires one)
     */
    public int getRadius() {
        return getRadius(inventory.getStackInSlot(SLOT_FOCUS));
    }

    /**
     * @return edge length of the area of this Transposer, 0 if it has no area
     */
    public int getAreaSize() {
        int radius = getRadius();
        return radius < 0 ? 0 : 2 * radius + 1;
    }

    /**
     * @return the position the Rune of Marking is bound to, if there is a bound rune
     */
    public @Nullable BlockPos getTarget() {
        ItemStack rune = inventory.getStackInSlot(SLOT_RUNE);
        return rune.getItem() instanceof ItemRuneMarking marking ? marking.getLocation(rune) : null;
    }

    /**
     * Only checks the rune, not whether there is a Transposer at the marked position: that would load its chunk,
     * the GUI shows the full {@link #getDisplayStatus status}.
     * @return whether the Rune of Marking is bound to another position
     */
    public boolean isLinked() {
        BlockPos target = getTarget();
        return target != null && !target.equals(worldPosition);
    }

    /**
     * Whether the areas are protected is only known after an activation, see {@link #getDisplayStatus}.
     * @param loadTarget may the chunk of the other Transposer be loaded to check it?
     *                   If not, an unloaded target results in {@link TransposerStatus#TARGET_NOT_LOADED}.
     */
    public @NotNull TransposerStatus getStatus(boolean loadTarget) {
        if (!(inventory.getStackInSlot(SLOT_RUNE).getItem() instanceof ItemRuneMarking)) {
            return TransposerStatus.NO_RUNE;
        }

        BlockPos target = getTarget();
        if (target == null) {
            return TransposerStatus.RUNE_WITHOUT_POSITION;
        }
        if (target.equals(worldPosition)) {
            return TransposerStatus.BOUND_TO_ITSELF;
        }

        int radius = getRadius();
        if (radius < 0) {
            return TransposerStatus.NO_FOCUS;
        }
        if (TranspositionHelper.getArea(worldPosition, radius).intersects(TranspositionHelper.getArea(target, radius))) {
            return TransposerStatus.AREAS_OVERLAP;
        }

        if (level == null || level.isOutsideBuildHeight(target)) {
            return TransposerStatus.NO_TRANSPOSER;
        }
        if (!loadTarget && !level.isLoaded(target)) {
            return TransposerStatus.TARGET_NOT_LOADED;
        }
        if (!(level.getBlockEntity(target) instanceof TransposerBlockEntity other)) {
            return TransposerStatus.NO_TRANSPOSER;
        }
        if (CommonConfig.TRANSPOSER_REQUIRE_SAME_OWNER.get() && !isSameOwner(owner, other.owner)) {
            return TransposerStatus.DIFFERENT_OWNER;
        }
        return TransposerStatus.READY;
    }

    /**
     * @return the status shown in the GUI: {@link #getStatus} without loading the target, or
     * {@link TransposerStatus#AREA_PROTECTED} if the last activation was prevented by protected land
     */
    public @NotNull TransposerStatus getDisplayStatus() {
        TransposerStatus status = getStatus(false);
        return blockedByProtection && status.canActivate() ? TransposerStatus.AREA_PROTECTED : status;
    }

    /**
     * @return whether the Transposer takes part in a transposition right now
     */
    public boolean isTransposing() {
        return transposing;
    }

    /**
     * Swaps the area above this Transposer with the area above the Transposer the Rune of Marking is bound to.
     * The chunks of the other area are loaded if necessary.
     * <br>Nothing is swapped unless the owners of both Transposers may change both areas, see
     * {@link TranspositionHelper#mayChange}.
     * <p>
     * Afterwards, both Transposers need a new rising redstone signal: once every block is in its new place, they adopt
     * their signal without being activated. Otherwise a moved redstone block (or a lever, ...) that powers the other
     * Transposer would swap the areas back and forth forever.
     * <br>Both Transposers show the transposition with particles (see {@link TransposerBlock#triggerEvent}) and a sound.
     * @return whether the areas were swapped
     */
    public boolean transpose() {
        if (!(level instanceof ServerLevel serverLevel) || getStatus(true) != TransposerStatus.READY
                || !(serverLevel.getBlockEntity(getTarget()) instanceof TransposerBlockEntity other)) {
            return false;
        }

        int radius = getRadius();
        BoundingBox area = TranspositionHelper.getArea(worldPosition, radius);
        BoundingBox otherArea = TranspositionHelper.getArea(other.worldPosition, radius);
        // A single check if both Transposers have the same owner
        List<Player> owners = Stream.of(getOwnerPlayer(serverLevel), other.getOwnerPlayer(serverLevel)).distinct().toList();
        blockedByProtection = !TranspositionHelper.mayChange(serverLevel, area, owners)
                || !TranspositionHelper.mayChange(serverLevel, otherArea, owners);
        if (blockedByProtection) {
            return false;
        }

        transposing = true;
        other.transposing = true;
        try {
            TranspositionHelper.swap(serverLevel, worldPosition, other.worldPosition, radius);
        } finally {
            transposing = false;
            other.transposing = false;
        }
        adoptRedstoneSignal(serverLevel, worldPosition);
        adoptRedstoneSignal(serverLevel, other.worldPosition);
        for (BlockPos transposer : List.of(worldPosition, other.worldPosition)) {
            serverLevel.blockEvent(transposer, getBlockState().getBlock(), TransposerBlock.EVENT_TRANSPOSED, radius);
            serverLevel.playSound(null, transposer.above(radius + 1), SFX.Spell.Impact.Single.ENDER, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return true;
    }

    private static void adoptRedstoneSignal(ServerLevel level, BlockPos transposer) {
        BlockState state = level.getBlockState(transposer);
        if (state.hasProperty(TransposerBlock.POWERED)) {
            TransposerBlock.updatePowered(level, transposer, state, false);
        }
    }

    /**
     * Drops the rune and the focus, called when the block is removed.
     */
    public void dropContents() {
        if (level == null) {
            return;
        }

        for (int i = 0; i < inventory.getSlots(); i++) {
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), inventory.getStackInSlot(i));
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put(TAG_INVENTORY, inventory.serializeNBT());
        if (owner != null) {
            pTag.put(TAG_OWNER, NbtUtils.writeGameProfile(new CompoundTag(), owner));
        }
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        inventory.deserializeNBT(pTag.getCompound(TAG_INVENTORY));
        owner = pTag.contains(TAG_OWNER) ? NbtUtils.readGameProfile(pTag.getCompound(TAG_OWNER)) : null;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return getBlockState().getBlock().getName();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new ContainerTransposer(pContainerId, pPlayerInventory, inventory, data, ContainerLevelAccess.create(pPlayer.level(), worldPosition));
    }
}
