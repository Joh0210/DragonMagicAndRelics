package de.joh.dragonmagicandrelics.events;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedSpellEffect;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import de.joh.dragonmagicandrelics.Commands;
import de.joh.dragonmagicandrelics.DragonMagicAndRelics;
import de.joh.dragonmagicandrelics.armorupgrades.ArmorUpgradeInit;
import de.joh.dragonmagicandrelics.item.items.DragonMageArmor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.mutable.MutableBoolean;
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
     * @param event
     */
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        Entity source = event.getSource().getEntity();
        LivingEntity living = event.getEntityLiving();

        if(living instanceof Player player){
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);

            //Spell
            if (!chest.isEmpty() && !player.level.isClientSide && chest.getItem() instanceof DragonMageArmor dragonMageArmor && dragonMageArmor.isSetEquipped(player)) {
                applySpell(false, player, source, chest);
                if(source != null){
                    applySpell(true, player, source, chest);
                }
            }

            //Damage Resistance
            if(chest.getItem() instanceof DragonMageArmor mmaArmor){
                event.setAmount(event.getAmount() * (1.0f - (float)mmaArmor.getUpgradeLevel(ArmorUpgradeInit.DAMAGE_RESISTANCE, player)* ArmorUpgradeInit.DAMAGE_REDUCTION_PER_LEVEL));
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
     * Processing of the fire resistance, explosion resistance and kinetic resistance upgrades resistance through jumpboost.
     * @see ArmorUpgradeInit
     */
    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if(event.getEntityLiving() instanceof Player player){
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
            if (!chest.isEmpty() && !player.level.isClientSide && chest.getItem() instanceof DragonMageArmor) {
                //protection against fire
                if (event.getSource().isFire() && ((DragonMageArmor) chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.FIRE_RESISTANCE, player) == 1) {

                    IPlayerMagic magic = player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
                    if (magic != null && magic.getCastingResource().hasEnoughAbsolute(player, ArmorUpgradeInit.MANA_PER_FIRE_DAMAGE)) {
                        magic.getCastingResource().consume(player, ArmorUpgradeInit.MANA_PER_FIRE_DAMAGE);
                        event.setCanceled(true);
                        return;
                    }
                }
                else if (event.getSource().isFire() && ((DragonMageArmor) chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.FIRE_RESISTANCE, player) == 2) {
                    event.setCanceled(true);
                    return;
                }

                //protection against explosions
                if (event.getSource().isExplosion() && ((DragonMageArmor) chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.EXPLOSION_RESISTANCE, player) == 1) {
                    event.setCanceled(true);
                    return;
                }

                //Protection from falling through jumpboost
                if(event.getSource() == DamageSource.FALL && ((DragonMageArmor) chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.JUMP, player) >= 1){
                    if((event.getAmount() - ((DragonMageArmor) chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.JUMP, player) * 3) <= 0){
                        event.setCanceled(true);
                        return;
                    }
                }

                //Protection from kinetic energy
                if((event.getSource() == DamageSource.FALL || event.getSource() == DamageSource.FLY_INTO_WALL) && ((DragonMageArmor) chest.getItem()).getUpgradeLevel(ArmorUpgradeInit.KINETIC_RESISTANCE, player) == 1){
                    event.setCanceled(true);
                    return;
                }
            }
        }
    }

    /**
     * Processing resistance through jumpboost.
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
            }
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
