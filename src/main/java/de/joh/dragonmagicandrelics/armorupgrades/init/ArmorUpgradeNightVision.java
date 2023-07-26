package de.joh.dragonmagicandrelics.armorupgrades.init;

import de.joh.dragonmagicandrelics.armorupgrades.types.ArmorUpgrade;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Allows the armor wearer to gain night vision with a button press.
 * Increasing the maximum level has no effect without further adjustments.
 * @see de.joh.dragonmagicandrelics.utils.KeybindInit
 * @see DragonMageArmor
 * @see de.joh.dragonmagicandrelics.networking.packet.ToggleNightVisionC2SPacket
 * @author Joh0210
 */
public class ArmorUpgradeNightVision extends ArmorUpgrade {
    /**
     * @param registryName    ID under which the upgrade can be recognized.
     */
    public ArmorUpgradeNightVision(@NotNull ResourceLocation registryName, int upgradeCost) {
        super(registryName, 1, false, upgradeCost);
    }

    @Override
    public void onRemove(Player player) {
        player.removeEffect(MobEffects.NIGHT_VISION);
    }
}
