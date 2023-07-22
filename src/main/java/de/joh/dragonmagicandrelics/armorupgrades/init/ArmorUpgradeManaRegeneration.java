package de.joh.dragonmagicandrelics.armorupgrades.init;

import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.factions.Factions;
import de.joh.dragonmagicandrelics.armorupgrades.types.IArmorUpgradeOnEquipped;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Upgrades of this type increase the Mama regeneration.
 * Function and constructor are initialized versions of parent class function/constructor.
 * @see de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit
 * @author Joh0210
 */
public class ArmorUpgradeManaRegeneration extends IArmorUpgradeOnEquipped {
    public ArmorUpgradeManaRegeneration(@NotNull ResourceLocation registryName, int maxUpgradeLevel) {
        super(registryName, maxUpgradeLevel);
    }

    @Override
    public void onEquip(Player player, int level) {
        player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent((p) -> {
            if (p.getAlliedFaction() != Factions.UNDEAD) {
                player.getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> m.getCastingResource().addRegenerationModifier("mma_magic_set_bonus", -0.1F * (float)level));
            }
        });
    }

    @Override
    public void onRemove(Player player) {
        (player).getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> m.getCastingResource().removeRegenerationModifier("mma_magic_set_bonus"));
    }
}
