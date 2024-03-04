package de.joh.dmnr.common.item;

import com.mna.api.items.ITieredItem;
import com.mna.inventory.ItemInventoryBase;
import com.mna.items.ItemInit;
import com.mna.items.base.IItemWithGui;
import com.mna.items.sorcery.ItemSpell;
import com.mna.spells.crafting.SpellRecipe;
import de.joh.dmnr.client.gui.NamedRingOfSpellStoringItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Ring that can store a spell to be cast with a keybind
 * @author Joh0210
 */
public class RingOfSpellStoringItem extends Item implements ITieredItem<RingOfSpellStoringItem>, IItemWithGui<RingOfSpellStoringItem> {
    private int _tier = -1;

    public RingOfSpellStoringItem(Properties itemProperties) {
        super(itemProperties);
    }


    public static void castSpell(@NotNull Player caster) {
        List<SlotResult> rings = CuriosApi.getCuriosHelper().findCurios(caster, de.joh.dmnr.common.init.ItemInit.RING_OF_SPELL_STORING.get());

        if(!rings.isEmpty()) {
            ItemInventoryBase inv = new ItemInventoryBase(rings.get(0).stack());
            ItemStack slot = inv.getStackInSlot(0);
            if (slot.getItem() != ItemInit.ENCHANTED_VELLUM.get()) {
                if (!slot.isEmpty() && SpellRecipe.stackContainsSpell(slot) && !caster.level().isClientSide) {
                    SpellRecipe recipe = SpellRecipe.fromNBT(slot.getTag());
                    if (recipe.isValid()) {
                        ItemSpell.castSpellOnUse(slot, caster.level(), caster, InteractionHand.MAIN_HAND, itemStack -> true);
                    }
                }
            }
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, @NotNull Player player, @NotNull InteractionHand hand) {

        ItemStack held = player.getItemInHand(hand);
        if (!world.isClientSide && this.openGuiIfModifierPressed(held, player, world)) {
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
        }

        return new InteractionResultHolder<>(InteractionResult.PASS, player.getItemInHand(hand));
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@Nullable ItemStack itemStack){
        return true;
    }

    @Override
    public MenuProvider getProvider(ItemStack itemStack) {
        return new NamedRingOfSpellStoringItem(itemStack);
    }

    public int getCachedTier() {
        return this._tier;
    }

    public void setCachedTier(int tier) {
        this._tier = tier;
    }
}
