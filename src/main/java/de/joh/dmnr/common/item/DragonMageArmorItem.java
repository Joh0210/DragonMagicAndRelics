package de.joh.dmnr.common.item;

import com.mna.items.armor.ISetItem;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.client.item.armor.DragonMageArmorRenderer;
import de.joh.dmnr.common.event.DamageEventHandler;
import de.joh.dmnr.common.item.material.ArmorMaterials;
import de.joh.dmnr.common.util.RLoc;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.extensions.IForgeItem;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

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

    private final ResourceLocation dragonMageArmorSetBonus;

    public DragonMageArmorItem(ArmorItem.Type type) {
        super(ArmorMaterials.DRAGON_MAGE_ARMOR_MATERIAL, type, new Item.Properties().rarity(Rarity.EPIC).fireResistant());
        this.dragonMageArmorSetBonus = RLoc.create(DragonMagicAndRelics.MOD_ID + "_dragon_armor_set_bonus");
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
//        if(entity instanceof Player){
//            this.addDragonMagic(itemStack, (Player) entity, "dm_armor");
//        }
    }

    public void onDiscard(ItemStack itemStack, LivingEntity entity) {
//        if(entity instanceof Player){
//            this.removeDragonMagic(itemStack, (Player) entity, "dm_armor");
//        }
    }
//
//    @Override
//    public @NotNull InteractionResultHolder<ItemStack> use(Level world, @NotNull Player player, @NotNull InteractionHand hand) {
//        if (!world.isClientSide && this.type == Type.CHESTPLATE) {
//            ItemStack held = player.getItemInHand(hand);
//            if (this.openGuiIfModifierPressed(held, player, world)) {
//                return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
//            }
//        }
//
//        return new InteractionResultHolder<>(InteractionResult.PASS, player.getItemInHand(hand));
//    }

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
