package de.joh.dragonmagicandrelics.item.items;

import com.mna.factions.Factions;
import com.mna.items.armor.ISetItem;
import de.joh.dragonmagicandrelics.CreativeModeTab;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.item.util.DragonMagicContainer;
import de.joh.dragonmagicandrelics.utils.RLoc;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.extensions.IForgeItem;
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
public class DragonMageArmor extends GeoArmorItem implements IAnimatable, IForgeItem, ISetItem, DragonMagicContainer {
    private final AnimationFactory factory =  GeckoLibUtil.createFactory(this);
    private final ResourceLocation DRAGON_MAGE_ARMOR_SET_BONUS;
    public final String TEXTURE_LOCATION;
    public final ResourceLocation WING_TEXTURE_LOCATION;

    public DragonMageArmor(ArmorMaterial pMaterial, EquipmentSlot pSlot, ResourceLocation faction) {
        super(pMaterial, pSlot, new Item.Properties().tab(CreativeModeTab.CreativeModeTab).rarity(Rarity.EPIC).fireResistant());

        if(faction == Factions.COUNCIL.getRegistryName()){
            TEXTURE_LOCATION = "textures/models/armor/arch_dragon_mage_armor_texture.png";
            WING_TEXTURE_LOCATION = RLoc.create("textures/models/armor/arch_dragon_wing.png");
            DRAGON_MAGE_ARMOR_SET_BONUS = RLoc.create(DragonMagicAndRelics.MOD_ID + "_arch_armor_set_bonus");
        } else if(faction == Factions.FEY.getRegistryName()){
            TEXTURE_LOCATION = "textures/models/armor/wild_dragon_mage_armor_texture.png";
            WING_TEXTURE_LOCATION = RLoc.create("textures/models/armor/wild_dragon_wing.png");
            DRAGON_MAGE_ARMOR_SET_BONUS = RLoc.create(DragonMagicAndRelics.MOD_ID + "_wild_armor_set_bonus");
        } else if(faction == Factions.UNDEAD.getRegistryName()){
            TEXTURE_LOCATION = "textures/models/armor/abyssal_dragon_mage_armor_texture.png";
            WING_TEXTURE_LOCATION = RLoc.create("textures/models/armor/abyssal_dragon_wing.png");
            DRAGON_MAGE_ARMOR_SET_BONUS = RLoc.create(DragonMagicAndRelics.MOD_ID + "_abyssal_armor_set_bonus");
        } else{
            TEXTURE_LOCATION = "textures/models/armor/infernal_dragon_mage_armor_texture.png";
            WING_TEXTURE_LOCATION = RLoc.create("textures/models/armor/infernal_dragon_wing.png");
            DRAGON_MAGE_ARMOR_SET_BONUS = RLoc.create(DragonMagicAndRelics.MOD_ID + "_infernal_armor_set_bonus");
        }
    }

    /**
     * Depending on the faction, a different texture will be returned.
     */
    public ResourceLocation getTextureLocation(){
//        return new ResourceLocation("minecraft:textures/entity/end_portal.png");
        return RLoc.create(TEXTURE_LOCATION);
    }

    @Override
    public int getMaxDragonMagic() {
        return slot == EquipmentSlot.CHEST ? 64 : 0; //Todo: adjust
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

    //todo
//    /**
//     * Adds a tooltip (when hovering over the item) to the item.
//     * The installed spells and upgrades are listed.
//     * Call from the game itself.
//     */
//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
//        if(this.slot != EquipmentSlot.CHEST){
//            return;
//        }
//
//        if(Screen.hasShiftDown()){
//            if(stack.hasTag()){
//                //Spell Tooltiip
//                if(!stack.getTag().getString(DragonMagicAndRelics.MOD_ID +"spell_other_name").equals("")){
//                    TranslatableComponent component = new TranslatableComponent("tooltip.dragonmagicandrelics.armor.tooltip.spell.other");
//                    tooltip.add(new TextComponent(component.getString() + stack.getTag().getString(DragonMagicAndRelics.MOD_ID +"spell_other_name")));
//                }
//                if(!stack.getTag().getString(DragonMagicAndRelics.MOD_ID +"spell_self_name").equals("")){
//                    TranslatableComponent component = new TranslatableComponent("tooltip.dragonmagicandrelics.armor.tooltip.spell.self");
//                    tooltip.add(new TextComponent(component.getString() + stack.getTag().getString(DragonMagicAndRelics.MOD_ID +"spell_self_name")));
//                }
//
//                //Upgrade Tooltip
//                tooltip.add(new TranslatableComponent("tooltip.dragonmagicandrelics.armor.tooltip.upgrade.base"));
//                ArmorUpgrade[] allUpgrades = ArmorUpgradeInit.getAllUpgrades();
//                for(ArmorUpgrade upgrade : allUpgrades){
//                    if(!stack.getTag().getString(DragonMagicAndRelics.MOD_ID + "ArmorTag_" + upgrade.getUpgradeId()).equals("") && !stack.getTag().getString(DragonMagicAndRelics.MOD_ID + "ArmorTag_" + upgrade.getUpgradeId()).equals("0")){
//                        TranslatableComponent component = new TranslatableComponent("tooltip.dragonmagicandrelics.armor.tooltip.upgrade."+upgrade.getUpgradeId());
//                        tooltip.add(new TextComponent(component.getString() + ": " + stack.getTag().getString(DragonMagicAndRelics.MOD_ID + "ArmorTag_" + upgrade.getUpgradeId())));
//                    }
//                }
//            }
//        }
//        else{
//            tooltip.add(new TranslatableComponent("tooltip.dragonmagicandrelics.armor.tooltip"));
//        }
//    }

    /**
     * Armor does not break
     */
    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return super.damageItem(stack, 0, entity, onBroken);
    }

    @Override
    public ResourceLocation getSetIdentifier() {
        return DRAGON_MAGE_ARMOR_SET_BONUS;
    }

    @Override
    public int itemsForSetBonus() {
        return 4;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller",
                20, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public boolean isFoil(ItemStack itemStack){
//        return false;
//    }
}
