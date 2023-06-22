package de.joh.dragonmagicandrelics.item.items;

import com.mna.api.items.IFactionSpecific;
import com.mna.api.items.TieredItem;
import com.mna.factions.Factions;
import de.joh.dragonmagicandrelics.effects.EffectInit;
import de.joh.dragonmagicandrelics.utils.RLoc;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
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
public class AngelRing extends TieredItem implements IForgeItem, ICurioItem, IFactionSpecific {
    private final ResourceLocation faction;
    private final int level;

    public AngelRing(Properties itemProperties, ResourceLocation faction) {
        super(itemProperties);
        this.faction = faction;
        this.level = (faction == Factions.UNDEAD.getRegistryName()) ? 2 : 1;
    }

    public static ResourceLocation getWingTextureLocation(ResourceLocation faction){
        if(faction == Factions.FEY.getRegistryName()) {
            return RLoc.create("textures/models/angel_ring_wing.png");
        }
        else {
            return RLoc.create("textures/models/fallen_angel_ring_wing.png");
        }
    }

    @Override
    public ResourceLocation getFaction() {
        return faction;
    }

    @Override
    public boolean makesPiglinsNeutral(SlotContext slotContext, ItemStack stack){
        return faction == Factions.FEY.getRegistryName();
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() instanceof Player player && player.hasEffect(EffectInit.ELYTRA.get())){
            player.removeEffect(EffectInit.ELYTRA.get());
        }
        onUnequip(slotContext.identifier(), slotContext.index(), slotContext.entity(), stack);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player){
            if(!player.hasEffect(EffectInit.ELYTRA.get()) || player.getEffect(EffectInit.ELYTRA.get()).getAmplifier() < (level)){
                player.addEffect(new MobEffectInstance(EffectInit.ELYTRA.get(), 100000, level, false, false, true));
            }
            else{
                player.getEffect(EffectInit.ELYTRA.get()).update(new MobEffectInstance(EffectInit.ELYTRA.get(), 100000, level, false, false, true));
            }
        }
        curioTick(slotContext.identifier(), slotContext.index(), slotContext.entity(), stack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@Nullable ItemStack itemStack){
        return true;
    }
}