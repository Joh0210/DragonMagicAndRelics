package de.joh.dmnr.common.item;

import com.mna.api.faction.IFaction;
import com.mna.api.items.IFactionSpecific;
import com.mna.api.items.TieredItem;
import com.mna.factions.Factions;
import de.joh.dmnr.common.util.RLoc;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeItem;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;

/**
 * This item allows a player to fly in creative mode or use the Elytra Fly.
 * The version for the undead does not consume any mana.
 * @author Joh0210
 */
public class FallenAngelRingItem extends TieredItem implements IForgeItem, ICurioItem, IFactionSpecific {

    public FallenAngelRingItem(Properties itemProperties) {
        super(itemProperties);
    }

    public static ResourceLocation getWingTextureLocation(){
        return RLoc.create("textures/models/fallen_angel_ring_wing.png");
    }

    @Override
    public IFaction getFaction() {
        return Factions.UNDEAD;
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        slotContext.entity().getPersistentData().putBoolean("bone_armor_set_bonus", true);
        onEquip(slotContext.identifier(), slotContext.index(), slotContext.entity(), stack);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        slotContext.entity().getPersistentData().putBoolean("bone_armor_set_bonus", false);
        onUnequip(slotContext.identifier(), slotContext.index(), slotContext.entity(), stack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@Nullable ItemStack itemStack){
        return true;
    }
}
