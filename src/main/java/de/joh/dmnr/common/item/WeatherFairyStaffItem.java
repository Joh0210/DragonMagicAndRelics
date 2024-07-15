package de.joh.dmnr.common.item;

import de.joh.dmnr.api.item.ScrollableItem;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

/**
 * Allows the user to Change the Weather
 * <br>Is a Relict
 * @author Joh0210
 */
public class WeatherFairyStaffItem extends SwordItem implements ScrollableItem {
    public WeatherFairyStaffItem() {
        super(Tiers.IRON, 3, -3.1F, new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC).setNoRepair());
    }

    @Override
    public int getIteratorSize(Player player) {
        return 3;
    }

    @Override
    public int incrementIterator(ItemStack stack, boolean inverted, Player player) {
        int value = ScrollableItem.super.incrementIterator(stack, inverted, player);
        player.displayClientMessage(Component.literal(Component.translatable("dmnr.feedback.selected.weather").getString() + getSelectedWeatherText(stack).getString()), true);
        return value;
    }

    public MutableComponent getSelectedWeatherText(ItemStack stack){
        return switch (getIterator(stack)){
            case 1 -> Component.translatable("dmnr.feedback.selected.weather.rain");
            case 2 -> Component.translatable("dmnr.feedback.selected.weather.storm");
            default -> Component.translatable("dmnr.feedback.selected.weather.sunshine");
        };
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player user, @NotNull InteractionHand hand) {
        InteractionResultHolder<ItemStack> ar = super.use(world, user, hand);

        if (!world.isClientSide()) {
            if(world.getBiome(user.blockPosition()).value().getPrecipitationAt(user.blockPosition()) == Biome.Precipitation.NONE){
                user.displayClientMessage(Component.translatable("dmnr.feedback.weather.not_changable"), true);
            } else {
                user.displayClientMessage(Component.literal(Component.translatable("dmnr.feedback.selected.weather").getString() + getSelectedWeatherText(user.getItemInHand(hand)).getString()), true);
                switch (getIterator(user.getItemInHand(hand))){
                    case 1 -> ((ServerLevel) world).setWeatherParameters(0, 6000, true, false);
                    case 2 -> ((ServerLevel) world).setWeatherParameters(0, 6000, true, true);
                    default -> ((ServerLevel) world).setWeatherParameters(30000, 0, false, false);
                }
            }
        }
        user.getCooldowns().addCooldown(this, 200);
        return ar;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("dmnr.feedback.selected.weather"));
        tooltip.add(getSelectedWeatherText((stack)));
        tooltip.add(Component.literal(""));
        tooltip.add(Component.translatable("tooltip.dmnr.weatherferystaff.how_to_change"));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@NotNull ItemStack itemStack){
        return true;
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return super.damageItem(stack, 0, entity, onBroken);
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(attacker.level());
        if(lightningbolt != null){
            lightningbolt.setPos(target.position());
            attacker.level().addFreshEntity(lightningbolt);
        }

        return super.hurtEnemy(stack, target, attacker);
    }
}