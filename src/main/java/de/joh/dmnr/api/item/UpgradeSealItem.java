package de.joh.dmnr.api.item;

import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.api.armorupgrade.ArmorUpgrade;
import de.joh.dmnr.common.init.ArmorUpgradeInit;
import de.joh.dmnr.common.util.Registries;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * These seals can be used to add an upgrade to the Dragon Mage Armor via <b>right click</b>.
 * @see DragonMageArmorItem
 * @see ArmorUpgradeInit
 * @author Joh0210
 */
public class UpgradeSealItem extends Item {
    /**
     * ID of the armor upgrade that can be installed with this UpgradeSeal
     */
    private final ResourceLocation armorUpgradeRL;

    /**
     * @param armorUpgrade ID of the armor upgrade that can be installed with this UpgradeSeal
     */
    public UpgradeSealItem(ResourceLocation armorUpgrade) {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)); //.rarity(rarity));
        this.armorUpgradeRL = armorUpgrade;
    }

    /**
     * Adds a tooltip (when hovering over the item) to the item.
     * Shows which Ritual Tier you need to install the upgrade.
     * Call from the game itself.
     */
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            ArmorUpgrade armorUpgrade = this.getArmorUpgrade();
            if (armorUpgrade != ArmorUpgrade.INSTANCE) {
                tooltip.add(Component.translatable(armorUpgrade.getRegistryName().toString() + ".description"));

                MutableComponent component0 = Component.translatable("tooltip.dmnr.upgradeseal.max_level");
                tooltip.add(Component.literal(component0.getString() + " " + armorUpgrade.maxUpgradeLevel));

                MutableComponent component1 = Component.translatable("tooltip.dmnr.upgradeseal");
                tooltip.add(Component.literal(component1.getString() + " " + armorUpgrade.upgradeCost));

                if (armorUpgrade.getStrongerAlternative() != null) {
                    tooltip.add(Component.literal("  "));
                    MutableComponent component2 = Component.translatable("tooltip.dmnr.upgradeseal.stronger_version");
                    MutableComponent component3 = Component.translatable(String.valueOf(armorUpgrade.getStrongerAlternative().getRegistryName()));
                    tooltip.add(Component.literal(component2.getString() + " " + component3.getString()));
                }

                if (!armorUpgrade.supportsOnExtraLevel) {
                    tooltip.add(Component.literal("  "));
                    tooltip.add(Component.translatable("tooltip.dmnr.upgradeseal.not_supports_extra_level"));
                }
            } else {
                tooltip.add(Component.literal("ERROR"));
            }
        }
        else{
            tooltip.add(Component.translatable("tooltip.dmnr.armor.tooltip"));
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@NotNull ItemStack itemstack) {
        return true;
    }

    @NotNull
    public ArmorUpgrade getArmorUpgrade(){
        ArmorUpgrade armorUpgrade = Registries.ARMOR_UPGRADE.get().getValue(armorUpgradeRL);
        if (armorUpgrade != null){
            return armorUpgrade;
        }
        else {
            DragonMagicAndRelics.LOGGER.error("UpgradeSealItem: The following armor upgrade could not be found: " + armorUpgradeRL);
            return ArmorUpgrade.INSTANCE;
        }
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemstack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack itemstack) {
        return 40;
    }

    @Override
    public void onUseTick(@NotNull Level world, @NotNull LivingEntity user, @NotNull ItemStack stack, int count) {
        if (count <= 1) {
            user.stopUsingItem();

            ItemStack dMContainerStack = user.getItemInHand(InteractionHand.OFF_HAND);
            if(dMContainerStack.getItem() instanceof IDragonMagicContainerItem dMContainer){
                int currentLevel = dMContainer.getUpgradeLevel(dMContainerStack, this.getArmorUpgrade());
                if(this.getArmorUpgrade().maxUpgradeLevel <= currentLevel){
                    if(user instanceof Player){
                        ((Player) user).displayClientMessage(Component.translatable("dmnr.ritual.output.upgrade.ritual.already_at_max.error"), true);
                    }
                    return;
                }

                if(!dMContainer.addDragonMagicToItem(dMContainerStack, this.getArmorUpgrade(), currentLevel + 1, false)){
                    if(user instanceof Player){
                        ((Player) user).displayClientMessage(Component.translatable("dmnr.ritual.output.upgrade.ritual.unexpected.error"), true);
                    }
                    return;
                }

                user.level().playSeededSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1f, 0.9F + (float)Math.random() * 0.2F, 0);
                if (!(user instanceof Player) || !((Player)user).isCreative()) {
                    user.setItemInHand(user.getUsedItemHand(), ItemStack.EMPTY);
                }
            }
            else if(user instanceof Player){
                ((Player) user).displayClientMessage(Component.translatable("dmnr.ritual.output.upgrade.ritual.no_dm_container.error"), true);
            }

            if(user instanceof Player){
                ((Player) user).getCooldowns().addCooldown(this, 20);
            }
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }
}
