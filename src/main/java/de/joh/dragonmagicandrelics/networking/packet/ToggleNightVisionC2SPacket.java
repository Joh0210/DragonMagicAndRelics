package de.joh.dragonmagicandrelics.networking.packet;

import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

/**
 * This class is the package that is sent to the server when the TOGGLE_NIGHT_VISION_KEY is pressed.
 * @see de.joh.dragonmagicandrelics.utils.KeybindInit
 * @see de.joh.dragonmagicandrelics.events.ClientEvents
 * @author Joh0210
 */
public class ToggleNightVisionC2SPacket {
    public ToggleNightVisionC2SPacket(){}

    public ToggleNightVisionC2SPacket(FriendlyByteBuf buf){}

    public void toBytes(FriendlyByteBuf buf){}

    /**
     * Processing of the event on the SERVER SIDE!
     */
    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()->{
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();

            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);

            if(chest.getItem() instanceof DragonMageArmor mmaArmor && mmaArmor.getUpgradeLevel(ArmorUpgradeInit.NIGHT_VISION, player) >= 1){
                if(player.hasEffect(MobEffects.NIGHT_VISION)){
                    player.removeEffect(MobEffects.NIGHT_VISION);
                }else{
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 100000, 0, false, false, true));
                }
            }
        });
        return true;
    }
}
