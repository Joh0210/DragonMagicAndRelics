package de.joh.dmnr.common.armorupgrade;

import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.factions.Factions;
import de.joh.dmnr.api.armorupgrade.OnEquippedArmorUpgrade;
import de.joh.dmnr.common.init.ArmorUpgradeInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

/**
 * Upgrades of this type increase the Mama regeneration.
 * Function and constructor are initialized versions of parent class function/constructor.
 * @see ArmorUpgradeInit
 * @author Joh0210
 */
public class ManaRegenerationArmorUpgrade extends OnEquippedArmorUpgrade {
    public ManaRegenerationArmorUpgrade(@NotNull ResourceLocation registryName, int maxUpgradeLevel, RegistryObject<Item> upgradeSealItem, int upgradeCost) {
        super(registryName, maxUpgradeLevel, upgradeSealItem, true, upgradeCost);
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
