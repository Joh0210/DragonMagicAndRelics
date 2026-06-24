package de.joh.dmnr.common.item;

import de.joh.dmnr.api.item.IDragonMagicItem;
import com.mna.api.items.TieredItem;
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
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.List;

public abstract class SpellRingBaseItem<T extends SpellRingBaseItem<T>> extends TieredItem implements ICurioItem, IItemWithGui<T>, IDragonMagicItem {
    public SpellRingBaseItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant());
    }

    public static void applySpell(ItemStack stack, boolean isOther, Player self, @Nullable Entity other) {
        ItemInventoryBase inv = new ItemInventoryBase(stack);
        ItemStack slot = inv.getStackInSlot(0);
        if (slot.getItem() != ItemInit.ENCHANTED_VELLUM.get() && (!isOther || other != null)) {
            if (!slot.isEmpty() && SpellRecipe.stackContainsSpell(slot) && !self.level().isClientSide) {
                SpellRecipe recipe = SpellRecipe.fromNBT(slot.getTag());
                if (recipe.isValid()) {
                    MutableBoolean consumed = new MutableBoolean(false);
                    float manaCost = recipe.getManaCost();
                    if(stack.getItem() instanceof IDragonMagicItem dmItem && dmItem.hasDragonMagic(self)){
                        manaCost *= 0.5f;
                    }

                    float finalManaCost = manaCost;
                    self.getCapability(PlayerMagicProvider.MAGIC).ifPresent((c) -> {
                        if (c.getCastingResource().hasEnoughAbsolute(self, finalManaCost)) {
                            c.getCastingResource().consume(self, finalManaCost);
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
    public String dragonMagicID() {
        return "dmnr.dragonmagic.spell_ring";
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        this.tooltipAddition(tooltip);
        super.appendHoverText(stack, world, tooltip, flag);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack pStack) {
        return this.isEnabled() && this.hasDragonMagic();
    }
}
