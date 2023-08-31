package de.joh.dragonmagicandrelics.events;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.entities.IFactionEnemy;
import com.mna.api.events.ComponentApplyingEvent;
import com.mna.api.faction.IFaction;
import com.mna.api.items.ChargeableItem;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedSpellEffect;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.config.GeneralModConfig;
import com.mna.effects.EffectInit;
import com.mna.entities.sorcery.EntityDecoy;
import com.mna.entities.utility.PresentItem;
import com.mna.factions.Factions;
import com.mna.interop.CuriosInterop;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import com.mna.tools.InventoryUtilities;
import com.mna.tools.ProjectileHelper;
import com.mna.tools.SummonUtils;
import com.mna.tools.TeleportHelper;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.capabilities.dragonmagic.ArmorUpgradeHelper;
import de.joh.dragonmagicandrelics.commands.Commands;
import de.joh.dragonmagicandrelics.config.CommonConfigs;
import de.joh.dragonmagicandrelics.item.ItemInit;
import de.joh.dragonmagicandrelics.item.items.BraceletOfFriendship;
import de.joh.dragonmagicandrelics.item.items.dragonmagearmor.DragonMageArmor;
import de.joh.dragonmagicandrelics.utils.ProjectileReflectionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
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
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.SlotTypePreset;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicReference;

/**
 * These event handlers take care of processing damage events.
 * Functions marked with @SubscribeEvent are called by the forge event bus handler.
 * @author Joh0210
 */
