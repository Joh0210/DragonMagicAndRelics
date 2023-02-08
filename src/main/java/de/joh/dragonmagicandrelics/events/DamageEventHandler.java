package de.joh.dragonmagicandrelics.events;

import com.ma.api.capabilities.Faction;
import com.ma.api.capabilities.IPlayerMagic;
import com.ma.api.entities.IFactionEnemy;
import com.ma.api.spells.ComponentApplicationResult;
import com.ma.api.spells.targeting.SpellContext;
import com.ma.api.spells.targeting.SpellSource;
import com.ma.api.spells.targeting.SpellTarget;
import com.ma.capabilities.playerdata.magic.PlayerMagicProvider;
import com.ma.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.ma.config.GeneralModConfig;
import com.ma.effects.EffectInit;
import com.ma.entities.utility.EntityPresentItem;
import com.ma.events.delayed.DelayedEventQueue;
import com.ma.events.delayed.TimedDelayedSpellEffect;
import com.ma.items.artifice.FactionSpecificSpellModifierRing;
import com.ma.spells.crafting.SpellRecipe;
import com.ma.tools.ProjectileHelper;
import com.ma.tools.SummonUtils;
import de.joh.dragonmagicandrelics.Commands;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeProjectileReflectionHelper;
import de.joh.dragonmagicandrelics.config.CommonConfigs;
import de.joh.dragonmagicandrelics.item.ItemInit;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraft.entity.*;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;
import javax.annotation.Nullable;

/**
 * These event handlers take care of processing damage events.
 * Functions marked with @SubscribeEvent are called by the forge event bus handler.
 */
