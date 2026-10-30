package de.joh.dmnr.common.item;

import de.joh.dmnr.common.init.EffectInit;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.common.util.CommonConfig;
import de.joh.dmnr.common.util.FactionArmorColors;
import de.joh.dmnr.networking.ModMessages;
import de.joh.dmnr.networking.packet.SpawnDragonMageArmorParticleS2CPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Item that drops from the Ender Dragon and is required for crafting
 * @author Joh0210
 */
public class DragonTotemItem extends Item {
    public DragonTotemItem() {
        super(new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1));
    }
    /**
     * Equips the Dragon Mage Armor when a player without armor kills the Ender Dragon while holding the Dragon Totem.
     * The armor is dyed in the colors of the faction of the player.
     * @see FactionArmorColors
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
                InteractionHand hand = getTotemHand(player);

                if (hand != null) {
                    FactionArmorColors.Colors colors = FactionArmorColors.getColors(player);
                    player.setItemSlot(EquipmentSlot.HEAD, dyedArmor(ItemInit.DRAGON_MAGE_HELMET.get(), colors));
                    player.setItemSlot(EquipmentSlot.CHEST, dyedArmor(ItemInit.DRAGON_MAGE_CHESTPLATE.get(), colors));
                    player.setItemSlot(EquipmentSlot.LEGS, dyedArmor(ItemInit.DRAGON_MAGE_LEGGING.get(), colors));
                    player.setItemSlot(EquipmentSlot.FEET, dyedArmor(ItemInit.DRAGON_MAGE_BOOTS.get(), colors));

                    player.getItemInHand(hand).shrink(1);
                    playTransformation(player);
                }
            }
        }
    }

    private static ItemStack dyedArmor(Item armor, FactionArmorColors.Colors colors) {
        ItemStack stack = new ItemStack(armor);
        DragonMageArmorItem.setColors(stack, colors.primary(), colors.secondary());
        return stack;
    }

    public static void eventHandleCursedKill(LivingDeathEvent event){
        if(event.getSource().getEntity() instanceof Player player && event.getEntity() != player){
            InteractionHand hand = getTotemHand(player);

            if (hand != null && countHarmfulEffects(event.getEntity(), null) >= CommonConfig.DRAGON_TOTEM_RING_HARMFUL_EFFECTS.get()) {
                transformTotem(player, hand, new ItemStack(ItemInit.RING_OF_POWER.get()));
            }
        }
    }

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
                InteractionHand hand = getTotemHand(player);

                if (hand != null) {
                    transformTotem(player, hand, new ItemStack(ItemInit.AMULET_OF_DRAGON_POWER.get()));
                }
            }
        }
    }

    public static void eventHandleEnduredCurses(MobEffectEvent.Added event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide()) {
            MobEffectInstance addedEffect = event.getEffectInstance();
            if (isCountedHarmful(addedEffect.getEffect())) {
                InteractionHand hand = getTotemHand(player);

                // The event is fired before the new effect is stored, so it is counted separately
                if (hand != null && countHarmfulEffects(player, addedEffect.getEffect()) + 1 >= CommonConfig.DRAGON_TOTEM_RING_HARMFUL_EFFECTS.get()) {
                    transformTotem(player, hand, new ItemStack(ItemInit.RING_OF_RULING.get()));
                }
            }
        }
    }

    @Nullable
    private static InteractionHand getTotemHand(Player player) {
        if (player.getMainHandItem().getItem() == ItemInit.DRAGON_TOTEM.get()) {
            return InteractionHand.MAIN_HAND;
        }
        if (player.getOffhandItem().getItem() == ItemInit.DRAGON_TOTEM.get()) {
            return InteractionHand.OFF_HAND;
        }
        return null;
    }

    private static void transformTotem(Player player, InteractionHand hand, ItemStack result) {
        player.setItemInHand(hand, result);
        playTransformation(player);
    }

    private static void playTransformation(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
            ModMessages.sendToPlayer(new SpawnDragonMageArmorParticleS2CPacket(player.getId()), serverPlayer);
        }
    }

    private static int countHarmfulEffects(LivingEntity entity, @Nullable MobEffect ignored) {
        return (int) entity.getActiveEffects().stream()
                .map(MobEffectInstance::getEffect)
                .filter(effect -> effect != ignored && isCountedHarmful(effect))
                .count();
    }

    private static boolean isCountedHarmful(@NotNull MobEffect effect) {
        return effect.getCategory() == MobEffectCategory.HARMFUL && effect != EffectInit.SPELL_STORING_COOLDOWN.get();
    }
}
