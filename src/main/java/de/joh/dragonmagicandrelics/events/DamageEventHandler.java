package de.joh.dragonmagicandrelics.events;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.capabilities.Faction;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.entities.IFactionEnemy;
import com.mna.api.items.ChargeableItem;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedSpellEffect;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgression;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.config.GeneralModConfig;
import com.mna.effects.EffectInit;
import com.mna.entities.sorcery.EntityDecoy;
import com.mna.entities.utility.EntityPresentItem;
import com.mna.interop.CuriosInterop;
import com.mna.items.artifice.FactionSpecificSpellModifierRing;
import com.mna.items.artifice.curio.ItemEmberglowBracelet;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import com.mna.tools.InventoryUtilities;
import com.mna.tools.ProjectileHelper;
import com.mna.tools.SummonUtils;
import com.mna.tools.TeleportHelper;
import de.joh.dragonmagicandrelics.Commands;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeProjectileReflectionHelper;
import de.joh.dragonmagicandrelics.config.CommonConfigs;
import de.joh.dragonmagicandrelics.item.ItemInit;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
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
import top.theillusivec4.curios.api.SlotTypePreset;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicReference;

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
     * @see de.joh.dragonmagicandrelics.item.items.FactionAmulet
     * @see de.joh.dragonmagicandrelics.rituals.contexts.FusionRitual
     */
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        Entity source = event.getSource().getEntity();
        LivingEntity living = event.getEntityLiving();

        if(living instanceof Player player){
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);

            //Spell
            if(!chest.isEmpty() && !player.level.isClientSide && chest.getItem() instanceof DragonMageArmor dragonMageArmor && dragonMageArmor.isSetEquipped(player)) {
                applySpell(false, player, source, chest);
                if(source != null && source != player){
                    applySpell(true, player, source, chest);
                }
            }

            //Damage Resistance
            if(chest.getItem() instanceof DragonMageArmor mmaArmor){
                event.setAmount(event.getAmount() * (1.0f - (float)mmaArmor.getUpgradeLevel(ArmorUpgradeInit.DAMAGE_RESISTANCE, player)* CommonConfigs.getDamageResistanceDamageReductionPerLevel()));
            }
        }

        AtomicReference<Faction> faction = new AtomicReference<>(Faction.NONE);
        if(living instanceof Player player){
            player.getCapability(ManaAndArtificeMod.getProgressionCapability()).ifPresent((p)-> {
                faction.set(p.getAlliedFaction());
            });
        }

        if((living instanceof IFactionEnemy || faction.get() != Faction.NONE) && source instanceof LivingEntity && ((LivingEntity)source).hasEffect(de.joh.dragonmagicandrelics.effects.EffectInit.PEACE_EFFECT.get())){
            ((LivingEntity)source).removeEffect(de.joh.dragonmagicandrelics.effects.EffectInit.PEACE_EFFECT.get());
            ((LivingEntity)source).addEffect(new MobEffectInstance((de.joh.dragonmagicandrelics.effects.EffectInit.BROKEN_PEACE_EFFECT.get()), 12000));
            if(source instanceof Player){
                source.getLevel().playSound(null, source.getX(), source.getY(), source.getZ(), SoundEvents.RAID_HORN, SoundSource.PLAYERS, 64.0F, 0.9F + (float)Math.random() * 0.2F);
            }

        }

        //Damage Boost
        if (source instanceof Player player){
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
            if(chest.getItem() instanceof DragonMageArmor mmaArmor) {
                event.setAmount(event.getAmount() * (1.0f + (float)mmaArmor.getUpgradeLevel(ArmorUpgradeInit.DAMAGE_BOOST, player)*0.25f));
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
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
            if (!chest.isEmpty() && !player.level.isClientSide && chest.getItem() instanceof DragonMageArmor dragonMageArmor  && dragonMageArmor.isSetEquipped(player)) {
                //protection against fire
                if (source.isFire() && dragonMageArmor.getUpgradeLevel(ArmorUpgradeInit.FIRE_RESISTANCE, player) == 1) {

                    IPlayerMagic magic = player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
                    if (magic != null && magic.getCastingResource().hasEnoughAbsolute(player, CommonConfigs.FIRE_RESISTANCE_MANA_PER_FIRE_DAMAGE.get())) {
                        magic.getCastingResource().consume(player, CommonConfigs.FIRE_RESISTANCE_MANA_PER_FIRE_DAMAGE.get());
                        event.setCanceled(true);
                        return;
                    }
                }
                else if (source.isFire() && dragonMageArmor.getUpgradeLevel(ArmorUpgradeInit.FIRE_RESISTANCE, player) == 2) {
                    event.setCanceled(true);
                    return;
                }

                //protection against explosions
                if (source.isExplosion() && dragonMageArmor.getUpgradeLevel(ArmorUpgradeInit.EXPLOSION_RESISTANCE, player) == 1) {
                    event.setCanceled(true);
                    return;
                }

                //Protection from falling through jumpboost
                if(source == DamageSource.FALL && dragonMageArmor.getUpgradeLevel(ArmorUpgradeInit.JUMP, player) >= 1){
                    if((event.getAmount() - dragonMageArmor.getUpgradeLevel(ArmorUpgradeInit.JUMP, player) * 3) <= 0){
                        event.setCanceled(true);
                        return;
                    }
                }

                //Protection from kinetic energy
                if((source == DamageSource.FALL || source == DamageSource.FLY_INTO_WALL) && dragonMageArmor.getUpgradeLevel(ArmorUpgradeInit.KINETIC_RESISTANCE, player) == 1){
                    event.setCanceled(true);
                    return;
                }

                //Projectile Reflection
                if(source.getDirectEntity() instanceof Projectile && dragonMageArmor.getUpgradeLevel(ArmorUpgradeInit.PROJECTILE_REFLECTION, player) > 0 && ArmorUpgradeProjectileReflectionHelper.consumeReflectCharge(player)){
                    event.setCanceled(true);
                    ProjectileHelper.ReflectProjectile(player, (Projectile)source.getDirectEntity(), true, 10.0F);
                    return;
                }
            }

            //Protection from kinetic energy
            if((source == DamageSource.FALL || source == DamageSource.FLY_INTO_WALL) && (CuriosApi.getCuriosHelper().findEquippedCurio(ItemInit.ANGEL_RING.get(), player).isPresent() || CuriosApi.getCuriosHelper().findEquippedCurio(ItemInit.FALLEN_ANGEL_RING.get(), player).isPresent()            )){
                event.setCanceled(true);
                return;
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
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
            if (!chest.isEmpty() && !player.level.isClientSide && chest.getItem() instanceof DragonMageArmor) {

                //Protection from falling through jumpboost
                if(event.getSource() == DamageSource.FALL && ((DragonMageArmor) chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.JUMP, player) >= 1){
                    int amount = (int)event.getAmount() - ((DragonMageArmor) chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.JUMP, player) * 3;
                    if(amount > 0){
                        event.setAmount(amount);
                    }
                }

                if (((DragonMageArmor) chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.MIST_FORM, player) >= 1 && !event.getSource().isBypassInvul() && player.getHealth() > 1.0F && event.getAmount() > player.getHealth()) {
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
                if (p.getAlliedFaction() == Faction.UNDEAD) {
                    ItemStack chest = sourcePlayer.getItemBySlot(EquipmentSlot.CHEST);
                    if (!chest.isEmpty() && !sourcePlayer.level.isClientSide && chest.getItem() instanceof DragonMageArmor) {
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
        } else if (target instanceof LivingEntity && (!(target instanceof PathfinderMob) || !SummonUtils.isSummon((PathfinderMob)target))) {
            if (target instanceof EntityDecoy) {
                return 0.0F;
            } else {
                MutableFloat restoreAmount = new MutableFloat(1.0F);
                if (target instanceof Player) {
                    restoreAmount.setValue((Number) GeneralModConfig.MA_SOULS_PLAYER.get());
                } else if (target instanceof Villager) {
                    restoreAmount.setValue((Number)GeneralModConfig.MA_SOULS_VILLAGER.get());
                } else if (target instanceof IFactionEnemy) {
                    restoreAmount.setValue((Number)GeneralModConfig.MA_SOULS_FACTION.get());
                } else if (((LivingEntity)target).isInvertedHealAndHarm()) {
                    restoreAmount.setValue((Number)GeneralModConfig.MA_SOULS_UNDEAD.get());
                } else if (target instanceof Animal) {
                    restoreAmount.setValue((Number)GeneralModConfig.MA_SOULS_ANIMAL.get());
                } else if (target instanceof AbstractGolem) {
                    restoreAmount.setValue(0.0F);
                } else if (target instanceof Mob) {
                    restoreAmount.setValue((Number)GeneralModConfig.MA_SOULS_MOB.get());
                }

                if (((LivingEntity)target).hasEffect((MobEffect) EffectInit.SOUL_VULNERABILITY.get())) {
                    restoreAmount.setValue(restoreAmount.getValue() * 5.0F);
                }

                if (((FactionSpecificSpellModifierRing) com.mna.items.ItemInit.BONE_RING.get()).isEquippedAndHasMana(soulRecipient, 3.5F, true)) {
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
            EntityPresentItem item = new EntityPresentItem(world, event.getEntityLiving().getX(), event.getEntityLiving().getY(), event.getEntityLiving().getZ(), new ItemStack(ItemInit.DRAGON_CORE.get()));
            world.addFreshEntity(item);
        }
    }

    /**
     * Performs one of the two spells from the Dragon Mage Armor.
     * @param isOther Should the source be hit? Should the offensive spell be used?
     * @param self Wearer of armor.
     * @param other Source of damage (only as entity).
     * @param chest Dragon Mage Armor Chestplate with the spells
     */
    public static void applySpell(boolean isOther, Player self, @Nullable Entity other, ItemStack chest){
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("spell", chest.getTag().getCompound(DragonMagicAndRelics.MOD_ID+(isOther ? "spell_other" : "spell_self")));
        SpellRecipe recipe = SpellRecipe.fromNBT(compoundTag);

        if (recipe.isValid()) {
            MutableBoolean consumed = new MutableBoolean(false);
            self.getCapability(PlayerMagicProvider.MAGIC).ifPresent((c) -> {
                if (c.getCastingResource().hasEnoughAbsolute(self, recipe.getManaCost())) {
                    c.getCastingResource().consume(self, recipe.getManaCost());
                    consumed.setTrue();
                }
            });

            if (consumed.getValue()) {
                SpellSource spellSource = new SpellSource(self, InteractionHand.MAIN_HAND);
                SpellContext context = new SpellContext((ServerLevel)self.level, recipe);
                recipe.iterateComponents((c) -> {
                    int delay = (int)(c.getValue(com.mna.api.spells.attributes.Attribute.DELAY) * 20.0F);
                    boolean appliedComponent = false;
                    if (delay > 0) {
                        DelayedEventQueue.pushEvent(self.level, new TimedDelayedSpellEffect(c.getPart().getRegistryName().toString(), delay, spellSource, new SpellTarget(isOther ? other : self ), c, context));
                        appliedComponent = true;
                    } else if (c.getPart().ApplyEffect(spellSource, new SpellTarget(isOther ? other : self), c, context) == ComponentApplicationResult.SUCCESS) {
                        appliedComponent = true;
                    }
                    if (appliedComponent) {
                        SpellCaster.addComponentRoteProgress(self, c.getPart());
                    }
                });
            }
        }
    }
}
