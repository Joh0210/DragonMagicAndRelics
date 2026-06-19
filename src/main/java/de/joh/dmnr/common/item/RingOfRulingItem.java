package de.joh.dmnr.common.item;

import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedSpellEffect;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.inventory.ItemInventoryBase;
import com.mna.items.ItemInit;
import com.mna.items.base.IItemWithGui;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import de.joh.dmnr.api.item.BaseTieredItem;
import de.joh.dmnr.client.gui.NamedRingOfPower;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.List;

// todo: on Attack revenge spell
public class RingOfRulingItem extends BaseTieredItem implements ICurioItem, IItemWithGui<RingOfRulingItem> {
    public RingOfRulingItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant());
    }

    public static void eventHandleDefense(LivingHurtEvent event){
        if(event.getEntity() instanceof Player defender){
            CuriosApi.getCuriosInventory(defender).ifPresent(curiosProvider -> curiosProvider.getCurios().forEach((identifier, stackHandler) -> {
                for (int k = 0; k < stackHandler.getSlots(); k++) {
                    ItemStack stack = stackHandler.getStacks().getStackInSlot(k);
                    if (!stack.isEmpty() && stack.getItem() instanceof RingOfPowerItem) {
                        RingOfPowerItem.applySpell(stack, false, defender, event.getSource().getEntity());
                    }
                }
            }));
        }
    }

    public static void applySpell(ItemStack stack, boolean isOther, Player self, @Nullable Entity other) {
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



    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, @NotNull Player player, @NotNull InteractionHand hand) {
        if (!world.isClientSide) {
            ItemStack held = player.getItemInHand(hand);
            if (this.openGuiIfModifierPressed(held, player, world)) {
                return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
            }
        }

        return new InteractionResultHolder<>(InteractionResult.PASS, player.getItemInHand(hand));
    }

    @Override
    public MenuProvider getProvider(ItemStack itemStack) {
        return new NamedRingOfPower(itemStack);
    }


    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.dmnr.ring_of_power.tooltip.one"));
        tooltip.add(Component.translatable("tooltip.dmnr.ring_of_power.tooltip.two"));
        super.appendHoverText(stack, world, tooltip, flag);
    }
}
