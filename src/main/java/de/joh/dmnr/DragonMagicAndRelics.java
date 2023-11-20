package de.joh.dmnr;

import com.mna.api.guidebook.RegisterGuidebooksEvent;
import com.mojang.logging.LogUtils;
import de.joh.dmnr.block.BlockInit;
import de.joh.dmnr.block.entity.BlockEntitieInit;
import de.joh.dmnr.config.CommonConfigs;
import de.joh.dmnr.effects.EffectInit;
import de.joh.dmnr.item.ItemInit;
import de.joh.dmnr.networking.ModMessages;
import de.joh.dmnr.utils.KeybindInit;
import de.joh.dmnr.utils.RLoc;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

/**
 * Main class of this mod, and initialization of some elements.
 * @author Joh0210
 */
@Mod(DragonMagicAndRelics.MOD_ID)
public class DragonMagicAndRelics {
    public static final String MOD_ID = "dmnr";
    public static DragonMagicAndRelics instance;
    public static final Logger LOGGER = LogUtils.getLogger();

    public DragonMagicAndRelics() {
        instance = this;

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemInit.register(eventBus);
        BlockInit.register(eventBus);
        EffectInit.register(eventBus);
        BlockEntitieInit.register(eventBus);

        eventBus.addListener(this::setup);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> eventBus.register(KeybindInit.class));

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfigs.SPEC, MOD_ID+"-common.toml");

        MinecraftForge.EVENT_BUS.register(this);
    }

    public final Player getClientPlayer() {
        return Minecraft.getInstance().player;
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
