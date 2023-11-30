package de.joh.dmnr.common.armorupgrade;

import de.joh.dmnr.api.armorupgrade.ArmorUpgrade;
import de.joh.dmnr.api.item.DragonMageArmorItem;
import de.joh.dmnr.common.init.KeybindInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Allows the armor wearer to gain night vision with a button press.
 * Increasing the maximum level has no effect without further adjustments.
 * @see KeybindInit
 * @see DragonMageArmorItem
 * @see de.joh.dmnr.networking.packet.ToggleNightVisionC2SPacket
 * @author Joh0210
 */
public class NightVisionArmorUpgrade extends ArmorUpgrade {
    /**
     * @param registryName    ID under which the upgrade can be recognized.
     */
    public NightVisionArmorUpgrade(@NotNull ResourceLocation registryName, int upgradeCost) {
        super(registryName, 1, false, upgradeCost);
    }

    @Override
    public void onRemove(Player player) {
        player.removeEffect(MobEffects.NIGHT_VISION);
    }
}
