package de.joh.dmnr.item.items.dragonmagearmor;

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
import de.joh.dmnr.CreativeModeTab;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.armorupgrades.ArmorUpgradeInit;
import de.joh.dmnr.capabilities.dragonmagic.ArmorUpgradeHelper;
import de.joh.dmnr.effects.EffectInit;
import de.joh.dmnr.item.util.ArmorMaterials;
import de.joh.dmnr.item.util.IDragonMagicContainer;
import net.minecraft.client.gui.screens.Screen;
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
import net.minecraftforge.common.extensions.IForgeItem;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

/**
 * This armor is the main item of this mod.
 * This armor defaults to netherite armor, which can be enhanced with ugprades.
 * The list of upgrades and their effects can be found in ArmorUpgradeInit.
 * In addition, it can be enhanced with spells that are cast when the wearer takes damage. (see DamageEventHandler)
 * @see ArmorUpgradeInit
 * @see de.joh.dmnr.events.DamageEventHandler
 * @author Joh0210
 */
public abstract class DragonMageArmor extends GeoArmorItem implements IItemWithGui<DragonMageArmor>, IAnimatable, IForgeItem, ISetItem, IDragonMagicContainer {
    private final AnimationFactory factory =  GeckoLibUtil.createFactory(this);
    private final ResourceLocation dragonMageArmorSetBonus;

    public DragonMageArmor(EquipmentSlot pSlot, ResourceLocation dragonMageArmorSetBonus, net.minecraft.world.item.CreativeModeTab tab) {
        super(ArmorMaterials.DRAGON_MAGE_ARMOR_MATERIAL, pSlot, new Item.Properties().tab(tab).rarity(Rarity.EPIC).fireResistant());
        this.dragonMageArmorSetBonus = dragonMageArmorSetBonus;
    }

    public DragonMageArmor(EquipmentSlot pSlot, ResourceLocation dragonMageArmorSetBonus) {
        this(pSlot, dragonMageArmorSetBonus, CreativeModeTab.CreativeModeTab);
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
        if(stack.getItem() instanceof DragonMageArmor && ((DragonMageArmor)stack.getItem()).slot == EquipmentSlot.CHEST){
            ItemInventoryBase inv = new ItemInventoryBase(stack);
            ItemStack slot = inv.getStackInSlot(isOther ? 1 : 0);
            if (slot.getItem() != ItemInit.ENCHANTED_VELLUM.get() && (!isOther || other != null)) {
                if (!slot.isEmpty() && SpellRecipe.stackContainsSpell(slot) && !self.level.isClientSide) {
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
                            SpellContext context = new SpellContext(self.level, recipe);
                            recipe.iterateComponents((c) -> {
                                int delay = (int)(c.getValue(com.mna.api.spells.attributes.Attribute.DELAY) * 20.0F);
                                boolean appliedComponent = false;
                                if (delay > 0) {
                                    DelayedEventQueue.pushEvent(self.level, new TimedDelayedSpellEffect(c.getPart().getRegistryName().toString(), delay, source, new SpellTarget(isOther ? other : self), c, context));
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
        return slot == EquipmentSlot.CHEST ? 64 : 0;
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
    public InteractionResultHolder<ItemStack> use(Level world, @NotNull Player player, @NotNull InteractionHand hand) {
        if (!world.isClientSide && this.slot == EquipmentSlot.CHEST) {
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
        if(this.slot != EquipmentSlot.CHEST){
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

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 20, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
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
                    if (stack.getItem() instanceof DragonMageArmor && ((DragonMageArmor)stack.getItem()).getSetIdentifier().equals(this.getSetIdentifier())) {
                        count++;
                    }
                }
            }

            if(item instanceof DragonMageArmor){
                boolean isValidSlot = false;
                EquipmentSlot slot = ((ArmorItem)item).getSlot();
                for(EquipmentSlot es : getValidSetSlots()){
                    isValidSlot = isValidSlot || slot == es;
                }
                if(isValidSlot){
                    ItemStack stack = living.getItemBySlot(slot);
                    if (!(stack.getItem() instanceof DragonMageArmor && ((DragonMageArmor)stack.getItem()).getSetIdentifier().equals(this.getSetIdentifier()))) {
                        count++;
                    }
                }
            }

            return count >= this.itemsForSetBonus();
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@NotNull ItemStack itemStack){
        return false;
    }
}
