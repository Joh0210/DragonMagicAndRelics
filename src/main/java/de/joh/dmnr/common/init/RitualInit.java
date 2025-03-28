package de.joh.dmnr.common.init;

import com.mna.Registries;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.ritual.*;
import de.joh.dmnr.common.util.RLoc;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

/**
 * Register all rituals. Call via the event bus.
 * @author Joh0210
 */
@Mod.EventBusSubscriber(modid= DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RitualInit {
    @SubscribeEvent
    public static void registerRitualEffects(RegisterEvent event) {
        event.register(Registries.RitualEffect.get().getRegistryKey(), (helper) -> {
            helper.register(RLoc.create("ritual-dragon_mage_ritual"), new DragonMageArmorRitual(RLoc.create("rituals/dragon_mage_ritual")));
            helper.register(RLoc.create("ritual-betrayal-ritual"), new BetrayalRitual(RLoc.create("rituals/betrayal_ritual")));
            helper.register(RLoc.create("ritual-artefact-trade"), new RelictTradeRitual(RLoc.create("rituals/artefact_trade")));
        });
    }
}
