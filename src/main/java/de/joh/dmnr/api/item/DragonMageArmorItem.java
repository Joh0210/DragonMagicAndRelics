package de.joh.dmnr.api.item;

import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedSpellEffect;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.inventory.ItemInventoryBase;
import com.mna.items.ItemInit;
import com.mna.items.armor.ISetItem;
import com.mna.items.base.IItemWithGui;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.capabilities.dragonmagic.ArmorUpgradeHelper;
import de.joh.dmnr.client.item.armor.DragonMageArmorRenderer;
import de.joh.dmnr.common.event.DamageEventHandler;
import de.joh.dmnr.common.init.ArmorUpgradeInit;
import de.joh.dmnr.common.init.EffectInit;
import de.joh.dmnr.common.item.material.ArmorMaterials;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.extensions.IForgeItem;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

/**
 * This armor is the main item of this mod.
 * This armor defaults to netherite armor, which can be enhanced with ugprades.
 * The list of upgrades and their effects can be found in ArmorUpgradeInit.
 * In addition, it can be enhanced with spells that are cast when the wearer takes damage. (see DamageEventHandler)
 * @see ArmorUpgradeInit
 * @see DamageEventHandler
 * @author Joh0210
 */
public abstract class DragonMageArmorItem extends ArmorItem implements IItemWithGui<DragonMageArmorItem>, IForgeItem, ISetItem, IDragonMagicContainerItem, GeoItem {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private final ResourceLocation dragonMageArmorSetBonus;

    public DragonMageArmorItem(ArmorItem.Type type, ResourceLocation dragonMageArmorSetBonus) {
        super(ArmorMaterials.DRAGON_MAGE_ARMOR_MATERIAL, type, new Item.Properties().rarity(Rarity.EPIC).fireResistant());
        this.dragonMageArmorSetBonus = dragonMageArmorSetBonus;
    }

    public abstract ResourceLocation getWingTextureLocation();

