package de.joh.dmnr.common.item;

import com.mna.api.items.IRelic;
import com.mna.entities.EntityInit;
import com.mna.entities.rituals.TimeChangeBall;
import de.joh.tnl.api.item.IDragonMagicItem;
import de.joh.tnl.api.item.ScrollableItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
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
public class WeatherFairyStaffItem extends SwordItem implements ScrollableItem, IRelic, IDragonMagicItem {
    public WeatherFairyStaffItem() {
        super(Tiers.IRON, 3, -3.1F, new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC).setNoRepair());
    }

    @Override
    public int getIteratorSize(Player player) {
        return hasDragonMagic(player) ? 5 : 3;
    }

    @Override
    public String dragonMagicID() {
        return "item.dmnr.weather_fairy_staff.dragonmagic";
    }

    @Override
    public int incrementIterator(ItemStack stack, boolean inverted, Player player) {
        int value = ScrollableItem.super.incrementIterator(stack, inverted, player);
        if(value < 3){
            player.displayClientMessage(Component.literal(Component.translatable("dmnr.feedback.selected.weather").getString() + getSelectedWeatherText(stack).getString()), true);
        } else {
            player.displayClientMessage(Component.literal(Component.translatable("dmnr.feedback.selected.time").getString() + getSelectedWeatherText(stack).getString()), true);
        }
        return value;
    }

    public MutableComponent getSelectedWeatherText(ItemStack stack){
        return switch (getIterator(stack)){
            case 1 -> Component.translatable("dmnr.feedback.selected.weather.rain");
            case 2 -> Component.translatable("dmnr.feedback.selected.weather.storm");
            case 3 -> Component.translatable("dmnr.feedback.selected.time.day");
            case 4 -> Component.translatable("dmnr.feedback.selected.time.night");
            default -> Component.translatable("dmnr.feedback.selected.weather.sunshine");
        };
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player user, @NotNull InteractionHand hand) {
        InteractionResultHolder<ItemStack> ar = super.use(world, user, hand);

        if (!world.isClientSide()) {
            int iterator = getIterator(user.getItemInHand(hand));
            if (iterator < 3) {
                if (world.getBiome(user.blockPosition()).value().getPrecipitationAt(user.blockPosition()) == Biome.Precipitation.NONE) {
                    user.displayClientMessage(Component.translatable("dmnr.feedback.weather.not_changable"), true);
                } else {
                    user.displayClientMessage(Component.literal(Component.translatable("dmnr.feedback.selected.weather").getString() + getSelectedWeatherText(user.getItemInHand(hand)).getString()), true);
                    switch (iterator) {
                        case 1 -> ((ServerLevel) world).setWeatherParameters(0, 6000, true, false);
                        case 2 -> ((ServerLevel) world).setWeatherParameters(0, 6000, true, true);
                        default -> ((ServerLevel) world).setWeatherParameters(30000, 0, false, false);
                    }
                }
            } else if (hasDragonMagic(user)) {
                user.displayClientMessage(Component.literal(Component.translatable("dmnr.feedback.selected.time").getString() + getSelectedWeatherText(user.getItemInHand(hand)).getString()), true);
                BlockPos blockPos = new BlockPos(user.getOnPos().getX(), (user.getOnPos().getY() + 3), user.getOnPos().getZ());
                TimeChangeBall auroraBall = EntityInit.STARBALL_ENTITY.get().spawn((ServerLevel) world, (CompoundTag) null, null, blockPos, MobSpawnType.TRIGGERED, true, false);
                if (auroraBall instanceof TimeChangeBall) {
                    auroraBall.setTimeChangeType(iterator == 3 ? TimeChangeBall.TIME_CHANGE_DAY : TimeChangeBall.TIME_CHANGE_NIGHT);
                }
            }
        }
        user.getCooldowns().addCooldown(this, 200);
        return ar;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        if(getIterator(stack) < 3){
            tooltip.add(Component.translatable("dmnr.feedback.selected.weather"));
        } else {
            tooltip.add(Component.translatable("dmnr.feedback.selected.time"));
        }
        tooltip.add(getSelectedWeatherText((stack)));
        tooltip.add(Component.translatable("item.dmnr.weather_fairy_staff.lore").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal(""));
        this.tooltipAddition(tooltip);
        tooltip.add(Component.translatable("tooltip.dmnr.weatherferystaff.how_to_change"));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@NotNull ItemStack pStack) {
        return this.isEnabled() && this.hasDragonMagic();
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