package de.joh.dmnr.common.armorupgrade;

import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import de.joh.dmnr.common.init.ArmorUpgradeInit;
import de.joh.dmnr.api.armorupgrade.ArmorUpgrade;
import de.joh.dmnr.api.armorupgrade.OnEquippedArmorUpgrade;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * If upgrades of this type are installed, the player receives a certain number of additional Mana.
 * Function and constructor are initialized versions of parent class function/constructor.
 * @see ArmorUpgradeInit
 * @author Joh0210
 */
public class ManaBoostArmorUpgrade extends OnEquippedArmorUpgrade {
    public ManaBoostArmorUpgrade(@NotNull ResourceLocation registryName, boolean isMajor, int upgradeCost) {
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
