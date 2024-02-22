package de.joh.dmnr.common.event;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.entities.IFactionEnemy;
import com.mna.api.events.ComponentApplyingEvent;
import com.mna.api.faction.IFaction;
import com.mna.api.items.ChargeableItem;
import com.mna.api.spells.SpellPartTags;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.config.GeneralConfig;
import com.mna.effects.EffectInit;
import com.mna.entities.sorcery.EntityDecoy;
import com.mna.entities.utility.PresentItem;
import com.mna.factions.Factions;
import com.mna.interop.CuriosInterop;
import com.mna.tools.InventoryUtilities;
import com.mna.tools.ProjectileHelper;
import com.mna.tools.SummonUtils;
import com.mna.tools.TeleportHelper;
import de.joh.dmnr.DragonMagicAndRelics;
import de.joh.dmnr.api.item.DragonMageArmorItem;
import de.joh.dmnr.capabilities.dragonmagic.ArmorUpgradeHelper;
import de.joh.dmnr.common.command.Commands;
import de.joh.dmnr.common.init.ArmorUpgradeInit;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.common.item.AngelRingItem;
import de.joh.dmnr.common.item.BraceletOfFriendshipItem;
import de.joh.dmnr.common.item.FactionAmuletItem;
import de.joh.dmnr.common.util.CommonConfig;
import de.joh.dmnr.common.util.ProjectileReflectionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.apache.commons.lang3.mutable.MutableFloat;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.SlotTypePreset;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * These event handlers take care of processing damage events.
 * Functions marked with @SubscribeEvent are called by the forge event bus handler.
 * @author Joh0210
 */
