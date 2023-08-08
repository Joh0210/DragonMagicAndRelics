package de.joh.dragonmagicandrelics.item.items;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import net.minecraft.Util;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class BraceletOfFriendship extends Item implements ICurioItem {

    public BraceletOfFriendship(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return false;
    }

    public boolean setPlayerTarget(Player entity, ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        LazyOptional<IPlayerMagic> magic = entity.getCapability(PlayerMagicProvider.MAGIC);
        if (magic.isPresent()) {
            nbt.putString("player_target_uuid", entity.getUUID().toString());
            nbt.putInt("player_target_salt", magic.orElse(null).getTeleportSalt());
            nbt.putString("player_target_name", entity.getGameProfile().getName());
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    public UUID getPlayerUUID(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null && nbt.contains("player_target_uuid") && nbt.contains("player_target_salt") ? UUID.fromString(nbt.getString("player_target_uuid")) : null;
    }

    @Nullable
    public Player getPlayerTarget(ItemStack stack, Level world) {
        UUID uuid = this.getPlayerUUID(stack);
        if (uuid != null) {
            CompoundTag nbt = stack.getTag();
            Player entity = world.getPlayerByUUID(uuid);
            if (entity != null) {
                LazyOptional<IPlayerMagic> magic = entity.getCapability(PlayerMagicProvider.MAGIC);
                if (magic.isPresent() && magic.orElse(null).getTeleportSalt() == nbt.getInt("player_target_salt")) {
                    return entity;
                }
            }
        }
        return null;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        if (!worldIn.isClientSide) {
            CompoundTag nbt = stack.getTag();
            if (nbt == null) {
                nbt = new CompoundTag();
            }

            if (!nbt.contains("player_target_uuid") && !nbt.contains("player_target_salt")) {
                this.setPlayerTarget(playerIn, stack);
                playerIn.sendMessage(new TextComponent("You've attuned this to yourself!"), Util.NIL_UUID);
            } else {
                playerIn.sendMessage(new TextComponent("This is already attuned to someone else."), Util.NIL_UUID);
            }
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        CompoundTag nbt = stack.getTag();
        if (nbt == null) {
            tooltip.add(new TextComponent(I18n.get("item.mna.player_charm.not_attuned")));
        } else {
            String playerName = nbt.getString("player_target_name");
            if (playerName != null) {
                tooltip.add(new TextComponent(I18n.get("item.mna.player_charm.attuned", playerName)));
            }
        }
    }
}