@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DamageEventHandler {

    /**
     * Processing of the damage boost and damaga resistance upgrades.
     * Casts a spell on the player or the source when the wearer of the Dragon Mage Armor takes damage.
     * @see ArmorUpgradeInit
     * @see Commands
     * @see de.joh.dragonmagicandrelics.rituals.contexts.FusionRitual
     */
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        Entity source = event.getSource().getTrueSource();
        LivingEntity living = event.getEntityLiving();

        if(living instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) living;
            ItemStack chest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);

            //Spell
            if (!chest.isEmpty() && !player.world.isRemote && chest.getItem() instanceof DragonMageArmor && ((DragonMageArmor) chest.getItem()).isSetEquipped(player)) {
                applySpell(false, player, source, chest);
                if(source != null){
                    applySpell(true, player, source, chest);
                }
            }

            //Damage Resistance
            if(chest.getItem() instanceof DragonMageArmor){
                event.setAmount(event.getAmount() * (1.0f - (float)((DragonMageArmor) chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.DAMAGE_RESISTANCE, player)* CommonConfigs.getDamageResistanceDamageReductionPerLevel()));
            }
        }

        //Damage Boost
        if (source instanceof PlayerEntity){
            ItemStack chest = ((PlayerEntity)source).getItemStackFromSlot(EquipmentSlotType.CHEST);
            if(chest.getItem() instanceof DragonMageArmor) {
                event.setAmount(event.getAmount() * (1.0f + (float)((DragonMageArmor) chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.DAMAGE_BOOST, ((PlayerEntity)source))*0.25f));
            }
        }
    }

    /**
     * Processing of the projectile reflection, fire resistance, explosion resistance and kinetic resistance upgrades resistance through jumpboost.
     * @see ArmorUpgradeInit
     */
    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if(event.getEntityLiving() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            ItemStack chest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
            if (!chest.isEmpty() && !player.world.isRemote && chest.getItem() instanceof DragonMageArmor && ((DragonMageArmor) chest.getItem()).isSetEquipped(player)) {
                DamageSource source = event.getSource();

                //protection against fire
                if (source.isFireDamage() && ((DragonMageArmor) chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.FIRE_RESISTANCE, player) == 1) {

                    IPlayerMagic magic = player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
                    if (magic != null && magic.getCastingResource().getAmount() > CommonConfigs.FIRE_RESISTANCE_MANA_PER_FIRE_DAMAGE.get()) {
                        magic.getCastingResource().consume(CommonConfigs.FIRE_RESISTANCE_MANA_PER_FIRE_DAMAGE.get());
                        event.setCanceled(true);
                        return;
                    }
                }
                else if (source.isFireDamage() && ((DragonMageArmor) chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.FIRE_RESISTANCE, player) == 2) {
                    event.setCanceled(true);
                    return;
                }

                //protection against explosions
                if (source.isExplosion() && ((DragonMageArmor) chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.EXPLOSION_RESISTANCE, player) == 1) {
                    event.setCanceled(true);
                    return;
                }

                //Protection from falling through jumpboost
                if(source == DamageSource.FALL && ((DragonMageArmor) chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.JUMP, player) >= 1){
                    if((event.getAmount() - ((DragonMageArmor) chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.JUMP, player) * 3) <= 0){
                        event.setCanceled(true);
                        return;
                    }
                }

                //Protection from kinetic energy
                if((source == DamageSource.FALL || source == DamageSource.FLY_INTO_WALL) && ((DragonMageArmor) chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.KINETIC_RESISTANCE, player) == 1){
                    event.setCanceled(true);
                    return;
                }

                //Projectile Reflection
                if(source.getImmediateSource() instanceof ProjectileEntity && ((DragonMageArmor) chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.PROJECTILE_REFLECTION, player) > 0 && ArmorUpgradeProjectileReflectionHelper.consumeReflectCharge(player)){
                    event.setCanceled(true);
                    ProjectileHelper.ReflectProjectile(player, (ProjectileEntity)source.getImmediateSource(), true, 10.0F);
                    return;
                }
            }
        }
    }

    /**
     * Processing resistance through jumpboost and the received Souls through Mana Reagen for Undeads
     * @see ArmorUpgradeInit
     */
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if(event.getEntityLiving() instanceof PlayerEntity ){
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            ItemStack chest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
            if (!chest.isEmpty() && !player.world.isRemote && chest.getItem() instanceof DragonMageArmor) {

                //Protection from falling through jumpboost
                if(event.getSource() == DamageSource.FALL && ((DragonMageArmor) chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.JUMP, player) >= 1){
                    int amount = (int)event.getAmount() - ((DragonMageArmor) chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.JUMP, player) * 3;
                    if(amount > 0){
                        event.setAmount(amount);
                    }
                }

                if (((DragonMageArmor) chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.MIST_FORM, player) >= 1 && !event.getSource().canHarmInCreative() && player.getHealth() > 1.0F && event.getAmount() > player.getHealth()) {
                    player.addPotionEffect(new EffectInstance(EffectInit.MIST_FORM.get(), 200, 0, true, true));
                    player.setHealth(1.0F);
                    event.setCanceled(true);
                }
            }
        }

        //Receiving Souls through Mana Reagen for Undeads
        LivingEntity living = event.getEntityLiving();
        Entity source = event.getSource().getTrueSource();
        if (source != null && source instanceof LivingEntity && source != event.getEntity() && source instanceof PlayerEntity) {
            PlayerEntity sourcePlayer = (PlayerEntity) source;
            sourcePlayer.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent((p) -> {
                if (p.getAlliedFaction() == Faction.UNDEAD) {
                    ItemStack chest = sourcePlayer.getItemStackFromSlot(EquipmentSlotType.CHEST);
                    if (!chest.isEmpty() && !sourcePlayer.world.isRemote && chest.getItem() instanceof DragonMageArmor) {
                        int manaRegenLevel = ((DragonMageArmor) chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.getArmorUpgradeFromString("mana_regen"), sourcePlayer);
                        if (manaRegenLevel > 0) {
                            float souls = getSoulsRestored(sourcePlayer, living);
                            if (souls > 0.0F) {
                                sourcePlayer.getCapability(PlayerMagicProvider.MAGIC).ifPresent((m) -> {
                                    m.getCastingResource().restore(souls * 0.05F * manaRegenLevel);
                                    m.getCastingResource().setNeedsSync();
                                });
                            }
                        }
                    }
                }
            });
        }
        if (event.getAmount() <= 0.0F) {
            event.setCanceled(true);
        }
    }

    /** Provides the souls given by a monster to an Undead upon death
     * @param soulRecipient Player attacking the target
     * @param target Target to be checked
     * @return number of souls
     */
    private static float getSoulsRestored(PlayerEntity soulRecipient, Entity target) {
        if (soulRecipient == null) {
            return 0.0F;
        } else if (target instanceof LivingEntity && (!(target instanceof CreatureEntity) || !SummonUtils.isSummon((CreatureEntity)target))) {
            MutableFloat restoreAmount = new MutableFloat(1.0F);
            if (target instanceof PlayerEntity) {
                restoreAmount.setValue((Number)GeneralModConfig.MA_SOULS_PLAYER.get());
            } else if (target instanceof VillagerEntity) {
                restoreAmount.setValue((Number)GeneralModConfig.MA_SOULS_VILLAGER.get());
            } else if (target instanceof IFactionEnemy) {
                restoreAmount.setValue((Number)GeneralModConfig.MA_SOULS_FACTION.get());
            } else if (((LivingEntity)target).isEntityUndead()) {
                restoreAmount.setValue((Number)GeneralModConfig.MA_SOULS_UNDEAD.get());
            } else if (target instanceof AnimalEntity) {
                restoreAmount.setValue((Number)GeneralModConfig.MA_SOULS_ANIMAL.get());
            } else if (target instanceof GolemEntity) {
                restoreAmount.setValue(0.0F);
            } else if (target instanceof MobEntity) {
                restoreAmount.setValue((Number)GeneralModConfig.MA_SOULS_MOB.get());
            }

            if (((LivingEntity)target).isPotionActive((Effect)EffectInit.SOUL_VULNERABILITY.get())) {
                restoreAmount.setValue(restoreAmount.getValue() * 5.0F);
            }

            if (((FactionSpecificSpellModifierRing) com.ma.items.ItemInit.BONE_RING.get()).isEquippedAndHasMana(soulRecipient, 3.5F, true)) {
                restoreAmount.setValue(restoreAmount.getValue() * 2.25F);
            }

            return restoreAmount.getValue();
        } else {
            return 0.0F;
        }
    }

    /**
     * Drops the Dragon Core when the Ender Dragon dies
     */
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event){
        if(event.getEntityLiving().getType() == EntityType.ENDER_DRAGON){
            World world = event.getEntityLiving().getEntityWorld();
            EntityPresentItem item = new EntityPresentItem(world, event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY(), event.getEntityLiving().getPosZ(), new ItemStack(ItemInit.DRAGON_CORE.get()));
            world.addEntity(item);
        }
    }

    /**
     * Performs one of the two spells from the Dragon Mage Armor.
     * @param isOther Should the source be hit? Should the offensive spell be used?
     * @param self Wearer of armor.
     * @param other Source of damage (only as entity).
     * @param chest Dragon Mage Armor Chestplate with the spells
     */
    public static void applySpell(boolean isOther, PlayerEntity self, @Nullable Entity other, ItemStack chest){
        CompoundNBT compoundTag = new CompoundNBT();
        compoundTag.put("spell", chest.getTag().getCompound(DragonMagicAndRelics.MOD_ID+(isOther ? "spell_other" : "spell_self")));
        SpellRecipe recipe = SpellRecipe.fromNBT(compoundTag);

        if (recipe.isValid()) {
            MutableBoolean consumed = new MutableBoolean(false);
            self.getCapability(PlayerMagicProvider.MAGIC).ifPresent((c) -> {
                if (c.getCastingResource().getAmount() > recipe.getManaCost()) {
                    c.getCastingResource().consume(recipe.getManaCost());
                    consumed.setTrue();
                }
            });

            if (consumed.getValue()) {
                SpellSource spellSource = new SpellSource(self, Hand.MAIN_HAND);
                SpellContext context = new SpellContext((ServerWorld) self.world, recipe);
                recipe.iterateComponents((c) -> {
                    int delay = (int)(c.getValue(com.ma.api.spells.attributes.Attribute.DELAY) * 20.0F);
                    boolean appliedComponent = false;
                    if (delay > 0) {
                        DelayedEventQueue.pushEvent(self.world, new TimedDelayedSpellEffect(c.getPart().getRegistryName().toString(), delay, spellSource, new SpellTarget(isOther ? other : self ), c, context));
                        appliedComponent = true;
                    } else if (c.getPart().ApplyEffect(spellSource, new SpellTarget(isOther ? other : self), c, context) == ComponentApplicationResult.SUCCESS) {
                        appliedComponent = true;
                    }
                });
            }
        }
    }
}
