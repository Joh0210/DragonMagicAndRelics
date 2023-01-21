package de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonfullyequipped;

import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import net.minecraft.world.entity.player.Player;

/**
 * If upgrades of this type are installed, the player receives a certain number of additional Mana.
 * Function and constructor are initialized versions of parent class function/constructor.
 * @see de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit
 * @author Joh0210
 */
public class ArmorUpgradeManaBoost  extends IArmorUpgradeOnFullyEquipped {
    public ArmorUpgradeManaBoost(String upgradeId, int maxUpgradeLevel) {
        super(upgradeId, maxUpgradeLevel);
    }
    public static int MANA_PER_MANABOOST = 200;

    @Override
    public void applySetBonus(Player player, int level) {
        player.getPersistentData().putBoolean("mma_magic_set_bonus", true);
        player.getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> m.getCastingResource().addModifier("mma_magic_set_bonus", MANA_PER_MANABOOST * level));
    }

    @Override
    public void removeSetBonus(Player player) {
        player.getPersistentData().putBoolean("mma_magic_set_bonus", false);
        (player).getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> m.getCastingResource().removeModifier("mma_magic_set_bonus"));
    }
}
