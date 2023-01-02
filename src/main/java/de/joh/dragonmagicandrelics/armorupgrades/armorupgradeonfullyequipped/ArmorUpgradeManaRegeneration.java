package de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonfullyequipped;

import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import net.minecraft.world.entity.player.Player;

/**
 * Upgrades of this type increase the Mama regeneration.
 * Function and constructor are initialized versions of parent class function/constructor.
 * @see de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit
 * @author Joh0210
 */
public class ArmorUpgradeManaRegeneration extends IArmorUpgradeOnFullyEquipped {
    public ArmorUpgradeManaRegeneration(String upgradeId, int maxUpgradeLevel) {
        super(upgradeId, maxUpgradeLevel);
    }

    @Override
    public void applySetBonus(Player player, int level) {
        player.getPersistentData().putBoolean("mma_magic_set_bonus", true);
        player.getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> {
            m.getCastingResource().addRegenerationModifier("mma_magic_set_bonus", -0.1F * (float)level);
        });
    }

    @Override
    public void removeSetBonus(Player player) {
        player.getPersistentData().putBoolean("mma_magic_set_bonus", false);
        (player).getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> {
            m.getCastingResource().removeRegenerationModifier("mma_magic_set_bonus");
        });
    }
}
