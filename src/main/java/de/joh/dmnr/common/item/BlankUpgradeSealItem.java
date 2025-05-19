package de.joh.dmnr.common.item;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.api.armorupgrade.ArmorUpgrade;
import de.joh.dmnr.api.item.IDragonMagicContainerItem;
import de.joh.dmnr.api.item.ScrollableItem;
import de.joh.dmnr.common.util.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class BlankUpgradeSealItem extends Item implements ScrollableItem {

    public BlankUpgradeSealItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    @Override
    public int getIteratorSize(Player player) {
        ItemStack offHandStack = player.getItemInHand(InteractionHand.OFF_HAND);
        int dragon_magics = 0;

        if(offHandStack.getItem() instanceof IDragonMagicContainerItem && offHandStack.getTag() != null && offHandStack.getTag().contains(DragonMagicAndRelics.MOD_ID + "armor_upgrade")){
            CompoundTag nbt = offHandStack.getTag().getCompound(DragonMagicAndRelics.MOD_ID + "armor_upgrade");
            if(!nbt.getAllKeys().isEmpty()){
                for(String key : nbt.getAllKeys()){
                    if(nbt.getInt(key) > 0){
                        dragon_magics += 1;
                    }
                }
            }

        }
        return dragon_magics;
    }

    @Nullable
    public String getUpgradeById(ItemStack dragonContainer, int id) {
        if(dragonContainer.getItem() instanceof IDragonMagicContainerItem && dragonContainer.getTag() != null && dragonContainer.getTag().contains(DragonMagicAndRelics.MOD_ID + "armor_upgrade")){
            CompoundTag nbt = dragonContainer.getTag().getCompound(DragonMagicAndRelics.MOD_ID + "armor_upgrade");
            if(!nbt.getAllKeys().isEmpty()){
                List<String> sortedList = new ArrayList<>(nbt.getAllKeys());
                Collections.sort(sortedList);

                Predicate<String> condition = key ->nbt.getInt(key) <= 0;
                sortedList.removeIf(condition);

                if(sortedList.size() <= id){
                    return null;
                }
                else{
                    return sortedList.get(id);
                }
            }
        }
        return null;
    }

    @Override
    public int incrementIterator(ItemStack stack, boolean inverted, Player player) {
        int value = ScrollableItem.super.incrementIterator(stack, inverted, player);

        ItemStack offHandStack = player.getItemInHand(InteractionHand.OFF_HAND);

        String upgrade = getUpgradeById(offHandStack, value);
        if(upgrade != null){
            player.displayClientMessage(Component.translatable(upgrade), true);
        }

        return value;
    }

    @Override
    public void onUseTick(@NotNull Level world, @NotNull LivingEntity player, @NotNull ItemStack stack, int count) {
        if (count <= 1) {
            int value = getIterator(stack);
            ItemStack offHandStack = player.getItemInHand(InteractionHand.OFF_HAND);

            if(offHandStack.getItem() instanceof IDragonMagicContainerItem dmContainerItem){
                String upgradeKey = getUpgradeById(offHandStack, value);
                if(upgradeKey != null) {
                    ArmorUpgrade installedArmorUpgrade = Registries.ARMOR_UPGRADE.get().getValue(new ResourceLocation(upgradeKey));
                    if(installedArmorUpgrade != null) {
                        int level = dmContainerItem.getUpgradeLevel(offHandStack, installedArmorUpgrade);
                        dmContainerItem.addDragonMagicToItem(offHandStack, installedArmorUpgrade, level -1, false);
                        player.setItemInHand(player.getUsedItemHand(), new ItemStack(installedArmorUpgrade.getSeal()));
                    }
                }
            }

            player.level().playSeededSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1F, 0.9F + (float)Math.random() * 0.2F, 0);
            if(player instanceof Player){
                ((Player) player).getCooldowns().addCooldown(this, 20);
            }
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemstack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack itemstack) {
        return 40;
    }
}
