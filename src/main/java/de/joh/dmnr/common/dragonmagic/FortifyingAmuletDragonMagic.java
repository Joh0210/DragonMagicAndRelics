package de.joh.dmnr.common.dragonmagic;

import com.mna.items.ItemInit;
import de.joh.dmnr.common.util.CommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

/**
 * Dragon Magic of the Fortifying Amulet of Mana and Artifice, which saves its wearer from death once per full charge
 * and is then on cooldown for 10 minutes.
 * <br> Dragon Magic: the cooldown only lasts {@link CommonConfig#FORTIFYING_AMULET_DM_COOLDOWN} seconds (1 minute by
 * default). Every rescue still needs the full charge of the amulet.
 * @author Joh0210
 */
public class FortifyingAmuletDragonMagic extends ExternalDragonMagic {
    private static final float COOLDOWN_JUST_STARTED = 0.99f;

    public FortifyingAmuletDragonMagic() {
        super(new ResourceLocation("mna", "crit_immune"));
    }

    @Override
    public boolean hasDragonMagicGlint(ItemStack itemStack) {
        // The amulet glints itself while it is fully charged, i.e. ready to save its wearer
        return false;
    }

    public static void eventHandleCooldownReduction(LivingDeathEvent event) {
        if (event.isCanceled()
                && event.getEntity() instanceof Player player
                && ExternalDragonMagicInit.FORTIFYING_AMULET.hasDragonMagic(player)) {
            Item amulet = ItemInit.CRIT_IMMUNITY_AMULET.get();
            ItemCooldowns cooldowns = player.getCooldowns();
            if (cooldowns.getCooldownPercent(amulet, 0) > COOLDOWN_JUST_STARTED) {
                int cooldown = CommonConfig.FORTIFYING_AMULET_DM_COOLDOWN.get() * 20;
                if (cooldown > 0) {
                    cooldowns.addCooldown(amulet, cooldown);
                } else {
                    cooldowns.removeCooldown(amulet);
                }
            }
        }
    }
}
