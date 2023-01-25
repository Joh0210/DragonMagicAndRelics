package de.joh.dragonmagicandrelics.item.items;

import com.mna.ManaAndArtifice;
import com.mna.api.capabilities.Faction;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.items.IFactionSpecific;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.items.armor.ISetItem;
import de.joh.dragonmagicandrelics.CreativeModeTab;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgrade;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradePotionEffect;
import de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonarmortick.IArmorUpgradeOnArmorTick;
import de.joh.dragonmagicandrelics.armorupgrades.armorupgradeonfullyequipped.IArmorUpgradeOnFullyEquipped;
import de.joh.dragonmagicandrelics.config.CommonConfigs;
import de.joh.dragonmagicandrelics.effects.EffectInit;
import de.joh.dragonmagicandrelics.utils.RLoc;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeItem;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
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
public class DragonMageArmor extends GeoArmorItem implements IAnimatable, IForgeItem, ISetItem, IFactionSpecific {
    private final AnimationFactory factory = new AnimationFactory(this);
    private final ResourceLocation DRAGON_MAGE_ARMOR_SET_BONUS;
    public final String TEXTURE_LOCATION;

    public DragonMageArmor(ArmorMaterial pMaterial, EquipmentSlot pSlot, Faction faction) {
        super(pMaterial, pSlot, new Item.Properties().tab(CreativeModeTab.CreativeModeTab).rarity(Rarity.EPIC).fireResistant());

        if(faction == Faction.ANCIENT_WIZARDS){
            TEXTURE_LOCATION = "textures/models/armor/arch_dragon_mage_armor_texture.png";
            DRAGON_MAGE_ARMOR_SET_BONUS = RLoc.create(DragonMagicAndRelics.MOD_ID + "_arch_armor_set_bonus");
        } else if(faction == Faction.FEY_COURT){
            TEXTURE_LOCATION = "textures/models/armor/wild_dragon_mage_armor_texture.png";
            DRAGON_MAGE_ARMOR_SET_BONUS = RLoc.create(DragonMagicAndRelics.MOD_ID + "_wild_armor_set_bonus");
        } else if(faction == Faction.UNDEAD){
            TEXTURE_LOCATION = "textures/models/armor/abyssal_dragon_mage_armor_texture.png";
            DRAGON_MAGE_ARMOR_SET_BONUS = RLoc.create(DragonMagicAndRelics.MOD_ID + "_abyssal_armor_set_bonus");
        } else{
            TEXTURE_LOCATION = "textures/models/armor/infernal_dragon_mage_armor_texture.png";
            DRAGON_MAGE_ARMOR_SET_BONUS = RLoc.create(DragonMagicAndRelics.MOD_ID + "_infernal_armor_set_bonus");
        }
    }

    /**
     * Depending on the faction, a different texture will be returned.
     */
    public String getTextureLocation(){
        return TEXTURE_LOCATION;
    }

    /**
     * Executes the effects of ARMOR_UPGRADE_ON_ARMOR_TICK effects every tick.
     * In addition, the potion effects of the ARMOR_UPGRADE_POTION_EFFECT are updated.
     * Call from the game itself.
     * @see ArmorUpgradeInit
     */
    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        if(slot == EquipmentSlot.CHEST && isSetEquipped(player)){
            for(IArmorUpgradeOnFullyEquipped upgrade : ArmorUpgradeInit.ARMOR_UPGRADE_ON_FULLY_EQUIPPED){
                upgrade.applySetBonus(player, getUpgradeLevel(upgrade, player));
            }

            this.usedByPlayer(player);
            IPlayerMagic magic = player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);

            //Armor Upgrades: On Armor Tick
            for(IArmorUpgradeOnArmorTick upgrade : ArmorUpgradeInit.ARMOR_UPGRADE_ON_ARMOR_TICK){
                upgrade.onArmorTick(world, player, getUpgradeLevel(upgrade, player), magic);
            }

