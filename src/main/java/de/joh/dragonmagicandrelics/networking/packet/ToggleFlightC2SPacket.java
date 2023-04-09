package de.joh.dragonmagicandrelics.networking.packet;

import com.mna.ManaAndArtifice;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.effects.EffectInit;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * This class is the package that is sent to the server when the DISABLE_FLIGHT_KEY is pressed.
 * @see de.joh.dragonmagicandrelics.utils.KeybindInit
 * @see de.joh.dragonmagicandrelics.events.ClientEvents
 * @see EffectInit
 * @author Joh0210
 */
public class ToggleFlightC2SPacket {
    public ToggleFlightC2SPacket(){}

    public ToggleFlightC2SPacket(FriendlyByteBuf buf){}

    public void toBytes(FriendlyByteBuf buf){}

    /**
     * Processing of the event on the SERVER SIDE!
     */
    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()->{
            ServerPlayer player = context.getSender();

            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);

            if(player.hasEffect(EffectInit.FLY_DISABLED.get())){
                player.removeEffect(EffectInit.FLY_DISABLED.get());
            }else{
                player.addEffect(new MobEffectInstance(EffectInit.FLY_DISABLED.get(), 100000, 0, false, false, true));

                ManaAndArtifice.instance.proxy.setFlySpeed(player, 0.05F);
                ManaAndArtifice.instance.proxy.setFlightEnabled(player, false);
            }
        });
        return true;
    }
}
