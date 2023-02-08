package de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonfullyequipped;

import com.ma.api.capabilities.Faction;
import com.ma.capabilities.playerdata.magic.PlayerMagicProvider;
import com.ma.capabilities.playerdata.progression.PlayerProgressionProvider;
import net.minecraft.entity.player.PlayerEntity;

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
    public void applySetBonus(PlayerEntity player, int level) {
        player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent((p) -> {
            if (p.getAlliedFaction() != Faction.UNDEAD) {
                player.getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> m.getCastingResource().addRegenerationModifier("mma_magic_set_bonus", -0.1F * (float)level));
            }
        });
    }

    @Override
    public void removeSetBonus(PlayerEntity player) {
        (player).getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> m.getCastingResource().removeRegenerationModifier("mma_magic_set_bonus"));
    }
}