@Mod.EventBusSubscriber(modid = DragonMagicAndRelics.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DamageEventHandler {

    /**
     * Processing of the damage boost and damaga resistance upgrades.
     * Casts a spell on the player or the source when the wearer of the Dragon Mage Armor takes damage.
     * @see ArmorUpgradeInit
     * @see Commands
     * @see de.joh.dragonmagicandrelics.item.items.FactionAmulet
     */
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        Entity source = event.getSource().getEntity();
        LivingEntity living = event.getEntityLiving();

        if(living instanceof Player player){
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);

            //Spell
            if(!chest.isEmpty() && !player.level.isClientSide && chest.getItem() instanceof DragonMageArmor dragonMageArmor && dragonMageArmor.isSetEquipped(player)) {
                DragonMageArmor.applySpell(chest, false, player, source);
                if(source != null && source != player){
                    DragonMageArmor.applySpell(chest, true, player, source);
                }
            }

            //Damage Resistance
            event.setAmount(event.getAmount() * (1.0f - (float) ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.DAMAGE_RESISTANCE)* CommonConfigs.getDamageResistanceDamageReductionPerLevel()));
        }

        AtomicReference<IFaction> faction = new AtomicReference<>(null);
        if(living instanceof Player player){
            player.getCapability(ManaAndArtificeMod.getProgressionCapability()).ifPresent((p)-> faction.set(p.getAlliedFaction()));
        }

        if((living instanceof IFactionEnemy || faction.get() != null) && source instanceof LivingEntity && ((LivingEntity)source).hasEffect(de.joh.dragonmagicandrelics.effects.EffectInit.PEACE_EFFECT.get())){
            ((LivingEntity)source).removeEffect(de.joh.dragonmagicandrelics.effects.EffectInit.PEACE_EFFECT.get());
            ((LivingEntity)source).addEffect(new MobEffectInstance((de.joh.dragonmagicandrelics.effects.EffectInit.BROKEN_PEACE_EFFECT.get()), 12000));
            if(source instanceof Player){
                source.getLevel().playSound(null, source.getX(), source.getY(), source.getZ(), SoundEvents.RAID_HORN, SoundSource.PLAYERS, 64.0F, 0.9F + (float)Math.random() * 0.2F);
            }

        }

        //Damage Boost
        if (source instanceof Player player){
            event.setAmount(event.getAmount() * (1.0f + ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.DAMAGE_BOOST)*0.25f));
        }
    }

    /**
     * Prevents friends from getting hurt
     * @see BraceletOfFriendship
     */
    @SubscribeEvent
    public static void onComponentApplying(ComponentApplyingEvent event){
        if(event.getSource().getCaster() instanceof Player player && event.getTarget().isLivingEntity() && event.getComponent().getUseTag() == SpellPartTags.HARMFUL){
            for(SlotResult curios : CuriosApi.getCuriosHelper().findCurios(player, ItemInit.BRACELET_OF_FRIENDSHIP.get())){
                if(curios.stack().getItem() instanceof BraceletOfFriendship){
                    for(Player friend : ((BraceletOfFriendship)curios.stack().getItem()).getPlayerTargets(curios.stack(), player.getLevel())){
                        if(friend == event.getTarget().getEntity()){
                            event.setCanceled(true);
                            return;
                        }
                    }
                }
            }
        }
    }

    /**
     * @see de.joh.dragonmagicandrelics.item.items.FactionAmulet
     */
    @SubscribeEvent
    public static void onLivingChangeTarget(LivingChangeTargetEvent event) {
        if(event.getEntityLiving() instanceof IFactionEnemy){
            if(event.getNewTarget() == null){
                return;
            }

            if(event.getNewTarget().hasEffect(de.joh.dragonmagicandrelics.effects.EffectInit.PEACE_EFFECT.get())){
                event.setNewTarget(null);
            } else if(!event.getNewTarget().hasEffect(de.joh.dragonmagicandrelics.effects.EffectInit.BROKEN_PEACE_EFFECT.get())
                    && ((ChargeableItem)(ItemInit.FACTION_AMULET.get())).isEquippedAndHasMana(event.getNewTarget(), 50.0F, true)){
                event.getNewTarget().addEffect(new MobEffectInstance((de.joh.dragonmagicandrelics.effects.EffectInit.PEACE_EFFECT.get()), 600));
                event.setNewTarget(null);
            }
        }
    }

    /**
     * Processing of the projectile reflection, fire resistance, explosion resistance and kinetic resistance upgrades resistance through jumpboost.
     * @see ArmorUpgradeInit
     * @see de.joh.dragonmagicandrelics.item.items.AngelRing
     */
    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if(event.getEntityLiving() instanceof Player player){
            if (checkAndConsumeVoidfeatherCharm(event, player)) {
                return;
            }
            DamageSource source = event.getSource();
            if (!player.level.isClientSide) {
                //protection against fire
                if (source.isFire() && ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.MAJOR_FIRE_RESISTANCE) >= 1) {
                    event.setCanceled(true);
                    return;
                } else if (source.isFire() && ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.MINOR_FIRE_RESISTANCE) >= 1) {
                    IPlayerMagic magic = player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
                    if (magic != null && magic.getCastingResource().hasEnoughAbsolute(player, CommonConfigs.FIRE_RESISTANCE_MANA_PER_FIRE_DAMAGE.get())) {
                        magic.getCastingResource().consume(player, CommonConfigs.FIRE_RESISTANCE_MANA_PER_FIRE_DAMAGE.get());
                        event.setCanceled(true);
                        return;
                    }
                }

                //protection against explosions
                if (source.isExplosion() && ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.EXPLOSION_RESISTANCE) == 1) {
                    event.setCanceled(true);
                    return;
                }

                //Protection from falling through jumpboost
                if(source.isFall()){
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
                if((source.isFall() || source == DamageSource.FLY_INTO_WALL) && ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.KINETIC_RESISTANCE) == 1){
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
            if((source.isFall() || source == DamageSource.FLY_INTO_WALL) && (CuriosApi.getCuriosHelper().findEquippedCurio(ItemInit.ANGEL_RING.get(), player).isPresent() || CuriosApi.getCuriosHelper().findEquippedCurio(ItemInit.FALLEN_ANGEL_RING.get(), player).isPresent()            )){
                event.setCanceled(true);
                return;
            }

            if(source.getEntity() instanceof Player sourceEntity){
                for(SlotResult curios : CuriosApi.getCuriosHelper().findCurios(sourceEntity, ItemInit.BRACELET_OF_FRIENDSHIP.get())){
                    if(curios.stack().getItem() instanceof BraceletOfFriendship){
                        for(Player friend : ((BraceletOfFriendship)curios.stack().getItem()).getPlayerTargets(curios.stack(), player.getLevel())){
                            if(friend  == player){
                                event.setCanceled(true);
                                return;
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
        if(event.getEntityLiving() instanceof Player player){
            if (!player.level.isClientSide) {

                //Protection from falling through jumpboost
                if(event.getSource().isFall()){
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

                if (ArmorUpgradeHelper.getUpgradeLevel(player, ArmorUpgradeInit.MIST_FORM) >= 1 && !event.getSource().isBypassInvul() && player.getHealth() > 1.0F && event.getAmount() > player.getHealth()) {
                    player.addEffect(new MobEffectInstance(EffectInit.MIST_FORM.get(), 200, 0, true, true));
                    player.setHealth(1.0F);
                    event.setCanceled(true);
                }
            }
        }

        //Receiving Souls through Mana Reagen for Undeads
        LivingEntity living = event.getEntityLiving();
        Entity source = event.getSource().getEntity();
        if (source instanceof LivingEntity && source != event.getEntity() && source instanceof Player sourcePlayer) {
            sourcePlayer.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent((p) -> {
                if (p.getAlliedFaction() == Factions.UNDEAD) {
                    if (!sourcePlayer.level.isClientSide) {
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
        if (!player.isCreative() && !player.isSpectator() && !player.level.isClientSide) {
            ServerPlayer spe = (ServerPlayer)player;

            if (event.getSource() == DamageSource.OUT_OF_WORLD && player.getHealth() - event.getAmount() <= 10) {
                boolean consumed_charm = false;
                if (CuriosInterop.IsItemInCurioSlot(ItemInit.VOIDFEATHER_CHARM.get(), player, SlotTypePreset.CHARM)) {
                    consumed_charm = true;
                    CuriosInterop.DamageCurioInSlot(ItemInit.VOIDFEATHER_CHARM.get(), player, SlotTypePreset.CHARM, 999);
                } else if (InventoryUtilities.removeItemFromInventory(new ItemStack(ItemInit.VOIDFEATHER_CHARM.get()), true, true, new InvWrapper(player.getInventory()))) {
                    consumed_charm = true;
                }

                if (consumed_charm) {
                    event.setCanceled(true);
                    player.resetFallDistance();
                    BlockPos bedPos = spe.getRespawnPosition();

                    if (bedPos == null) {
                        bedPos = player.level.getServer().getLevel(player.level.dimension()).getSharedSpawnPos();
                    }

                    player.level.playSound(null, spe.getX(), spe.getY(), spe.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 0.9F + (float)Math.random() * 0.2F);
                    TeleportHelper.teleportEntity(spe,spe.getRespawnDimension(), new Vec3((double)bedPos.getX() + 0.5, (double)bedPos.getY(), (double)bedPos.getZ() + 0.5));
                    player.level.playSound(null, bedPos.getX(), bedPos.getY(), bedPos.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 0.9F + (float)Math.random() * 0.2F);
                    player.level.broadcastEntityEvent(spe, (byte)46);
                    return true;
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
                    restoreAmount.setValue(GeneralModConfig.MA_SOULS_PLAYER.get());
                } else if (target instanceof Villager) {
                    restoreAmount.setValue(GeneralModConfig.MA_SOULS_VILLAGER.get());
                } else if (target instanceof IFactionEnemy) {
                    restoreAmount.setValue(GeneralModConfig.MA_SOULS_FACTION.get());
                } else if (((LivingEntity)target).isInvertedHealAndHarm()) {
                    restoreAmount.setValue(GeneralModConfig.MA_SOULS_UNDEAD.get());
                } else if (target instanceof Animal) {
                    restoreAmount.setValue(GeneralModConfig.MA_SOULS_ANIMAL.get());
                } else if (target instanceof AbstractGolem) {
                    restoreAmount.setValue(0.0F);
                } else if (target instanceof Mob) {
                    restoreAmount.setValue(GeneralModConfig.MA_SOULS_MOB.get());
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
        if(event.getEntityLiving().getType() == EntityType.ENDER_DRAGON){
            Level world = event.getEntityLiving().getLevel();
            PresentItem item = new PresentItem(world, event.getEntityLiving().getX(), event.getEntityLiving().getY(), event.getEntityLiving().getZ(), new ItemStack(ItemInit.DRAGON_CORE.get()));
            world.addFreshEntity(item);
        }
    }
}
