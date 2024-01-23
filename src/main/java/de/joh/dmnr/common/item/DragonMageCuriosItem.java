package de.joh.dmnr.common.item;

import com.mna.api.items.ITieredItem;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.common.event.DamageEventHandler;
import de.joh.dmnr.common.init.ArmorUpgradeInit;
import de.joh.dmnr.api.item.IDragonMagicContainerItem;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

/**
 * This armor is the main item of this mod.
 * This armor defaults to netherite armor, which can be enhanced with ugprades.
 * The list of upgrades and their effects can be found in ArmorUpgradeInit.
 * In addition, it can be enhanced with spells that are cast when the wearer takes damage. (see DamageEventHandler)
 * @see ArmorUpgradeInit
 * @see DamageEventHandler
 * @author Joh0210
 */
public class DragonMageCuriosItem extends Item implements ITieredItem<DragonMageCuriosItem>, ICurioItem, IDragonMagicContainerItem {
    private final int maxDragonMagic;
    private final String dmSource;

    private int _tier = -1;

    @Override
    public void setCachedTier(int tier) {
        this._tier = tier;
    }

    @Override
    public int getCachedTier() {
        return this._tier;
    }

    public DragonMageCuriosItem(int maxDragonMagic, String dmSource, Properties pProperties) {
        super(pProperties);
        this.maxDragonMagic = maxDragonMagic;
        this.dmSource = dmSource;
    }

    @Override
    public int getMaxDragonMagic(ItemStack itemStack) {
        return maxDragonMagic;
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if(slotContext.entity() instanceof Player){
            this.addDragonMagic(stack, (Player) slotContext.entity(), dmSource);
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if(slotContext.entity() instanceof Player){
            this.removeDragonMagic(stack, (Player) slotContext.entity(), dmSource);
        }
    }

    /**
     * Adds a tooltip (when hovering over the item) to the item.
     * The installed spells and upgrades are listed.
     * Call from the game itself.
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
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
        }
        else{
            tooltip.add(Component.translatable("tooltip.dmnr.armor.tooltip"));
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@NotNull ItemStack itemStack){
        return true;
    }
}