            //Armor Upgrades: Potion Effects
            if(!world.isClientSide()) {
                if(this.isSetEquipped(player)) {
                    for(ArmorUpgradePotionEffect upgrade : ArmorUpgradeInit.ARMOR_UPGRADE_POTION_EFFECT){
                        if(getUpgradeLevel(upgrade, player) > 0){
                            upgrade.applyPotionAffect(player, getUpgradeLevel(upgrade, player));
                        }
                    }
                }
            }
        }
    }

    /**
     * Removes the permanent effects of ARMOR_UPGRADE_ON_FULLY_EQUIPPED when armor is deequipped.
     * Removes the Night Vision effect in case the Night Vision upgrade was installed.
     * Reset the fly values in case the fly upgrade was installed.
     * Call from the game itself.
     */
    @Override
    public void removeSetBonus(LivingEntity living, EquipmentSlot... setSlots) {
        living.removeEffect(MobEffects.NIGHT_VISION);

        if (living instanceof Player player) {
            for(IArmorUpgradeOnFullyEquipped upgrade : ArmorUpgradeInit.ARMOR_UPGRADE_ON_FULLY_EQUIPPED){
                upgrade.removeSetBonus(player);
            }

            ManaAndArtifice.instance.proxy.setFlySpeed(player, 0.05F);
            ManaAndArtifice.instance.proxy.setFlightEnabled(player, false);
        }
    }

    /**
     * @param upgrade Upgrade to review.
     * @param entity Wearer of armor.
     * @return What level of upgrade is installed.
     */
    public int getUpgradeLevel(ArmorUpgrade upgrade, LivingEntity entity){
        //Chestplate only and Full Armor only
        if (!(entity instanceof Player) || slot != EquipmentSlot.CHEST || !this.isSetEquipped(entity)){
            return 0;
        }

        if(entity.hasEffect(EffectInit.ULTIMATE_ARMOR.get())){
            return upgrade.getMaxUpgradeLevel();
        }

        ItemStack chest = entity.getItemBySlot(EquipmentSlot.CHEST);
        if(!chest.hasTag() || chest.getTag().getString(DragonMagicAndRelics.MOD_ID + "ArmorTag_" + upgrade.getUpgradeId()).equals("")){
            return 0;
        }
        else{
            try{
                int level = Integer.parseInt(chest.getTag().getString(DragonMagicAndRelics.MOD_ID + "ArmorTag_" + upgrade.getUpgradeId()));
                return (0 <= level && level <= upgrade.getMaxUpgradeLevel())? level : 0;
            }catch (NumberFormatException ex){
                return 0;
            }
        }
    }

    /**
     * Adds an upgrade to armor.
     * @param upgrade Upgrade to install.
     * @param level Level at which the upgrade should be installed.
     * @param entity Wearer of armor.
     */
    public void setUpgradeLevel(ArmorUpgrade upgrade, int level, LivingEntity entity){
        //Chestplate only
        if (!(entity instanceof Player) || slot != EquipmentSlot.CHEST){
            return;
        }

        if(upgrade.isLevelCorreckt(level)){
            ItemStack chest = entity.getItemBySlot(EquipmentSlot.CHEST);
            if(chest.hasTag() && !chest.getTag().getString(DragonMagicAndRelics.MOD_ID + "ArmorTag_" + upgrade.getUpgradeId()).equals("")){
                chest.getTag().remove(DragonMagicAndRelics.MOD_ID + "ArmorTag_" + upgrade.getUpgradeId());
            }
            CompoundTag nbtData = new CompoundTag();
            nbtData.putString(DragonMagicAndRelics.MOD_ID + "ArmorTag_" + upgrade.getUpgradeId(), Integer.toString(level));
            chest.getTag().merge(nbtData);
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
        if(this.slot != EquipmentSlot.CHEST){
            return;
        }

        if(Screen.hasShiftDown()){
            if(stack.hasTag()){
                //Spell Tooltiip
                if(!stack.getTag().getString(DragonMagicAndRelics.MOD_ID +"spell_other_name").equals("")){
                    TranslatableComponent component = new TranslatableComponent("tooltip.dragonmagicandrelics.armor.tooltip.spell.other");
                    tooltip.add(new TextComponent(component.getString() + stack.getTag().getString(DragonMagicAndRelics.MOD_ID +"spell_other_name")));
                }
                if(!stack.getTag().getString(DragonMagicAndRelics.MOD_ID +"spell_self_name").equals("")){
                    TranslatableComponent component = new TranslatableComponent("tooltip.dragonmagicandrelics.armor.tooltip.spell.self");
                    tooltip.add(new TextComponent(component.getString() + stack.getTag().getString(DragonMagicAndRelics.MOD_ID +"spell_self_name")));
                }

                //Upgrade Tooltip
                tooltip.add(new TranslatableComponent("tooltip.dragonmagicandrelics.armor.tooltip.upgrade.base"));
                ArmorUpgrade[] allUpgrades = ArmorUpgradeInit.getAllUpgrades();
                for(ArmorUpgrade upgrade : allUpgrades){
                    if(!stack.getTag().getString(DragonMagicAndRelics.MOD_ID + "ArmorTag_" + upgrade.getUpgradeId()).equals("") && !stack.getTag().getString(DragonMagicAndRelics.MOD_ID + "ArmorTag_" + upgrade.getUpgradeId()).equals("0")){
                        TranslatableComponent component = new TranslatableComponent("tooltip.dragonmagicandrelics.armor.tooltip.upgrade."+upgrade.getUpgradeId());
                        tooltip.add(new TextComponent(component.getString() + ": " + stack.getTag().getString(DragonMagicAndRelics.MOD_ID + "ArmorTag_" + upgrade.getUpgradeId())));
                    }
                }
            }
        }
        else{
            tooltip.add(new TranslatableComponent("tooltip.dragonmagicandrelics.armor.tooltip"));
        }
    }

    /**
     * Processing of the Elytra upgrade.
     * Call from the game itself.
     */
    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return (getUpgradeLevel(ArmorUpgradeInit.ELYTRA, entity) > 0) && !entity.isInWaterOrBubble() && !entity.isInLava() && this.isSetEquipped(entity) && entity.getCapability(PlayerMagicProvider.MAGIC).isPresent() && entity.getCapability(PlayerMagicProvider.MAGIC).orElse(null).getCastingResource().hasEnoughAbsolute(entity, CommonConfigs.getElytraManaCostPerTick());
    }

    /**
     * Processing of the elytra upgrade and the of the elytra-boost
     * Call from the game itself.
     */
    @Override
    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        if (!(entity instanceof Player)) {
            return false;
        } else if(getUpgradeLevel(ArmorUpgradeInit.ELYTRA, entity) == 1){
            return true;
        } else {
            if (flightTicks % 100 == 0) {
                this.usedByPlayer((Player)entity);
            }

            IPlayerMagic magic = entity.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
            if (magic != null && magic.getCastingResource().hasEnoughAbsolute(entity, 0.75F)) {
                Vec3 look = entity.getLookAngle();
                Vec3 pos;
                float maxLength;
                double lookScale;
                Vec3 scaled_look;
                if (!entity.isShiftKeyDown()) {
                    magic.getCastingResource().consume(entity, CommonConfigs.getElytraManaCostPerTick());
                    pos = entity.getDeltaMovement();
                    maxLength = 1.75F;
                    lookScale = 0.06D;
                    scaled_look = look.scale(lookScale);
                    pos = pos.add(scaled_look);
                    if (pos.length() > (double)maxLength) {
                        pos = pos.scale((double)maxLength / pos.length());
                    }
                    entity.setDeltaMovement(pos);
                } else {
                    magic.getCastingResource().consume(entity, CommonConfigs.getElytraManaCostPerTick() /2.0f);
                    pos = entity.getDeltaMovement();
                    maxLength = 0.1F;
                    lookScale = -0.01D;
                    scaled_look = look.scale(lookScale);
                    pos = pos.add(scaled_look);
                    if (pos.length() < (double)maxLength) {
                        pos = pos.scale((double)maxLength / pos.length());
                    }
                    entity.setDeltaMovement(pos);
                }

                if (entity.level.isClientSide) {
                    pos = entity.position().add(look.scale(3.0D));
                    for(int i = 0; i < 5; ++i) {
                        entity.level.addParticle((new MAParticleType(ParticleInit.AIR_VELOCITY.get())).setScale(0.2F).setColor(10, 10, 10), pos.x - 0.5D + Math.random(), pos.y - 0.5D + Math.random(), pos.z - 0.5D + Math.random(), -look.x, -look.y, -look.z);
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }

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
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
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