@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DamageEventHandler {

    /**
     * Processing of the damage boost and damage resistance upgrades.
     * Casts a spell on the player or the source when the wearer of the Dragon Mage Armor takes damage.
     * <br> - Glass Cannon Belt
     * <br> - Sturdy Belt
     * @see ArmorUpgradeInit
     * @see Commands
     * @see FactionAmuletItem
     */
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        Entity sourceEntity = event.getSource().getEntity();
        LivingEntity targetEntity = event.getEntity();

        if(targetEntity instanceof Player player){
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);

            //Spell
            if(!chest.isEmpty() && !player.level().isClientSide && chest.getItem() instanceof DragonMageArmorItem dragonMageArmor && dragonMageArmor.isSetEquipped(player)) {
                DragonMageArmorItem.applySpell(chest, false, player, sourceEntity);
                if(sourceEntity != null && sourceEntity != player){
                    DragonMageArmorItem.applySpell(chest, true, player, sourceEntity);
                }
            }

            //Damage Resistance
            event.setAmount(event.getAmount() * (1.0f - (float) ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.DAMAGE_RESISTANCE)* CommonConfig.getDamageResistanceDamageReductionPerLevel()));
        }

        AtomicReference<IFaction> faction = new AtomicReference<>(null);
        if(targetEntity instanceof Player player){
            player.getCapability(ManaAndArtificeMod.getProgressionCapability()).ifPresent((p)-> faction.set(p.getAlliedFaction()));
        }

        if((targetEntity instanceof IFactionEnemy || faction.get() != null) && sourceEntity instanceof LivingEntity && ((LivingEntity)sourceEntity).hasEffect(de.joh.dmnr.common.init.EffectInit.PEACE_EFFECT.get())){
            ((LivingEntity)sourceEntity).removeEffect(de.joh.dmnr.common.init.EffectInit.PEACE_EFFECT.get());
            ((LivingEntity)sourceEntity).addEffect(new MobEffectInstance((de.joh.dmnr.common.init.EffectInit.BROKEN_PEACE_EFFECT.get()), 12000));
            if(sourceEntity instanceof Player){
                sourceEntity.level().playSound(null, sourceEntity.getX(), sourceEntity.getY(), sourceEntity.getZ(), SoundEvents.RAID_HORN.get(), SoundSource.PLAYERS, 64.0F, 0.9F + (float)Math.random() * 0.2F);
            }

        }

        //Damage Boost
        if (sourceEntity instanceof Player player){
            event.setAmount(event.getAmount() * (1.0f + ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.DAMAGE_BOOST)*0.25f));
        }

        //Glass Cannon
        if (sourceEntity instanceof LivingEntity && !CuriosApi.getCuriosHelper().findCurios((LivingEntity) sourceEntity, ItemInit.GLASS_CANNON_BELT.get()).isEmpty()){
            event.setAmount(event.getAmount() * Math.max(CommonConfig.MINOTAUR_BELT_MULTIPLICATION.get(), 1));
        }
        if (targetEntity != null && !CuriosApi.getCuriosHelper().findCurios(targetEntity, ItemInit.GLASS_CANNON_BELT.get()).isEmpty()){
            event.setAmount(event.getAmount() * Math.max(CommonConfig.MINOTAUR_BELT_MULTIPLICATION.get(), 1));
        }

        // Sturdy
        if (sourceEntity instanceof LivingEntity && event.getAmount() >= 1 && !CuriosApi.getCuriosHelper().findCurios((LivingEntity) sourceEntity, ItemInit.STURDY_BELT.get()).isEmpty()){
            event.setAmount(Math.max(1, event.getAmount()*0.5f));
        }
        if (targetEntity != null && event.getAmount() >= 1 && !CuriosApi.getCuriosHelper().findCurios(targetEntity, ItemInit.STURDY_BELT.get()).isEmpty()){
            event.setAmount(Math.max(1, event.getAmount()*0.5f));
        }
    }

    /**
     * Prevents friends from getting hurt
     * @see BraceletOfFriendshipItem
     */
    @SubscribeEvent
    public static void onComponentApplying(ComponentApplyingEvent event){
        if(event.getSource().getCaster() instanceof Player player && event.getTarget().isLivingEntity() && event.getComponent().getUseTag() == SpellPartTags.HARMFUL){
            if(player != event.getTarget().getEntity()) {
                for (SlotResult curios : CuriosApi.getCuriosHelper().findCurios(player, ItemInit.BRACELET_OF_FRIENDSHIP.get())) {
                    if (curios.stack().getItem() instanceof BraceletOfFriendshipItem) {
                        Player referenceTarget = playerOrOwner(event.getTarget().getEntity());
                        if(referenceTarget == player){
                            event.setCanceled(true);
                            return;
                        }
                        else if(referenceTarget != null){
                            for (Player friend : ((BraceletOfFriendshipItem) curios.stack().getItem()).getPlayerTargets(curios.stack(), player.level())) {
                                if (friend == referenceTarget) {
                                    event.setCanceled(true);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @return Player itself or PlayerOwner of an Animal
     * <br>-If it is a player, it will be returned.
     * <br>-If it is a {@link OwnableEntity}, the owner is returned if it is a player. If the owner is a {@link OwnableEntity}, the owner will continue to be searched until it is a player or else null is returned.
     * <br>-In all other cases null is returned.
     */
    @Nullable
    public static Player playerOrOwner(Entity target){
        while(target instanceof OwnableEntity){
            target = ((OwnableEntity)target).getOwner();
        }

        if(target instanceof Player){
            return  (Player) target;
        } else{
            return null;
        }
    }

    /**
     * @see FactionAmuletItem
     */
    @SubscribeEvent
    public static void onLivingChangeTarget(LivingChangeTargetEvent event) {
        if(event.getEntity() instanceof IFactionEnemy){
            if(event.getNewTarget() == null){
                return;
            }

            if(event.getNewTarget().hasEffect(de.joh.dmnr.common.init.EffectInit.PEACE_EFFECT.get())){
                event.setNewTarget(null);
            } else if(!event.getNewTarget().hasEffect(de.joh.dmnr.common.init.EffectInit.BROKEN_PEACE_EFFECT.get())
                    && ((ChargeableItem)(ItemInit.FACTION_AMULET.get())).isEquippedAndHasMana(event.getNewTarget(), 50.0F, true)){
                event.getNewTarget().addEffect(new MobEffectInstance((de.joh.dmnr.common.init.EffectInit.PEACE_EFFECT.get()), 600));
                event.setNewTarget(null);
            }
        }
    }

    /**
     * Processing of the projectile reflection, fire resistance, explosion resistance and kinetic resistance upgrades resistance through jumpboost.
     * @see ArmorUpgradeInit
     * @see AngelRingItem
     */
    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        DamageSource source = event.getSource();
        if(event.getEntity() instanceof Player player){
            if (checkAndConsumeVoidfeatherCharm(event, player)) {
                return;
            }
            if (!player.level().isClientSide) {
                //protection against fire
                if (source.is(DamageTypeTags.IS_FIRE) && ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.MAJOR_FIRE_RESISTANCE) >= 1) {
                    event.setCanceled(true);
                    return;
                } else if (source.is(DamageTypeTags.IS_FIRE) && ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.MINOR_FIRE_RESISTANCE) >= 1) {
                    AtomicBoolean doReturn = new AtomicBoolean(false);
                    player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(magic -> {
                        if (magic.getCastingResource().hasEnoughAbsolute(player, CommonConfig.FIRE_RESISTANCE_MANA_PER_FIRE_DAMAGE.get()/5.0f)) {
                            magic.getCastingResource().consume(player, CommonConfig.FIRE_RESISTANCE_MANA_PER_FIRE_DAMAGE.get()/5.0f);
                            event.setCanceled(true);
                            if(player.isOnFire()){
                                player.clearFire();
                            }
                            doReturn.set(true);
                        }
                    });
                    if(doReturn.get()){
                        return;
                    }
                }

                //protection against explosions
                if (source.is(DamageTypeTags.IS_EXPLOSION) && ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.EXPLOSION_RESISTANCE) == 1) {
                    event.setCanceled(true);
                    return;
                }

                //Protection from falling through jumpboost
                if(source.is(DamageTypeTags.IS_FALL)){
                    if(ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.JUMP) >= 1){
                        if((event.getAmount() - ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.JUMP) * 3) <= 0){
                            event.setCanceled(true);
                            return;
                        }
                    }
                    else if(ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.BURNING_FRENZY) >= 1){
                        if((event.getAmount() - ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.BURNING_FRENZY) * 9) <= 0){
                            event.setCanceled(true);
                            return;
                        }
                    }
                }

                //Protection from kinetic energy
                if((source.is(DamageTypeTags.IS_FALL) || source.is(DamageTypes.FLY_INTO_WALL)) && ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.KINETIC_RESISTANCE) == 1){
                    event.setCanceled(true);
                    return;
                }

                //Projectile Reflection
                if(source.getDirectEntity() instanceof Projectile && ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.PROJECTILE_REFLECTION) > 0 && ProjectileReflectionHelper.consumeReflectCharge(player)){
                    event.setCanceled(true);
                    ProjectileHelper.ReflectProjectile(player, (Projectile)source.getDirectEntity(), true, 10.0F);
                    return;
                }
            }

            //Protection from kinetic energy
            if((source.is(DamageTypeTags.IS_FALL) || source.is(DamageTypes.FLY_INTO_WALL)) && CuriosApi.getCuriosHelper().findFirstCurio(player, ItemInit.ANGEL_RING.get()).isPresent()){
                event.setCanceled(true);
                return;
            }
        }

        if(source.getEntity() instanceof Player sourceEntity){
            if(sourceEntity != event.getEntity()) {
                for (SlotResult curios : CuriosApi.getCuriosHelper().findCurios(sourceEntity, ItemInit.BRACELET_OF_FRIENDSHIP.get())) {
                    if (curios.stack().getItem() instanceof BraceletOfFriendshipItem) {
                        Player referenceTarget = playerOrOwner(event.getEntity());
                        if(referenceTarget == sourceEntity){
                            event.setCanceled(true);
                            return;
                        }
                        else if(referenceTarget != null){
                            for (Player friend : ((BraceletOfFriendshipItem) curios.stack().getItem()).getPlayerTargets(curios.stack(), sourceEntity.level())) {
                                if (friend == referenceTarget) {
                                    event.setCanceled(true);
                                    return;
                                }
                            }
                        }
                    }
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
        if(event.getEntity() instanceof Player player){
            if (!player.level().isClientSide) {

                //Protection from falling through jumpboost
                if(event.getSource().is(DamageTypeTags.IS_FALL)){
                    if(ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.JUMP) >= 1){
                        int amount = (int)event.getAmount() - ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.JUMP) * 3;
                        if(amount > 0){
                            event.setAmount(amount);
                        }
                    }
                    else if(ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.BURNING_FRENZY) >= 1){
                        int amount = (int)event.getAmount() - ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.BURNING_FRENZY) * 9;
                        if(amount > 0){
                            event.setAmount(amount);
                        }
                    }
                }

                if (ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.MIST_FORM) >= 1 && !event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) && player.getHealth() > 1.0F && event.getAmount() > player.getHealth()) {
                    player.addEffect(new MobEffectInstance(EffectInit.MIST_FORM.get(), 200, 0, true, true));
                    player.setHealth(1.0F);
                    event.setCanceled(true);
                }
            }
        }

        //Receiving Souls through Mana Reagen for Undeads
        LivingEntity living = event.getEntity();
        Entity source = event.getSource().getEntity();
        if (source instanceof LivingEntity && source != event.getEntity() && source instanceof Player sourcePlayer) {
            sourcePlayer.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent((p) -> {
                if (p.getAlliedFaction() == Factions.UNDEAD) {
                    if (!sourcePlayer.level().isClientSide) {
                        int manaRegenLevel = ArmorUpgradeHelper.getUpgradeLevel(sourcePlayer, ArmorUpgradeInit.MANA_REGEN);
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

    /**
     * @param event  LivingAttackEvent event on which the check takes place
     * @param player Player wearing the charm
     * @return Was the charm used and destroyed?
     */
    private static boolean checkAndConsumeVoidfeatherCharm(LivingAttackEvent event, Player player) {
        if (!player.isCreative() && !player.isSpectator() && !player.level().isClientSide) {
            ServerPlayer spe = (ServerPlayer)player;

            if (event.getSource().is(DamageTypes.FELL_OUT_OF_WORLD) && player.getHealth() - event.getAmount() <= 10) {
                boolean consumed_charm = false;
                BlockPos bedPos = spe.getRespawnPosition();
                if (bedPos == null && player.level().getServer() != null) {
                    ServerLevel level = player.level().getServer().getLevel(player.level().dimension());
                    if(level != null){
                        //todo log else: no valid dimension
                        bedPos = level.getSharedSpawnPos();
                    }
                }

                if(bedPos != null){
                    //todo: log else: no bed found
                    if (CuriosInterop.IsItemInCurioSlot(ItemInit.VOIDFEATHER_CHARM.get(), player, SlotTypePreset.CHARM)) {
                        consumed_charm = true;
                        CuriosInterop.DamageCurioInSlot(ItemInit.VOIDFEATHER_CHARM.get(), player, SlotTypePreset.CHARM, 999);
                    } else if (InventoryUtilities.removeItemFromInventory(new ItemStack(ItemInit.VOIDFEATHER_CHARM.get()), true, true, new InvWrapper(player.getInventory()))) {
                        consumed_charm = true;
                    }

                    if (consumed_charm) {
                        event.setCanceled(true);
                        player.resetFallDistance();

                        player.level().playSound(null, spe.getX(), spe.getY(), spe.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 0.9F + (float)Math.random() * 0.2F);
                        TeleportHelper.teleportEntity(spe,spe.getRespawnDimension(), new Vec3((double)bedPos.getX() + 0.5, bedPos.getY(), (double)bedPos.getZ() + 0.5));
                        player.level().playSound(null, bedPos.getX(), bedPos.getY(), bedPos.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 0.9F + (float)Math.random() * 0.2F);
                        player.level().broadcastEntityEvent(spe, (byte)46);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /** Provides the souls given by a monster to an Undead upon death
     * @param soulRecipient Player attacking the target
     * @param target Target to be checked
     * @return number of souls
     */
    private static float getSoulsRestored(Player soulRecipient, Entity target) {
        if (soulRecipient == null) {
            return 0.0F;
        } else if (target instanceof LivingEntity && (!(target instanceof PathfinderMob) || !SummonUtils.isSummon(target))) {
            if (target instanceof EntityDecoy) {
                return 0.0F;
            } else {
                MutableFloat restoreAmount = new MutableFloat(1.0F);
                if (target instanceof Player) {
                    restoreAmount.setValue(GeneralConfig.SoulsForPlayerKill);
                } else if (target instanceof Villager) {
                    restoreAmount.setValue(GeneralConfig.SoulsForVillagerKill);
                } else if (target instanceof IFactionEnemy) {
                    restoreAmount.setValue(GeneralConfig.SoulsForFactionMobKill);
                } else if (((LivingEntity)target).isInvertedHealAndHarm()) {
                    restoreAmount.setValue(GeneralConfig.SoulsForUndeadKill);
                } else if (target instanceof Animal) {
                    restoreAmount.setValue(GeneralConfig.SoulsForAnimalKill);
                } else if (target instanceof AbstractGolem) {
                    restoreAmount.setValue(0.0F);
                } else if (target instanceof Mob) {
                    restoreAmount.setValue(GeneralConfig.SoulsForMobKill);
                }

                if (((LivingEntity)target).hasEffect(EffectInit.SOUL_VULNERABILITY.get())) {
                    restoreAmount.setValue(restoreAmount.getValue() * 5.0F);
                }

                if (com.mna.items.ItemInit.BONE_RING.get().isEquippedAndHasMana(soulRecipient, 3.5F, true)) {
                    restoreAmount.setValue(restoreAmount.getValue() * 2.25F);
                }

                return restoreAmount.getValue();
            }
        } else {
            return 0.0F;
        }
    }

    /**
     * Drops the Dragon Core when the Ender Dragon dies
     */
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event){
        if(event.getEntity().getType() == EntityType.ENDER_DRAGON){
            Level world = event.getEntity().level();
            PresentItem item = new PresentItem(world, event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), new ItemStack(ItemInit.DRAGON_CORE.get()));
            world.addFreshEntity(item);
        }
    }
}
