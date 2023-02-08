package de.joh.dragonmagicandrelics.events;

import de.joh.dragonmagicandrelics.Commands;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
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
        if(event.getEntityLiving() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            ItemStack chest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
            if (player.isSprinting() && !chest.isEmpty() && chest.getItem() instanceof DragonMageArmor) {
                float boost = ((float)((DragonMageArmor)chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.JUMP, player)/10.0f);
                player.addVelocity((float)(event.getEntityLiving().getMotion().x * boost), boost*2, (float)(event.getEntityLiving().getMotion().z * boost));
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
}
