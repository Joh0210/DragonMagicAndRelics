package de.joh.dragonmagicandrelics.networking.packet;

import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * This class is the package that is sent to the server when the TOGGLE_NIGHT_VISION_KEY is pressed.
 * @see de.joh.dragonmagicandrelics.utils.KeybindInit
 * @see de.joh.dragonmagicandrelics.events.ClientEvents
 * @author Joh0210
 */
public class ToggleNightVisionC2SPacket {
    public ToggleNightVisionC2SPacket(){}

    public ToggleNightVisionC2SPacket(PacketBuffer buf){}

    public void toBytes(PacketBuffer buf){}

    /**
     * Processing of the event on the SERVER SIDE!
     */
    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()->{
            ServerPlayerEntity player = context.getSender();

            ItemStack chest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);

            if(chest.getItem() instanceof DragonMageArmor && ((DragonMageArmor) chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.NIGHT_VISION, player) >= 1){
                if(player.getActivePotionEffect(Effects.NIGHT_VISION) != null){
                    player.removePotionEffect(Effects.NIGHT_VISION);
                }else{
                    player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 100000, 0, false, false, true));
                }
            }
        });
        return true;
    }
}
