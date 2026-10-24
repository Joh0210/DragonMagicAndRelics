package de.joh.dmnr.compat.ae2.client;

import appeng.api.config.RedstoneMode;
import appeng.api.config.Settings;
import appeng.api.config.YesNo;
import appeng.client.gui.implementations.UpgradeableScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.ServerSettingToggleButton;
import appeng.client.gui.widgets.SettingToggleButton;
import appeng.core.definitions.AEItems;
import de.joh.dmnr.compat.ae2.menu.AltarInterfaceMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/**
 * Screen of the Altar ME Interface. Its layout is defined in {@code assets/ae2/screens/dmnr_altar_me_interface.json}
 * (AE2 only looks for screen styles in its own namespace) and reuses the background of the ME Interface.
 * @see AltarInterfaceMenu
 * @author Joh0210
 */
public class AltarInterfaceScreen extends UpgradeableScreen<AltarInterfaceMenu> {
    /** Path of the style document, see {@code appeng.client.gui.style.StyleManager#loadStyleDoc}. */
    public static final String STYLE = "/screens/dmnr_altar_me_interface.json";

    private static final String TEXT_LINK_STATUS = "link_status";

    private final SettingToggleButton<RedstoneMode> redstoneMode;
    private final SettingToggleButton<YesNo> craftMode;

    public AltarInterfaceScreen(AltarInterfaceMenu menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);

        this.redstoneMode = new ServerSettingToggleButton<>(Settings.REDSTONE_CONTROLLED, RedstoneMode.IGNORE);
        this.craftMode = new ServerSettingToggleButton<>(Settings.CRAFT_ONLY, YesNo.NO);
        addToLeftToolbar(this.redstoneMode);
        addToLeftToolbar(this.craftMode);
    }

    @Override
    protected void updateBeforeRender() {
        super.updateBeforeRender();

        this.redstoneMode.set(menu.getRedStoneMode());
        this.redstoneMode.setVisibility(menu.hasUpgrade(AEItems.REDSTONE_CARD));
        this.craftMode.set(menu.getCraftingMode());
        this.craftMode.setVisibility(menu.hasUpgrade(AEItems.CRAFTING_CARD));

        setTextContent(TEXT_LINK_STATUS, menu.getLinkStatus().getText());
    }
}
