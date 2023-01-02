package de.joh.dragonmagicandrelics.events;

import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.commands.Commands;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import de.joh.dragonmagicandrelics.utils.RLoc;
import de.joh.dragonmagicandrelics.capabilities.secondchance.PlayerSecondChance;
import de.joh.dragonmagicandrelics.capabilities.secondchance.PlayerSecondChanceProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

/**
 * These event handlers take care of processing events which are on the server and client. (No damage events)
 * Functions marked with @SubscribeEvent are called by the forge event bus handler.
 */
@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEventHandler {

    /**
     * Processing of the jump upgrade.
     * @see ArmorUpgradeInit
     */
    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if(event.getEntityLiving() instanceof Player player){
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
            if (!chest.isEmpty() && chest.getItem() instanceof DragonMageArmor) {
                float boost = ((float)((DragonMageArmor)chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.JUMP, player)/10.0f);
                player.push((double)((float)(event.getEntityLiving().getDeltaMovement().x * boost)), boost, (double)((float)(event.getEntityLiving().getDeltaMovement().z * boost)));
            }
        }
    }

    /**
     * Registration of Commands
     * @see Commands
     */
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event){
        new Commands(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }

    /**
     * Saving the player's position for the Phoenix Ritual when they die
     * @see de.joh.dragonmagicandrelics.rituals.contexts.PhoenixRitual
     * @see PlayerSecondChance
     * @see PlayerSecondChanceProvider
     */
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getPlayer().getCapability(PlayerSecondChanceProvider.PLAYER_SECOND_CHANCE).ifPresent(secondChance -> {
                secondChance.setSecondChance(event.getOriginal());
            });
        }
    }

    /**
     * Adding capabilities to players
     */
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            if(!event.getObject().getCapability(PlayerSecondChanceProvider.PLAYER_SECOND_CHANCE).isPresent()) {
                event.addCapability(RLoc.create("properties"), new PlayerSecondChanceProvider());
            }
        }
    }

    /**
     * Registration of the Capabilities
     */
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerSecondChance.class);
    }



}
