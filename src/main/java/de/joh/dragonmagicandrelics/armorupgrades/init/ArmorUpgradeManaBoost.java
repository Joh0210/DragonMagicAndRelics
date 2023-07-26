package de.joh.dragonmagicandrelics.armorupgrades.init;

import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import de.joh.dragonmagicandrelics.armorupgrades.types.IArmorUpgradeOnEquipped;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * If upgrades of this type are installed, the player receives a certain number of additional Mana.
 * Function and constructor are initialized versions of parent class function/constructor.
 * @see de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit
 * @author Joh0210
 */
public class ArmorUpgradeManaBoost  extends IArmorUpgradeOnEquipped {
    public ArmorUpgradeManaBoost(@NotNull ResourceLocation registryName, int maxUpgradeLevel, int upgradeCost) {
        super(registryName, maxUpgradeLevel, true, upgradeCost);
    }
    public static int MANA_PER_MANABOOST = 200;

    @Override
    public void onEquip(Player player, int level) {
        player.getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> m.getCastingResource().addModifier("mma_magic_set_bonus", MANA_PER_MANABOOST * level));
    }

    @Override
    public void onRemove(Player player) {
        (player).getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> m.getCastingResource().removeModifier("mma_magic_set_bonus"));
    }
}
