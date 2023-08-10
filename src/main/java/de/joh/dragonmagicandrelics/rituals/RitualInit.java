package de.joh.dragonmagicandrelics.rituals;

import com.mna.api.rituals.RitualEffect;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.rituals.contexts.*;
import de.joh.dragonmagicandrelics.utils.RLoc;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Register all rituals. Call via the event bus.
 * @author Joh0210
 */
@Mod.EventBusSubscriber(modid= DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RitualInit {
    @SubscribeEvent
    public static void registerRitualEffects(RegistryEvent.Register<RitualEffect> event) {
        event.getRegistry().registerAll(
            new DragonMageArmorRitual(RLoc.create("rituals/dragon_mage_ritual")).setRegistryName(RLoc.create("ritual-dragon_mage_ritual")),
            new UpgradeRitual(RLoc.create("rituals/upgrade_ritual")).setRegistryName(RLoc.create("ritual-upgrade-ritual")),
            new PhoenixRitual(RLoc.create("rituals/phoenix_ritual")).setRegistryName(RLoc.create("ritual-phoenix-ritual")),
            new BetrayalRitual(RLoc.create("rituals/betrayal_ritual")).setRegistryName(RLoc.create("ritual-betrayal-ritual"))
        );
    }
}
