package de.joh.dmnr.common.item;

import de.joh.dmnr.common.init.EffectInit;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.networking.ModMessages;
import de.joh.dmnr.networking.packet.SpawnDragonMageArmorParticleS2CPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;

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

            if (hasNoArmor) {
                ItemStack mainHand = player.getMainHandItem();
                ItemStack offHand = player.getOffhandItem();
                boolean totemInMain = mainHand.getItem() == ItemInit.DRAGON_TOTEM.get();
                boolean totemInOff = offHand.getItem() == ItemInit.DRAGON_TOTEM.get();

                if (totemInMain || totemInOff) {
                    player.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ItemInit.DRAGON_MAGE_HELMET.get()));
                    player.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ItemInit.DRAGON_MAGE_CHESTPLATE.get()));
                    player.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ItemInit.DRAGON_MAGE_LEGGING.get()));
                    player.setItemSlot(EquipmentSlot.FEET, new ItemStack(ItemInit.DRAGON_MAGE_BOOTS.get()));

                    if (totemInMain) {
                        mainHand.shrink(1);
                    } else {
                        offHand.shrink(1);
                    }

                    if (player instanceof ServerPlayer serverPlayer) {
                        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                        ModMessages.sendToPlayer(new SpawnDragonMageArmorParticleS2CPacket(player.getId()), serverPlayer);
                    }
                }
            }
        }
    }

    /**
     * Replaces all Dragon Totems in the player's inventory with empty totems when the player dies.
     */
    public static void eventHandlePlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack.getItem() == ItemInit.DRAGON_TOTEM.get()) {
                    int count = stack.getCount();
                    player.getInventory().setItem(i, new ItemStack(ItemInit.DRAGON_TOTEM_EMPTY.get(), count));
                }
            }
        }
    }

    public static void eventHandleSorcerersPride(MobEffectEvent.Added event) {
        if (event.getEntity() instanceof Player player) {
            MobEffectInstance prideEffect = event.getEffectInstance();
            if (prideEffect.getEffect() == EffectInit.SORCERERS_PRIDE.get() && prideEffect.getAmplifier() >= 4) {
                ItemStack mainHand = player.getMainHandItem();
                ItemStack offHand = player.getOffhandItem();
                boolean totemInMain = mainHand.getItem() == ItemInit.DRAGON_TOTEM.get();
                boolean totemInOff = offHand.getItem() == ItemInit.DRAGON_TOTEM.get();

                if (totemInMain || totemInOff) {
                    if (totemInMain) {
                        mainHand.shrink(1);
                        player.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemInit.AMULET_OF_DRAGON_POWER.get()));
                    } else {
                        offHand.shrink(1);
                        player.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ItemInit.AMULET_OF_DRAGON_POWER.get()));
                    }

                    if (player instanceof ServerPlayer serverPlayer) {
                        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                        ModMessages.sendToPlayer(new SpawnDragonMageArmorParticleS2CPacket(player.getId()), serverPlayer);
                    }
                }
            }
        }
    }
}
