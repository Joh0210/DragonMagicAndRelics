package de.joh.dmnr.armorupgrades.init;

import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.factions.Factions;
import de.joh.dmnr.armorupgrades.types.ArmorUpgradeOnEquipped;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Upgrades of this type increase the Mama regeneration.
 * Function and constructor are initialized versions of parent class function/constructor.
 * @see de.joh.dmnr.armorupgrades.ArmorUpgradeInit
 * @author Joh0210
 */
public class ArmorUpgradeManaRegeneration extends ArmorUpgradeOnEquipped {
    public ArmorUpgradeManaRegeneration(@NotNull ResourceLocation registryName, int maxUpgradeLevel, int upgradeCost) {
        super(registryName, maxUpgradeLevel, true, upgradeCost);
    }

    @Override
    public void onEquip(Player player, int level) {
        player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent((p) -> {
            if (p.getAlliedFaction() != Factions.UNDEAD) {
                player.getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> m.getCastingResource().addRegenerationModifier("mma_magic_set_bonus", -0.15F * (float)level));
            }
        });
    }

    @Override
    public void onRemove(Player player) {
        (player).getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> m.getCastingResource().removeRegenerationModifier("mma_magic_set_bonus"));
    }
}