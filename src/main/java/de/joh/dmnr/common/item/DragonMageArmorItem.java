package de.joh.dmnr.common.item;

import com.mna.items.armor.ISetItem;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.api.event.DragonMagicChangeEvent;
import de.joh.dmnr.client.item.armor.DragonMageArmorRenderer;
import de.joh.dmnr.common.event.DamageEventHandler;
import de.joh.dmnr.common.item.material.ArmorMaterials;
import de.joh.dmnr.common.util.RLoc;
import de.joh.dmnr.networking.ModMessages;
import de.joh.dmnr.networking.packet.ToggleCurioBoostEnabledS2CPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeItem;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * This armor is the main item of this mod.
 * This armor defaults to netherite armor, which can be enhanced with ugprades.
 * The list of upgrades and their effects can be found in ArmorUpgradeInit.
 * In addition, it can be enhanced with spells that are cast when the wearer takes damage. (see DamageEventHandler)
 * @see DamageEventHandler
 * @author Joh0210
 */
public class DragonMageArmorItem extends ArmorItem implements IForgeItem, ISetItem, GeoItem, DyeableLeatherItem {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final static int DEFAULT_COLOR =0xffb736;

    private final String curioType = "curio";
    private final UUID curiosID;
    private final AttributeModifier curiosMod;

    private final ResourceLocation dragonMageArmorSetBonus;

    public DragonMageArmorItem(ArmorItem.Type type) {
        super(ArmorMaterials.DRAGON_MAGE_ARMOR_MATERIAL, type, new Item.Properties().rarity(Rarity.EPIC).fireResistant());
        this.dragonMageArmorSetBonus = RLoc.create("dragon_armor_set_bonus");

        String id = DragonMagicAndRelics.MOD_ID + "_dm_armor" + type.getName();
        this.curiosID = UUID.nameUUIDFromBytes(id.getBytes());
        this.curiosMod = new AttributeModifier(curiosID, id, 1, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public int getColor(ItemStack stack) {
        CompoundTag displayTag = stack.getTagElement("display");
        return displayTag != null && displayTag.contains("color", 99) ? displayTag.getInt("color") : DEFAULT_COLOR;
    }

    @Override
    public void setColor(ItemStack stack, int color) {
        stack.getOrCreateTagElement("display").putInt("color", color);
    }

    @Override
    public void clearColor(ItemStack stack) {
        CompoundTag displayTag = stack.getTagElement("display");
        if (displayTag != null && displayTag.contains("color")) {
            displayTag.remove("color");
        }
    }

    public void onEquip(ItemStack itemStack, LivingEntity entity) {
        var opt = CuriosApi.getCuriosInventory(entity).resolve()
                .flatMap(x -> x.getStacksHandler(curioType));
        opt.ifPresent(
                iCurioStacksHandler -> iCurioStacksHandler.addTransientModifier(curiosMod));
    }

    public void onDiscard(ItemStack itemStack, LivingEntity entity) {
        var opt = CuriosApi.getCuriosInventory(entity).resolve()
                .flatMap(x -> x.getStacksHandler(curioType));
        opt.ifPresent(iCurioStacksHandler -> iCurioStacksHandler.removeModifier(curiosID));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("dmnr:dragon_armor_bonus").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        this.addSetTooltip(tooltip);
        tooltip.add(Component.literal("  "));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    /**
     * Armor does not break
     */
    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return super.damageItem(stack, 0, entity, onBroken);
    }

    @Override
    public ResourceLocation getSetIdentifier() {
        return dragonMageArmorSetBonus;
    }

    @Override
    public int itemsForSetBonus() {
        return 4;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private DragonMageArmorRenderer renderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack,
                                                                   EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.renderer == null)
                    this.renderer = new DragonMageArmorRenderer();

                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }

    @Override
    public void applySetBonus(LivingEntity entity, EquipmentSlot... setSlots) {
        if (entity instanceof Player player) {
            if(entity instanceof ServerPlayer) {
                ModMessages.sendToPlayer(new ToggleCurioBoostEnabledS2CPacket(true), (ServerPlayer) entity);
            }

            DragonMagicChangeEvent event = new DragonMagicChangeEvent(player, true);
            MinecraftForge.EVENT_BUS.post(event);
        }
    }

    @Override
    public void removeSetBonus(LivingEntity entity, EquipmentSlot... setSlots) {
        if (entity instanceof Player player) {
            if(entity instanceof ServerPlayer) {
                ModMessages.sendToPlayer(new ToggleCurioBoostEnabledS2CPacket(false), (ServerPlayer) entity);
            }

            DragonMagicChangeEvent event = new DragonMagicChangeEvent(player, false);
            MinecraftForge.EVENT_BUS.post(event);
        }


    }

    private PlayState predicate(AnimationState animationState) {
        animationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@NotNull ItemStack itemStack){
        return false;
    }
}
