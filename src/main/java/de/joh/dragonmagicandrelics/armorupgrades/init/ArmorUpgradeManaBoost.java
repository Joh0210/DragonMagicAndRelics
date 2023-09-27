package de.joh.dragonmagicandrelics.armorupgrades.init;

import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.armorupgrades.types.ArmorUpgrade;
import de.joh.dragonmagicandrelics.armorupgrades.types.ArmorUpgradeOnEquipped;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * If upgrades of this type are installed, the player receives a certain number of additional Mana.
 * Function and constructor are initialized versions of parent class function/constructor.
 * @see de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit
 * @author Joh0210
 */
public class ArmorUpgradeManaBoost  extends ArmorUpgradeOnEquipped {
    public ArmorUpgradeManaBoost(@NotNull ResourceLocation registryName, boolean isMajor, int upgradeCost) {
        super(registryName, 4, true, upgradeCost);
        this.isMajor = isMajor;
    }
    public static final int MINOR_MANA_PER_MANABOOST = 100;
    public static final int MAJOR_MANA_PER_MANABOOST = 250;
    public final boolean isMajor;

    @Override
    public @Nullable ArmorUpgrade getStrongerAlternative() {
        return isMajor ? null : ArmorUpgradeInit.MAJOR_MANA_BOOST;
    }

    @Override
    public void onEquip(Player player, int level) {
        player.getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> m.getCastingResource().addModifier("mma_magic_set_bonus" + (isMajor ? "1" : ""), (isMajor ? MAJOR_MANA_PER_MANABOOST : MINOR_MANA_PER_MANABOOST) * level));
    }

    @Override
    public void onRemove(Player player) {
        (player).getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> m.getCastingResource().removeModifier("mma_magic_set_bonus" + (isMajor ? "1" : "")));
    }
}