    /**
     * Performs one of the two spells from the Dragon Mage Armor.
     * @param stack Dragon Mage Armor Chestplate with the spells.
     * @param isOther Should the source be hit? Should the offensive spell be used?
     * @param self Wearer of armor.
     * @param other Source of damage (only as entity).
     */
    public static void applySpell(ItemStack stack, boolean isOther, Player self, @Nullable Entity other) {
        if(stack.getItem() instanceof DragonMageArmorItem && ((DragonMageArmorItem)stack.getItem()).type == Type.CHESTPLATE){
            ItemInventoryBase inv = new ItemInventoryBase(stack);
            ItemStack slot = inv.getStackInSlot(isOther ? 1 : 0);
            if (slot.getItem() != ItemInit.ENCHANTED_VELLUM.get() && (!isOther || other != null)) {
                if (!slot.isEmpty() && SpellRecipe.stackContainsSpell(slot) && !self.level().isClientSide) {
                    SpellRecipe recipe = SpellRecipe.fromNBT(slot.getTag());
                    if (recipe.isValid()) {
                        MutableBoolean consumed = new MutableBoolean(false);
                        self.getCapability(PlayerMagicProvider.MAGIC).ifPresent((c) -> {
                            if (c.getCastingResource().hasEnoughAbsolute(self, recipe.getManaCost())) {
                                c.getCastingResource().consume(self, recipe.getManaCost());
                                consumed.setTrue();
                            }

                        });
                        if (consumed.getValue()) {
                            SpellSource source = new SpellSource(self, InteractionHand.MAIN_HAND);
                            SpellContext context = new SpellContext(self.level(), recipe);
                            recipe.iterateComponents((c) -> {
                                int delay = (int)(c.getValue(com.mna.api.spells.attributes.Attribute.DELAY) * 20.0F);
                                boolean appliedComponent = false;
                                if (delay > 0) {
                                    DelayedEventQueue.pushEvent(self.level(), new TimedDelayedSpellEffect(c.getPart().getRegistryName().toString(), delay, source, new SpellTarget(isOther ? other : self), c, context));
                                    appliedComponent = true;
                                } else if (c.getPart().ApplyEffect(source, new SpellTarget(isOther ? other : self), c, context) == ComponentApplicationResult.SUCCESS) {
                                    appliedComponent = true;
                                }

                                if (appliedComponent) {
                                    SpellCaster.addComponentRoteProgress(self, c.getPart());
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    /**
     * Depending on the faction, a different texture will be returned.
     */
    public abstract ResourceLocation getTextureLocation();

    @Override
    public int getMaxDragonMagic(ItemStack itemStack) {
        return type == Type.CHESTPLATE ? 64 : 0;
    }


    public void applyDragonMagicSetBonus(LivingEntity living) {
        if(living instanceof Player){
            if(living.hasEffect(EffectInit.ULTIMATE_ARMOR.get())){
                ArmorUpgradeHelper.ultimateArmorStart((Player) living);
            } else {
                ArmorUpgradeHelper.activateOnEquip((Player) living);
            }
        }
    }

    public void removeDragonMagicSetBonus(LivingEntity living) {
        if(living instanceof Player){
            if(living.hasEffect(EffectInit.ULTIMATE_ARMOR.get())){
                ArmorUpgradeHelper.ultimateArmorFin((Player) living);
            } else {
                ArmorUpgradeHelper.deactivateAll((Player) living);
            }
        }
    }

    public void onEquip(ItemStack itemStack, LivingEntity entity) {
        if(entity instanceof Player){
            this.addDragonMagic(itemStack, (Player) entity, "dm_armor");
        }
    }

    public void onDiscard(ItemStack itemStack, LivingEntity entity) {
        if(entity instanceof Player){
            this.removeDragonMagic(itemStack, (Player) entity, "dm_armor");
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, @NotNull Player player, @NotNull InteractionHand hand) {
        if (!world.isClientSide && this.type == Type.CHESTPLATE) {
            ItemStack held = player.getItemInHand(hand);
            if (this.openGuiIfModifierPressed(held, player, world)) {
                return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
            }
        }

        return new InteractionResultHolder<>(InteractionResult.PASS, player.getItemInHand(hand));
    }

    /**
     * Adds a tooltip (when hovering over the item) to the item, so that the installed upgrades will be listed.
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        if(this.type != Type.CHESTPLATE){
            return;
        }

        if(Screen.hasShiftDown()){
            if(stack.getTag() != null){
                if(stack.getTag().contains(DragonMagicAndRelics.MOD_ID + "armor_upgrade")){
                    CompoundTag nbt = stack.getTag().getCompound(DragonMagicAndRelics.MOD_ID + "armor_upgrade");
                    if(!nbt.getAllKeys().isEmpty()){
                        tooltip.add(Component.translatable("tooltip.dmnr.armor.tooltip.upgrade.base"));
                        for(String key : nbt.getAllKeys()){
                            if(nbt.getInt(key) > 0){
                                MutableComponent component = Component.translatable(key);
                                tooltip.add(Component.literal(component.getString() + ": " + nbt.getInt(key)));
                            }
                        }
                        tooltip.add(Component.literal("  "));
                    }
                }
            }

            MutableComponent component = Component.translatable("tooltip.dmnr.dm_container.tooltip.remaining.dmpoints");
            tooltip.add(Component.literal(component.getString() + (getMaxDragonMagic(stack) - getSpentDragonPoints(stack))));
            tooltip.add(Component.literal("  "));
            super.appendHoverText(stack, world, tooltip, flag);
        }
        else{
            tooltip.add(Component.translatable("tooltip.dmnr.armor.tooltip"));
        }
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

    public boolean wouldSetBeEquipped(LivingEntity living, Item item) {
        if (living == null) {
            return false;
        } else {
            int count = 0;
            this.getValidSetSlots();

            for(int i = 0; i < this.getValidSetSlots().length; ++i) {
                EquipmentSlot slot = this.getValidSetSlots()[i];
                if (slot != EquipmentSlot.MAINHAND && slot != EquipmentSlot.OFFHAND) {
                    ItemStack stack = living.getItemBySlot(slot);
                    if (stack.getItem() instanceof DragonMageArmorItem && ((DragonMageArmorItem)stack.getItem()).getSetIdentifier().equals(this.getSetIdentifier())) {
                        count++;
                    }
                }
            }

            if(item instanceof DragonMageArmorItem){
                boolean isValidSlot = false;
                EquipmentSlot slot = ((ArmorItem)item).getType().getSlot();
                for(EquipmentSlot es : getValidSetSlots()){
                    isValidSlot = isValidSlot || slot == es;
                }
                if(isValidSlot){
                    ItemStack stack = living.getItemBySlot(slot);
                    if (!(stack.getItem() instanceof DragonMageArmorItem && ((DragonMageArmorItem)stack.getItem()).getSetIdentifier().equals(this.getSetIdentifier()))) {
                        count++;
                    }
                }
            }

            return count >= this.itemsForSetBonus();
        }
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
