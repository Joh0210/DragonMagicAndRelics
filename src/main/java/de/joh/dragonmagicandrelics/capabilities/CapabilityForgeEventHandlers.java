package de.joh.dragonmagicandrelics.capabilities;

import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.capabilities.dragonmagic.PlayerDragonMagic;
import de.joh.dragonmagicandrelics.capabilities.dragonmagic.PlayerDragonMagicProvider;
import de.joh.dragonmagicandrelics.utils.RLoc;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilityForgeEventHandlers {
    public CapabilityForgeEventHandlers() {
    }

    @SubscribeEvent
    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(RLoc.create("dragon_magic"), new PlayerDragonMagicProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player player = event.getPlayer();
        Player original = event.getOriginal();
        original.reviveCaps();
        player.getCapability(PlayerDragonMagicProvider.PLAYER_DRAGON_MAGIC).ifPresent((magic) -> {
            original.getCapability(PlayerDragonMagicProvider.PLAYER_DRAGON_MAGIC).ifPresent((oldMagic) -> {
                magic.copyFrom(oldMagic);
            });
        });
        event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerDragonMagic.class);
    }
}
