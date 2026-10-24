package de.joh.dmnr.compat.ae2.menu;

import appeng.api.config.Settings;
import appeng.api.inventories.InternalInventory;
import appeng.api.util.IConfigManager;
import appeng.menu.MenuOpener;
import appeng.menu.SlotSemantics;
import appeng.menu.guisync.GuiSync;
import appeng.menu.implementations.UpgradeableMenu;
import appeng.menu.locator.MenuLocator;
import appeng.menu.locator.MenuLocators;
import appeng.menu.slot.AppEngSlot;
import com.mna.items.runes.ItemRuneMarking;
import de.joh.dmnr.compat.ae2.AE2Init;
import de.joh.dmnr.compat.ae2.blockentity.AltarInterfaceBlockEntity;
import de.joh.dmnr.compat.ae2.logic.AltarInterfaceLogic;
import de.joh.dmnr.compat.ae2.logic.AltarLinkStatus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

/**
 * Menu of the {@link AltarInterfaceBlockEntity}. Uses the framework of AE2, so the upgrade slots, the toolbox and the
 * settings buttons behave like in the screens of AE2 itself.
 * @see de.joh.dmnr.compat.ae2.client.AltarInterfaceScreen
 * @author Joh0210
 */
public class AltarInterfaceMenu extends UpgradeableMenu<AltarInterfaceBlockEntity> {
    /** The ids of the {@link GuiSync} fields of the superclasses are all below this. */
    private static final short SYNC_LINK_STATUS = 30;

    @GuiSync(SYNC_LINK_STATUS)
    public int linkStatus = AltarLinkStatus.NO_RUNE.ordinal();

    public AltarInterfaceMenu(int id, Inventory playerInventory, AltarInterfaceBlockEntity host) {
        super(AE2Init.ALTAR_INTERFACE_MENU.get(), id, playerInventory, host);

        AltarInterfaceLogic logic = host.getLogic();
        addSlot(new RuneSlot(logic.getRuneInventory(), 0), SlotSemantics.CONFIG);

        InternalInventory storage = logic.getStorage();
        for (int slot = 0; slot < storage.size(); slot++) {
            addSlot(new TakeOnlySlot(storage, slot), SlotSemantics.STORAGE);
        }
    }

    public @NotNull AltarLinkStatus getLinkStatus() {
        return AltarLinkStatus.byOrdinal(linkStatus);
    }

    @Override
    public void broadcastChanges() {
        if (isServerSide()) {
            linkStatus = getHost().getLogic().evaluateLink().status().ordinal();
        }
        super.broadcastChanges();
    }

    @Override
    protected void loadSettingsFromHost(IConfigManager cm) {
        setRedStoneMode(cm.getSetting(Settings.REDSTONE_CONTROLLED));
        setCraftingMode(cm.getSetting(Settings.CRAFT_ONLY));
    }

    /**
     * Only Runes of Marking fit in. Runes that are not bound yet are accepted too, so the screen can tell what is missing.
     */
    private static class RuneSlot extends AppEngSlot {
        RuneSlot(InternalInventory inventory, int slot) {
            super(inventory, slot);
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return stack.getItem() instanceof ItemRuneMarking && super.mayPlace(stack);
        }
    }

    /**
     * The items in the storage are managed by the interface: players can take them out, but not put anything in.
     */
    private static class TakeOnlySlot extends AppEngSlot {
        TakeOnlySlot(InternalInventory inventory, int slot) {
            super(inventory, slot);
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return false;
        }
    }

    /**
     * Counterpart of {@code MenuTypeBuilder} of AE2 for the menu type of this mod: creates the menu on the client
     * from the packet sent by {@link #open}.
     */
    public static AltarInterfaceMenu fromNetwork(int containerId, Inventory playerInventory, FriendlyByteBuf buffer) {
        MenuLocator locator = MenuLocators.readFromPacket(buffer);
        AltarInterfaceBlockEntity host = locator.locate(playerInventory.player, AltarInterfaceBlockEntity.class);
        if (host == null) {
            throw new IllegalStateException("Couldn't find the Altar ME Interface for the menu at " + locator + " on the client.");
        }

        AltarInterfaceMenu menu = new AltarInterfaceMenu(containerId, playerInventory, host);
        menu.setReturnedFromSubScreen(buffer.readBoolean());
        return menu;
    }

    /**
     * Opens the menu for the player on the server. Registered at the {@link MenuOpener} of AE2, which is also what
     * AE2 uses to return to a menu after one of its sub screens.
     */
    public static boolean open(@NotNull Player player, @NotNull MenuLocator locator, boolean fromSubMenu) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return false;
        }

        AltarInterfaceBlockEntity host = locator.locate(player, AltarInterfaceBlockEntity.class);
        if (host == null) {
            return false;
        }

        MenuProvider provider = new SimpleMenuProvider((containerId, inventory, ignored) -> {
            AltarInterfaceMenu menu = new AltarInterfaceMenu(containerId, inventory, host);
            // To find its way back after having been closed.
            menu.setLocator(locator);
            return menu;
        }, host.getBlockState().getBlock().getName());

        NetworkHooks.openScreen(serverPlayer, provider, buffer -> {
            MenuLocators.writeToPacket(buffer, locator);
            buffer.writeBoolean(fromSubMenu);
        });
        return true;
    }
}
