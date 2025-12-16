package de.joh.dmnr.common.item;

import com.mna.api.items.TieredItem;
import com.mna.interop.CuriosInterop;
import com.mna.tools.InventoryUtilities;
import de.joh.dmnr.common.init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Teleports the player back to the spawn if he has fallen into the void and has too few lives
 * @author Joh0210
 */
public class VoidfeatherCharmItem extends TieredItem {

    public VoidfeatherCharmItem(Properties itemProperties) {
        super(itemProperties);
    }

    public boolean consume(ServerPlayer player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() != this) {
            return false;
        } else {
            stack.hurtAndBreak(1, player, (e) -> e.broadcastBreakEvent(hand));
            return stack.isEmpty() || player.isCreative();
        }
    }

    public boolean consume(ServerPlayer player) {
        return InventoryUtilities.removeItemFromInventory(new ItemStack(this), true, true, new InvWrapper(player.getInventory()));
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("item.dmnr.voidfeather_charm.description"));
    }

    /**
     * Teleports the player back to the spawn if he has fallen into the void and has too few lives (if charm is equipped)
     */
    public static boolean eventHandleVoidProtection(LivingAttackEvent event) {
        if(event.getEntity() instanceof Player player){
            if (!player.isCreative() && !player.isSpectator() && !player.level().isClientSide) {
                ServerPlayer spe = (ServerPlayer)player;


                if (event.getSource().is(DamageTypes.FELL_OUT_OF_WORLD) && player.getHealth() - event.getAmount() <= 10) {
                    MinecraftServer server = player.level().getServer();
                    if (server != null){
                        boolean consumed_charm = false;
                        BlockPos bedPos = spe.getRespawnPosition();
                        if (bedPos == null) {
                            ServerLevel level = server.getLevel(player.level().dimension());
                            if(level != null){
                                bedPos = level.getSharedSpawnPos();
                            }
                        }

                        if(bedPos != null){
                            Optional<SlotResult> result = CuriosInterop.GetSingleItem(player, ItemInit.VOIDFEATHER_CHARM.get());
                            if (result.isPresent()) {
                                consumed_charm = true;
                                result.get().stack().hurtAndBreak(999, spe, (damager) -> CuriosApi.broadcastCurioBreakEvent(result.get().slotContext()));
                            }
                            else if (InventoryUtilities.removeItemFromInventory(new ItemStack(ItemInit.VOIDFEATHER_CHARM.get(), 1), true, true, new InvWrapper(player.getInventory()))) {
                                consumed_charm = true;
                            }

                            if (consumed_charm) {
                                event.setCanceled(true);
                                player.resetFallDistance();


                                player.level().playSeededSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1F, 0.9F + (float)Math.random() * 0.2F, 0);

                                final double bed_x = (double)bedPos.getX() + 0.5;
                                final double bed_y = (double)bedPos.getY() + 0.5;
                                final double bed_z = (double)bedPos.getZ() + 0.5;

                                ServerLevel homePlane = server.getLevel(spe.getRespawnDimension());
                                if (homePlane != null && !player.isPassenger() && spe.level().dimension() != spe.getRespawnDimension()) {
                                    spe.changeDimension(homePlane, new net.minecraftforge.common.util.ITeleporter() {
                                        @Override
                                        public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destinationWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
                                            entity = repositionEntity.apply(false);
                                            entity.setPos(bed_x, bed_y, bed_z);
                                            entity.level().broadcastEntityEvent(spe, (byte)46);
                                            return entity;
                                        }
                                    });
                                }
                                // ensures the player is teleported to the correct coordinates
                                player.teleportTo(bed_x,bed_y,bed_z);

                                player.level().playSeededSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1F, 0.9F + (float)Math.random() * 0.2F, 0);
                                player.level().broadcastEntityEvent(spe, (byte)46);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
