package de.joh.dmnr.common.item.spellstoring;

import com.mna.api.items.ITieredItem;
import com.mna.api.spells.SpellReagent;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.inventory.InventoryRitualKit;
import com.mna.inventory.ItemInventoryBase;
import com.mna.items.ItemInit;
import com.mna.items.base.IItemWithGui;
import com.mna.items.ritual.PractitionersPouch;
import com.mna.items.ritual.PractitionersPouchPatches;
import com.mna.items.sorcery.ItemSpell;
import com.mna.spells.crafting.SpellRecipe;
import com.mna.tools.InventoryUtilities;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Ring that can store a spell to be cast with a keybind
 * @see RingOfNormalSpellStoringItem
 * @author Joh0210
 */
public abstract class RingOfSpellStoringItem extends Item implements ITieredItem<RingOfSpellStoringItem>, IItemWithGui<RingOfSpellStoringItem> {
    private int _tier = -1;

    public RingOfSpellStoringItem(Properties itemProperties) {
        super(itemProperties);
    }


    public static void castSpell(@NotNull Player caster) {
        List<SlotResult> rings = CuriosApi.getCuriosHelper().findCurios(caster, de.joh.dmnr.common.init.ItemInit.RING_OF_SPELL_STORING.get());
        if(rings.isEmpty()){
            rings = CuriosApi.getCuriosHelper().findCurios(caster, de.joh.dmnr.common.init.ItemInit.RING_OF_SPELL_STORING_COOLDOWN.get());
        }


        if(!rings.isEmpty()) {
            ItemStack ring = rings.get(0).stack();
            if(ring.getItem() instanceof RingOfSpellStoringItem ringOfSpellStoring){
                ItemInventoryBase inv = new ItemInventoryBase(ring);
                ItemStack slot = inv.getStackInSlot(0);
                if (slot.getItem() != ItemInit.ENCHANTED_VELLUM.get()) {
                    if (!slot.isEmpty() && SpellRecipe.stackContainsSpell(slot) && !caster.level().isClientSide) {
                        SpellRecipe recipe = SpellRecipe.fromNBT(slot.getTag());
                        if (recipe.isValid() && ringOfSpellStoring.canUse(caster, ring, recipe)) {
                            if(!recipe.getShape().getPart().isChanneled()){
                                HashMap<Item, Boolean> missing = checkReagents(caster, recipe);
                                if (!caster.isCreative()) {
                                    List<Map.Entry<Item, Boolean>> missingRequired = missing.entrySet().stream().filter((e) -> !(Boolean)e.getValue()).toList();
                                    if (!missingRequired.isEmpty()) {
                                        if (!caster.level().isClientSide) {
                                            if (missing.size() > 1) {
                                                caster.sendSystemMessage(Component.translatable("item.mna.spell.reagents-missing.multi"));
                                            } else {
                                                caster.sendSystemMessage(Component.translatable("item.mna.spell.reagents-missing.single").append(Component.translatable(((Item)((Map.Entry)missingRequired.get(0)).getKey()).getDescriptionId())));
                                            }
                                        }

                                        return;
                                    }
                                }

                                ItemSpell.castSpellOnUse(slot, caster.level(), caster, InteractionHand.MAIN_HAND, itemStack -> false);
                                ringOfSpellStoring.onUse(caster, ring, recipe);
                            } else {
                                caster.displayClientMessage(Component.translatable("dmnr.ring_of_spell_storing.no_channeled.error"), true);
                            }
                        }
                    }
                }
            }
        } else {
            caster.displayClientMessage(Component.translatable("dmnr.ring_of_spell_storing.no_ring.error"), true);
        }
    }

    private static HashMap<Item, Boolean> checkReagents(Player caster, SpellRecipe recipe) {
        HashMap<Item, Boolean> missing = new HashMap<>();
        if (caster.isCreative()) {
            return missing;
        } else {
            List<Pair<IItemHandler, Direction>> inventories = getReagentSearchInventories(caster);

            for (SpellReagent reagent : recipe.getReagents(caster, null, null)) {
                if (!InventoryUtilities.consumeAcrossInventories(reagent.getReagentStack(), reagent.getIgnoreDurability(), reagent.getCompareNBT(), true, inventories)) {
                    missing.put(reagent.getReagentStack().getItem(), reagent.getOptional());
                }
            }

            if(missing.isEmpty()){
                for (SpellReagent reagent : recipe.getReagents(caster, null, null)) {
                    InventoryUtilities.consumeAcrossInventories(reagent.getReagentStack(), reagent.getIgnoreDurability(), reagent.getCompareNBT(), false, inventories);
                }
            }

            return missing;
        }
    }

    private static List<Pair<IItemHandler, Direction>> getReagentSearchInventories(Player caster) {
        ArrayList<Pair<IItemHandler, Direction>> output = new ArrayList<>();

        for(int i = 0; i < caster.getInventory().getContainerSize(); ++i) {
            ItemStack invStack = caster.getInventory().getItem(i);
            if (!invStack.isEmpty() && invStack.getItem() instanceof PractitionersPouch) {
                PractitionersPouch item = (PractitionersPouch)invStack.getItem();
                Pair<IItemHandler, Direction> remoteInv = item.resolveRemoteInventory(invStack, caster.level());
                if (remoteInv.getFirst() != null) {
                    output.add(remoteInv);
                }

                if (item.getPatchLevel(invStack, PractitionersPouchPatches.RIFT) > 0) {
                    caster.getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> {
                        output.add(new Pair<>(new InvWrapper(m.getRiftInventory()), Direction.UP));
                    });
                }

                InventoryRitualKit kit = new InventoryRitualKit(invStack);
                output.add(new Pair<>(kit, Direction.UP));
            }
        }

        output.add(new Pair<>(new InvWrapper(caster.getInventory()), Direction.UP));
        return output;
    }

    protected abstract boolean canUse(Player player, ItemStack ring, SpellRecipe recipe);

    /**
     * Consumes all Resources needed for the Spell
     */
    protected abstract void onUse(Player player, ItemStack ring, SpellRecipe recipe);

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

    public int getCachedTier() {
        return this._tier;
    }

    public void setCachedTier(int tier) {
        this._tier = tier;
    }
}
