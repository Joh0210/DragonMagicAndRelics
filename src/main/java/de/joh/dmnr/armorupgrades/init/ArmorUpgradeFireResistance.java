package de.joh.dmnr.armorupgrades.init;

import de.joh.dmnr.armorupgrades.ArmorUpgradeInit;
import de.joh.dmnr.armorupgrades.types.ArmorUpgrade;
import de.joh.dmnr.armorupgrades.types.ArmorUpgradeOnEquipped;
import de.joh.dmnr.networking.ModMessages;
import de.joh.dmnr.networking.packet.ToggleMajorFireResS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This upgrade protects you from fire damage. Level 1 Consumes mana instead. Level 2 protects you completely.
 * Increasing the maximum level has no effect without further adjustments.
 * @see de.joh.dmnr.events.DamageEventHandler
 * @author Joh0210
 */
public class ArmorUpgradeFireResistance extends ArmorUpgradeOnEquipped {
    public final boolean hasStrongerAlternative;

    /**
     * @param registryName ID under which the upgrade can be recognized.
     */
    public ArmorUpgradeFireResistance(@NotNull ResourceLocation registryName, boolean hasStrongerAlternative, int upgradeCost) {
        super(registryName, 1, false, upgradeCost);
        this.hasStrongerAlternative = hasStrongerAlternative;
    }

    @Override
    public @Nullable ArmorUpgrade getStrongerAlternative() {
        return hasStrongerAlternative ? ArmorUpgradeInit.MAJOR_FIRE_RESISTANCE : null;
    }

    @Override
    public void onEquip(Player player, int level) {
        if(!this.hasStrongerAlternative() && level >= 1 && player instanceof ServerPlayer) {
            ModMessages.sendToPlayer(new ToggleMajorFireResS2CPacket(true), (ServerPlayer) player);
        }
    }

    @Override
    public void onRemove(Player player) {
        if(!this.hasStrongerAlternative() && player instanceof ServerPlayer) {
            ModMessages.sendToPlayer(new ToggleMajorFireResS2CPacket(false), (ServerPlayer) player);
        }
    }
}