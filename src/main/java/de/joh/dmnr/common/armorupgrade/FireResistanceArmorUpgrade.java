package de.joh.dmnr.common.armorupgrade;

import de.joh.dmnr.common.event.DamageEventHandler;
import de.joh.dmnr.common.init.ArmorUpgradeInit;
import de.joh.dmnr.api.armorupgrade.ArmorUpgrade;
import de.joh.dmnr.api.armorupgrade.OnEquippedArmorUpgrade;
import de.joh.dmnr.networking.ModMessages;
import de.joh.dmnr.networking.packet.ToggleMajorFireResS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This upgrade protects you from fire damage. Level 1 Consumes mana instead. Level 2 protects you completely.
 * Increasing the maximum level has no effect without further adjustments.
 * @see DamageEventHandler
 * @author Joh0210
 */
public class FireResistanceArmorUpgrade extends OnEquippedArmorUpgrade {
    public final boolean hasStrongerAlternative;

    /**
     * @param registryName ID under which the upgrade can be recognized.
     */
    public FireResistanceArmorUpgrade(@NotNull ResourceLocation registryName, RegistryObject<Item> upgradeSealItem, boolean hasStrongerAlternative, int upgradeCost) {
        super(registryName, 1, upgradeSealItem, false, upgradeCost);
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
