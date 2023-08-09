package de.joh.dragonmagicandrelics.item.items;

import de.joh.dragonmagicandrelics.CreativeModeTab;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.capabilities.dragonmagic.ArmorUpgradeHelper;
import de.joh.dragonmagicandrelics.effects.EffectInit;
import de.joh.dragonmagicandrelics.item.util.IDragonMagicContainer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.function.Consumer;

/**
 * This armor is the main item of this mod.
 * This armor defaults to netherite armor, which can be enhanced with ugprades.
 * The list of upgrades and their effects can be found in ArmorUpgradeInit.
 * In addition, it can be enhanced with spells that are cast when the wearer takes damage. (see DamageEventHandler)
 * @see ArmorUpgradeInit
 * @see de.joh.dragonmagicandrelics.events.DamageEventHandler
 * @author Joh0210
 */
public class DragonMageCurios extends Item implements ICurioItem, IDragonMagicContainer {
    private final int maxDragonMagic;
    private final String dmSource;

    public DragonMageCurios(int maxDragonMagic, String dmSource, Properties pProperties) {
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
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        if(Screen.hasShiftDown()){
            if(stack.hasTag()){
                if(stack.getTag().contains(DragonMagicAndRelics.MOD_ID + "armor_upgrade")){
                    CompoundTag nbt = stack.getTag().getCompound(DragonMagicAndRelics.MOD_ID + "armor_upgrade");
                    if(nbt.getAllKeys().size() > 0){
                        tooltip.add(new TranslatableComponent("tooltip.dragonmagicandrelics.armor.tooltip.upgrade.base"));
                        for(String key : nbt.getAllKeys()){
                            if(nbt.getInt(key) > 0){
                                TranslatableComponent component = new TranslatableComponent(key);
                                tooltip.add(new TextComponent(component.getString() + ": " + nbt.getInt(key)));
                            }
                        }
                        tooltip.add(new TextComponent("  "));
                    }
                }
            }

            TranslatableComponent component = new TranslatableComponent("tooltip.dragonmagicandrelics.dm_container.tooltip.remaining.dmpoints");
            tooltip.add(new TextComponent(component.getString() + (getMaxDragonMagic(stack) - getSpentDragonPoints(stack))));
        }
        else{
            tooltip.add(new TranslatableComponent("tooltip.dragonmagicandrelics.armor.tooltip"));
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(ItemStack itemStack){
        return true;
    }
}
