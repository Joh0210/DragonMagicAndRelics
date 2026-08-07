package de.joh.dmnr.common.item;

import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.networking.ModMessages;
import de.joh.dmnr.networking.packet.SpawnDragonMageArmorParticleS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

/**
 * Item that drops from the Ender Dragon and is required for crafting
 * @author Joh0210
 */
public class DragonTotemItem extends Item {
    public DragonTotemItem() {
        super(new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1));
    }
    /**
     * Drops the Dragon Core when the Ender Dragon dies
     */
    public static void eventHandleDragonDeath(LivingDeathEvent event){
        if(event.getEntity().getType() == EntityType.ENDER_DRAGON && event.getSource().getEntity() instanceof Player player){
            boolean hasNoArmor = true;
            for (ItemStack armor : player.getArmorSlots()) {
                if (!armor.isEmpty()) {
                    hasNoArmor = false;
                    break;
                }
            }

            if (hasNoArmor && (player.getMainHandItem().getItem() == ItemInit.DRAGON_TOTEM.get() || player.getOffhandItem().getItem() == ItemInit.DRAGON_TOTEM.get())) {
                player.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ItemInit.DRAGON_MAGE_HELMET.get()));
                player.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ItemInit.DRAGON_MAGE_CHESTPLATE.get()));
                player.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ItemInit.DRAGON_MAGE_LEGGING.get()));
                player.setItemSlot(EquipmentSlot.FEET, new ItemStack(ItemInit.DRAGON_MAGE_BOOTS.get()));

                if (player instanceof ServerPlayer serverPlayer) {
                    ModMessages.sendToPlayer(new SpawnDragonMageArmorParticleS2CPacket(player.getId()), serverPlayer);
                }
            }
        }
    }
}
