package de.joh.dragonmagicandrelics;

import com.ma.guide.GuideBookEntries;
import de.joh.dragonmagicandrelics.config.CommonConfigs;
import de.joh.dragonmagicandrelics.config.InitialUpgradesConfigs;
import de.joh.dragonmagicandrelics.effects.EffectInit;
import de.joh.dragonmagicandrelics.item.ItemInit;
import de.joh.dragonmagicandrelics.networking.ModMessages;
import de.joh.dragonmagicandrelics.utils.KeybindInit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main class of this mod, and initialization of some elements.
 * @author Joh0210
 */
@Mod(DragonMagicAndRelics.MOD_ID)
public class DragonMagicAndRelics {
    public static final String MOD_ID = "dragonmagicandrelics";
    public static final Logger LOGGER = LogManager.getLogger();

    public DragonMagicAndRelics() {

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemInit.register(eventBus);
        EffectInit.register(eventBus);

        eventBus.addListener(this::setup);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            MinecraftForge.EVENT_BUS.register(GuideBookEntries.class);
            eventBus.register(KeybindInit.class);
        });

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> eventBus.register(KeybindInit.class));

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfigs.SPEC, MOD_ID+"-common.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, InitialUpgradesConfigs.SPEC, MOD_ID+"-initial-upgrades.toml");

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event){
        event.enqueueWork(ModMessages::register);

        LOGGER.info(MOD_ID + ": init");
    }
}
