package de.joh.dragonmagicandrelics;

import com.mna.api.guidebook.RegisterGuidebooksEvent;
import com.mojang.logging.LogUtils;
import de.joh.dragonmagicandrelics.effects.EffectInit;
import de.joh.dragonmagicandrelics.item.ItemInit;
import de.joh.dragonmagicandrelics.networking.ModMessages;
import de.joh.dragonmagicandrelics.utils.KeybindInit;
import de.joh.dragonmagicandrelics.utils.RLoc;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

/**
 * Main class of this mod, and initialization of some elements.
 * @author Joh0210
 */
@Mod(DragonMagicAndRelics.MOD_ID)
public class DragonMagicAndRelics {
    public static final String MOD_ID = "dragonmagicandrelics";
    public static final Logger LOGGER = LogUtils.getLogger();

    public DragonMagicAndRelics() {

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemInit.register(eventBus);
        EffectInit.register(eventBus);

        eventBus.addListener(this::setup);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> eventBus.register(KeybindInit.class));

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event){
        event.enqueueWork(ModMessages::register);

        LOGGER.info(MOD_ID + ": init");
    }

    /**
     * Registration of the ingame guide. Called by the game itself.
     */
    @SubscribeEvent
    public void onRegisterGuidebooks(final RegisterGuidebooksEvent event) {
        event.getRegistry().addGuidebookPath(RLoc.create("guide"));
    }
}
