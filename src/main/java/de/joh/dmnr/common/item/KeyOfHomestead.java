package de.joh.dmnr.common.item;

import com.mna.api.items.IRelic;
import com.mna.tools.TeleportHelper;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.init.ItemInit;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A key that allows the user to teleport to its spawn point/bad and back
 * @author Joh0210
 */
public class KeyOfHomestead extends Item implements IRelic {
    private static final String RESET_TAG = DragonMagicAndRelics.MOD_ID + "_reset";
    private static final String TP_TAG_X = DragonMagicAndRelics.MOD_ID + "_tp_x";
    private static final String TP_TAG_Y = DragonMagicAndRelics.MOD_ID + "_tp_y";
    private static final String TP_TAG_Z = DragonMagicAndRelics.MOD_ID + "_tp_z";
    private static final String TP_TAG_LEVEL = DragonMagicAndRelics.MOD_ID + "_tp_level";

    public KeyOfHomestead() {
        super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC));
    }

    public static void onPlayerLeftClick(PlayerInteractEvent event){
        ItemStack heldItem = event.getEntity().getMainHandItem();

        if (heldItem.getItem() == ItemInit.KEY_OF_HOMESTEAD.get()) {
            KeyOfHomestead.cancelReset(heldItem);
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player user, @NotNull InteractionHand hand) {
        InteractionResultHolder<ItemStack> ar = super.use(world, user, hand);
        ItemStack stack = user.getItemInHand(hand);

        if(world.getServer() != null && stack != ItemStack.EMPTY) {

            if(user.isShiftKeyDown()) {
                if(this.targetsBack(stack)){
                    user.getCooldowns().addCooldown(this, 30);
                    this.manuelReset(user, stack);
                }
            }

            else {
                KeyOfHomestead.cancelReset(stack);
                user.getCooldowns().addCooldown(this, 100);
                if(this.targetsBack(stack)){
                    this.tpBack(user, stack);
                    this.removeBackV3(stack);
                }
                else {
                    this.saveBackV3(user, stack);
                    this.tpHome(user);
                }

            }
        }

        return ar;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack pStack) {
        return targetsBack(pStack);
    }

    private void manuelReset(@NotNull Player user, @NotNull ItemStack stack) {
        CompoundTag tag = new CompoundTag();
        if(stack.getTag() != null){
            tag = stack.getTag();
        }

        if(tag.getBoolean(RESET_TAG)){
            this.removeBackV3(stack);
            tag.putBoolean(RESET_TAG, false);
        }
        else {
            user.displayClientMessage(Component.translatable("item.dmnr.key_of_homestead.remove"),true);
            tag.putBoolean(RESET_TAG, true);
        }

        stack.setTag(tag);
    }

    private static void cancelReset(@NotNull ItemStack stack) {
        CompoundTag tag = new CompoundTag();
        if(stack.getTag() != null){
            tag = stack.getTag();
        }
        tag.putBoolean(RESET_TAG, false);
        stack.setTag(tag);
    }

    private boolean targetsBack(@NotNull ItemStack stack){
        CompoundTag tag = stack.getTag();
        return  tag != null && tag.contains(TP_TAG_X) && tag.contains(TP_TAG_Y) && tag.contains(TP_TAG_Z) && tag.contains(TP_TAG_LEVEL);
    }

    private void saveBackV3(@NotNull Player user, @NotNull ItemStack stack){
        CompoundTag tag = new CompoundTag();
        if(stack.getTag() != null){
            tag = stack.getTag();
        }

        tag.putDouble(TP_TAG_X, user.getX());
        tag.putDouble(TP_TAG_Y, user.getY());
        tag.putDouble(TP_TAG_Z, user.getZ());
        ResourceKey<Level> targetWorld = user.level().dimension();
        tag.putString(TP_TAG_LEVEL, targetWorld.location().toString());

        stack.setTag(tag);
    }

    private void removeBackV3(@NotNull ItemStack stack){
        CompoundTag tag = new CompoundTag();
        if(stack.getTag() != null){
            tag = stack.getTag();
        }

        tag.remove(TP_TAG_X);
        tag.remove(TP_TAG_Y);
        tag.remove(TP_TAG_Z);
        tag.remove(TP_TAG_LEVEL);

        stack.setTag(tag);
    }


    private void tpBack(@NotNull Player user, @NotNull ItemStack stack) {

        CompoundTag tag = stack.getTag();
        if(!user.level().isClientSide() && tag != null && tag.contains(TP_TAG_X) && tag.contains(TP_TAG_Y) && tag.contains(TP_TAG_Z) && tag.contains(TP_TAG_LEVEL)){
            double cordX = tag.getDouble(TP_TAG_X);
            double cordY = tag.getDouble(TP_TAG_Y);
            double cordZ = tag.getDouble(TP_TAG_Z);
            String dimensionString = tag.getString(TP_TAG_LEVEL);
            ResourceLocation resourceLocation = new ResourceLocation(dimensionString);
            ResourceKey<Level> targetWorld = ResourceKey.create(Registries.DIMENSION, resourceLocation);

            MinecraftServer server = user.getServer();
            if (server != null && server.getLevel(targetWorld) != null) {

                user.level().playSeededSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1F, 0.9F + (float)Math.random() * 0.2F, 0);
                TeleportHelper.teleportEntity(user, targetWorld, new Vec3(cordX, cordY, cordZ));
                return;
            }
        }

        user.displayClientMessage(Component.translatable("item.dmnr.key_of_homestead.no_return"),true);
    }

    private void tpHome(@NotNull Player user) {
        ServerPlayer spe = (ServerPlayer)user;
        MinecraftServer server = user.level().getServer();
        if (!user.level().isClientSide() && server != null){
            BlockPos bedPos = spe.getRespawnPosition();
            if (bedPos == null) {
                ServerLevel level = server.getLevel(user.level().dimension());
                if(level != null){
                    bedPos = level.getSharedSpawnPos();
                }
            }

            if(bedPos != null){
                user.resetFallDistance();
                user.level().playSeededSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1F, 0.9F + (float)Math.random() * 0.2F, 0);
                TeleportHelper.teleportEntity(user, spe.getRespawnDimension(), new Vec3(bedPos.getX() + 0.5, bedPos.getY() + 1, bedPos.getZ() + 0.5));
                user.level().broadcastEntityEvent(spe, (byte)46);
            }
        }



    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.dmnr.key_of_homestead.lore").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("  "));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}
