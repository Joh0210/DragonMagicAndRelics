package de.joh.dragonmagicandrelics.rituals.contexts;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.items.ItemInit;
import com.mna.spells.crafting.SpellRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

/**
 * The ritual gets a spell as input. This spell is then broken down back into its components and passed to the caster.
 * @author Joh0210
 */
public class DispellingRitual extends RitualEffect {

    public DispellingRitual(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        Player caster = context.getCaster();
        Level world = context.getWorld();

        for(ItemStack itemStack : getRecipeItems(getSpell(context))){
            ItemEntity item = new ItemEntity(world, caster.getX(), caster.getY(), caster.getZ(), itemStack);
            world.addFreshEntity(item);
        }

        return true;
    }

    /**
     * @return The spell to be disassembled
     */
    private ItemStack getSpell(IRitualContext context) {
        ItemStack recipeItem = ItemStack.EMPTY;

        for (ItemStack stack : context.getCollectedReagents()) {
            if (SpellRecipe.isReagentContainer(stack)) {
                recipeItem = stack;
                break;
            }
        }

        return recipeItem;
    }

    /**
     * @param dataStack spell to be disassembled
     * @return List of all items that the spell is made of.
     */
    public List<ItemStack> getRecipeItems(ItemStack dataStack) {
        List<ItemStack> items = new ArrayList<>();
        List<ResourceLocation> rLocItems = new ArrayList<>();

        if (!SpellRecipe.isReagentContainer(dataStack) || dataStack == null) {
            return items;
        }

        rLocItems.addAll(SpellRecipe.getShapeReagents(dataStack));
        rLocItems.addAll(SpellRecipe.getComponentReagents(dataStack));
        rLocItems.addAll(SpellRecipe.getModifierReagents(dataStack, 0));
        rLocItems.addAll(SpellRecipe.getModifierReagents(dataStack, 1));
        rLocItems.addAll(SpellRecipe.getModifierReagents(dataStack, 2));
        rLocItems.addAll(SpellRecipe.getComplexityReagents(dataStack));

        for (ResourceLocation indexRloc : rLocItems) {
            if(indexRloc != null){
                items.add(new ItemStack(ForgeRegistries.ITEMS.getValue(indexRloc)));
            }
        }
        items.add(new ItemStack(ItemInit.VELLUM.get()));

        return items;
    }

    @Override
    protected int getApplicationTicks(IRitualContext iRitualContext) {
        return 0;
    }
}
