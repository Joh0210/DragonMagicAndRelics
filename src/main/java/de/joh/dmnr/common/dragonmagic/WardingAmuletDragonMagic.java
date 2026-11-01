package de.joh.dmnr.common.dragonmagic;

import com.mna.items.ItemInit;
import de.joh.dmnr.common.util.CommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

/**
 * Dragon Magic of the Warding Amulet of Mana and Artifice, which reduces magic damage by 20% itself.
 * <br> Dragon Magic: reduces magic damage additionally by the percentage set in
 * {@link CommonConfig#WARDING_AMULET_DM_DAMAGE_REDUCTION} (25% by default, 40% in total).
 * @author Joh0210
 */
public class WardingAmuletDragonMagic extends ExternalDragonMagic {
    public WardingAmuletDragonMagic() {
        super(new ResourceLocation("mna", "warding_amulet"));
    }

    /**
     * Reduces magic damage further while the Warding Amulet protects its wearer. Uses the same conditions as Mana and
     * Artifice: damage of the damage type tag {@code minecraft:witch_resistant_to} and an amulet with charge left.
     * <br> Must run before Mana and Artifice, which consumes the charge of the amulet in the same event.
     */
    public static void eventHandleMagicProtection(LivingDamageEvent event) {
        int reduction = CommonConfig.WARDING_AMULET_DM_DAMAGE_REDUCTION.get();
        if (reduction > 0
                && event.getEntity() instanceof Player player
                && event.getSource().is(DamageTypeTags.WITCH_RESISTANT_TO)
                && ItemInit.WARDING_AMULET.get().isEquippedAndHasMana(player, 1.0f, false)
                && ExternalDragonMagicInit.WARDING_AMULET.hasDragonMagic(player)) {
            event.setAmount(event.getAmount() * (1 - reduction / 100f));
        }
    }
}
